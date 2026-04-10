package org.gta.backend_locator.service.impl;

import lombok.RequiredArgsConstructor;
import org.gta.backend_locator.dto.response.*;
import org.gta.backend_locator.model.PlanCarga;
import org.gta.backend_locator.model.PlanCargaCamion;
import org.gta.backend_locator.model.PlanCargaCamionTienda;
import org.gta.backend_locator.repository.PlanCargaCamionRepository;
import org.gta.backend_locator.repository.PlanCargaCamionTiendaRepository;
import org.gta.backend_locator.repository.PlanCargaRepository;
import org.gta.backend_locator.service.PlanCargaService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanCargaServiceImpl implements PlanCargaService {

    private final PlanCargaRepository planRepo;
    private final PlanCargaCamionRepository camionRepo;
    private final PlanCargaCamionTiendaRepository camionTiendaRepo;

    private final JdbcTemplate jdbcTemplate;

    private static final BigDecimal OCUPACION_MINIMA = new BigDecimal("0.80");

    @Override
    public PlanCargaResponse generarPlan(Long ventanaId) {

        String sql = """
        SELECT dp.tienda_id,
               t.recibe_tonelaje,
               SUM(dpd.cantidad * dpd.peso_unitario_kg) AS peso_total,
               SUM(dpd.cantidad * dpd.volumen_unitario_m3) AS volumen_total
        FROM despacho_proceso dp
        JOIN despacho_proceso_detalle dpd ON dp.id = dpd.despacho_id
        JOIN tienda t ON t.id = dp.tienda_id
        WHERE dp.estado = 'EN_PROCESO'
        GROUP BY dp.tienda_id, t.recibe_tonelaje
    """;

        List<Map<String, Object>> demanda = jdbcTemplate.queryForList(sql);

        PlanCarga plan = new PlanCarga();
        plan.setVentanaId(ventanaId);
        plan.setFecha(LocalDate.now());
        plan.setEstado("GENERADO");
        plan.setUsuarioCreacion("demo");
        plan.setFechaCreacion(LocalDateTime.now());

        plan = planRepo.save(plan);

        List<CamionConfig> tipos = List.of(
                new CamionConfig("5T",  new BigDecimal("5000"),  new BigDecimal("30")),
                new CamionConfig("10T", new BigDecimal("10000"), new BigDecimal("50")),
                new CamionConfig("22T", new BigDecimal("22000"), new BigDecimal("70"))
        );

        List<PlanCargaCamion> camiones = new ArrayList<>();
        List<PlanCargaCamionTienda> asignaciones = new ArrayList<>();

        demanda.sort((a, b) -> {
            BigDecimal pesoA = toBigDecimal(a.get("peso_total"));
            BigDecimal pesoB = toBigDecimal(b.get("peso_total"));
            return pesoB.compareTo(pesoA);
        });

        for (Map<String, Object> row : demanda) {

            Long tiendaId = ((Number) row.get("tienda_id")).longValue();
            BigDecimal pesoPendiente = toBigDecimal(row.get("peso_total"));
            BigDecimal volumenPendiente = toBigDecimal(row.get("volumen_total"));

            String recibeTonelaje = row.get("recibe_tonelaje") != null
                    ? row.get("recibe_tonelaje").toString()
                    : null;

            Set<String> tiposPermitidos = parsearTiposPermitidos(recibeTonelaje);

            if (tiposPermitidos.isEmpty()) {
                throw new IllegalStateException(
                        "La tienda " + tiendaId + " no tiene tonelajes permitidos configurados."
                );
            }

            while (esMayorQueCero(pesoPendiente) || esMayorQueCero(volumenPendiente)) {

                boolean huboAsignacion = false;

                // 1. Intentar asignar a camiones existentes válidos para la tienda
                for (PlanCargaCamion camion : camiones) {

                    if (!tiposPermitidos.contains(normalizarTipoCamion(camion.getTipoCamion()))) {
                        continue;
                    }

                    BigDecimal disponiblePeso = camion.getCapacidadPeso().subtract(camion.getPesoActual());
                    BigDecimal disponibleVolumen = camion.getCapacidadVolumen().subtract(camion.getVolumenActual());

                    if (disponiblePeso.compareTo(BigDecimal.ZERO) <= 0 ||
                            disponibleVolumen.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }

                    BigDecimal factorAsignacion = calcularFactorAsignacion(
                            pesoPendiente,
                            volumenPendiente,
                            disponiblePeso,
                            disponibleVolumen
                    );

                    if (factorAsignacion.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }

                    BigDecimal pesoAsignado = pesoPendiente.multiply(factorAsignacion)
                            .setScale(4, RoundingMode.HALF_UP);
                    BigDecimal volumenAsignado = volumenPendiente.multiply(factorAsignacion)
                            .setScale(4, RoundingMode.HALF_UP);

                    pesoAsignado = pesoAsignado.min(disponiblePeso).min(pesoPendiente);
                    volumenAsignado = volumenAsignado.min(disponibleVolumen).min(volumenPendiente);

                    if (!esMayorQueCero(pesoAsignado) && !esMayorQueCero(volumenAsignado)) {
                        continue;
                    }

                    camion.setPesoActual(camion.getPesoActual().add(pesoAsignado));
                    camion.setVolumenActual(camion.getVolumenActual().add(volumenAsignado));
                    camionRepo.save(camion);

                    PlanCargaCamionTienda ct = new PlanCargaCamionTienda();
                    ct.setPlanCamionId(camion.getIdPlanCamion());
                    ct.setTiendaId(tiendaId);
                    ct.setPesoAsignado(pesoAsignado);
                    ct.setVolumenAsignado(volumenAsignado);
                    ct.setEstado("ACTIVO");
                    ct.setUsuarioCreacion("demo");
                    ct.setFechaCreacion(LocalDateTime.now());
                    camionTiendaRepo.save(ct);

                    asignaciones.add(ct);

                    pesoPendiente = pesoPendiente.subtract(pesoAsignado).max(BigDecimal.ZERO);
                    volumenPendiente = volumenPendiente.subtract(volumenAsignado).max(BigDecimal.ZERO);

                    huboAsignacion = true;

                    if (!esMayorQueCero(pesoPendiente) && !esMayorQueCero(volumenPendiente)) {
                        break;
                    }
                }

                if (!esMayorQueCero(pesoPendiente) && !esMayorQueCero(volumenPendiente)) {
                    break;
                }

                // 2. Crear camión nuevo solo con tonelajes permitidos para la tienda
                if (!huboAsignacion) {

                    CamionConfig tipo = seleccionarTipoCamion(tipos, tiposPermitidos, pesoPendiente, volumenPendiente);

                    PlanCargaCamion nuevo = new PlanCargaCamion();
                    nuevo.setPlanId(plan.getIdPlan());
                    nuevo.setTipoCamion(tipo.tipo());
                    nuevo.setCapacidadPeso(tipo.capacidadPeso());
                    nuevo.setCapacidadVolumen(tipo.capacidadVolumen());
                    nuevo.setPesoActual(BigDecimal.ZERO);
                    nuevo.setVolumenActual(BigDecimal.ZERO);
                    nuevo.setEstado("EN_CARGA");
                    nuevo.setUsuarioCreacion("demo");
                    nuevo.setFechaCreacion(LocalDateTime.now());

                    nuevo = camionRepo.save(nuevo);
                    camiones.add(nuevo);

                    BigDecimal factorAsignacion = calcularFactorAsignacion(
                            pesoPendiente,
                            volumenPendiente,
                            nuevo.getCapacidadPeso(),
                            nuevo.getCapacidadVolumen()
                    );

                    BigDecimal pesoAsignado = pesoPendiente.multiply(factorAsignacion)
                            .setScale(4, RoundingMode.HALF_UP);
                    BigDecimal volumenAsignado = volumenPendiente.multiply(factorAsignacion)
                            .setScale(4, RoundingMode.HALF_UP);

                    pesoAsignado = pesoAsignado.min(nuevo.getCapacidadPeso()).min(pesoPendiente);
                    volumenAsignado = volumenAsignado.min(nuevo.getCapacidadVolumen()).min(volumenPendiente);

                    if (!esMayorQueCero(pesoAsignado) && !esMayorQueCero(volumenAsignado)) {
                        throw new IllegalStateException(
                                "No fue posible asignar carga para la tienda " + tiendaId +
                                        " con los tonelajes permitidos: " + tiposPermitidos
                        );
                    }

                    nuevo.setPesoActual(pesoAsignado);
                    nuevo.setVolumenActual(volumenAsignado);
                    camionRepo.save(nuevo);

                    PlanCargaCamionTienda ct = new PlanCargaCamionTienda();
                    ct.setPlanCamionId(nuevo.getIdPlanCamion());
                    ct.setTiendaId(tiendaId);
                    ct.setPesoAsignado(pesoAsignado);
                    ct.setVolumenAsignado(volumenAsignado);
                    ct.setEstado("ACTIVO");
                    ct.setUsuarioCreacion("demo");
                    ct.setFechaCreacion(LocalDateTime.now());
                    camionTiendaRepo.save(ct);

                    asignaciones.add(ct);

                    pesoPendiente = pesoPendiente.subtract(pesoAsignado).max(BigDecimal.ZERO);
                    volumenPendiente = volumenPendiente.subtract(volumenAsignado).max(BigDecimal.ZERO);
                }
            }
        }

        for (PlanCargaCamion c : camiones) {

            BigDecimal porcentajePeso = dividirSeguro(c.getPesoActual(), c.getCapacidadPeso());
            BigDecimal porcentajeVolumen = dividirSeguro(c.getVolumenActual(), c.getCapacidadVolumen());

            BigDecimal ocupacion = porcentajePeso.max(porcentajeVolumen);

            c.setPorcentajeOcupacion(ocupacion);
            c.setEstado(
                    ocupacion.compareTo(OCUPACION_MINIMA) >= 0
                            ? "LISTO"
                            : "PENDIENTE"
            );

            camionRepo.save(c);
        }

        PlanCargaResponse response = new PlanCargaResponse();
        response.planId = plan.getIdPlan();
        response.camiones = camiones.stream()
                .map(c -> {
                    CamionResponse cr = new CamionResponse();
                    cr.tipoCamion = c.getTipoCamion();
                    cr.pesoActual = c.getPesoActual();
                    cr.volumenActual = c.getVolumenActual();
                    cr.porcentajeOcupacion = c.getPorcentajeOcupacion();

                    cr.tiendas = asignaciones.stream()
                            .filter(a -> a.getPlanCamionId().equals(c.getIdPlanCamion()))
                            .map(a -> {
                                TiendaCargaResponse tr = new TiendaCargaResponse();
                                tr.tiendaId = a.getTiendaId();
                                tr.pesoAsignado = a.getPesoAsignado();
                                tr.volumenAsignado = a.getVolumenAsignado();
                                return tr;
                            })
                            .toList();

                    return cr;
                })
                .toList();

        return response;
    }

    private CamionConfig seleccionarTipoCamion(List<CamionConfig> tipos,
                                               Set<String> tiposPermitidos,
                                               BigDecimal pesoPendiente,
                                               BigDecimal volumenPendiente) {

        List<CamionConfig> tiposFiltrados = tipos.stream()
                .filter(t -> tiposPermitidos.contains(normalizarTipoCamion(t.tipo())))
                .sorted(Comparator.comparing(CamionConfig::capacidadPeso))
                .toList();

        if (tiposFiltrados.isEmpty()) {
            throw new IllegalStateException("No hay tipos de camión permitidos para la tienda.");
        }

        // Busca el más pequeño permitido que pueda llevar TODO el remanente
        return tiposFiltrados.stream()
                .filter(t -> pesoPendiente.compareTo(t.capacidadPeso()) <= 0
                        && volumenPendiente.compareTo(t.capacidadVolumen()) <= 0)
                .findFirst()
                // Si ninguno puede llevar todo el remanente, usar el mayor permitido
                // para ir partiendo la carga en varios camiones válidos.
                .orElse(tiposFiltrados.get(tiposFiltrados.size() - 1));
    }

    private BigDecimal calcularFactorAsignacion(BigDecimal pesoPendiente,
                                                BigDecimal volumenPendiente,
                                                BigDecimal capacidadDisponiblePeso,
                                                BigDecimal capacidadDisponibleVolumen) {

        boolean hayPeso = esMayorQueCero(pesoPendiente);
        boolean hayVolumen = esMayorQueCero(volumenPendiente);

        if (!hayPeso && !hayVolumen) {
            return BigDecimal.ZERO;
        }

        BigDecimal factorPeso = BigDecimal.ONE;
        BigDecimal factorVolumen = BigDecimal.ONE;

        if (hayPeso) {
            factorPeso = capacidadDisponiblePeso.divide(pesoPendiente, 8, RoundingMode.DOWN)
                    .min(BigDecimal.ONE);
        }

        if (hayVolumen) {
            factorVolumen = capacidadDisponibleVolumen.divide(volumenPendiente, 8, RoundingMode.DOWN)
                    .min(BigDecimal.ONE);
        }

        return factorPeso.min(factorVolumen).max(BigDecimal.ZERO);
    }

    private BigDecimal dividirSeguro(BigDecimal numerador, BigDecimal denominador) {
        if (denominador == null || denominador.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerador.divide(denominador, 4, RoundingMode.HALF_UP);
    }

    private boolean esMayorQueCero(BigDecimal valor) {
        return valor != null && valor.compareTo(new BigDecimal("0.0001")) > 0;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number n) {
            return BigDecimal.valueOf(n.doubleValue());
        }
        return new BigDecimal(value.toString());
    }

    private record CamionConfig(String tipo, BigDecimal capacidadPeso, BigDecimal capacidadVolumen) {
    }

    private Set<String> parsearTiposPermitidos(String recibeTonelaje) {
        if (recibeTonelaje == null || recibeTonelaje.isBlank()) {
            return Set.of();
        }

        Set<String> permitidos = new HashSet<>();

        String[] valores = recibeTonelaje.split(",");

        for (String valor : valores) {
            String normalizado = normalizarTipoCamion(valor);
            if (!normalizado.isBlank()) {
                permitidos.add(normalizado);
            }
        }

        return permitidos;
    }

    private String normalizarTipoCamion(String valor) {
        if (valor == null) {
            return "";
        }

        String limpio = valor.trim().toUpperCase();

        if (limpio.isBlank()) {
            return "";
        }

        limpio = limpio.replace("TON", "")
                .replace("TONS", "")
                .replace("TONELADAS", "")
                .replace(" ", "");

        if (limpio.endsWith("T")) {
            limpio = limpio.substring(0, limpio.length() - 1);
        }

        return limpio + "T";
    }

    @Override
    public List<PlanCargaCamionResponse> getPlanes() {
        List<PlanCargaCamion> camiones = camionRepo.findAllByOrderByIdPlanCamionDesc();
        return buildCamionResponse(camiones);
    }

    private List<PlanCargaCamionResponse> buildCamionResponse(List<PlanCargaCamion> camiones) {
        List<PlanCargaCamionResponse> response = new ArrayList<>();

        for (PlanCargaCamion camion : camiones) {
            PlanCargaCamionResponse item = new PlanCargaCamionResponse();
            item.setIdPlanCamion(camion.getIdPlanCamion());
            item.setPlanId(camion.getPlanId());
            item.setTipoCamion(camion.getTipoCamion());
            item.setCapacidadPeso(camion.getCapacidadPeso());
            item.setCapacidadVolumen(camion.getCapacidadVolumen());
            item.setPesoActual(camion.getPesoActual());
            item.setVolumenActual(camion.getVolumenActual());
            item.setPorcentajeOcupacion(camion.getPorcentajeOcupacion());
            item.setEstado(camion.getEstado());
            item.setTiendas(getTiendasByCamion(camion.getIdPlanCamion()));
            response.add(item);
        }

        return response;
    }


    private List<PlanCargaTiendaResponse> getTiendasByCamion(Long planCamionId) {
        List<PlanCargaCamionTienda> relaciones =
                camionTiendaRepo.findByPlanCamionIdOrderByTiendaIdAsc(planCamionId);

        List<PlanCargaTiendaResponse> tiendas = new ArrayList<>();

        for (PlanCargaCamionTienda relacion : relaciones) {
            PlanCargaTiendaResponse item = new PlanCargaTiendaResponse();
            item.setTiendaId(relacion.getTiendaId());
            item.setPesoAsignado(relacion.getPesoAsignado());
            item.setVolumenAsignado(relacion.getVolumenAsignado());
            tiendas.add(item);
        }

        return tiendas;
    }

 /*   private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(value.toString());
    }
*/
 //   private record CamionConfig(String tipo, int capacidad, int volumen) {}
}

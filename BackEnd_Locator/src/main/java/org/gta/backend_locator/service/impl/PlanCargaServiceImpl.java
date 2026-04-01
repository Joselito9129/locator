package org.gta.backend_locator.service.impl;

import lombok.RequiredArgsConstructor;
import org.gta.backend_locator.dto.response.CamionResponse;
import org.gta.backend_locator.dto.response.PlanCargaCamionResponse;
import org.gta.backend_locator.dto.response.PlanCargaResponse;
import org.gta.backend_locator.dto.response.PlanCargaTiendaResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            SELECT tienda_id,
                   SUM(cantidad * peso_unitario_kg) peso_total,
                   SUM(cantidad * volumen_unitario_m3) volumen_total
            FROM despacho_proceso dp
            JOIN despacho_proceso_detalle dpd ON dp.id_despacho = dpd.despacho_id
            WHERE dp.estado = 'EN_PROCESO'
            GROUP BY tienda_id
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
                new CamionConfig("22T", 22000),
                new CamionConfig("10T", 10000),
                new CamionConfig("5T", 5000)
        );

        List<PlanCargaCamion> camiones = new ArrayList<>();

        // Algoritmo disponibilidad
        for (Map<String, Object> row : demanda) {

            Long tiendaId = ((Number) row.get("tienda_id")).longValue();
            BigDecimal peso = (BigDecimal) row.get("peso_total");

            boolean asignado = false;

            for (PlanCargaCamion camion : camiones) {

                BigDecimal nuevoPeso = camion.getPesoActual().add(peso);

                if (nuevoPeso.compareTo(camion.getCapacidadPeso()) <= 0) {

                    camion.setPesoActual(nuevoPeso);

                    PlanCargaCamionTienda ct = new PlanCargaCamionTienda();
                    ct.setPlanCamionId(camion.getIdPlanCamion());
                    ct.setTiendaId(tiendaId);
                    ct.setPesoAsignado(peso);
                    ct.setEstado("ACTIVO");
                    ct.setUsuarioCreacion("demo");
                    ct.setFechaCreacion(LocalDateTime.now());

                    camionTiendaRepo.save(ct);

                    asignado = true;
                    break;
                }
            }

            if (!asignado) {

                CamionConfig tipo = tipos.stream()
                        .filter(t -> peso.compareTo(BigDecimal.valueOf(t.capacidad)) <= 0)
                        .findFirst()
                        .orElse(tipos.get(0));

                PlanCargaCamion nuevo = new PlanCargaCamion();
                nuevo.setPlanId(plan.getIdPlan());
                nuevo.setTipoCamion(tipo.tipo);
                nuevo.setCapacidadPeso(BigDecimal.valueOf(tipo.capacidad));
                nuevo.setPesoActual(peso);
                nuevo.setEstado("EN_CARGA");
                nuevo.setUsuarioCreacion("demo");
                nuevo.setFechaCreacion(LocalDateTime.now());

                nuevo = camionRepo.save(nuevo);

                PlanCargaCamionTienda ct = new PlanCargaCamionTienda();
                ct.setPlanCamionId(nuevo.getIdPlanCamion());
                ct.setTiendaId(tiendaId);
                ct.setPesoAsignado(peso);
                ct.setEstado("ACTIVO");
                ct.setUsuarioCreacion("demo");
                ct.setFechaCreacion(LocalDateTime.now());

                camionTiendaRepo.save(ct);

                camiones.add(nuevo);
            }
        }

        // Ocupacion de camion
        for (PlanCargaCamion c : camiones) {
            BigDecimal ocupacion = c.getPesoActual()
                    .divide(c.getCapacidadPeso(), 2, RoundingMode.HALF_UP);

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
        response.camiones = camiones.stream().map(c -> {
            CamionResponse cr = new CamionResponse();
            cr.tipoCamion = c.getTipoCamion();
            cr.porcentajeOcupacion = c.getPorcentajeOcupacion();
            cr.pesoActual = c.getPesoActual();
            cr.tiendas = new ArrayList<>();
            return cr;
        }).toList();

        return response;
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

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(value.toString());
    }

    private record CamionConfig(String tipo, int capacidad) {}
}

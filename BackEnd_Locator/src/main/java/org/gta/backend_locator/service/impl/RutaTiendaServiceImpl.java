package org.gta.backend_locator.service.impl;

import org.gta.backend_locator.dto.request.RutaTiendaRequest;
import org.gta.backend_locator.dto.response.RutaTiendaResponse;
import org.gta.backend_locator.exception.ApplicationException;
import org.gta.backend_locator.exception.RepositoryException;
import org.gta.backend_locator.mapper.RutaTiendaMapper;
import org.gta.backend_locator.model.RutaTienda;
import org.gta.backend_locator.repository.RutaTiendaPuntoRepository;
import org.gta.backend_locator.repository.RutaTiendaRepository;
import org.gta.backend_locator.service.RutaTiendaService;
import org.gta.backend_locator.utils.RutaTiendaValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RutaTiendaServiceImpl implements RutaTiendaService {

    private final RutaTiendaRepository rutaTiendaRepo;
    private final RutaTiendaPuntoRepository rutaTiendaPuntoRepo;
    private final RutaTiendaMapper rutaTiendaMapper;
    private final RutaTiendaValidator validator;

    public RutaTiendaServiceImpl(RutaTiendaRepository rutaTiendaRepo,
                                 RutaTiendaPuntoRepository rutaTiendaPuntoRepo,
                                 RutaTiendaMapper rutaTiendaMapper,
                                 RutaTiendaValidator validator) {
        this.rutaTiendaRepo = rutaTiendaRepo;
        this.rutaTiendaPuntoRepo = rutaTiendaPuntoRepo;
        this.rutaTiendaMapper = rutaTiendaMapper;
        this.validator = validator;
    }

    @Override
    public RutaTiendaResponse crearRuta(RutaTiendaRequest request) {
        validator.validateCreateRuta(request);

        try {
            RutaTienda entity = rutaTiendaMapper.toEntity(request);
            entity.setId(null);
            entity.setFechaCreacion(LocalDateTime.now());
            entity.setUsuarioActualizacion(null);
            entity.setFechaActualizacion(null);
            entity.setCentroId(1L);
            entity.setNombreRuta("Ruta de Prueba");

            RutaTienda saved = rutaTiendaRepo.save(entity);
            return rutaTiendaMapper.toResponse(saved);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al crear la ruta_tienda", e);
        }
    }

    @Override
    public RutaTiendaResponse actualizarRuta(Long id, RutaTiendaRequest request) {
        validator.validateUpdateRuta(id, request);

        try {
            RutaTienda entity = rutaTiendaRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe la ruta_tienda con id: " + id));

            entity.setTiendaId(request.getTiendaId());
            entity.setEstado(request.getEstado());
            entity.setUsuarioActualizacion(request.getUsuarioActualizacion());
            entity.setFechaActualizacion(LocalDateTime.now());

            RutaTienda updated = rutaTiendaRepo.save(entity);
            return rutaTiendaMapper.toResponse(updated);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al actualizar la ruta_tienda", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RutaTiendaResponse obtenerRutaPorId(Long id) {
        validator.validateId(id, "El id de la ruta");

        try {
            RutaTienda entity = rutaTiendaRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe la ruta_tienda con id: " + id));

            return rutaTiendaMapper.toResponse(entity);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al consultar la ruta_tienda", e);
        }
    }

    @Override
    public List<RutaTiendaResponse> obtenerRutas() {
        try{
            return rutaTiendaMapper.toResponseList(rutaTiendaRepo.findAll());
        }catch (DataAccessException e){
            throw new RepositoryException("Error al consultar todas las rutas", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutaTiendaResponse> consultarRutasPorTienda(Long tiendaId) {
        validator.validateId(tiendaId, "El tiendaId");

        try {
            return rutaTiendaMapper.toResponseList(
                    rutaTiendaRepo.findByTiendaIdOrderByFechaCreacionDesc(tiendaId)
            );
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al consultar rutas por tienda", e);
        }
    }

    @Override
    public void eliminarRuta(Long id) {
        validator.validateId(id, "El id de la ruta");

        try {
            RutaTienda entity = rutaTiendaRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe la ruta_tienda con id: " + id));

            boolean tienePuntos = rutaTiendaPuntoRepo.existsByRutaTiendaId(id);
            if (tienePuntos) {
                throw new ApplicationException("No se puede eliminar la ruta porque tiene puntos asociados");
            }

            rutaTiendaRepo.delete(entity);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al eliminar la ruta_tienda", e);
        }
    }
}
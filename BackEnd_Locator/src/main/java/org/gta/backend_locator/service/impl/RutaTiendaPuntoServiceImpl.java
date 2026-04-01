package org.gta.backend_locator.service.impl;

import org.gta.backend_locator.dto.request.RutaTiendaPuntoRequest;
import org.gta.backend_locator.dto.response.RutaTiendaPuntoResponse;
import org.gta.backend_locator.exception.ApplicationException;
import org.gta.backend_locator.exception.RepositoryException;
import org.gta.backend_locator.mapper.RutaTiendaPuntoMapper;
import org.gta.backend_locator.model.RutaTiendaPunto;
import org.gta.backend_locator.repository.RutaTiendaPuntoRepository;
import org.gta.backend_locator.repository.RutaTiendaRepository;
import org.gta.backend_locator.service.RutaTiendaPuntoService;
import org.gta.backend_locator.utils.RutaTiendaValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RutaTiendaPuntoServiceImpl implements RutaTiendaPuntoService {

    private final RutaTiendaPuntoRepository rutaTiendaPuntoRepo;
    private final RutaTiendaRepository rutaTiendaRepo;
    private final RutaTiendaPuntoMapper rutaTiendaPuntoMapper;
    private final RutaTiendaValidator validator;

    public RutaTiendaPuntoServiceImpl(RutaTiendaPuntoRepository rutaTiendaPuntoRepo,
                                      RutaTiendaRepository rutaTiendaRepo,
                                      RutaTiendaPuntoMapper rutaTiendaPuntoMapper,
                                      RutaTiendaValidator validator) {
        this.rutaTiendaPuntoRepo = rutaTiendaPuntoRepo;
        this.rutaTiendaRepo = rutaTiendaRepo;
        this.rutaTiendaPuntoMapper = rutaTiendaPuntoMapper;
        this.validator = validator;
    }

    @Override
    public RutaTiendaPuntoResponse crearPunto(RutaTiendaPuntoRequest request) {
        validator.validateCreatePunto(request);

        try {
            validarRutaExiste(request.getRutaTiendaId());
            validarOrdenDuplicado(request.getRutaTiendaId(), request.getOrden(), null);

            RutaTiendaPunto entity = rutaTiendaPuntoMapper.toEntity(request);
            entity.setId(null);
            entity.setFechaCreacion(LocalDateTime.now());
            entity.setUsuarioActualizacion(null);
            entity.setFechaActualizacion(null);
            entity.setLatitud(234.3);
            entity.setLongitud(234.3);
            entity.setTipoPunto("P");

            RutaTiendaPunto saved = rutaTiendaPuntoRepo.save(entity);
            return rutaTiendaPuntoMapper.toResponse(saved);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al crear el ruta_tienda_punto", e);
        }
    }

    @Override
    public RutaTiendaPuntoResponse actualizarPunto(Long id, RutaTiendaPuntoRequest request) {
        validator.validateUpdatePunto(id, request);

        try {
            validarRutaExiste(request.getRutaTiendaId());

            RutaTiendaPunto entity = rutaTiendaPuntoRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe el ruta_tienda_punto con id: " + id));

            validarOrdenDuplicado(request.getRutaTiendaId(), request.getOrden(), entity.getId());

            entity.setRutaTiendaId(request.getRutaTiendaId());
            entity.setOrden(request.getOrden());
            entity.setEstado(request.getEstado());
            entity.setUsuarioActualizacion(request.getUsuarioActualizacion());
            entity.setFechaActualizacion(LocalDateTime.now());

            RutaTiendaPunto updated = rutaTiendaPuntoRepo.save(entity);
            return rutaTiendaPuntoMapper.toResponse(updated);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al actualizar el ruta_tienda_punto", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RutaTiendaPuntoResponse obtenerPuntoPorId(Long id) {
        validator.validateId(id, "El id del punto");

        try {
            RutaTiendaPunto entity = rutaTiendaPuntoRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe el ruta_tienda_punto con id: " + id));

            return rutaTiendaPuntoMapper.toResponse(entity);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al consultar el ruta_tienda_punto", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RutaTiendaPuntoResponse> listarPuntosOrdenados(Long rutaTiendaId) {
        validator.validateId(rutaTiendaId, "El rutaTiendaId");

        try {
            validarRutaExiste(rutaTiendaId);

            return rutaTiendaPuntoMapper.toResponseList(
                    rutaTiendaPuntoRepo.findByRutaTiendaIdOrderByOrdenAsc(rutaTiendaId)
            );
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al listar puntos ordenados", e);
        }
    }

    @Override
    public void eliminarPunto(Long id) {
        validator.validateId(id, "El id del punto");

        try {
            RutaTiendaPunto entity = rutaTiendaPuntoRepo.findById(id)
                    .orElseThrow(() -> new ApplicationException("No existe el ruta_tienda_punto con id: " + id));

            rutaTiendaPuntoRepo.delete(entity);
        } catch (DataAccessException e) {
            throw new RepositoryException("Error al eliminar el ruta_tienda_punto", e);
        }
    }

    private void validarRutaExiste(Long rutaTiendaId) {
        boolean exists = rutaTiendaRepo.existsById(rutaTiendaId);
        if (!exists) {
            throw new ApplicationException("No existe la ruta_tienda con id: " + rutaTiendaId);
        }
    }

    private void validarOrdenDuplicado(Long rutaTiendaId, Integer orden, Long puntoActualId) {
        List<RutaTiendaPunto> puntos = rutaTiendaPuntoRepo.findByRutaTiendaIdOrderByOrdenAsc(rutaTiendaId);

        boolean duplicado = puntos.stream()
                .anyMatch(p -> p.getOrden().equals(orden) && (puntoActualId == null || !p.getId().equals(puntoActualId)));

        if (duplicado) {
            throw new ApplicationException("Ya existe un punto con el mismo orden para la ruta indicada");
        }
    }
}

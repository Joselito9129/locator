package org.gta.backend_locator.service.impl;

import lombok.RequiredArgsConstructor;
import org.gta.backend_locator.dto.request.RutaGeoRequest;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.RutaGeoResponse;
import org.gta.backend_locator.exception.ApplicationException;
import org.gta.backend_locator.exception.RepositoryException;
import org.gta.backend_locator.mapper.RutaGeoMapper;
import org.gta.backend_locator.model.RutaGeo;
import org.gta.backend_locator.repository.RutaGeoRepository;
import org.gta.backend_locator.service.RutaGeoService;
import org.gta.backend_locator.utils.DataValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaGeoServiceImpl implements RutaGeoService {

    private final RutaGeoRepository repository;
    private final RutaGeoMapper mapper;
    private final DataValidator dataValidator;

    @Override
    @Transactional
    public GenericResponse<RutaGeoResponse> crear(RutaGeoRequest request) {
        try {
            dataValidator.validateRutaGeoRequest(request);

            RutaGeo entity = mapper.toEntity(request);
            entity.setEstado(request.getEstado().toUpperCase());
            entity.setFechaCreacion(LocalDateTime.now());

            RutaGeo saved = repository.save(entity);

            return GenericResponse.<RutaGeoResponse>builder()
                    .success(true)
                    .message("Registro creado correctamente")
                    .data(mapper.toResponse(saved))
                    .build();

        } catch (ApplicationException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al guardar la ruta geolocalizada", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<RutaGeoResponse> obtenerPorId(Long id) {
        try {
            RutaGeo entity = repository.findById(id)
                    .orElseThrow(() -> new ApplicationException("No se encontró el registro con id: " + id));

            return GenericResponse.<RutaGeoResponse>builder()
                    .success(true)
                    .message("Registro encontrado")
                    .data(mapper.toResponse(entity))
                    .build();

        } catch (ApplicationException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al consultar el registro por id", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<RutaGeoResponse> obtenerPorGuia(String guia) {
        try {
            RutaGeo entity = repository.findByGuia(guia)
                    .orElseThrow(() -> new ApplicationException("No se encontró el registro con guía: " + guia));

            return GenericResponse.<RutaGeoResponse>builder()
                    .success(true)
                    .message("Registro encontrado")
                    .data(mapper.toResponse(entity))
                    .build();

        } catch (ApplicationException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al consultar el registro por guía", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<RutaGeoResponse>> listarPorEstado(String estado) {
        try {
            List<RutaGeoResponse> response = repository.findAllByEstado(estado.toUpperCase())
                    .stream()
                    .map(mapper::toResponse)
                    .toList();

            return GenericResponse.<List<RutaGeoResponse>>builder()
                    .success(true)
                    .message("Consulta realizada correctamente")
                    .data(response)
                    .build();

        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al listar registros por estado", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<RutaGeoResponse>> listarTodos() {
        try {
            List<RutaGeoResponse> response = repository.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();

            return GenericResponse.<List<RutaGeoResponse>>builder()
                    .success(true)
                    .message("Consulta realizada correctamente")
                    .data(response)
                    .build();

        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al listar todos los registros", ex);
        }
    }

    @Override
    @Transactional
    public GenericResponse<RutaGeoResponse> actualizar(Long id, RutaGeoRequest request) {
        try {
            dataValidator.validateRutaGeoRequest(request);

            RutaGeo entity = repository.findById(id)
                    .orElseThrow(() -> new ApplicationException("No se encontró el registro con id: " + id));

            entity.setLatitudOrigen(request.getLatitudOrigen());
            entity.setLongitudOrigen(request.getLongitudOrigen());
            entity.setLatitudDestino(request.getLatitudDestino());
            entity.setLongitudDestino(request.getLongitudDestino());
            entity.setEstado(request.getEstado().toUpperCase());
            entity.setUsuarioCreacion(request.getUsuarioCreacion());
            entity.setGuia(request.getGuia());

            RutaGeo updated = repository.save(entity);

            return GenericResponse.<RutaGeoResponse>builder()
                    .success(true)
                    .message("Registro actualizado correctamente")
                    .data(mapper.toResponse(updated))
                    .build();

        } catch (ApplicationException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al actualizar el registro", ex);
        }
    }

    @Override
    @Transactional
    public GenericResponse<Void> eliminarLogico(Long id) {
        try {
            RutaGeo entity = repository.findById(id)
                    .orElseThrow(() -> new ApplicationException("No se encontró el registro con id: " + id));

            entity.setEstado("I");
            repository.save(entity);

            return GenericResponse.<Void>builder()
                    .success(true)
                    .message("Registro inactivado correctamente")
                    .data(null)
                    .build();

        } catch (ApplicationException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new RepositoryException("Error al eliminar lógicamente el registro", ex);
        }
    }
}

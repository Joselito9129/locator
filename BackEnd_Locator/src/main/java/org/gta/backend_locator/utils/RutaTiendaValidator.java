package org.gta.backend_locator.utils;

import org.gta.backend_locator.dto.request.RutaTiendaPuntoRequest;
import org.gta.backend_locator.dto.request.RutaTiendaRequest;
import org.gta.backend_locator.exception.ApplicationException;
import org.springframework.stereotype.Component;

@Component
public class RutaTiendaValidator {

    public void validateCreateRuta(RutaTiendaRequest request) {
        if (request == null) {
            throw new ApplicationException("La solicitud de ruta es requerida");
        }
        if (request.getTiendaId() == null) {
            throw new ApplicationException("El tiendaId es requerido");
        }
        if (isBlank(request.getEstado())) {
            throw new ApplicationException("El estado es requerido");
        }
        if (isBlank(request.getUsuarioCreacion())) {
            throw new ApplicationException("El usuarioCreacion es requerido");
        }
    }

    public void validateUpdateRuta(Long id, RutaTiendaRequest request) {
        if (id == null) {
            throw new ApplicationException("El id de la ruta es requerido");
        }
        if (request == null) {
            throw new ApplicationException("La solicitud de ruta es requerida");
        }
        if (request.getTiendaId() == null) {
            throw new ApplicationException("El tiendaId es requerido");
        }
        if (isBlank(request.getEstado())) {
            throw new ApplicationException("El estado es requerido");
        }
        if (isBlank(request.getUsuarioActualizacion())) {
            throw new ApplicationException("El usuarioActualizacion es requerido");
        }
    }

    public void validateCreatePunto(RutaTiendaPuntoRequest request) {
        if (request == null) {
            throw new ApplicationException("La solicitud del punto es requerida");
        }
        if (request.getRutaTiendaId() == null) {
            throw new ApplicationException("El rutaTiendaId es requerido");
        }
        if (request.getOrden() == null) {
            throw new ApplicationException("El orden es requerido");
        }
        if (request.getOrden() <= 0) {
            throw new ApplicationException("El orden debe ser mayor a cero");
        }
        if (isBlank(request.getEstado())) {
            throw new ApplicationException("El estado es requerido");
        }
        if (isBlank(request.getUsuarioCreacion())) {
            throw new ApplicationException("El usuarioCreacion es requerido");
        }
    }

    public void validateUpdatePunto(Long id, RutaTiendaPuntoRequest request) {
        if (id == null) {
            throw new ApplicationException("El id del punto es requerido");
        }
        if (request == null) {
            throw new ApplicationException("La solicitud del punto es requerida");
        }
        if (request.getRutaTiendaId() == null) {
            throw new ApplicationException("El rutaTiendaId es requerido");
        }
        if (request.getOrden() == null) {
            throw new ApplicationException("El orden es requerido");
        }
        if (request.getOrden() <= 0) {
            throw new ApplicationException("El orden debe ser mayor a cero");
        }
        if (isBlank(request.getEstado())) {
            throw new ApplicationException("El estado es requerido");
        }
        if (isBlank(request.getUsuarioActualizacion())) {
            throw new ApplicationException("El usuarioActualizacion es requerido");
        }
    }

    public void validateId(Long id, String fieldName) {
        if (id == null) {
            throw new ApplicationException(fieldName + " es requerido");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
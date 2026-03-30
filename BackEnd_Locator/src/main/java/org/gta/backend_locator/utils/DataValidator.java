package org.gta.backend_locator.utils;

import org.gta.backend_locator.dto.request.RutaGeoRequest;
import org.gta.backend_locator.exception.ApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class DataValidator {

    public void validateRutaGeoRequest(RutaGeoRequest request) {
        if (request == null) {
            throw new ApplicationException("El request no puede ser nulo");
        }

        validateCoordinate(request.getLatitudOrigen(), "latitudOrigen", -90, 90);
        validateCoordinate(request.getLongitudOrigen(), "longitudOrigen", -180, 180);
        validateCoordinate(request.getLatitudDestino(), "latitudDestino", -90, 90);
        validateCoordinate(request.getLongitudDestino(), "longitudDestino", -180, 180);

        if (!StringUtils.hasText(request.getEstado())) {
            throw new ApplicationException("El estado es obligatorio");
        }

        if (!"A".equalsIgnoreCase(request.getEstado()) && !"I".equalsIgnoreCase(request.getEstado())) {
            throw new ApplicationException("El estado solo puede ser A o I");
        }

        if (!StringUtils.hasText(request.getUsuarioCreacion())) {
            throw new ApplicationException("El usuario_creacion es obligatorio");
        }

        if (!StringUtils.hasText(request.getGuia())) {
            throw new ApplicationException("La guía es obligatoria");
        }
    }

    private void validateCoordinate(BigDecimal value, String fieldName, double min, double max) {
        if (value == null) {
            throw new ApplicationException("El campo " + fieldName + " es obligatorio");
        }

        if (value.doubleValue() < min || value.doubleValue() > max) {
            throw new ApplicationException("El campo " + fieldName + " está fuera de rango");
        }
    }
}

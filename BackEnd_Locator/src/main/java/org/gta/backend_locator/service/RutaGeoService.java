package org.gta.backend_locator.service;

import org.gta.backend_locator.dto.request.RutaGeoRequest;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.RutaGeoResponse;

import java.util.List;

public interface RutaGeoService {

    GenericResponse<RutaGeoResponse> crear(RutaGeoRequest request);
    GenericResponse<RutaGeoResponse> obtenerPorId(Long id);
    GenericResponse<RutaGeoResponse> obtenerPorGuia(String guia);
    GenericResponse<List<RutaGeoResponse>> listarPorEstado(String estado);
    GenericResponse<List<RutaGeoResponse>> listarTodos();
    GenericResponse<RutaGeoResponse> actualizar(Long id, RutaGeoRequest request);
    GenericResponse<Void> eliminarLogico(Long id);
    
}

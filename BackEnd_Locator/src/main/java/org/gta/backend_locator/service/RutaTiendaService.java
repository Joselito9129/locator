package org.gta.backend_locator.service;

import org.gta.backend_locator.dto.request.RutaTiendaRequest;
import org.gta.backend_locator.dto.response.RutaTiendaResponse;

import java.util.List;

public interface RutaTiendaService {

    RutaTiendaResponse crearRuta(RutaTiendaRequest request);
    RutaTiendaResponse actualizarRuta(Long id, RutaTiendaRequest request);
    RutaTiendaResponse obtenerRutaPorId(Long id);
    List<RutaTiendaResponse> obtenerRutas();
    List<RutaTiendaResponse> consultarRutasPorTienda(Long tiendaId);
    void eliminarRuta(Long id);
}
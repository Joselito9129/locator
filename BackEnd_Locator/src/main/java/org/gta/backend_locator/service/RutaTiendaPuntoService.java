package org.gta.backend_locator.service;

import org.gta.backend_locator.dto.request.RutaTiendaPuntoRequest;
import org.gta.backend_locator.dto.response.RutaTiendaPuntoResponse;

import java.util.List;

public interface RutaTiendaPuntoService {

    RutaTiendaPuntoResponse crearPunto(RutaTiendaPuntoRequest request);
    RutaTiendaPuntoResponse actualizarPunto(Long id, RutaTiendaPuntoRequest request);
    RutaTiendaPuntoResponse obtenerPuntoPorId(Long id);
    List<RutaTiendaPuntoResponse> listarPuntosOrdenados(Long rutaTiendaId);
    void eliminarPunto(Long id);
}
package org.gta.backend_locator.controller;

import lombok.RequiredArgsConstructor;
import org.gta.backend_locator.dto.request.RutaGeoRequest;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.RutaGeoResponse;
import org.gta.backend_locator.service.RutaGeoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-geo")
@RequiredArgsConstructor
public class RutaGeoController {

    private final RutaGeoService service;

    @PostMapping
    public GenericResponse<RutaGeoResponse> crear(@RequestBody RutaGeoRequest request) {
        return service.crear(request);
    }

    @GetMapping("/{id}")
    public GenericResponse<RutaGeoResponse> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/guia/{guia}")
    public GenericResponse<RutaGeoResponse> obtenerPorGuia(@PathVariable String guia) {
        return service.obtenerPorGuia(guia);
    }

    @GetMapping("/estado/{estado}")
    public GenericResponse<List<RutaGeoResponse>> listarPorEstado(@PathVariable String estado) {
        return service.listarPorEstado(estado);
    }

    @GetMapping
    public GenericResponse<List<RutaGeoResponse>> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}")
    public GenericResponse<RutaGeoResponse> actualizar(@PathVariable Long id,
                                                                   @RequestBody RutaGeoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> eliminarLogico(@PathVariable Long id) {
        return service.eliminarLogico(id);
    }
    
}

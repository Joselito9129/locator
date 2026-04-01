package org.gta.backend_locator.controller;

import org.gta.backend_locator.dto.request.RutaTiendaRequest;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.RutaTiendaResponse;
import org.gta.backend_locator.service.RutaTiendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ruta-tienda")
public class RutaTiendaController {

    private final RutaTiendaService rutaTiendaService;

    public RutaTiendaController(RutaTiendaService rutaTiendaService) {
        this.rutaTiendaService = rutaTiendaService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<RutaTiendaResponse>> crearRuta(@RequestBody RutaTiendaRequest request) {
        RutaTiendaResponse data = rutaTiendaService.crearRuta(request);
        return ResponseEntity.ok(buildResponse(true, "Ruta creada correctamente", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<RutaTiendaResponse>> actualizarRuta(@PathVariable Long id,
                                                                              @RequestBody RutaTiendaRequest request) {
        RutaTiendaResponse data = rutaTiendaService.actualizarRuta(id, request);
        return ResponseEntity.ok(buildResponse(true, "Ruta actualizada correctamente", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<RutaTiendaResponse>> obtenerRutaPorId(@PathVariable Long id) {
        RutaTiendaResponse data = rutaTiendaService.obtenerRutaPorId(id);
        return ResponseEntity.ok(buildResponse(true, "Ruta consultada correctamente", data));
    }

    @GetMapping()
    public ResponseEntity<GenericResponse<List<RutaTiendaResponse>>> obtenerRutas() {
        List<RutaTiendaResponse> data = rutaTiendaService.obtenerRutas();
        return ResponseEntity.ok(buildResponse(true, "Ruta consultada correctamente", data));
    }

    @GetMapping("/tienda/{tiendaId}")
    public ResponseEntity<GenericResponse<List<RutaTiendaResponse>>> consultarPorTienda(@PathVariable Long tiendaId) {
        List<RutaTiendaResponse> data = rutaTiendaService.consultarRutasPorTienda(tiendaId);
        return ResponseEntity.ok(buildResponse(true, "Rutas consultadas correctamente", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> eliminarRuta(@PathVariable Long id) {
        rutaTiendaService.eliminarRuta(id);
        return ResponseEntity.ok(buildResponse(true, "Ruta eliminada correctamente", null));
    }

    private <T> GenericResponse<T> buildResponse(boolean success, String message, T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}

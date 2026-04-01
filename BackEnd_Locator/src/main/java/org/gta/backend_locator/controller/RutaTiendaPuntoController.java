package org.gta.backend_locator.controller;

import org.gta.backend_locator.dto.request.RutaTiendaPuntoRequest;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.RutaTiendaPuntoResponse;
import org.gta.backend_locator.service.RutaTiendaPuntoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ruta-tienda-punto")
public class RutaTiendaPuntoController {

    private final RutaTiendaPuntoService rutaTiendaPuntoService;

    public RutaTiendaPuntoController(RutaTiendaPuntoService rutaTiendaPuntoService) {
        this.rutaTiendaPuntoService = rutaTiendaPuntoService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<RutaTiendaPuntoResponse>> crearPunto(@RequestBody RutaTiendaPuntoRequest request) {
        RutaTiendaPuntoResponse data = rutaTiendaPuntoService.crearPunto(request);
        return ResponseEntity.ok(buildResponse(true, "Punto creado correctamente", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<RutaTiendaPuntoResponse>> actualizarPunto(@PathVariable Long id,
                                                                                    @RequestBody RutaTiendaPuntoRequest request) {
        RutaTiendaPuntoResponse data = rutaTiendaPuntoService.actualizarPunto(id, request);
        return ResponseEntity.ok(buildResponse(true, "Punto actualizado correctamente", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<RutaTiendaPuntoResponse>> obtenerPuntoPorId(@PathVariable Long id) {
        RutaTiendaPuntoResponse data = rutaTiendaPuntoService.obtenerPuntoPorId(id);
        return ResponseEntity.ok(buildResponse(true, "Punto consultado correctamente", data));
    }

    @GetMapping("/ruta/{rutaTiendaId}")
    public ResponseEntity<GenericResponse<List<RutaTiendaPuntoResponse>>> listarPuntosOrdenados(@PathVariable Long rutaTiendaId) {
        List<RutaTiendaPuntoResponse> data = rutaTiendaPuntoService.listarPuntosOrdenados(rutaTiendaId);
        return ResponseEntity.ok(buildResponse(true, "Puntos consultados correctamente", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> eliminarPunto(@PathVariable Long id) {
        rutaTiendaPuntoService.eliminarPunto(id);
        return ResponseEntity.ok(buildResponse(true, "Punto eliminado correctamente", null));
    }

    private <T> GenericResponse<T> buildResponse(boolean success, String message, T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}

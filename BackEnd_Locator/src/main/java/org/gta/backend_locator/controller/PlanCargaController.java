package org.gta.backend_locator.controller;

import lombok.RequiredArgsConstructor;
import org.gta.backend_locator.dto.response.GenericResponse;
import org.gta.backend_locator.dto.response.PlanCargaCamionResponse;
import org.gta.backend_locator.dto.response.PlanCargaResponse;
import org.gta.backend_locator.service.PlanCargaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plan-carga")
@RequiredArgsConstructor
public class PlanCargaController {

    private final PlanCargaService service;

    @PostMapping("/generar/{ventanaId}")
    public ResponseEntity<GenericResponse<PlanCargaResponse>> generar(@PathVariable Long ventanaId) {

        PlanCargaResponse response = service.generarPlan(ventanaId);

        return ResponseEntity.ok(buildResponse(true, "Plan creado correctamente", response));
    }

    @GetMapping()
    public ResponseEntity<GenericResponse<List<PlanCargaCamionResponse>>> getPlanes() {

        List<PlanCargaCamionResponse> response = service.getPlanes();

        return ResponseEntity.ok(buildResponse(true, "Consulta realizada correctamente", response));
    }

    private <T> GenericResponse<T> buildResponse(boolean success, String message, T data) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}

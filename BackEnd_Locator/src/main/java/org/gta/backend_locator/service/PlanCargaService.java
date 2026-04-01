package org.gta.backend_locator.service;

import org.gta.backend_locator.dto.response.PlanCargaCamionResponse;
import org.gta.backend_locator.dto.response.PlanCargaResponse;

import java.util.List;

public interface PlanCargaService {
    PlanCargaResponse generarPlan(Long ventanaId);

    List<PlanCargaCamionResponse> getPlanes();
}
package org.gta.backend_locator.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCargaResponse {
    public Long planId;
    public List<CamionResponse> camiones;
}
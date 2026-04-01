package org.gta.backend_locator.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCargaCamionResponse {

    private Long idPlanCamion;
    private Long planId;
    private String tipoCamion;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private BigDecimal pesoActual;
    private BigDecimal volumenActual;
    private BigDecimal porcentajeOcupacion;
    private String estado;
    private List<PlanCargaTiendaResponse> tiendas;
}

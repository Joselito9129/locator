package org.gta.backend_locator.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCargaTiendaResponse {

    private Long tiendaId;
    private BigDecimal pesoAsignado;
    private BigDecimal volumenAsignado;
}

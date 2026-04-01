package org.gta.backend_locator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CamionResponse {
    public String tipoCamion;
    public BigDecimal porcentajeOcupacion;
    public BigDecimal pesoActual;
    public List<TiendaCargaResponse> tiendas;
}

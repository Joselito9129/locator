package org.gta.backend_locator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TiendaCargaResponse {
    public Long tiendaId;
    public BigDecimal peso;
}

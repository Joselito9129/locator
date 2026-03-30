package org.gta.backend_locator.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaGeoRequest extends GenericRequest {

    private BigDecimal latitudOrigen;
    private BigDecimal longitudOrigen;
    private BigDecimal latitudDestino;
    private BigDecimal longitudDestino;
    private String estado;
    private String usuarioCreacion;
    private String guia;

}

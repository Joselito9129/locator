package org.gta.backend_locator.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaGeoResponse extends GenericResponse {

    private Long id;
    private BigDecimal latitudOrigen;
    private BigDecimal longitudOrigen;
    private BigDecimal latitudDestino;
    private BigDecimal longitudDestino;
    private String estado;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String guia;

}

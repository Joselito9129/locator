package org.gta.backend_locator.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaGeoResponse {

    private Long id;
    private BigDecimal latitudOrigen;
    private BigDecimal longitudOrigen;
    private BigDecimal latitudDestino;
    private BigDecimal longitudDestino;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime etainicio;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime etaactual;
    private String estado;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String guia;

}

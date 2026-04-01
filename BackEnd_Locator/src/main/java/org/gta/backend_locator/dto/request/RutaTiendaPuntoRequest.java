package org.gta.backend_locator.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaTiendaPuntoRequest {

    private Long rutaTiendaId;
    private Integer orden;
    private String estado;
    private String usuarioCreacion;
    private String usuarioActualizacion;
}

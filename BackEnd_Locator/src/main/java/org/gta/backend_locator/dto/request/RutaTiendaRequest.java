package org.gta.backend_locator.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaTiendaRequest {

    private Long tiendaId;
    private String estado;
    private String usuarioCreacion;
    private String usuarioActualizacion;
}

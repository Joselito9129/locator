package org.gta.backend_locator.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ruta_tienda_punto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaTiendaPunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta_punto")
    private Long id;

    @Column(name = "id_ruta", nullable = false)
    private Long rutaTiendaId;

    @Column(name = "orden_punto", nullable = false)
    private Integer orden;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "tipo_punto")
    private String tipoPunto;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "usuario_creacion", nullable = false, length = 100)
    private String usuarioCreacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_actualizacion", length = 100)
    private String usuarioActualizacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}

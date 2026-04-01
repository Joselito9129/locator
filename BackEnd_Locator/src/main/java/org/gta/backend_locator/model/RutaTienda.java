package org.gta.backend_locator.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ruta_tienda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutaTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long id;

    @Column(name = "id_tienda", nullable = false)
    private Long tiendaId;

    @Column(name = "id_centro", nullable = false)
    private Long centroId;

    @Column(name = "nombre_ruta", nullable = false, length = 20)
    private String nombreRuta;

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

package org.gta.backend_locator.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plan_carga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;

    private Long ventanaId;
    private LocalDate fecha;
    private String estado;

    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;

    private String usuarioActualizacion;
    private LocalDateTime fechaActualizacion;
}
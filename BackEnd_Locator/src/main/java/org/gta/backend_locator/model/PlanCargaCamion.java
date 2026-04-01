package org.gta.backend_locator.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plan_carga_camion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCargaCamion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanCamion;

    private Long planId;
    private String tipoCamion;

    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;

    private BigDecimal pesoActual;
    private BigDecimal volumenActual;

    private BigDecimal porcentajeOcupacion;

    private String estado;

    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;

    private String usuarioActualizacion;
    private LocalDateTime fechaActualizacion;
}
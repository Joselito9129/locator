package org.gta.backend_locator.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plan_carga_camion_tienda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCargaCamionTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlanCamionTienda;

    private Long planCamionId;
    private Long tiendaId;

    private BigDecimal pesoAsignado;
    private BigDecimal volumenAsignado;

    private String estado;

    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;

    private String usuarioActualizacion;
    private LocalDateTime fechaActualizacion;
}

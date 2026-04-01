package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.PlanCargaCamionTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanCargaCamionTiendaRepository extends JpaRepository<PlanCargaCamionTienda, Long> {
    List<PlanCargaCamionTienda> findByPlanCamionIdOrderByTiendaIdAsc(Long planCamionId);
}

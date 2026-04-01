package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.PlanCargaCamion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanCargaCamionRepository extends JpaRepository<PlanCargaCamion, Long> {
    List<PlanCargaCamion> findAllByOrderByIdPlanCamionDesc();
}

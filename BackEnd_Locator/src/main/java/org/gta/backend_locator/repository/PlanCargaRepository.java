package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.PlanCarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCargaRepository extends JpaRepository<PlanCarga, Long> {}

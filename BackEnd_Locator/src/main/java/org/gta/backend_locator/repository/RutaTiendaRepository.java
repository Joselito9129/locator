package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.RutaTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaTiendaRepository extends JpaRepository<RutaTienda, Long> {

    List<RutaTienda> findByTiendaIdOrderByFechaCreacionDesc(Long tiendaId);
}
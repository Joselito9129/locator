package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.RutaTiendaPunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaTiendaPuntoRepository extends JpaRepository<RutaTiendaPunto, Long> {

    List<RutaTiendaPunto> findByRutaTiendaIdOrderByOrdenAsc(Long rutaTiendaId);

    boolean existsByRutaTiendaIdAndOrden(Long rutaTiendaId, Integer orden);

    boolean existsByRutaTiendaId(Long rutaTiendaId);
}

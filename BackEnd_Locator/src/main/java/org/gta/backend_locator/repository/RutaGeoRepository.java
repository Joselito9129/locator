package org.gta.backend_locator.repository;

import org.gta.backend_locator.model.RutaGeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaGeoRepository extends JpaRepository<RutaGeo, Long> {

    Optional<RutaGeo> findByGuia(String guia);

    List<RutaGeo> findAllByEstado(String estado);

    List<RutaGeo> findAllByGuia(String guia);
    boolean existsByGuia(String guia);

}

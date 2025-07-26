package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Cargoes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cargoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargoesRepository extends JpaRepository<Cargoes, Long> {}

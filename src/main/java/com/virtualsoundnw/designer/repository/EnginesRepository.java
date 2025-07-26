package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Engines;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Engines entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnginesRepository extends JpaRepository<Engines, Long> {}

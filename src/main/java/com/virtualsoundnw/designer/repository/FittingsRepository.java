package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Fittings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fittings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FittingsRepository extends JpaRepository<Fittings, Long> {}

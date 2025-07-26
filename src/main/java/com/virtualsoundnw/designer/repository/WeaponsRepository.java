package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Weapons;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Weapons entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponsRepository extends JpaRepository<Weapons, Long> {}

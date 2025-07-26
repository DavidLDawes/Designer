package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Vehicles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long> {}

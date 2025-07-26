package com.virtualsoundnw.designer.repository;

import com.virtualsoundnw.designer.domain.Ships;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ships entity.
 */
@Repository
public interface ShipsRepository extends JpaRepository<Ships, Long> {
    @Query("select ships from Ships ships where ships.user.login = ?#{authentication.name}")
    List<Ships> findByUserIsCurrentUser();

    default Optional<Ships> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ships> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ships> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select ships from Ships ships left join fetch ships.user", countQuery = "select count(ships) from Ships ships")
    Page<Ships> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ships from Ships ships left join fetch ships.user")
    List<Ships> findAllWithToOneRelationships();

    @Query("select ships from Ships ships left join fetch ships.user where ships.id =:id")
    Optional<Ships> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Ships;
import com.virtualsoundnw.designer.repository.ShipsRepository;
import com.virtualsoundnw.designer.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Ships}.
 */
@RestController
@RequestMapping("/api/ships")
@Transactional
public class ShipsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipsResource.class);

    private static final String ENTITY_NAME = "ships";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipsRepository shipsRepository;

    public ShipsResource(ShipsRepository shipsRepository) {
        this.shipsRepository = shipsRepository;
    }

    /**
     * {@code POST  /ships} : Create a new ships.
     *
     * @param ships the ships to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ships, or with status {@code 400 (Bad Request)} if the ships has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ships> createShips(@Valid @RequestBody Ships ships) throws URISyntaxException {
        LOG.debug("REST request to save Ships : {}", ships);
        if (ships.getId() != null) {
            throw new BadRequestAlertException("A new ships cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ships = shipsRepository.save(ships);
        return ResponseEntity.created(new URI("/api/ships/" + ships.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ships.getId().toString()))
            .body(ships);
    }

    /**
     * {@code PUT  /ships/:id} : Updates an existing ships.
     *
     * @param id the id of the ships to save.
     * @param ships the ships to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ships,
     * or with status {@code 400 (Bad Request)} if the ships is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ships couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ships> updateShips(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ships ships)
        throws URISyntaxException {
        LOG.debug("REST request to update Ships : {}, {}", id, ships);
        if (ships.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ships.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ships = shipsRepository.save(ships);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ships.getId().toString()))
            .body(ships);
    }

    /**
     * {@code PATCH  /ships/:id} : Partial updates given fields of an existing ships, field will ignore if it is null
     *
     * @param id the id of the ships to save.
     * @param ships the ships to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ships,
     * or with status {@code 400 (Bad Request)} if the ships is not valid,
     * or with status {@code 404 (Not Found)} if the ships is not found,
     * or with status {@code 500 (Internal Server Error)} if the ships couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ships> partialUpdateShips(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ships ships
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ships partially : {}, {}", id, ships);
        if (ships.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ships.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ships> result = shipsRepository
            .findById(ships.getId())
            .map(existingShips -> {
                if (ships.getShipId() != null) {
                    existingShips.setShipId(ships.getShipId());
                }
                if (ships.getUserId() != null) {
                    existingShips.setUserId(ships.getUserId());
                }
                if (ships.getTl() != null) {
                    existingShips.setTl(ships.getTl());
                }
                if (ships.getTonnage() != null) {
                    existingShips.setTonnage(ships.getTonnage());
                }
                if (ships.getConf() != null) {
                    existingShips.setConf(ships.getConf());
                }
                if (ships.getCode() != null) {
                    existingShips.setCode(ships.getCode());
                }
                if (ships.getHull() != null) {
                    existingShips.setHull(ships.getHull());
                }
                if (ships.getStructure() != null) {
                    existingShips.setStructure(ships.getStructure());
                }
                if (ships.getSections() != null) {
                    existingShips.setSections(ships.getSections());
                }
                if (ships.getCapital() != null) {
                    existingShips.setCapital(ships.getCapital());
                }
                if (ships.getMilitary() != null) {
                    existingShips.setMilitary(ships.getMilitary());
                }

                return existingShips;
            })
            .map(shipsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ships.getId().toString())
        );
    }

    /**
     * {@code GET  /ships} : get all the ships.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ships in body.
     */
    @GetMapping("")
    public List<Ships> getAllShips(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Ships");
        if (eagerload) {
            return shipsRepository.findAllWithEagerRelationships();
        } else {
            return shipsRepository.findAll();
        }
    }

    /**
     * {@code GET  /ships/:id} : get the "id" ships.
     *
     * @param id the id of the ships to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ships, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ships> getShips(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ships : {}", id);
        Optional<Ships> ships = shipsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ships);
    }

    /**
     * {@code DELETE  /ships/:id} : delete the "id" ships.
     *
     * @param id the id of the ships to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShips(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ships : {}", id);
        shipsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

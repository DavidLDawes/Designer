package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Engines;
import com.virtualsoundnw.designer.repository.EnginesRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Engines}.
 */
@RestController
@RequestMapping("/api/engines")
@Transactional
public class EnginesResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnginesResource.class);

    private static final String ENTITY_NAME = "engines";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnginesRepository enginesRepository;

    public EnginesResource(EnginesRepository enginesRepository) {
        this.enginesRepository = enginesRepository;
    }

    /**
     * {@code POST  /engines} : Create a new engines.
     *
     * @param engines the engines to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new engines, or with status {@code 400 (Bad Request)} if the engines has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Engines> createEngines(@Valid @RequestBody Engines engines) throws URISyntaxException {
        LOG.debug("REST request to save Engines : {}", engines);
        if (engines.getId() != null) {
            throw new BadRequestAlertException("A new engines cannot already have an ID", ENTITY_NAME, "idexists");
        }
        engines = enginesRepository.save(engines);
        return ResponseEntity.created(new URI("/api/engines/" + engines.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, engines.getId().toString()))
            .body(engines);
    }

    /**
     * {@code PUT  /engines/:id} : Updates an existing engines.
     *
     * @param id the id of the engines to save.
     * @param engines the engines to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated engines,
     * or with status {@code 400 (Bad Request)} if the engines is not valid,
     * or with status {@code 500 (Internal Server Error)} if the engines couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Engines> updateEngines(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Engines engines
    ) throws URISyntaxException {
        LOG.debug("REST request to update Engines : {}, {}", id, engines);
        if (engines.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, engines.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enginesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        engines = enginesRepository.save(engines);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, engines.getId().toString()))
            .body(engines);
    }

    /**
     * {@code PATCH  /engines/:id} : Partial updates given fields of an existing engines, field will ignore if it is null
     *
     * @param id the id of the engines to save.
     * @param engines the engines to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated engines,
     * or with status {@code 400 (Bad Request)} if the engines is not valid,
     * or with status {@code 404 (Not Found)} if the engines is not found,
     * or with status {@code 500 (Internal Server Error)} if the engines couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Engines> partialUpdateEngines(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Engines engines
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Engines partially : {}, {}", id, engines);
        if (engines.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, engines.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enginesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Engines> result = enginesRepository
            .findById(engines.getId())
            .map(existingEngines -> {
                if (engines.geteShipId() != null) {
                    existingEngines.seteShipId(engines.geteShipId());
                }
                if (engines.getEngine() != null) {
                    existingEngines.setEngine(engines.getEngine());
                }
                if (engines.getMass() != null) {
                    existingEngines.setMass(engines.getMass());
                }
                if (engines.getCost() != null) {
                    existingEngines.setCost(engines.getCost());
                }
                if (engines.getTl() != null) {
                    existingEngines.setTl(engines.getTl());
                }

                return existingEngines;
            })
            .map(enginesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, engines.getId().toString())
        );
    }

    /**
     * {@code GET  /engines} : get all the engines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of engines in body.
     */
    @GetMapping("")
    public List<Engines> getAllEngines() {
        LOG.debug("REST request to get all Engines");
        return enginesRepository.findAll();
    }

    /**
     * {@code GET  /engines/:id} : get the "id" engines.
     *
     * @param id the id of the engines to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the engines, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Engines> getEngines(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Engines : {}", id);
        Optional<Engines> engines = enginesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(engines);
    }

    /**
     * {@code DELETE  /engines/:id} : delete the "id" engines.
     *
     * @param id the id of the engines to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngines(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Engines : {}", id);
        enginesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

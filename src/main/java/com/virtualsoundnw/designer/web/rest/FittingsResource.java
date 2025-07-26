package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Fittings;
import com.virtualsoundnw.designer.repository.FittingsRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Fittings}.
 */
@RestController
@RequestMapping("/api/fittings")
@Transactional
public class FittingsResource {

    private static final Logger LOG = LoggerFactory.getLogger(FittingsResource.class);

    private static final String ENTITY_NAME = "fittings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FittingsRepository fittingsRepository;

    public FittingsResource(FittingsRepository fittingsRepository) {
        this.fittingsRepository = fittingsRepository;
    }

    /**
     * {@code POST  /fittings} : Create a new fittings.
     *
     * @param fittings the fittings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fittings, or with status {@code 400 (Bad Request)} if the fittings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Fittings> createFittings(@Valid @RequestBody Fittings fittings) throws URISyntaxException {
        LOG.debug("REST request to save Fittings : {}", fittings);
        if (fittings.getId() != null) {
            throw new BadRequestAlertException("A new fittings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fittings = fittingsRepository.save(fittings);
        return ResponseEntity.created(new URI("/api/fittings/" + fittings.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fittings.getId().toString()))
            .body(fittings);
    }

    /**
     * {@code PUT  /fittings/:id} : Updates an existing fittings.
     *
     * @param id the id of the fittings to save.
     * @param fittings the fittings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fittings,
     * or with status {@code 400 (Bad Request)} if the fittings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fittings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fittings> updateFittings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fittings fittings
    ) throws URISyntaxException {
        LOG.debug("REST request to update Fittings : {}, {}", id, fittings);
        if (fittings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fittings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fittingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fittings = fittingsRepository.save(fittings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fittings.getId().toString()))
            .body(fittings);
    }

    /**
     * {@code PATCH  /fittings/:id} : Partial updates given fields of an existing fittings, field will ignore if it is null
     *
     * @param id the id of the fittings to save.
     * @param fittings the fittings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fittings,
     * or with status {@code 400 (Bad Request)} if the fittings is not valid,
     * or with status {@code 404 (Not Found)} if the fittings is not found,
     * or with status {@code 500 (Internal Server Error)} if the fittings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fittings> partialUpdateFittings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fittings fittings
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Fittings partially : {}, {}", id, fittings);
        if (fittings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fittings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fittingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fittings> result = fittingsRepository
            .findById(fittings.getId())
            .map(existingFittings -> {
                if (fittings.getFitting() != null) {
                    existingFittings.setFitting(fittings.getFitting());
                }
                if (fittings.getfShipId() != null) {
                    existingFittings.setfShipId(fittings.getfShipId());
                }
                if (fittings.getCount() != null) {
                    existingFittings.setCount(fittings.getCount());
                }
                if (fittings.getMass() != null) {
                    existingFittings.setMass(fittings.getMass());
                }
                if (fittings.getCost() != null) {
                    existingFittings.setCost(fittings.getCost());
                }
                if (fittings.getArmored() != null) {
                    existingFittings.setArmored(fittings.getArmored());
                }

                return existingFittings;
            })
            .map(fittingsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fittings.getId().toString())
        );
    }

    /**
     * {@code GET  /fittings} : get all the fittings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fittings in body.
     */
    @GetMapping("")
    public List<Fittings> getAllFittings() {
        LOG.debug("REST request to get all Fittings");
        return fittingsRepository.findAll();
    }

    /**
     * {@code GET  /fittings/:id} : get the "id" fittings.
     *
     * @param id the id of the fittings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fittings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fittings> getFittings(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Fittings : {}", id);
        Optional<Fittings> fittings = fittingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fittings);
    }

    /**
     * {@code DELETE  /fittings/:id} : delete the "id" fittings.
     *
     * @param id the id of the fittings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFittings(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Fittings : {}", id);
        fittingsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Defenses;
import com.virtualsoundnw.designer.repository.DefensesRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Defenses}.
 */
@RestController
@RequestMapping("/api/defenses")
@Transactional
public class DefensesResource {

    private static final Logger LOG = LoggerFactory.getLogger(DefensesResource.class);

    private static final String ENTITY_NAME = "defenses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DefensesRepository defensesRepository;

    public DefensesResource(DefensesRepository defensesRepository) {
        this.defensesRepository = defensesRepository;
    }

    /**
     * {@code POST  /defenses} : Create a new defenses.
     *
     * @param defenses the defenses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new defenses, or with status {@code 400 (Bad Request)} if the defenses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Defenses> createDefenses(@Valid @RequestBody Defenses defenses) throws URISyntaxException {
        LOG.debug("REST request to save Defenses : {}", defenses);
        if (defenses.getId() != null) {
            throw new BadRequestAlertException("A new defenses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        defenses = defensesRepository.save(defenses);
        return ResponseEntity.created(new URI("/api/defenses/" + defenses.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, defenses.getId().toString()))
            .body(defenses);
    }

    /**
     * {@code PUT  /defenses/:id} : Updates an existing defenses.
     *
     * @param id the id of the defenses to save.
     * @param defenses the defenses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defenses,
     * or with status {@code 400 (Bad Request)} if the defenses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the defenses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Defenses> updateDefenses(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Defenses defenses
    ) throws URISyntaxException {
        LOG.debug("REST request to update Defenses : {}, {}", id, defenses);
        if (defenses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defenses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defensesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        defenses = defensesRepository.save(defenses);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, defenses.getId().toString()))
            .body(defenses);
    }

    /**
     * {@code PATCH  /defenses/:id} : Partial updates given fields of an existing defenses, field will ignore if it is null
     *
     * @param id the id of the defenses to save.
     * @param defenses the defenses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defenses,
     * or with status {@code 400 (Bad Request)} if the defenses is not valid,
     * or with status {@code 404 (Not Found)} if the defenses is not found,
     * or with status {@code 500 (Internal Server Error)} if the defenses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Defenses> partialUpdateDefenses(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Defenses defenses
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Defenses partially : {}, {}", id, defenses);
        if (defenses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defenses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defensesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Defenses> result = defensesRepository
            .findById(defenses.getId())
            .map(existingDefenses -> {
                if (defenses.getDefense() != null) {
                    existingDefenses.setDefense(defenses.getDefense());
                }
                if (defenses.getdShipId() != null) {
                    existingDefenses.setdShipId(defenses.getdShipId());
                }
                if (defenses.getBattery() != null) {
                    existingDefenses.setBattery(defenses.getBattery());
                }
                if (defenses.getCount() != null) {
                    existingDefenses.setCount(defenses.getCount());
                }
                if (defenses.getMass() != null) {
                    existingDefenses.setMass(defenses.getMass());
                }
                if (defenses.getCost() != null) {
                    existingDefenses.setCost(defenses.getCost());
                }
                if (defenses.getArmored() != null) {
                    existingDefenses.setArmored(defenses.getArmored());
                }

                return existingDefenses;
            })
            .map(defensesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, defenses.getId().toString())
        );
    }

    /**
     * {@code GET  /defenses} : get all the defenses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of defenses in body.
     */
    @GetMapping("")
    public List<Defenses> getAllDefenses() {
        LOG.debug("REST request to get all Defenses");
        return defensesRepository.findAll();
    }

    /**
     * {@code GET  /defenses/:id} : get the "id" defenses.
     *
     * @param id the id of the defenses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the defenses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Defenses> getDefenses(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Defenses : {}", id);
        Optional<Defenses> defenses = defensesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(defenses);
    }

    /**
     * {@code DELETE  /defenses/:id} : delete the "id" defenses.
     *
     * @param id the id of the defenses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefenses(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Defenses : {}", id);
        defensesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

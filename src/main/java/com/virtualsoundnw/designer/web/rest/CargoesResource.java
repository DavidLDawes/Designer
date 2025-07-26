package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Cargoes;
import com.virtualsoundnw.designer.repository.CargoesRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Cargoes}.
 */
@RestController
@RequestMapping("/api/cargoes")
@Transactional
public class CargoesResource {

    private static final Logger LOG = LoggerFactory.getLogger(CargoesResource.class);

    private static final String ENTITY_NAME = "cargoes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoesRepository cargoesRepository;

    public CargoesResource(CargoesRepository cargoesRepository) {
        this.cargoesRepository = cargoesRepository;
    }

    /**
     * {@code POST  /cargoes} : Create a new cargoes.
     *
     * @param cargoes the cargoes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoes, or with status {@code 400 (Bad Request)} if the cargoes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Cargoes> createCargoes(@Valid @RequestBody Cargoes cargoes) throws URISyntaxException {
        LOG.debug("REST request to save Cargoes : {}", cargoes);
        if (cargoes.getId() != null) {
            throw new BadRequestAlertException("A new cargoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cargoes = cargoesRepository.save(cargoes);
        return ResponseEntity.created(new URI("/api/cargoes/" + cargoes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cargoes.getId().toString()))
            .body(cargoes);
    }

    /**
     * {@code PUT  /cargoes/:id} : Updates an existing cargoes.
     *
     * @param id the id of the cargoes to save.
     * @param cargoes the cargoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoes,
     * or with status {@code 400 (Bad Request)} if the cargoes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cargoes> updateCargoes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cargoes cargoes
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cargoes : {}, {}", id, cargoes);
        if (cargoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cargoes = cargoesRepository.save(cargoes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoes.getId().toString()))
            .body(cargoes);
    }

    /**
     * {@code PATCH  /cargoes/:id} : Partial updates given fields of an existing cargoes, field will ignore if it is null
     *
     * @param id the id of the cargoes to save.
     * @param cargoes the cargoes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoes,
     * or with status {@code 400 (Bad Request)} if the cargoes is not valid,
     * or with status {@code 404 (Not Found)} if the cargoes is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cargoes> partialUpdateCargoes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cargoes cargoes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cargoes partially : {}, {}", id, cargoes);
        if (cargoes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cargoes> result = cargoesRepository
            .findById(cargoes.getId())
            .map(existingCargoes -> {
                if (cargoes.getCargo() != null) {
                    existingCargoes.setCargo(cargoes.getCargo());
                }
                if (cargoes.getcShipId() != null) {
                    existingCargoes.setcShipId(cargoes.getcShipId());
                }
                if (cargoes.getMass() != null) {
                    existingCargoes.setMass(cargoes.getMass());
                }
                if (cargoes.getCost() != null) {
                    existingCargoes.setCost(cargoes.getCost());
                }
                if (cargoes.getArmored() != null) {
                    existingCargoes.setArmored(cargoes.getArmored());
                }

                return existingCargoes;
            })
            .map(cargoesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoes.getId().toString())
        );
    }

    /**
     * {@code GET  /cargoes} : get all the cargoes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoes in body.
     */
    @GetMapping("")
    public List<Cargoes> getAllCargoes() {
        LOG.debug("REST request to get all Cargoes");
        return cargoesRepository.findAll();
    }

    /**
     * {@code GET  /cargoes/:id} : get the "id" cargoes.
     *
     * @param id the id of the cargoes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cargoes> getCargoes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cargoes : {}", id);
        Optional<Cargoes> cargoes = cargoesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cargoes);
    }

    /**
     * {@code DELETE  /cargoes/:id} : delete the "id" cargoes.
     *
     * @param id the id of the cargoes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargoes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cargoes : {}", id);
        cargoesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Weapons;
import com.virtualsoundnw.designer.repository.WeaponsRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Weapons}.
 */
@RestController
@RequestMapping("/api/weapons")
@Transactional
public class WeaponsResource {

    private static final Logger LOG = LoggerFactory.getLogger(WeaponsResource.class);

    private static final String ENTITY_NAME = "weapons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeaponsRepository weaponsRepository;

    public WeaponsResource(WeaponsRepository weaponsRepository) {
        this.weaponsRepository = weaponsRepository;
    }

    /**
     * {@code POST  /weapons} : Create a new weapons.
     *
     * @param weapons the weapons to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weapons, or with status {@code 400 (Bad Request)} if the weapons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Weapons> createWeapons(@Valid @RequestBody Weapons weapons) throws URISyntaxException {
        LOG.debug("REST request to save Weapons : {}", weapons);
        if (weapons.getId() != null) {
            throw new BadRequestAlertException("A new weapons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        weapons = weaponsRepository.save(weapons);
        return ResponseEntity.created(new URI("/api/weapons/" + weapons.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, weapons.getId().toString()))
            .body(weapons);
    }

    /**
     * {@code PUT  /weapons/:id} : Updates an existing weapons.
     *
     * @param id the id of the weapons to save.
     * @param weapons the weapons to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weapons,
     * or with status {@code 400 (Bad Request)} if the weapons is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weapons couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Weapons> updateWeapons(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Weapons weapons
    ) throws URISyntaxException {
        LOG.debug("REST request to update Weapons : {}, {}", id, weapons);
        if (weapons.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weapons.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        weapons = weaponsRepository.save(weapons);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weapons.getId().toString()))
            .body(weapons);
    }

    /**
     * {@code PATCH  /weapons/:id} : Partial updates given fields of an existing weapons, field will ignore if it is null
     *
     * @param id the id of the weapons to save.
     * @param weapons the weapons to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weapons,
     * or with status {@code 400 (Bad Request)} if the weapons is not valid,
     * or with status {@code 404 (Not Found)} if the weapons is not found,
     * or with status {@code 500 (Internal Server Error)} if the weapons couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Weapons> partialUpdateWeapons(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Weapons weapons
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Weapons partially : {}, {}", id, weapons);
        if (weapons.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weapons.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weaponsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Weapons> result = weaponsRepository
            .findById(weapons.getId())
            .map(existingWeapons -> {
                if (weapons.getWeapon() != null) {
                    existingWeapons.setWeapon(weapons.getWeapon());
                }
                if (weapons.getwShipId() != null) {
                    existingWeapons.setwShipId(weapons.getwShipId());
                }
                if (weapons.getBattery() != null) {
                    existingWeapons.setBattery(weapons.getBattery());
                }
                if (weapons.getCount() != null) {
                    existingWeapons.setCount(weapons.getCount());
                }
                if (weapons.getMass() != null) {
                    existingWeapons.setMass(weapons.getMass());
                }
                if (weapons.getCost() != null) {
                    existingWeapons.setCost(weapons.getCost());
                }
                if (weapons.getArmored() != null) {
                    existingWeapons.setArmored(weapons.getArmored());
                }

                return existingWeapons;
            })
            .map(weaponsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weapons.getId().toString())
        );
    }

    /**
     * {@code GET  /weapons} : get all the weapons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weapons in body.
     */
    @GetMapping("")
    public List<Weapons> getAllWeapons() {
        LOG.debug("REST request to get all Weapons");
        return weaponsRepository.findAll();
    }

    /**
     * {@code GET  /weapons/:id} : get the "id" weapons.
     *
     * @param id the id of the weapons to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weapons, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Weapons> getWeapons(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Weapons : {}", id);
        Optional<Weapons> weapons = weaponsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weapons);
    }

    /**
     * {@code DELETE  /weapons/:id} : delete the "id" weapons.
     *
     * @param id the id of the weapons to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeapons(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Weapons : {}", id);
        weaponsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.virtualsoundnw.designer.web.rest;

import com.virtualsoundnw.designer.domain.Vehicles;
import com.virtualsoundnw.designer.repository.VehiclesRepository;
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
 * REST controller for managing {@link com.virtualsoundnw.designer.domain.Vehicles}.
 */
@RestController
@RequestMapping("/api/vehicles")
@Transactional
public class VehiclesResource {

    private static final Logger LOG = LoggerFactory.getLogger(VehiclesResource.class);

    private static final String ENTITY_NAME = "vehicles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehiclesRepository vehiclesRepository;

    public VehiclesResource(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    /**
     * {@code POST  /vehicles} : Create a new vehicles.
     *
     * @param vehicles the vehicles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicles, or with status {@code 400 (Bad Request)} if the vehicles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Vehicles> createVehicles(@Valid @RequestBody Vehicles vehicles) throws URISyntaxException {
        LOG.debug("REST request to save Vehicles : {}", vehicles);
        if (vehicles.getId() != null) {
            throw new BadRequestAlertException("A new vehicles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicles = vehiclesRepository.save(vehicles);
        return ResponseEntity.created(new URI("/api/vehicles/" + vehicles.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicles.getId().toString()))
            .body(vehicles);
    }

    /**
     * {@code PUT  /vehicles/:id} : Updates an existing vehicles.
     *
     * @param id the id of the vehicles to save.
     * @param vehicles the vehicles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicles,
     * or with status {@code 400 (Bad Request)} if the vehicles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vehicles> updateVehicles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Vehicles vehicles
    ) throws URISyntaxException {
        LOG.debug("REST request to update Vehicles : {}, {}", id, vehicles);
        if (vehicles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiclesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicles = vehiclesRepository.save(vehicles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicles.getId().toString()))
            .body(vehicles);
    }

    /**
     * {@code PATCH  /vehicles/:id} : Partial updates given fields of an existing vehicles, field will ignore if it is null
     *
     * @param id the id of the vehicles to save.
     * @param vehicles the vehicles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicles,
     * or with status {@code 400 (Bad Request)} if the vehicles is not valid,
     * or with status {@code 404 (Not Found)} if the vehicles is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vehicles> partialUpdateVehicles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Vehicles vehicles
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vehicles partially : {}, {}", id, vehicles);
        if (vehicles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiclesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vehicles> result = vehiclesRepository
            .findById(vehicles.getId())
            .map(existingVehicles -> {
                if (vehicles.getVehicle() != null) {
                    existingVehicles.setVehicle(vehicles.getVehicle());
                }
                if (vehicles.getvShipId() != null) {
                    existingVehicles.setvShipId(vehicles.getvShipId());
                }
                if (vehicles.getMass() != null) {
                    existingVehicles.setMass(vehicles.getMass());
                }
                if (vehicles.getCost() != null) {
                    existingVehicles.setCost(vehicles.getCost());
                }
                if (vehicles.getArmored() != null) {
                    existingVehicles.setArmored(vehicles.getArmored());
                }
                if (vehicles.getRepairBay() != null) {
                    existingVehicles.setRepairBay(vehicles.getRepairBay());
                }

                return existingVehicles;
            })
            .map(vehiclesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicles.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicles} : get all the vehicles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicles in body.
     */
    @GetMapping("")
    public List<Vehicles> getAllVehicles() {
        LOG.debug("REST request to get all Vehicles");
        return vehiclesRepository.findAll();
    }

    /**
     * {@code GET  /vehicles/:id} : get the "id" vehicles.
     *
     * @param id the id of the vehicles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicles> getVehicles(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vehicles : {}", id);
        Optional<Vehicles> vehicles = vehiclesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicles);
    }

    /**
     * {@code DELETE  /vehicles/:id} : delete the "id" vehicles.
     *
     * @param id the id of the vehicles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicles(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vehicles : {}", id);
        vehiclesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

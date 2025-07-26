package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.VehiclesAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Vehicles;
import com.virtualsoundnw.designer.domain.enumeration.Vehicle;
import com.virtualsoundnw.designer.repository.VehiclesRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehiclesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehiclesResourceIT {

    private static final Vehicle DEFAULT_VEHICLE = Vehicle.Air;
    private static final Vehicle UPDATED_VEHICLE = Vehicle.Raft;

    private static final Integer DEFAULT_V_SHIP_ID = 1;
    private static final Integer UPDATED_V_SHIP_ID = 2;

    private static final Float DEFAULT_MASS = 1F;
    private static final Float UPDATED_MASS = 2F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final Boolean DEFAULT_ARMORED = false;
    private static final Boolean UPDATED_ARMORED = true;

    private static final Boolean DEFAULT_REPAIR_BAY = false;
    private static final Boolean UPDATED_REPAIR_BAY = true;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehiclesRepository vehiclesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiclesMockMvc;

    private Vehicles vehicles;

    private Vehicles insertedVehicles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createEntity() {
        return new Vehicles()
            .vehicle(DEFAULT_VEHICLE)
            .vShipId(DEFAULT_V_SHIP_ID)
            .mass(DEFAULT_MASS)
            .cost(DEFAULT_COST)
            .armored(DEFAULT_ARMORED)
            .repairBay(DEFAULT_REPAIR_BAY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createUpdatedEntity() {
        return new Vehicles()
            .vehicle(UPDATED_VEHICLE)
            .vShipId(UPDATED_V_SHIP_ID)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED)
            .repairBay(UPDATED_REPAIR_BAY);
    }

    @BeforeEach
    void initTest() {
        vehicles = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVehicles != null) {
            vehiclesRepository.delete(insertedVehicles);
            insertedVehicles = null;
        }
    }

    @Test
    @Transactional
    void createVehicles() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vehicles
        var returnedVehicles = om.readValue(
            restVehiclesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vehicles.class
        );

        // Validate the Vehicles in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehiclesUpdatableFieldsEquals(returnedVehicles, getPersistedVehicles(returnedVehicles));

        insertedVehicles = returnedVehicles;
    }

    @Test
    @Transactional
    void createVehiclesWithExistingId() throws Exception {
        // Create the Vehicles with an existing ID
        vehicles.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setVehicle(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkvShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setvShipId(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setMass(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setCost(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArmoredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setArmored(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRepairBayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicles.setRepairBay(null);

        // Create the Vehicles, which fails.

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicles.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.toString())))
            .andExpect(jsonPath("$.[*].vShipId").value(hasItem(DEFAULT_V_SHIP_ID)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].armored").value(hasItem(DEFAULT_ARMORED)))
            .andExpect(jsonPath("$.[*].repairBay").value(hasItem(DEFAULT_REPAIR_BAY)));
    }

    @Test
    @Transactional
    void getVehicles() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        // Get the vehicles
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicles.getId().intValue()))
            .andExpect(jsonPath("$.vehicle").value(DEFAULT_VEHICLE.toString()))
            .andExpect(jsonPath("$.vShipId").value(DEFAULT_V_SHIP_ID))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.armored").value(DEFAULT_ARMORED))
            .andExpect(jsonPath("$.repairBay").value(DEFAULT_REPAIR_BAY));
    }

    @Test
    @Transactional
    void getNonExistingVehicles() throws Exception {
        // Get the vehicles
        restVehiclesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicles() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicles
        Vehicles updatedVehicles = vehiclesRepository.findById(vehicles.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicles are not directly saved in db
        em.detach(updatedVehicles);
        updatedVehicles
            .vehicle(UPDATED_VEHICLE)
            .vShipId(UPDATED_V_SHIP_ID)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED)
            .repairBay(UPDATED_REPAIR_BAY);

        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicles))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehiclesToMatchAllProperties(updatedVehicles);
    }

    @Test
    @Transactional
    void putNonExistingVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicles.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehiclesWithPatch() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicles using partial update
        Vehicles partialUpdatedVehicles = new Vehicles();
        partialUpdatedVehicles.setId(vehicles.getId());

        partialUpdatedVehicles.vehicle(UPDATED_VEHICLE).cost(UPDATED_COST).armored(UPDATED_ARMORED);

        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicles.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicles))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehiclesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVehicles, vehicles), getPersistedVehicles(vehicles));
    }

    @Test
    @Transactional
    void fullUpdateVehiclesWithPatch() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicles using partial update
        Vehicles partialUpdatedVehicles = new Vehicles();
        partialUpdatedVehicles.setId(vehicles.getId());

        partialUpdatedVehicles
            .vehicle(UPDATED_VEHICLE)
            .vShipId(UPDATED_V_SHIP_ID)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED)
            .repairBay(UPDATED_REPAIR_BAY);

        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicles.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicles))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehiclesUpdatableFieldsEquals(partialUpdatedVehicles, getPersistedVehicles(partialUpdatedVehicles));
    }

    @Test
    @Transactional
    void patchNonExistingVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicles.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicles() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicles.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicles in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicles() throws Exception {
        // Initialize the database
        insertedVehicles = vehiclesRepository.saveAndFlush(vehicles);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicles
        restVehiclesMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehiclesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Vehicles getPersistedVehicles(Vehicles vehicles) {
        return vehiclesRepository.findById(vehicles.getId()).orElseThrow();
    }

    protected void assertPersistedVehiclesToMatchAllProperties(Vehicles expectedVehicles) {
        assertVehiclesAllPropertiesEquals(expectedVehicles, getPersistedVehicles(expectedVehicles));
    }

    protected void assertPersistedVehiclesToMatchUpdatableProperties(Vehicles expectedVehicles) {
        assertVehiclesAllUpdatablePropertiesEquals(expectedVehicles, getPersistedVehicles(expectedVehicles));
    }
}

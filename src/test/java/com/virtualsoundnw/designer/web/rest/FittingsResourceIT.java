package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.FittingsAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Fittings;
import com.virtualsoundnw.designer.domain.enumeration.Fitting;
import com.virtualsoundnw.designer.repository.FittingsRepository;
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
 * Integration tests for the {@link FittingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FittingsResourceIT {

    private static final Fitting DEFAULT_FITTING = Fitting.Bridge;
    private static final Fitting UPDATED_FITTING = Fitting.Comms;

    private static final Integer DEFAULT_F_SHIP_ID = 1;
    private static final Integer UPDATED_F_SHIP_ID = 2;

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Float DEFAULT_MASS = 1F;
    private static final Float UPDATED_MASS = 2F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final Boolean DEFAULT_ARMORED = false;
    private static final Boolean UPDATED_ARMORED = true;

    private static final String ENTITY_API_URL = "/api/fittings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FittingsRepository fittingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFittingsMockMvc;

    private Fittings fittings;

    private Fittings insertedFittings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fittings createEntity() {
        return new Fittings()
            .fitting(DEFAULT_FITTING)
            .fShipId(DEFAULT_F_SHIP_ID)
            .count(DEFAULT_COUNT)
            .mass(DEFAULT_MASS)
            .cost(DEFAULT_COST)
            .armored(DEFAULT_ARMORED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fittings createUpdatedEntity() {
        return new Fittings()
            .fitting(UPDATED_FITTING)
            .fShipId(UPDATED_F_SHIP_ID)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);
    }

    @BeforeEach
    void initTest() {
        fittings = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFittings != null) {
            fittingsRepository.delete(insertedFittings);
            insertedFittings = null;
        }
    }

    @Test
    @Transactional
    void createFittings() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Fittings
        var returnedFittings = om.readValue(
            restFittingsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Fittings.class
        );

        // Validate the Fittings in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFittingsUpdatableFieldsEquals(returnedFittings, getPersistedFittings(returnedFittings));

        insertedFittings = returnedFittings;
    }

    @Test
    @Transactional
    void createFittingsWithExistingId() throws Exception {
        // Create the Fittings with an existing ID
        fittings.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFittingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setFitting(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkfShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setfShipId(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setCount(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setMass(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setCost(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArmoredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fittings.setArmored(null);

        // Create the Fittings, which fails.

        restFittingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFittings() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        // Get all the fittingsList
        restFittingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fittings.getId().intValue())))
            .andExpect(jsonPath("$.[*].fitting").value(hasItem(DEFAULT_FITTING.toString())))
            .andExpect(jsonPath("$.[*].fShipId").value(hasItem(DEFAULT_F_SHIP_ID)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].armored").value(hasItem(DEFAULT_ARMORED)));
    }

    @Test
    @Transactional
    void getFittings() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        // Get the fittings
        restFittingsMockMvc
            .perform(get(ENTITY_API_URL_ID, fittings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fittings.getId().intValue()))
            .andExpect(jsonPath("$.fitting").value(DEFAULT_FITTING.toString()))
            .andExpect(jsonPath("$.fShipId").value(DEFAULT_F_SHIP_ID))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.armored").value(DEFAULT_ARMORED));
    }

    @Test
    @Transactional
    void getNonExistingFittings() throws Exception {
        // Get the fittings
        restFittingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFittings() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fittings
        Fittings updatedFittings = fittingsRepository.findById(fittings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFittings are not directly saved in db
        em.detach(updatedFittings);
        updatedFittings
            .fitting(UPDATED_FITTING)
            .fShipId(UPDATED_F_SHIP_ID)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restFittingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFittings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFittings))
            )
            .andExpect(status().isOk());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFittingsToMatchAllProperties(updatedFittings);
    }

    @Test
    @Transactional
    void putNonExistingFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fittings.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fittings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFittingsWithPatch() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fittings using partial update
        Fittings partialUpdatedFittings = new Fittings();
        partialUpdatedFittings.setId(fittings.getId());

        partialUpdatedFittings.mass(UPDATED_MASS).cost(UPDATED_COST).armored(UPDATED_ARMORED);

        restFittingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFittings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFittings))
            )
            .andExpect(status().isOk());

        // Validate the Fittings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFittingsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFittings, fittings), getPersistedFittings(fittings));
    }

    @Test
    @Transactional
    void fullUpdateFittingsWithPatch() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fittings using partial update
        Fittings partialUpdatedFittings = new Fittings();
        partialUpdatedFittings.setId(fittings.getId());

        partialUpdatedFittings
            .fitting(UPDATED_FITTING)
            .fShipId(UPDATED_F_SHIP_ID)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restFittingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFittings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFittings))
            )
            .andExpect(status().isOk());

        // Validate the Fittings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFittingsUpdatableFieldsEquals(partialUpdatedFittings, getPersistedFittings(partialUpdatedFittings));
    }

    @Test
    @Transactional
    void patchNonExistingFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fittings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fittings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fittings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFittings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fittings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFittingsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fittings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fittings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFittings() throws Exception {
        // Initialize the database
        insertedFittings = fittingsRepository.saveAndFlush(fittings);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fittings
        restFittingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, fittings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fittingsRepository.count();
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

    protected Fittings getPersistedFittings(Fittings fittings) {
        return fittingsRepository.findById(fittings.getId()).orElseThrow();
    }

    protected void assertPersistedFittingsToMatchAllProperties(Fittings expectedFittings) {
        assertFittingsAllPropertiesEquals(expectedFittings, getPersistedFittings(expectedFittings));
    }

    protected void assertPersistedFittingsToMatchUpdatableProperties(Fittings expectedFittings) {
        assertFittingsAllUpdatablePropertiesEquals(expectedFittings, getPersistedFittings(expectedFittings));
    }
}

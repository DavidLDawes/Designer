package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.EnginesAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Engines;
import com.virtualsoundnw.designer.domain.enumeration.Engine;
import com.virtualsoundnw.designer.domain.enumeration.TL;
import com.virtualsoundnw.designer.repository.EnginesRepository;
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
 * Integration tests for the {@link EnginesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnginesResourceIT {

    private static final Integer DEFAULT_E_SHIP_ID = 1;
    private static final Integer UPDATED_E_SHIP_ID = 2;

    private static final Engine DEFAULT_ENGINE = Engine.Jump;
    private static final Engine UPDATED_ENGINE = Engine.Spare;

    private static final Float DEFAULT_MASS = 1F;
    private static final Float UPDATED_MASS = 2F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final TL DEFAULT_TL = TL.A;
    private static final TL UPDATED_TL = TL.B;

    private static final String ENTITY_API_URL = "/api/engines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnginesRepository enginesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnginesMockMvc;

    private Engines engines;

    private Engines insertedEngines;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Engines createEntity() {
        return new Engines().eShipId(DEFAULT_E_SHIP_ID).engine(DEFAULT_ENGINE).mass(DEFAULT_MASS).cost(DEFAULT_COST).tl(DEFAULT_TL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Engines createUpdatedEntity() {
        return new Engines().eShipId(UPDATED_E_SHIP_ID).engine(UPDATED_ENGINE).mass(UPDATED_MASS).cost(UPDATED_COST).tl(UPDATED_TL);
    }

    @BeforeEach
    void initTest() {
        engines = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEngines != null) {
            enginesRepository.delete(insertedEngines);
            insertedEngines = null;
        }
    }

    @Test
    @Transactional
    void createEngines() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Engines
        var returnedEngines = om.readValue(
            restEnginesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Engines.class
        );

        // Validate the Engines in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEnginesUpdatableFieldsEquals(returnedEngines, getPersistedEngines(returnedEngines));

        insertedEngines = returnedEngines;
    }

    @Test
    @Transactional
    void createEnginesWithExistingId() throws Exception {
        // Create the Engines with an existing ID
        engines.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkeShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engines.seteShipId(null);

        // Create the Engines, which fails.

        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEngineIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engines.setEngine(null);

        // Create the Engines, which fails.

        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engines.setMass(null);

        // Create the Engines, which fails.

        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engines.setCost(null);

        // Create the Engines, which fails.

        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engines.setTl(null);

        // Create the Engines, which fails.

        restEnginesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEngines() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        // Get all the enginesList
        restEnginesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engines.getId().intValue())))
            .andExpect(jsonPath("$.[*].eShipId").value(hasItem(DEFAULT_E_SHIP_ID)))
            .andExpect(jsonPath("$.[*].engine").value(hasItem(DEFAULT_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].tl").value(hasItem(DEFAULT_TL.toString())));
    }

    @Test
    @Transactional
    void getEngines() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        // Get the engines
        restEnginesMockMvc
            .perform(get(ENTITY_API_URL_ID, engines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(engines.getId().intValue()))
            .andExpect(jsonPath("$.eShipId").value(DEFAULT_E_SHIP_ID))
            .andExpect(jsonPath("$.engine").value(DEFAULT_ENGINE.toString()))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.tl").value(DEFAULT_TL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEngines() throws Exception {
        // Get the engines
        restEnginesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEngines() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engines
        Engines updatedEngines = enginesRepository.findById(engines.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEngines are not directly saved in db
        em.detach(updatedEngines);
        updatedEngines.eShipId(UPDATED_E_SHIP_ID).engine(UPDATED_ENGINE).mass(UPDATED_MASS).cost(UPDATED_COST).tl(UPDATED_TL);

        restEnginesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEngines.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEngines))
            )
            .andExpect(status().isOk());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnginesToMatchAllProperties(updatedEngines);
    }

    @Test
    @Transactional
    void putNonExistingEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(put(ENTITY_API_URL_ID, engines.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isBadRequest());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(engines))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engines)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnginesWithPatch() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engines using partial update
        Engines partialUpdatedEngines = new Engines();
        partialUpdatedEngines.setId(engines.getId());

        partialUpdatedEngines.eShipId(UPDATED_E_SHIP_ID).engine(UPDATED_ENGINE).mass(UPDATED_MASS).tl(UPDATED_TL);

        restEnginesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEngines.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEngines))
            )
            .andExpect(status().isOk());

        // Validate the Engines in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnginesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEngines, engines), getPersistedEngines(engines));
    }

    @Test
    @Transactional
    void fullUpdateEnginesWithPatch() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engines using partial update
        Engines partialUpdatedEngines = new Engines();
        partialUpdatedEngines.setId(engines.getId());

        partialUpdatedEngines.eShipId(UPDATED_E_SHIP_ID).engine(UPDATED_ENGINE).mass(UPDATED_MASS).cost(UPDATED_COST).tl(UPDATED_TL);

        restEnginesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEngines.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEngines))
            )
            .andExpect(status().isOk());

        // Validate the Engines in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnginesUpdatableFieldsEquals(partialUpdatedEngines, getPersistedEngines(partialUpdatedEngines));
    }

    @Test
    @Transactional
    void patchNonExistingEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, engines.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(engines))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(engines))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEngines() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engines.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(engines)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Engines in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEngines() throws Exception {
        // Initialize the database
        insertedEngines = enginesRepository.saveAndFlush(engines);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the engines
        restEnginesMockMvc
            .perform(delete(ENTITY_API_URL_ID, engines.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enginesRepository.count();
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

    protected Engines getPersistedEngines(Engines engines) {
        return enginesRepository.findById(engines.getId()).orElseThrow();
    }

    protected void assertPersistedEnginesToMatchAllProperties(Engines expectedEngines) {
        assertEnginesAllPropertiesEquals(expectedEngines, getPersistedEngines(expectedEngines));
    }

    protected void assertPersistedEnginesToMatchUpdatableProperties(Engines expectedEngines) {
        assertEnginesAllUpdatablePropertiesEquals(expectedEngines, getPersistedEngines(expectedEngines));
    }
}

package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.CargoesAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Cargoes;
import com.virtualsoundnw.designer.domain.enumeration.Cargo;
import com.virtualsoundnw.designer.repository.CargoesRepository;
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
 * Integration tests for the {@link CargoesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoesResourceIT {

    private static final Cargo DEFAULT_CARGO = Cargo.Cargo;
    private static final Cargo UPDATED_CARGO = Cargo.Bay;

    private static final Integer DEFAULT_C_SHIP_ID = 1;
    private static final Integer UPDATED_C_SHIP_ID = 2;

    private static final Float DEFAULT_MASS = 1F;
    private static final Float UPDATED_MASS = 2F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final Boolean DEFAULT_ARMORED = false;
    private static final Boolean UPDATED_ARMORED = true;

    private static final String ENTITY_API_URL = "/api/cargoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CargoesRepository cargoesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoesMockMvc;

    private Cargoes cargoes;

    private Cargoes insertedCargoes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cargoes createEntity() {
        return new Cargoes().cargo(DEFAULT_CARGO).cShipId(DEFAULT_C_SHIP_ID).mass(DEFAULT_MASS).cost(DEFAULT_COST).armored(DEFAULT_ARMORED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cargoes createUpdatedEntity() {
        return new Cargoes().cargo(UPDATED_CARGO).cShipId(UPDATED_C_SHIP_ID).mass(UPDATED_MASS).cost(UPDATED_COST).armored(UPDATED_ARMORED);
    }

    @BeforeEach
    void initTest() {
        cargoes = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCargoes != null) {
            cargoesRepository.delete(insertedCargoes);
            insertedCargoes = null;
        }
    }

    @Test
    @Transactional
    void createCargoes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cargoes
        var returnedCargoes = om.readValue(
            restCargoesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cargoes.class
        );

        // Validate the Cargoes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCargoesUpdatableFieldsEquals(returnedCargoes, getPersistedCargoes(returnedCargoes));

        insertedCargoes = returnedCargoes;
    }

    @Test
    @Transactional
    void createCargoesWithExistingId() throws Exception {
        // Create the Cargoes with an existing ID
        cargoes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCargoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargoes.setCargo(null);

        // Create the Cargoes, which fails.

        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkcShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargoes.setcShipId(null);

        // Create the Cargoes, which fails.

        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargoes.setMass(null);

        // Create the Cargoes, which fails.

        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargoes.setCost(null);

        // Create the Cargoes, which fails.

        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArmoredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargoes.setArmored(null);

        // Create the Cargoes, which fails.

        restCargoesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCargoes() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        // Get all the cargoesList
        restCargoesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())))
            .andExpect(jsonPath("$.[*].cShipId").value(hasItem(DEFAULT_C_SHIP_ID)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].armored").value(hasItem(DEFAULT_ARMORED)));
    }

    @Test
    @Transactional
    void getCargoes() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        // Get the cargoes
        restCargoesMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoes.getId().intValue()))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()))
            .andExpect(jsonPath("$.cShipId").value(DEFAULT_C_SHIP_ID))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.armored").value(DEFAULT_ARMORED));
    }

    @Test
    @Transactional
    void getNonExistingCargoes() throws Exception {
        // Get the cargoes
        restCargoesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoes() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoes
        Cargoes updatedCargoes = cargoesRepository.findById(cargoes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCargoes are not directly saved in db
        em.detach(updatedCargoes);
        updatedCargoes.cargo(UPDATED_CARGO).cShipId(UPDATED_C_SHIP_ID).mass(UPDATED_MASS).cost(UPDATED_COST).armored(UPDATED_ARMORED);

        restCargoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCargoes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCargoes))
            )
            .andExpect(status().isOk());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCargoesToMatchAllProperties(updatedCargoes);
    }

    @Test
    @Transactional
    void putNonExistingCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(put(ENTITY_API_URL_ID, cargoes.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isBadRequest());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoesWithPatch() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoes using partial update
        Cargoes partialUpdatedCargoes = new Cargoes();
        partialUpdatedCargoes.setId(cargoes.getId());

        partialUpdatedCargoes.mass(UPDATED_MASS).armored(UPDATED_ARMORED);

        restCargoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoes))
            )
            .andExpect(status().isOk());

        // Validate the Cargoes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCargoes, cargoes), getPersistedCargoes(cargoes));
    }

    @Test
    @Transactional
    void fullUpdateCargoesWithPatch() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargoes using partial update
        Cargoes partialUpdatedCargoes = new Cargoes();
        partialUpdatedCargoes.setId(cargoes.getId());

        partialUpdatedCargoes
            .cargo(UPDATED_CARGO)
            .cShipId(UPDATED_C_SHIP_ID)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restCargoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargoes))
            )
            .andExpect(status().isOk());

        // Validate the Cargoes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoesUpdatableFieldsEquals(partialUpdatedCargoes, getPersistedCargoes(partialUpdatedCargoes));
    }

    @Test
    @Transactional
    void patchNonExistingCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoes.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargoes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargoes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargoes() throws Exception {
        // Initialize the database
        insertedCargoes = cargoesRepository.saveAndFlush(cargoes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cargoes
        restCargoesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cargoesRepository.count();
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

    protected Cargoes getPersistedCargoes(Cargoes cargoes) {
        return cargoesRepository.findById(cargoes.getId()).orElseThrow();
    }

    protected void assertPersistedCargoesToMatchAllProperties(Cargoes expectedCargoes) {
        assertCargoesAllPropertiesEquals(expectedCargoes, getPersistedCargoes(expectedCargoes));
    }

    protected void assertPersistedCargoesToMatchUpdatableProperties(Cargoes expectedCargoes) {
        assertCargoesAllUpdatablePropertiesEquals(expectedCargoes, getPersistedCargoes(expectedCargoes));
    }
}

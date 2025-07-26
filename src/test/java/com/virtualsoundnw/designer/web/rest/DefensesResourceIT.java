package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.DefensesAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Defenses;
import com.virtualsoundnw.designer.domain.enumeration.Defense;
import com.virtualsoundnw.designer.repository.DefensesRepository;
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
 * Integration tests for the {@link DefensesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DefensesResourceIT {

    private static final Defense DEFAULT_DEFENSE = Defense.Armor;
    private static final Defense UPDATED_DEFENSE = Defense.Nuclear;

    private static final Integer DEFAULT_D_SHIP_ID = 1;
    private static final Integer UPDATED_D_SHIP_ID = 2;

    private static final Integer DEFAULT_BATTERY = 1;
    private static final Integer UPDATED_BATTERY = 2;

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Float DEFAULT_MASS = 1F;
    private static final Float UPDATED_MASS = 2F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final Boolean DEFAULT_ARMORED = false;
    private static final Boolean UPDATED_ARMORED = true;

    private static final String ENTITY_API_URL = "/api/defenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DefensesRepository defensesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDefensesMockMvc;

    private Defenses defenses;

    private Defenses insertedDefenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Defenses createEntity() {
        return new Defenses()
            .defense(DEFAULT_DEFENSE)
            .dShipId(DEFAULT_D_SHIP_ID)
            .battery(DEFAULT_BATTERY)
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
    public static Defenses createUpdatedEntity() {
        return new Defenses()
            .defense(UPDATED_DEFENSE)
            .dShipId(UPDATED_D_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);
    }

    @BeforeEach
    void initTest() {
        defenses = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDefenses != null) {
            defensesRepository.delete(insertedDefenses);
            insertedDefenses = null;
        }
    }

    @Test
    @Transactional
    void createDefenses() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Defenses
        var returnedDefenses = om.readValue(
            restDefensesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Defenses.class
        );

        // Validate the Defenses in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDefensesUpdatableFieldsEquals(returnedDefenses, getPersistedDefenses(returnedDefenses));

        insertedDefenses = returnedDefenses;
    }

    @Test
    @Transactional
    void createDefensesWithExistingId() throws Exception {
        // Create the Defenses with an existing ID
        defenses.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDefenseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setDefense(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkdShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setdShipId(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatteryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setBattery(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setCount(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setMass(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setCost(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArmoredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defenses.setArmored(null);

        // Create the Defenses, which fails.

        restDefensesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDefenses() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        // Get all the defensesList
        restDefensesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].defense").value(hasItem(DEFAULT_DEFENSE.toString())))
            .andExpect(jsonPath("$.[*].dShipId").value(hasItem(DEFAULT_D_SHIP_ID)))
            .andExpect(jsonPath("$.[*].battery").value(hasItem(DEFAULT_BATTERY)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].armored").value(hasItem(DEFAULT_ARMORED)));
    }

    @Test
    @Transactional
    void getDefenses() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        // Get the defenses
        restDefensesMockMvc
            .perform(get(ENTITY_API_URL_ID, defenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(defenses.getId().intValue()))
            .andExpect(jsonPath("$.defense").value(DEFAULT_DEFENSE.toString()))
            .andExpect(jsonPath("$.dShipId").value(DEFAULT_D_SHIP_ID))
            .andExpect(jsonPath("$.battery").value(DEFAULT_BATTERY))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.armored").value(DEFAULT_ARMORED));
    }

    @Test
    @Transactional
    void getNonExistingDefenses() throws Exception {
        // Get the defenses
        restDefensesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDefenses() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defenses
        Defenses updatedDefenses = defensesRepository.findById(defenses.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDefenses are not directly saved in db
        em.detach(updatedDefenses);
        updatedDefenses
            .defense(UPDATED_DEFENSE)
            .dShipId(UPDATED_D_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restDefensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDefenses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDefenses))
            )
            .andExpect(status().isOk());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDefensesToMatchAllProperties(updatedDefenses);
    }

    @Test
    @Transactional
    void putNonExistingDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, defenses.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(defenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDefensesWithPatch() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defenses using partial update
        Defenses partialUpdatedDefenses = new Defenses();
        partialUpdatedDefenses.setId(defenses.getId());

        partialUpdatedDefenses.dShipId(UPDATED_D_SHIP_ID);

        restDefensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefenses))
            )
            .andExpect(status().isOk());

        // Validate the Defenses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefensesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDefenses, defenses), getPersistedDefenses(defenses));
    }

    @Test
    @Transactional
    void fullUpdateDefensesWithPatch() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defenses using partial update
        Defenses partialUpdatedDefenses = new Defenses();
        partialUpdatedDefenses.setId(defenses.getId());

        partialUpdatedDefenses
            .defense(UPDATED_DEFENSE)
            .dShipId(UPDATED_D_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restDefensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefenses))
            )
            .andExpect(status().isOk());

        // Validate the Defenses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefensesUpdatableFieldsEquals(partialUpdatedDefenses, getPersistedDefenses(partialUpdatedDefenses));
    }

    @Test
    @Transactional
    void patchNonExistingDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, defenses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defenses))
            )
            .andExpect(status().isBadRequest());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDefenses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defenses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefensesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(defenses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Defenses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDefenses() throws Exception {
        // Initialize the database
        insertedDefenses = defensesRepository.saveAndFlush(defenses);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the defenses
        restDefensesMockMvc
            .perform(delete(ENTITY_API_URL_ID, defenses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return defensesRepository.count();
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

    protected Defenses getPersistedDefenses(Defenses defenses) {
        return defensesRepository.findById(defenses.getId()).orElseThrow();
    }

    protected void assertPersistedDefensesToMatchAllProperties(Defenses expectedDefenses) {
        assertDefensesAllPropertiesEquals(expectedDefenses, getPersistedDefenses(expectedDefenses));
    }

    protected void assertPersistedDefensesToMatchUpdatableProperties(Defenses expectedDefenses) {
        assertDefensesAllUpdatablePropertiesEquals(expectedDefenses, getPersistedDefenses(expectedDefenses));
    }
}

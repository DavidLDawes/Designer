package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.WeaponsAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Weapons;
import com.virtualsoundnw.designer.domain.enumeration.Weapon;
import com.virtualsoundnw.designer.repository.WeaponsRepository;
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
 * Integration tests for the {@link WeaponsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeaponsResourceIT {

    private static final Weapon DEFAULT_WEAPON = Weapon.Pulse;
    private static final Weapon UPDATED_WEAPON = Weapon.Laser;

    private static final Integer DEFAULT_W_SHIP_ID = 1;
    private static final Integer UPDATED_W_SHIP_ID = 2;

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

    private static final String ENTITY_API_URL = "/api/weapons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WeaponsRepository weaponsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeaponsMockMvc;

    private Weapons weapons;

    private Weapons insertedWeapons;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weapons createEntity() {
        return new Weapons()
            .weapon(DEFAULT_WEAPON)
            .wShipId(DEFAULT_W_SHIP_ID)
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
    public static Weapons createUpdatedEntity() {
        return new Weapons()
            .weapon(UPDATED_WEAPON)
            .wShipId(UPDATED_W_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);
    }

    @BeforeEach
    void initTest() {
        weapons = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWeapons != null) {
            weaponsRepository.delete(insertedWeapons);
            insertedWeapons = null;
        }
    }

    @Test
    @Transactional
    void createWeapons() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Weapons
        var returnedWeapons = om.readValue(
            restWeaponsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Weapons.class
        );

        // Validate the Weapons in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWeaponsUpdatableFieldsEquals(returnedWeapons, getPersistedWeapons(returnedWeapons));

        insertedWeapons = returnedWeapons;
    }

    @Test
    @Transactional
    void createWeaponsWithExistingId() throws Exception {
        // Create the Weapons with an existing ID
        weapons.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWeaponIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setWeapon(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkwShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setwShipId(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatteryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setBattery(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setCount(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMassIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setMass(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setCost(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArmoredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weapons.setArmored(null);

        // Create the Weapons, which fails.

        restWeaponsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeapons() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        // Get all the weaponsList
        restWeaponsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weapons.getId().intValue())))
            .andExpect(jsonPath("$.[*].weapon").value(hasItem(DEFAULT_WEAPON.toString())))
            .andExpect(jsonPath("$.[*].wShipId").value(hasItem(DEFAULT_W_SHIP_ID)))
            .andExpect(jsonPath("$.[*].battery").value(hasItem(DEFAULT_BATTERY)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].mass").value(hasItem(DEFAULT_MASS.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].armored").value(hasItem(DEFAULT_ARMORED)));
    }

    @Test
    @Transactional
    void getWeapons() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        // Get the weapons
        restWeaponsMockMvc
            .perform(get(ENTITY_API_URL_ID, weapons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weapons.getId().intValue()))
            .andExpect(jsonPath("$.weapon").value(DEFAULT_WEAPON.toString()))
            .andExpect(jsonPath("$.wShipId").value(DEFAULT_W_SHIP_ID))
            .andExpect(jsonPath("$.battery").value(DEFAULT_BATTERY))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.mass").value(DEFAULT_MASS.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.armored").value(DEFAULT_ARMORED));
    }

    @Test
    @Transactional
    void getNonExistingWeapons() throws Exception {
        // Get the weapons
        restWeaponsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeapons() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weapons
        Weapons updatedWeapons = weaponsRepository.findById(weapons.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWeapons are not directly saved in db
        em.detach(updatedWeapons);
        updatedWeapons
            .weapon(UPDATED_WEAPON)
            .wShipId(UPDATED_W_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restWeaponsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWeapons.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWeapons))
            )
            .andExpect(status().isOk());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWeaponsToMatchAllProperties(updatedWeapons);
    }

    @Test
    @Transactional
    void putNonExistingWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(put(ENTITY_API_URL_ID, weapons.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isBadRequest());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weapons))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeaponsWithPatch() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weapons using partial update
        Weapons partialUpdatedWeapons = new Weapons();
        partialUpdatedWeapons.setId(weapons.getId());

        partialUpdatedWeapons.weapon(UPDATED_WEAPON).battery(UPDATED_BATTERY).cost(UPDATED_COST);

        restWeaponsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeapons.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeapons))
            )
            .andExpect(status().isOk());

        // Validate the Weapons in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeaponsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWeapons, weapons), getPersistedWeapons(weapons));
    }

    @Test
    @Transactional
    void fullUpdateWeaponsWithPatch() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weapons using partial update
        Weapons partialUpdatedWeapons = new Weapons();
        partialUpdatedWeapons.setId(weapons.getId());

        partialUpdatedWeapons
            .weapon(UPDATED_WEAPON)
            .wShipId(UPDATED_W_SHIP_ID)
            .battery(UPDATED_BATTERY)
            .count(UPDATED_COUNT)
            .mass(UPDATED_MASS)
            .cost(UPDATED_COST)
            .armored(UPDATED_ARMORED);

        restWeaponsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeapons.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeapons))
            )
            .andExpect(status().isOk());

        // Validate the Weapons in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeaponsUpdatableFieldsEquals(partialUpdatedWeapons, getPersistedWeapons(partialUpdatedWeapons));
    }

    @Test
    @Transactional
    void patchNonExistingWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weapons.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(weapons))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weapons))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeapons() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weapons.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeaponsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(weapons)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weapons in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeapons() throws Exception {
        // Initialize the database
        insertedWeapons = weaponsRepository.saveAndFlush(weapons);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the weapons
        restWeaponsMockMvc
            .perform(delete(ENTITY_API_URL_ID, weapons.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return weaponsRepository.count();
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

    protected Weapons getPersistedWeapons(Weapons weapons) {
        return weaponsRepository.findById(weapons.getId()).orElseThrow();
    }

    protected void assertPersistedWeaponsToMatchAllProperties(Weapons expectedWeapons) {
        assertWeaponsAllPropertiesEquals(expectedWeapons, getPersistedWeapons(expectedWeapons));
    }

    protected void assertPersistedWeaponsToMatchUpdatableProperties(Weapons expectedWeapons) {
        assertWeaponsAllUpdatablePropertiesEquals(expectedWeapons, getPersistedWeapons(expectedWeapons));
    }
}

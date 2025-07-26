package com.virtualsoundnw.designer.web.rest;

import static com.virtualsoundnw.designer.domain.ShipsAsserts.*;
import static com.virtualsoundnw.designer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualsoundnw.designer.IntegrationTest;
import com.virtualsoundnw.designer.domain.Ships;
import com.virtualsoundnw.designer.domain.enumeration.Config;
import com.virtualsoundnw.designer.domain.enumeration.TL;
import com.virtualsoundnw.designer.repository.ShipsRepository;
import com.virtualsoundnw.designer.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShipsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShipsResourceIT {

    private static final Integer DEFAULT_SHIP_ID = 1;
    private static final Integer UPDATED_SHIP_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final TL DEFAULT_TL = TL.A;
    private static final TL UPDATED_TL = TL.B;

    private static final Integer DEFAULT_TONNAGE = 1;
    private static final Integer UPDATED_TONNAGE = 2;

    private static final Config DEFAULT_CONF = Config.Needle;
    private static final Config UPDATED_CONF = Config.Wedge;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HULL = 1;
    private static final Integer UPDATED_HULL = 2;

    private static final Integer DEFAULT_STRUCTURE = 1;
    private static final Integer UPDATED_STRUCTURE = 2;

    private static final Integer DEFAULT_SECTIONS = 1;
    private static final Integer UPDATED_SECTIONS = 2;

    private static final Boolean DEFAULT_CAPITAL = false;
    private static final Boolean UPDATED_CAPITAL = true;

    private static final Boolean DEFAULT_MILITARY = false;
    private static final Boolean UPDATED_MILITARY = true;

    private static final String ENTITY_API_URL = "/api/ships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipsRepository shipsRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ShipsRepository shipsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipsMockMvc;

    private Ships ships;

    private Ships insertedShips;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ships createEntity() {
        return new Ships()
            .shipId(DEFAULT_SHIP_ID)
            .userId(DEFAULT_USER_ID)
            .tl(DEFAULT_TL)
            .tonnage(DEFAULT_TONNAGE)
            .conf(DEFAULT_CONF)
            .code(DEFAULT_CODE)
            .hull(DEFAULT_HULL)
            .structure(DEFAULT_STRUCTURE)
            .sections(DEFAULT_SECTIONS)
            .capital(DEFAULT_CAPITAL)
            .military(DEFAULT_MILITARY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ships createUpdatedEntity() {
        return new Ships()
            .shipId(UPDATED_SHIP_ID)
            .userId(UPDATED_USER_ID)
            .tl(UPDATED_TL)
            .tonnage(UPDATED_TONNAGE)
            .conf(UPDATED_CONF)
            .code(UPDATED_CODE)
            .hull(UPDATED_HULL)
            .structure(UPDATED_STRUCTURE)
            .sections(UPDATED_SECTIONS)
            .capital(UPDATED_CAPITAL)
            .military(UPDATED_MILITARY);
    }

    @BeforeEach
    void initTest() {
        ships = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedShips != null) {
            shipsRepository.delete(insertedShips);
            insertedShips = null;
        }
    }

    @Test
    @Transactional
    void createShips() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ships
        var returnedShips = om.readValue(
            restShipsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ships.class
        );

        // Validate the Ships in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShipsUpdatableFieldsEquals(returnedShips, getPersistedShips(returnedShips));

        insertedShips = returnedShips;
    }

    @Test
    @Transactional
    void createShipsWithExistingId() throws Exception {
        // Create the Ships with an existing ID
        ships.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkShipIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setShipId(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setUserId(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setTl(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTonnageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setTonnage(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setConf(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setCode(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHullIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setHull(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStructureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setStructure(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setSections(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapitalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setCapital(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMilitaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ships.setMilitary(null);

        // Create the Ships, which fails.

        restShipsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShips() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        // Get all the shipsList
        restShipsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ships.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipId").value(hasItem(DEFAULT_SHIP_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].tl").value(hasItem(DEFAULT_TL.toString())))
            .andExpect(jsonPath("$.[*].tonnage").value(hasItem(DEFAULT_TONNAGE)))
            .andExpect(jsonPath("$.[*].conf").value(hasItem(DEFAULT_CONF.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].hull").value(hasItem(DEFAULT_HULL)))
            .andExpect(jsonPath("$.[*].structure").value(hasItem(DEFAULT_STRUCTURE)))
            .andExpect(jsonPath("$.[*].sections").value(hasItem(DEFAULT_SECTIONS)))
            .andExpect(jsonPath("$.[*].capital").value(hasItem(DEFAULT_CAPITAL)))
            .andExpect(jsonPath("$.[*].military").value(hasItem(DEFAULT_MILITARY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shipsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shipsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shipsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShipsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shipsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShips() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        // Get the ships
        restShipsMockMvc
            .perform(get(ENTITY_API_URL_ID, ships.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ships.getId().intValue()))
            .andExpect(jsonPath("$.shipId").value(DEFAULT_SHIP_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.tl").value(DEFAULT_TL.toString()))
            .andExpect(jsonPath("$.tonnage").value(DEFAULT_TONNAGE))
            .andExpect(jsonPath("$.conf").value(DEFAULT_CONF.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.hull").value(DEFAULT_HULL))
            .andExpect(jsonPath("$.structure").value(DEFAULT_STRUCTURE))
            .andExpect(jsonPath("$.sections").value(DEFAULT_SECTIONS))
            .andExpect(jsonPath("$.capital").value(DEFAULT_CAPITAL))
            .andExpect(jsonPath("$.military").value(DEFAULT_MILITARY));
    }

    @Test
    @Transactional
    void getNonExistingShips() throws Exception {
        // Get the ships
        restShipsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShips() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ships
        Ships updatedShips = shipsRepository.findById(ships.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShips are not directly saved in db
        em.detach(updatedShips);
        updatedShips
            .shipId(UPDATED_SHIP_ID)
            .userId(UPDATED_USER_ID)
            .tl(UPDATED_TL)
            .tonnage(UPDATED_TONNAGE)
            .conf(UPDATED_CONF)
            .code(UPDATED_CODE)
            .hull(UPDATED_HULL)
            .structure(UPDATED_STRUCTURE)
            .sections(UPDATED_SECTIONS)
            .capital(UPDATED_CAPITAL)
            .military(UPDATED_MILITARY);

        restShipsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShips.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShips))
            )
            .andExpect(status().isOk());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipsToMatchAllProperties(updatedShips);
    }

    @Test
    @Transactional
    void putNonExistingShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(put(ENTITY_API_URL_ID, ships.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isBadRequest());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ships))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ships)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipsWithPatch() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ships using partial update
        Ships partialUpdatedShips = new Ships();
        partialUpdatedShips.setId(ships.getId());

        partialUpdatedShips
            .userId(UPDATED_USER_ID)
            .tonnage(UPDATED_TONNAGE)
            .code(UPDATED_CODE)
            .hull(UPDATED_HULL)
            .structure(UPDATED_STRUCTURE)
            .capital(UPDATED_CAPITAL);

        restShipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShips.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShips))
            )
            .andExpect(status().isOk());

        // Validate the Ships in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShips, ships), getPersistedShips(ships));
    }

    @Test
    @Transactional
    void fullUpdateShipsWithPatch() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ships using partial update
        Ships partialUpdatedShips = new Ships();
        partialUpdatedShips.setId(ships.getId());

        partialUpdatedShips
            .shipId(UPDATED_SHIP_ID)
            .userId(UPDATED_USER_ID)
            .tl(UPDATED_TL)
            .tonnage(UPDATED_TONNAGE)
            .conf(UPDATED_CONF)
            .code(UPDATED_CODE)
            .hull(UPDATED_HULL)
            .structure(UPDATED_STRUCTURE)
            .sections(UPDATED_SECTIONS)
            .capital(UPDATED_CAPITAL)
            .military(UPDATED_MILITARY);

        restShipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShips.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShips))
            )
            .andExpect(status().isOk());

        // Validate the Ships in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipsUpdatableFieldsEquals(partialUpdatedShips, getPersistedShips(partialUpdatedShips));
    }

    @Test
    @Transactional
    void patchNonExistingShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ships.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ships))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ships))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShips() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ships.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ships)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ships in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShips() throws Exception {
        // Initialize the database
        insertedShips = shipsRepository.saveAndFlush(ships);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ships
        restShipsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ships.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipsRepository.count();
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

    protected Ships getPersistedShips(Ships ships) {
        return shipsRepository.findById(ships.getId()).orElseThrow();
    }

    protected void assertPersistedShipsToMatchAllProperties(Ships expectedShips) {
        assertShipsAllPropertiesEquals(expectedShips, getPersistedShips(expectedShips));
    }

    protected void assertPersistedShipsToMatchUpdatableProperties(Ships expectedShips) {
        assertShipsAllUpdatablePropertiesEquals(expectedShips, getPersistedShips(expectedShips));
    }
}

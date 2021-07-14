package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdContractCharacteristics;
import com.apptium.repository.OrdContractCharacteristicsRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrdContractCharacteristicsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdContractCharacteristicsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ord-contract-characteristics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdContractCharacteristicsRepository ordContractCharacteristicsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdContractCharacteristicsMockMvc;

    private OrdContractCharacteristics ordContractCharacteristics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContractCharacteristics createEntity(EntityManager em) {
        OrdContractCharacteristics ordContractCharacteristics = new OrdContractCharacteristics()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return ordContractCharacteristics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContractCharacteristics createUpdatedEntity(EntityManager em) {
        OrdContractCharacteristics ordContractCharacteristics = new OrdContractCharacteristics()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        return ordContractCharacteristics;
    }

    @BeforeEach
    public void initTest() {
        ordContractCharacteristics = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeCreate = ordContractCharacteristicsRepository.findAll().size();
        // Create the OrdContractCharacteristics
        restOrdContractCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isCreated());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeCreate + 1);
        OrdContractCharacteristics testOrdContractCharacteristics = ordContractCharacteristicsList.get(
            ordContractCharacteristicsList.size() - 1
        );
        assertThat(testOrdContractCharacteristics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdContractCharacteristics.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdContractCharacteristics.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOrdContractCharacteristics.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdContractCharacteristics.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createOrdContractCharacteristicsWithExistingId() throws Exception {
        // Create the OrdContractCharacteristics with an existing ID
        ordContractCharacteristics.setId(1L);

        int databaseSizeBeforeCreate = ordContractCharacteristicsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdContractCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdContractCharacteristics() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        // Get all the ordContractCharacteristicsList
        restOrdContractCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordContractCharacteristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdContractCharacteristics() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        // Get the ordContractCharacteristics
        restOrdContractCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL_ID, ordContractCharacteristics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordContractCharacteristics.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdContractCharacteristics() throws Exception {
        // Get the ordContractCharacteristics
        restOrdContractCharacteristicsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdContractCharacteristics() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();

        // Update the ordContractCharacteristics
        OrdContractCharacteristics updatedOrdContractCharacteristics = ordContractCharacteristicsRepository
            .findById(ordContractCharacteristics.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrdContractCharacteristics are not directly saved in db
        em.detach(updatedOrdContractCharacteristics);
        updatedOrdContractCharacteristics
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdContractCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdContractCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdContractCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdContractCharacteristics testOrdContractCharacteristics = ordContractCharacteristicsList.get(
            ordContractCharacteristicsList.size() - 1
        );
        assertThat(testOrdContractCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdContractCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdContractCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdContractCharacteristics.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdContractCharacteristics.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordContractCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdContractCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();

        // Update the ordContractCharacteristics using partial update
        OrdContractCharacteristics partialUpdatedOrdContractCharacteristics = new OrdContractCharacteristics();
        partialUpdatedOrdContractCharacteristics.setId(ordContractCharacteristics.getId());

        partialUpdatedOrdContractCharacteristics.value(UPDATED_VALUE).valueType(UPDATED_VALUE_TYPE);

        restOrdContractCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContractCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContractCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdContractCharacteristics testOrdContractCharacteristics = ordContractCharacteristicsList.get(
            ordContractCharacteristicsList.size() - 1
        );
        assertThat(testOrdContractCharacteristics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdContractCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdContractCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdContractCharacteristics.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdContractCharacteristics.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdContractCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();

        // Update the ordContractCharacteristics using partial update
        OrdContractCharacteristics partialUpdatedOrdContractCharacteristics = new OrdContractCharacteristics();
        partialUpdatedOrdContractCharacteristics.setId(ordContractCharacteristics.getId());

        partialUpdatedOrdContractCharacteristics
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdContractCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContractCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContractCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdContractCharacteristics testOrdContractCharacteristics = ordContractCharacteristicsList.get(
            ordContractCharacteristicsList.size() - 1
        );
        assertThat(testOrdContractCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdContractCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdContractCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdContractCharacteristics.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdContractCharacteristics.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordContractCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdContractCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordContractCharacteristicsRepository.findAll().size();
        ordContractCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContractCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContractCharacteristics in the database
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdContractCharacteristics() throws Exception {
        // Initialize the database
        ordContractCharacteristicsRepository.saveAndFlush(ordContractCharacteristics);

        int databaseSizeBeforeDelete = ordContractCharacteristicsRepository.findAll().size();

        // Delete the ordContractCharacteristics
        restOrdContractCharacteristicsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordContractCharacteristics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdContractCharacteristics> ordContractCharacteristicsList = ordContractCharacteristicsRepository.findAll();
        assertThat(ordContractCharacteristicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

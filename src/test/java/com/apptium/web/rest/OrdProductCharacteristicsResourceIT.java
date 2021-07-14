package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdProductCharacteristics;
import com.apptium.repository.OrdProductCharacteristicsRepository;
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
 * Integration tests for the {@link OrdProductCharacteristicsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdProductCharacteristicsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-product-characteristics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdProductCharacteristicsRepository ordProductCharacteristicsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdProductCharacteristicsMockMvc;

    private OrdProductCharacteristics ordProductCharacteristics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProductCharacteristics createEntity(EntityManager em) {
        OrdProductCharacteristics ordProductCharacteristics = new OrdProductCharacteristics()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE);
        return ordProductCharacteristics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProductCharacteristics createUpdatedEntity(EntityManager em) {
        OrdProductCharacteristics ordProductCharacteristics = new OrdProductCharacteristics()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);
        return ordProductCharacteristics;
    }

    @BeforeEach
    public void initTest() {
        ordProductCharacteristics = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeCreate = ordProductCharacteristicsRepository.findAll().size();
        // Create the OrdProductCharacteristics
        restOrdProductCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isCreated());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeCreate + 1);
        OrdProductCharacteristics testOrdProductCharacteristics = ordProductCharacteristicsList.get(
            ordProductCharacteristicsList.size() - 1
        );
        assertThat(testOrdProductCharacteristics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdProductCharacteristics.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdProductCharacteristics.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
    }

    @Test
    @Transactional
    void createOrdProductCharacteristicsWithExistingId() throws Exception {
        // Create the OrdProductCharacteristics with an existing ID
        ordProductCharacteristics.setId(1L);

        int databaseSizeBeforeCreate = ordProductCharacteristicsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdProductCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdProductCharacteristics() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        // Get all the ordProductCharacteristicsList
        restOrdProductCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordProductCharacteristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)));
    }

    @Test
    @Transactional
    void getOrdProductCharacteristics() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        // Get the ordProductCharacteristics
        restOrdProductCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL_ID, ordProductCharacteristics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordProductCharacteristics.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingOrdProductCharacteristics() throws Exception {
        // Get the ordProductCharacteristics
        restOrdProductCharacteristicsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdProductCharacteristics() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();

        // Update the ordProductCharacteristics
        OrdProductCharacteristics updatedOrdProductCharacteristics = ordProductCharacteristicsRepository
            .findById(ordProductCharacteristics.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrdProductCharacteristics are not directly saved in db
        em.detach(updatedOrdProductCharacteristics);
        updatedOrdProductCharacteristics.name(UPDATED_NAME).value(UPDATED_VALUE).valueType(UPDATED_VALUE_TYPE);

        restOrdProductCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdProductCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdProductCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdProductCharacteristics testOrdProductCharacteristics = ordProductCharacteristicsList.get(
            ordProductCharacteristicsList.size() - 1
        );
        assertThat(testOrdProductCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProductCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdProductCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordProductCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdProductCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();

        // Update the ordProductCharacteristics using partial update
        OrdProductCharacteristics partialUpdatedOrdProductCharacteristics = new OrdProductCharacteristics();
        partialUpdatedOrdProductCharacteristics.setId(ordProductCharacteristics.getId());

        partialUpdatedOrdProductCharacteristics.name(UPDATED_NAME);

        restOrdProductCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProductCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProductCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdProductCharacteristics testOrdProductCharacteristics = ordProductCharacteristicsList.get(
            ordProductCharacteristicsList.size() - 1
        );
        assertThat(testOrdProductCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProductCharacteristics.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdProductCharacteristics.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOrdProductCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();

        // Update the ordProductCharacteristics using partial update
        OrdProductCharacteristics partialUpdatedOrdProductCharacteristics = new OrdProductCharacteristics();
        partialUpdatedOrdProductCharacteristics.setId(ordProductCharacteristics.getId());

        partialUpdatedOrdProductCharacteristics.name(UPDATED_NAME).value(UPDATED_VALUE).valueType(UPDATED_VALUE_TYPE);

        restOrdProductCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProductCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProductCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdProductCharacteristics testOrdProductCharacteristics = ordProductCharacteristicsList.get(
            ordProductCharacteristicsList.size() - 1
        );
        assertThat(testOrdProductCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProductCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdProductCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordProductCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdProductCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordProductCharacteristicsRepository.findAll().size();
        ordProductCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProductCharacteristics in the database
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdProductCharacteristics() throws Exception {
        // Initialize the database
        ordProductCharacteristicsRepository.saveAndFlush(ordProductCharacteristics);

        int databaseSizeBeforeDelete = ordProductCharacteristicsRepository.findAll().size();

        // Delete the ordProductCharacteristics
        restOrdProductCharacteristicsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordProductCharacteristics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdProductCharacteristics> ordProductCharacteristicsList = ordProductCharacteristicsRepository.findAll();
        assertThat(ordProductCharacteristicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

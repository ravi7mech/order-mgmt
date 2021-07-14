package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdCharacteristics;
import com.apptium.repository.OrdCharacteristicsRepository;
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
 * Integration tests for the {@link OrdCharacteristicsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdCharacteristicsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-characteristics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdCharacteristicsRepository ordCharacteristicsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdCharacteristicsMockMvc;

    private OrdCharacteristics ordCharacteristics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdCharacteristics createEntity(EntityManager em) {
        OrdCharacteristics ordCharacteristics = new OrdCharacteristics()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE);
        return ordCharacteristics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdCharacteristics createUpdatedEntity(EntityManager em) {
        OrdCharacteristics ordCharacteristics = new OrdCharacteristics()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE);
        return ordCharacteristics;
    }

    @BeforeEach
    public void initTest() {
        ordCharacteristics = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdCharacteristics() throws Exception {
        int databaseSizeBeforeCreate = ordCharacteristicsRepository.findAll().size();
        // Create the OrdCharacteristics
        restOrdCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isCreated());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeCreate + 1);
        OrdCharacteristics testOrdCharacteristics = ordCharacteristicsList.get(ordCharacteristicsList.size() - 1);
        assertThat(testOrdCharacteristics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdCharacteristics.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdCharacteristics.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
    }

    @Test
    @Transactional
    void createOrdCharacteristicsWithExistingId() throws Exception {
        // Create the OrdCharacteristics with an existing ID
        ordCharacteristics.setId(1L);

        int databaseSizeBeforeCreate = ordCharacteristicsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdCharacteristicsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdCharacteristics() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        // Get all the ordCharacteristicsList
        restOrdCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordCharacteristics.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)));
    }

    @Test
    @Transactional
    void getOrdCharacteristics() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        // Get the ordCharacteristics
        restOrdCharacteristicsMockMvc
            .perform(get(ENTITY_API_URL_ID, ordCharacteristics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordCharacteristics.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingOrdCharacteristics() throws Exception {
        // Get the ordCharacteristics
        restOrdCharacteristicsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdCharacteristics() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();

        // Update the ordCharacteristics
        OrdCharacteristics updatedOrdCharacteristics = ordCharacteristicsRepository.findById(ordCharacteristics.getId()).get();
        // Disconnect from session so that the updates on updatedOrdCharacteristics are not directly saved in db
        em.detach(updatedOrdCharacteristics);
        updatedOrdCharacteristics.name(UPDATED_NAME).value(UPDATED_VALUE).valueType(UPDATED_VALUE_TYPE);

        restOrdCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdCharacteristics testOrdCharacteristics = ordCharacteristicsList.get(ordCharacteristicsList.size() - 1);
        assertThat(testOrdCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordCharacteristics.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();

        // Update the ordCharacteristics using partial update
        OrdCharacteristics partialUpdatedOrdCharacteristics = new OrdCharacteristics();
        partialUpdatedOrdCharacteristics.setId(ordCharacteristics.getId());

        restOrdCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdCharacteristics testOrdCharacteristics = ordCharacteristicsList.get(ordCharacteristicsList.size() - 1);
        assertThat(testOrdCharacteristics.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdCharacteristics.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdCharacteristics.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOrdCharacteristicsWithPatch() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();

        // Update the ordCharacteristics using partial update
        OrdCharacteristics partialUpdatedOrdCharacteristics = new OrdCharacteristics();
        partialUpdatedOrdCharacteristics.setId(ordCharacteristics.getId());

        partialUpdatedOrdCharacteristics.name(UPDATED_NAME).value(UPDATED_VALUE).valueType(UPDATED_VALUE_TYPE);

        restOrdCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdCharacteristics))
            )
            .andExpect(status().isOk());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
        OrdCharacteristics testOrdCharacteristics = ordCharacteristicsList.get(ordCharacteristicsList.size() - 1);
        assertThat(testOrdCharacteristics.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdCharacteristics.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdCharacteristics.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordCharacteristics.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdCharacteristics() throws Exception {
        int databaseSizeBeforeUpdate = ordCharacteristicsRepository.findAll().size();
        ordCharacteristics.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdCharacteristicsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordCharacteristics))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdCharacteristics in the database
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdCharacteristics() throws Exception {
        // Initialize the database
        ordCharacteristicsRepository.saveAndFlush(ordCharacteristics);

        int databaseSizeBeforeDelete = ordCharacteristicsRepository.findAll().size();

        // Delete the ordCharacteristics
        restOrdCharacteristicsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordCharacteristics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdCharacteristics> ordCharacteristicsList = ordCharacteristicsRepository.findAll();
        assertThat(ordCharacteristicsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

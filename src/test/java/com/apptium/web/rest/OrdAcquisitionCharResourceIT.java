package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdAcquisitionChar;
import com.apptium.repository.OrdAcquisitionCharRepository;
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
 * Integration tests for the {@link OrdAcquisitionCharResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdAcquisitionCharResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ord-acquisition-chars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdAcquisitionCharRepository ordAcquisitionCharRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdAcquisitionCharMockMvc;

    private OrdAcquisitionChar ordAcquisitionChar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdAcquisitionChar createEntity(EntityManager em) {
        OrdAcquisitionChar ordAcquisitionChar = new OrdAcquisitionChar()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return ordAcquisitionChar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdAcquisitionChar createUpdatedEntity(EntityManager em) {
        OrdAcquisitionChar ordAcquisitionChar = new OrdAcquisitionChar()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        return ordAcquisitionChar;
    }

    @BeforeEach
    public void initTest() {
        ordAcquisitionChar = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeCreate = ordAcquisitionCharRepository.findAll().size();
        // Create the OrdAcquisitionChar
        restOrdAcquisitionCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isCreated());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeCreate + 1);
        OrdAcquisitionChar testOrdAcquisitionChar = ordAcquisitionCharList.get(ordAcquisitionCharList.size() - 1);
        assertThat(testOrdAcquisitionChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdAcquisitionChar.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdAcquisitionChar.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOrdAcquisitionChar.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdAcquisitionChar.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createOrdAcquisitionCharWithExistingId() throws Exception {
        // Create the OrdAcquisitionChar with an existing ID
        ordAcquisitionChar.setId(1L);

        int databaseSizeBeforeCreate = ordAcquisitionCharRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdAcquisitionCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdAcquisitionChars() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        // Get all the ordAcquisitionCharList
        restOrdAcquisitionCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordAcquisitionChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdAcquisitionChar() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        // Get the ordAcquisitionChar
        restOrdAcquisitionCharMockMvc
            .perform(get(ENTITY_API_URL_ID, ordAcquisitionChar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordAcquisitionChar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdAcquisitionChar() throws Exception {
        // Get the ordAcquisitionChar
        restOrdAcquisitionCharMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdAcquisitionChar() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();

        // Update the ordAcquisitionChar
        OrdAcquisitionChar updatedOrdAcquisitionChar = ordAcquisitionCharRepository.findById(ordAcquisitionChar.getId()).get();
        // Disconnect from session so that the updates on updatedOrdAcquisitionChar are not directly saved in db
        em.detach(updatedOrdAcquisitionChar);
        updatedOrdAcquisitionChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdAcquisitionCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdAcquisitionChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdAcquisitionChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisitionChar testOrdAcquisitionChar = ordAcquisitionCharList.get(ordAcquisitionCharList.size() - 1);
        assertThat(testOrdAcquisitionChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdAcquisitionChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdAcquisitionChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdAcquisitionChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdAcquisitionChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordAcquisitionChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdAcquisitionCharWithPatch() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();

        // Update the ordAcquisitionChar using partial update
        OrdAcquisitionChar partialUpdatedOrdAcquisitionChar = new OrdAcquisitionChar();
        partialUpdatedOrdAcquisitionChar.setId(ordAcquisitionChar.getId());

        partialUpdatedOrdAcquisitionChar.valueType(UPDATED_VALUE_TYPE).createdBy(UPDATED_CREATED_BY);

        restOrdAcquisitionCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdAcquisitionChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdAcquisitionChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisitionChar testOrdAcquisitionChar = ordAcquisitionCharList.get(ordAcquisitionCharList.size() - 1);
        assertThat(testOrdAcquisitionChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdAcquisitionChar.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdAcquisitionChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdAcquisitionChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdAcquisitionChar.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdAcquisitionCharWithPatch() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();

        // Update the ordAcquisitionChar using partial update
        OrdAcquisitionChar partialUpdatedOrdAcquisitionChar = new OrdAcquisitionChar();
        partialUpdatedOrdAcquisitionChar.setId(ordAcquisitionChar.getId());

        partialUpdatedOrdAcquisitionChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdAcquisitionCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdAcquisitionChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdAcquisitionChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisitionChar testOrdAcquisitionChar = ordAcquisitionCharList.get(ordAcquisitionCharList.size() - 1);
        assertThat(testOrdAcquisitionChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdAcquisitionChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdAcquisitionChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdAcquisitionChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdAcquisitionChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordAcquisitionChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdAcquisitionChar() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionCharRepository.findAll().size();
        ordAcquisitionChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionCharMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisitionChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdAcquisitionChar in the database
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdAcquisitionChar() throws Exception {
        // Initialize the database
        ordAcquisitionCharRepository.saveAndFlush(ordAcquisitionChar);

        int databaseSizeBeforeDelete = ordAcquisitionCharRepository.findAll().size();

        // Delete the ordAcquisitionChar
        restOrdAcquisitionCharMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordAcquisitionChar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdAcquisitionChar> ordAcquisitionCharList = ordAcquisitionCharRepository.findAll();
        assertThat(ordAcquisitionCharList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

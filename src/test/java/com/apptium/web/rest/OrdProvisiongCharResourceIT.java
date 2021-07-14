package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdProvisiongChar;
import com.apptium.repository.OrdProvisiongCharRepository;
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
 * Integration tests for the {@link OrdProvisiongCharResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdProvisiongCharResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ord-provisiong-chars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdProvisiongCharRepository ordProvisiongCharRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdProvisiongCharMockMvc;

    private OrdProvisiongChar ordProvisiongChar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProvisiongChar createEntity(EntityManager em) {
        OrdProvisiongChar ordProvisiongChar = new OrdProvisiongChar()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return ordProvisiongChar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProvisiongChar createUpdatedEntity(EntityManager em) {
        OrdProvisiongChar ordProvisiongChar = new OrdProvisiongChar()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        return ordProvisiongChar;
    }

    @BeforeEach
    public void initTest() {
        ordProvisiongChar = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeCreate = ordProvisiongCharRepository.findAll().size();
        // Create the OrdProvisiongChar
        restOrdProvisiongCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isCreated());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeCreate + 1);
        OrdProvisiongChar testOrdProvisiongChar = ordProvisiongCharList.get(ordProvisiongCharList.size() - 1);
        assertThat(testOrdProvisiongChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdProvisiongChar.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdProvisiongChar.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOrdProvisiongChar.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdProvisiongChar.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createOrdProvisiongCharWithExistingId() throws Exception {
        // Create the OrdProvisiongChar with an existing ID
        ordProvisiongChar.setId(1L);

        int databaseSizeBeforeCreate = ordProvisiongCharRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdProvisiongCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdProvisiongChars() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        // Get all the ordProvisiongCharList
        restOrdProvisiongCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordProvisiongChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdProvisiongChar() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        // Get the ordProvisiongChar
        restOrdProvisiongCharMockMvc
            .perform(get(ENTITY_API_URL_ID, ordProvisiongChar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordProvisiongChar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdProvisiongChar() throws Exception {
        // Get the ordProvisiongChar
        restOrdProvisiongCharMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdProvisiongChar() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();

        // Update the ordProvisiongChar
        OrdProvisiongChar updatedOrdProvisiongChar = ordProvisiongCharRepository.findById(ordProvisiongChar.getId()).get();
        // Disconnect from session so that the updates on updatedOrdProvisiongChar are not directly saved in db
        em.detach(updatedOrdProvisiongChar);
        updatedOrdProvisiongChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdProvisiongCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdProvisiongChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdProvisiongChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
        OrdProvisiongChar testOrdProvisiongChar = ordProvisiongCharList.get(ordProvisiongCharList.size() - 1);
        assertThat(testOrdProvisiongChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProvisiongChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdProvisiongChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdProvisiongChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdProvisiongChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordProvisiongChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdProvisiongCharWithPatch() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();

        // Update the ordProvisiongChar using partial update
        OrdProvisiongChar partialUpdatedOrdProvisiongChar = new OrdProvisiongChar();
        partialUpdatedOrdProvisiongChar.setId(ordProvisiongChar.getId());

        partialUpdatedOrdProvisiongChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdProvisiongCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProvisiongChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProvisiongChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
        OrdProvisiongChar testOrdProvisiongChar = ordProvisiongCharList.get(ordProvisiongCharList.size() - 1);
        assertThat(testOrdProvisiongChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProvisiongChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdProvisiongChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdProvisiongChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdProvisiongChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdProvisiongCharWithPatch() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();

        // Update the ordProvisiongChar using partial update
        OrdProvisiongChar partialUpdatedOrdProvisiongChar = new OrdProvisiongChar();
        partialUpdatedOrdProvisiongChar.setId(ordProvisiongChar.getId());

        partialUpdatedOrdProvisiongChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdProvisiongCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProvisiongChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProvisiongChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
        OrdProvisiongChar testOrdProvisiongChar = ordProvisiongCharList.get(ordProvisiongCharList.size() - 1);
        assertThat(testOrdProvisiongChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdProvisiongChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdProvisiongChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdProvisiongChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdProvisiongChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordProvisiongChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdProvisiongChar() throws Exception {
        int databaseSizeBeforeUpdate = ordProvisiongCharRepository.findAll().size();
        ordProvisiongChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProvisiongCharMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProvisiongChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProvisiongChar in the database
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdProvisiongChar() throws Exception {
        // Initialize the database
        ordProvisiongCharRepository.saveAndFlush(ordProvisiongChar);

        int databaseSizeBeforeDelete = ordProvisiongCharRepository.findAll().size();

        // Delete the ordProvisiongChar
        restOrdProvisiongCharMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordProvisiongChar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdProvisiongChar> ordProvisiongCharList = ordProvisiongCharRepository.findAll();
        assertThat(ordProvisiongCharList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

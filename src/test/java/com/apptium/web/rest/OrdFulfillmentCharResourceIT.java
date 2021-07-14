package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdFulfillmentChar;
import com.apptium.repository.OrdFulfillmentCharRepository;
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
 * Integration tests for the {@link OrdFulfillmentCharResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdFulfillmentCharResourceIT {

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

    private static final String ENTITY_API_URL = "/api/ord-fulfillment-chars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdFulfillmentCharRepository ordFulfillmentCharRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdFulfillmentCharMockMvc;

    private OrdFulfillmentChar ordFulfillmentChar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdFulfillmentChar createEntity(EntityManager em) {
        OrdFulfillmentChar ordFulfillmentChar = new OrdFulfillmentChar()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return ordFulfillmentChar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdFulfillmentChar createUpdatedEntity(EntityManager em) {
        OrdFulfillmentChar ordFulfillmentChar = new OrdFulfillmentChar()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        return ordFulfillmentChar;
    }

    @BeforeEach
    public void initTest() {
        ordFulfillmentChar = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeCreate = ordFulfillmentCharRepository.findAll().size();
        // Create the OrdFulfillmentChar
        restOrdFulfillmentCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isCreated());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeCreate + 1);
        OrdFulfillmentChar testOrdFulfillmentChar = ordFulfillmentCharList.get(ordFulfillmentCharList.size() - 1);
        assertThat(testOrdFulfillmentChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdFulfillmentChar.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdFulfillmentChar.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOrdFulfillmentChar.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdFulfillmentChar.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createOrdFulfillmentCharWithExistingId() throws Exception {
        // Create the OrdFulfillmentChar with an existing ID
        ordFulfillmentChar.setId(1L);

        int databaseSizeBeforeCreate = ordFulfillmentCharRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdFulfillmentCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdFulfillmentChars() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        // Get all the ordFulfillmentCharList
        restOrdFulfillmentCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordFulfillmentChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdFulfillmentChar() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        // Get the ordFulfillmentChar
        restOrdFulfillmentCharMockMvc
            .perform(get(ENTITY_API_URL_ID, ordFulfillmentChar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordFulfillmentChar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdFulfillmentChar() throws Exception {
        // Get the ordFulfillmentChar
        restOrdFulfillmentCharMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdFulfillmentChar() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();

        // Update the ordFulfillmentChar
        OrdFulfillmentChar updatedOrdFulfillmentChar = ordFulfillmentCharRepository.findById(ordFulfillmentChar.getId()).get();
        // Disconnect from session so that the updates on updatedOrdFulfillmentChar are not directly saved in db
        em.detach(updatedOrdFulfillmentChar);
        updatedOrdFulfillmentChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdFulfillmentCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdFulfillmentChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdFulfillmentChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillmentChar testOrdFulfillmentChar = ordFulfillmentCharList.get(ordFulfillmentCharList.size() - 1);
        assertThat(testOrdFulfillmentChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdFulfillmentChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdFulfillmentChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdFulfillmentChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdFulfillmentChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordFulfillmentChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdFulfillmentCharWithPatch() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();

        // Update the ordFulfillmentChar using partial update
        OrdFulfillmentChar partialUpdatedOrdFulfillmentChar = new OrdFulfillmentChar();
        partialUpdatedOrdFulfillmentChar.setId(ordFulfillmentChar.getId());

        partialUpdatedOrdFulfillmentChar
            .name(UPDATED_NAME)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdFulfillmentCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdFulfillmentChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdFulfillmentChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillmentChar testOrdFulfillmentChar = ordFulfillmentCharList.get(ordFulfillmentCharList.size() - 1);
        assertThat(testOrdFulfillmentChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdFulfillmentChar.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrdFulfillmentChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdFulfillmentChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdFulfillmentChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdFulfillmentCharWithPatch() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();

        // Update the ordFulfillmentChar using partial update
        OrdFulfillmentChar partialUpdatedOrdFulfillmentChar = new OrdFulfillmentChar();
        partialUpdatedOrdFulfillmentChar.setId(ordFulfillmentChar.getId());

        partialUpdatedOrdFulfillmentChar
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrdFulfillmentCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdFulfillmentChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdFulfillmentChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillmentChar testOrdFulfillmentChar = ordFulfillmentCharList.get(ordFulfillmentCharList.size() - 1);
        assertThat(testOrdFulfillmentChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdFulfillmentChar.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrdFulfillmentChar.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOrdFulfillmentChar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdFulfillmentChar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordFulfillmentChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdFulfillmentChar() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentCharRepository.findAll().size();
        ordFulfillmentChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentCharMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillmentChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdFulfillmentChar in the database
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdFulfillmentChar() throws Exception {
        // Initialize the database
        ordFulfillmentCharRepository.saveAndFlush(ordFulfillmentChar);

        int databaseSizeBeforeDelete = ordFulfillmentCharRepository.findAll().size();

        // Delete the ordFulfillmentChar
        restOrdFulfillmentCharMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordFulfillmentChar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdFulfillmentChar> ordFulfillmentCharList = ordFulfillmentCharRepository.findAll();
        assertThat(ordFulfillmentCharList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdOrderItemChar;
import com.apptium.repository.OrdOrderItemCharRepository;
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
 * Integration tests for the {@link OrdOrderItemCharResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdOrderItemCharResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-order-item-chars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdOrderItemCharRepository ordOrderItemCharRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdOrderItemCharMockMvc;

    private OrdOrderItemChar ordOrderItemChar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemChar createEntity(EntityManager em) {
        OrdOrderItemChar ordOrderItemChar = new OrdOrderItemChar().name(DEFAULT_NAME).value(DEFAULT_VALUE);
        return ordOrderItemChar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemChar createUpdatedEntity(EntityManager em) {
        OrdOrderItemChar ordOrderItemChar = new OrdOrderItemChar().name(UPDATED_NAME).value(UPDATED_VALUE);
        return ordOrderItemChar;
    }

    @BeforeEach
    public void initTest() {
        ordOrderItemChar = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeCreate = ordOrderItemCharRepository.findAll().size();
        // Create the OrdOrderItemChar
        restOrdOrderItemCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isCreated());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeCreate + 1);
        OrdOrderItemChar testOrdOrderItemChar = ordOrderItemCharList.get(ordOrderItemCharList.size() - 1);
        assertThat(testOrdOrderItemChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdOrderItemChar.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createOrdOrderItemCharWithExistingId() throws Exception {
        // Create the OrdOrderItemChar with an existing ID
        ordOrderItemChar.setId(1L);

        int databaseSizeBeforeCreate = ordOrderItemCharRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdOrderItemCharMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdOrderItemChars() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        // Get all the ordOrderItemCharList
        restOrdOrderItemCharMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordOrderItemChar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getOrdOrderItemChar() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        // Get the ordOrderItemChar
        restOrdOrderItemCharMockMvc
            .perform(get(ENTITY_API_URL_ID, ordOrderItemChar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordOrderItemChar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingOrdOrderItemChar() throws Exception {
        // Get the ordOrderItemChar
        restOrdOrderItemCharMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdOrderItemChar() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();

        // Update the ordOrderItemChar
        OrdOrderItemChar updatedOrdOrderItemChar = ordOrderItemCharRepository.findById(ordOrderItemChar.getId()).get();
        // Disconnect from session so that the updates on updatedOrdOrderItemChar are not directly saved in db
        em.detach(updatedOrdOrderItemChar);
        updatedOrdOrderItemChar.name(UPDATED_NAME).value(UPDATED_VALUE);

        restOrdOrderItemCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdOrderItemChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdOrderItemChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemChar testOrdOrderItemChar = ordOrderItemCharList.get(ordOrderItemCharList.size() - 1);
        assertThat(testOrdOrderItemChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdOrderItemChar.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordOrderItemChar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdOrderItemCharWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();

        // Update the ordOrderItemChar using partial update
        OrdOrderItemChar partialUpdatedOrdOrderItemChar = new OrdOrderItemChar();
        partialUpdatedOrdOrderItemChar.setId(ordOrderItemChar.getId());

        partialUpdatedOrdOrderItemChar.value(UPDATED_VALUE);

        restOrdOrderItemCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemChar testOrdOrderItemChar = ordOrderItemCharList.get(ordOrderItemCharList.size() - 1);
        assertThat(testOrdOrderItemChar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdOrderItemChar.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateOrdOrderItemCharWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();

        // Update the ordOrderItemChar using partial update
        OrdOrderItemChar partialUpdatedOrdOrderItemChar = new OrdOrderItemChar();
        partialUpdatedOrdOrderItemChar.setId(ordOrderItemChar.getId());

        partialUpdatedOrdOrderItemChar.name(UPDATED_NAME).value(UPDATED_VALUE);

        restOrdOrderItemCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemChar))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemChar testOrdOrderItemChar = ordOrderItemCharList.get(ordOrderItemCharList.size() - 1);
        assertThat(testOrdOrderItemChar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdOrderItemChar.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordOrderItemChar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdOrderItemChar() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemCharRepository.findAll().size();
        ordOrderItemChar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemCharMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemChar))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemChar in the database
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdOrderItemChar() throws Exception {
        // Initialize the database
        ordOrderItemCharRepository.saveAndFlush(ordOrderItemChar);

        int databaseSizeBeforeDelete = ordOrderItemCharRepository.findAll().size();

        // Delete the ordOrderItemChar
        restOrdOrderItemCharMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordOrderItemChar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdOrderItemChar> ordOrderItemCharList = ordOrderItemCharRepository.findAll();
        assertThat(ordOrderItemCharList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

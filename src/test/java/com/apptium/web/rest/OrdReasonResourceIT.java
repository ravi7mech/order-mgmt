package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdReason;
import com.apptium.repository.OrdReasonRepository;
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
 * Integration tests for the {@link OrdReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdReasonResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdReasonRepository ordReasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdReasonMockMvc;

    private OrdReason ordReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdReason createEntity(EntityManager em) {
        OrdReason ordReason = new OrdReason().reason(DEFAULT_REASON).description(DEFAULT_DESCRIPTION);
        return ordReason;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdReason createUpdatedEntity(EntityManager em) {
        OrdReason ordReason = new OrdReason().reason(UPDATED_REASON).description(UPDATED_DESCRIPTION);
        return ordReason;
    }

    @BeforeEach
    public void initTest() {
        ordReason = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdReason() throws Exception {
        int databaseSizeBeforeCreate = ordReasonRepository.findAll().size();
        // Create the OrdReason
        restOrdReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordReason)))
            .andExpect(status().isCreated());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeCreate + 1);
        OrdReason testOrdReason = ordReasonList.get(ordReasonList.size() - 1);
        assertThat(testOrdReason.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testOrdReason.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createOrdReasonWithExistingId() throws Exception {
        // Create the OrdReason with an existing ID
        ordReason.setId(1L);

        int databaseSizeBeforeCreate = ordReasonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordReason)))
            .andExpect(status().isBadRequest());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdReasons() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        // Get all the ordReasonList
        restOrdReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOrdReason() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        // Get the ordReason
        restOrdReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, ordReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordReason.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingOrdReason() throws Exception {
        // Get the ordReason
        restOrdReasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdReason() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();

        // Update the ordReason
        OrdReason updatedOrdReason = ordReasonRepository.findById(ordReason.getId()).get();
        // Disconnect from session so that the updates on updatedOrdReason are not directly saved in db
        em.detach(updatedOrdReason);
        updatedOrdReason.reason(UPDATED_REASON).description(UPDATED_DESCRIPTION);

        restOrdReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdReason.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdReason))
            )
            .andExpect(status().isOk());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
        OrdReason testOrdReason = ordReasonList.get(ordReasonList.size() - 1);
        assertThat(testOrdReason.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testOrdReason.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordReason.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordReason)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdReasonWithPatch() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();

        // Update the ordReason using partial update
        OrdReason partialUpdatedOrdReason = new OrdReason();
        partialUpdatedOrdReason.setId(ordReason.getId());

        partialUpdatedOrdReason.reason(UPDATED_REASON).description(UPDATED_DESCRIPTION);

        restOrdReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdReason))
            )
            .andExpect(status().isOk());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
        OrdReason testOrdReason = ordReasonList.get(ordReasonList.size() - 1);
        assertThat(testOrdReason.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testOrdReason.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOrdReasonWithPatch() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();

        // Update the ordReason using partial update
        OrdReason partialUpdatedOrdReason = new OrdReason();
        partialUpdatedOrdReason.setId(ordReason.getId());

        partialUpdatedOrdReason.reason(UPDATED_REASON).description(UPDATED_DESCRIPTION);

        restOrdReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdReason))
            )
            .andExpect(status().isOk());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
        OrdReason testOrdReason = ordReasonList.get(ordReasonList.size() - 1);
        assertThat(testOrdReason.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testOrdReason.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdReason() throws Exception {
        int databaseSizeBeforeUpdate = ordReasonRepository.findAll().size();
        ordReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdReasonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdReason in the database
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdReason() throws Exception {
        // Initialize the database
        ordReasonRepository.saveAndFlush(ordReason);

        int databaseSizeBeforeDelete = ordReasonRepository.findAll().size();

        // Delete the ordReason
        restOrdReasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordReason.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdReason> ordReasonList = ordReasonRepository.findAll();
        assertThat(ordReasonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

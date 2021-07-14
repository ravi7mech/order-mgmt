package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdAcquisition;
import com.apptium.repository.OrdAcquisitionRepository;
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
 * Integration tests for the {@link OrdAcquisitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdAcquisitionResourceIT {

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_AFFILIATE = "AAAAAAAAAA";
    private static final String UPDATED_AFFILIATE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTNER = "AAAAAAAAAA";
    private static final String UPDATED_PARTNER = "BBBBBBBBBB";

    private static final String DEFAULT_ACQUISITION_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_ACQUISITION_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-acquisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdAcquisitionRepository ordAcquisitionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdAcquisitionMockMvc;

    private OrdAcquisition ordAcquisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdAcquisition createEntity(EntityManager em) {
        OrdAcquisition ordAcquisition = new OrdAcquisition()
            .channel(DEFAULT_CHANNEL)
            .affiliate(DEFAULT_AFFILIATE)
            .partner(DEFAULT_PARTNER)
            .acquisitionAgent(DEFAULT_ACQUISITION_AGENT)
            .action(DEFAULT_ACTION);
        return ordAcquisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdAcquisition createUpdatedEntity(EntityManager em) {
        OrdAcquisition ordAcquisition = new OrdAcquisition()
            .channel(UPDATED_CHANNEL)
            .affiliate(UPDATED_AFFILIATE)
            .partner(UPDATED_PARTNER)
            .acquisitionAgent(UPDATED_ACQUISITION_AGENT)
            .action(UPDATED_ACTION);
        return ordAcquisition;
    }

    @BeforeEach
    public void initTest() {
        ordAcquisition = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdAcquisition() throws Exception {
        int databaseSizeBeforeCreate = ordAcquisitionRepository.findAll().size();
        // Create the OrdAcquisition
        restOrdAcquisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isCreated());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeCreate + 1);
        OrdAcquisition testOrdAcquisition = ordAcquisitionList.get(ordAcquisitionList.size() - 1);
        assertThat(testOrdAcquisition.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testOrdAcquisition.getAffiliate()).isEqualTo(DEFAULT_AFFILIATE);
        assertThat(testOrdAcquisition.getPartner()).isEqualTo(DEFAULT_PARTNER);
        assertThat(testOrdAcquisition.getAcquisitionAgent()).isEqualTo(DEFAULT_ACQUISITION_AGENT);
        assertThat(testOrdAcquisition.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void createOrdAcquisitionWithExistingId() throws Exception {
        // Create the OrdAcquisition with an existing ID
        ordAcquisition.setId(1L);

        int databaseSizeBeforeCreate = ordAcquisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdAcquisitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdAcquisitions() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        // Get all the ordAcquisitionList
        restOrdAcquisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL)))
            .andExpect(jsonPath("$.[*].affiliate").value(hasItem(DEFAULT_AFFILIATE)))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER)))
            .andExpect(jsonPath("$.[*].acquisitionAgent").value(hasItem(DEFAULT_ACQUISITION_AGENT)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)));
    }

    @Test
    @Transactional
    void getOrdAcquisition() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        // Get the ordAcquisition
        restOrdAcquisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, ordAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordAcquisition.getId().intValue()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL))
            .andExpect(jsonPath("$.affiliate").value(DEFAULT_AFFILIATE))
            .andExpect(jsonPath("$.partner").value(DEFAULT_PARTNER))
            .andExpect(jsonPath("$.acquisitionAgent").value(DEFAULT_ACQUISITION_AGENT))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION));
    }

    @Test
    @Transactional
    void getNonExistingOrdAcquisition() throws Exception {
        // Get the ordAcquisition
        restOrdAcquisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdAcquisition() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();

        // Update the ordAcquisition
        OrdAcquisition updatedOrdAcquisition = ordAcquisitionRepository.findById(ordAcquisition.getId()).get();
        // Disconnect from session so that the updates on updatedOrdAcquisition are not directly saved in db
        em.detach(updatedOrdAcquisition);
        updatedOrdAcquisition
            .channel(UPDATED_CHANNEL)
            .affiliate(UPDATED_AFFILIATE)
            .partner(UPDATED_PARTNER)
            .acquisitionAgent(UPDATED_ACQUISITION_AGENT)
            .action(UPDATED_ACTION);

        restOrdAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdAcquisition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdAcquisition))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisition testOrdAcquisition = ordAcquisitionList.get(ordAcquisitionList.size() - 1);
        assertThat(testOrdAcquisition.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testOrdAcquisition.getAffiliate()).isEqualTo(UPDATED_AFFILIATE);
        assertThat(testOrdAcquisition.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testOrdAcquisition.getAcquisitionAgent()).isEqualTo(UPDATED_ACQUISITION_AGENT);
        assertThat(testOrdAcquisition.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    void putNonExistingOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordAcquisition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordAcquisition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdAcquisitionWithPatch() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();

        // Update the ordAcquisition using partial update
        OrdAcquisition partialUpdatedOrdAcquisition = new OrdAcquisition();
        partialUpdatedOrdAcquisition.setId(ordAcquisition.getId());

        partialUpdatedOrdAcquisition.affiliate(UPDATED_AFFILIATE).partner(UPDATED_PARTNER).acquisitionAgent(UPDATED_ACQUISITION_AGENT);

        restOrdAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdAcquisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdAcquisition))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisition testOrdAcquisition = ordAcquisitionList.get(ordAcquisitionList.size() - 1);
        assertThat(testOrdAcquisition.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testOrdAcquisition.getAffiliate()).isEqualTo(UPDATED_AFFILIATE);
        assertThat(testOrdAcquisition.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testOrdAcquisition.getAcquisitionAgent()).isEqualTo(UPDATED_ACQUISITION_AGENT);
        assertThat(testOrdAcquisition.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void fullUpdateOrdAcquisitionWithPatch() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();

        // Update the ordAcquisition using partial update
        OrdAcquisition partialUpdatedOrdAcquisition = new OrdAcquisition();
        partialUpdatedOrdAcquisition.setId(ordAcquisition.getId());

        partialUpdatedOrdAcquisition
            .channel(UPDATED_CHANNEL)
            .affiliate(UPDATED_AFFILIATE)
            .partner(UPDATED_PARTNER)
            .acquisitionAgent(UPDATED_ACQUISITION_AGENT)
            .action(UPDATED_ACTION);

        restOrdAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdAcquisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdAcquisition))
            )
            .andExpect(status().isOk());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        OrdAcquisition testOrdAcquisition = ordAcquisitionList.get(ordAcquisitionList.size() - 1);
        assertThat(testOrdAcquisition.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testOrdAcquisition.getAffiliate()).isEqualTo(UPDATED_AFFILIATE);
        assertThat(testOrdAcquisition.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testOrdAcquisition.getAcquisitionAgent()).isEqualTo(UPDATED_ACQUISITION_AGENT);
        assertThat(testOrdAcquisition.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    void patchNonExistingOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordAcquisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = ordAcquisitionRepository.findAll().size();
        ordAcquisition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdAcquisitionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordAcquisition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdAcquisition in the database
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdAcquisition() throws Exception {
        // Initialize the database
        ordAcquisitionRepository.saveAndFlush(ordAcquisition);

        int databaseSizeBeforeDelete = ordAcquisitionRepository.findAll().size();

        // Delete the ordAcquisition
        restOrdAcquisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordAcquisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdAcquisition> ordAcquisitionList = ordAcquisitionRepository.findAll();
        assertThat(ordAcquisitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

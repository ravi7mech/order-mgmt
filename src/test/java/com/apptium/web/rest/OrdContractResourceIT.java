package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdContract;
import com.apptium.repository.OrdContractRepository;
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
 * Integration tests for the {@link OrdContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdContractResourceIT {

    private static final Long DEFAULT_CONTRACT_ID = 1L;
    private static final Long UPDATED_CONTRACT_ID = 2L;

    private static final Long DEFAULT_LANGUAGE_ID = 1L;
    private static final Long UPDATED_LANGUAGE_ID = 2L;

    private static final String DEFAULT_TERM_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TERM_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdContractRepository ordContractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdContractMockMvc;

    private OrdContract ordContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContract createEntity(EntityManager em) {
        OrdContract ordContract = new OrdContract()
            .contractId(DEFAULT_CONTRACT_ID)
            .languageId(DEFAULT_LANGUAGE_ID)
            .termTypeCode(DEFAULT_TERM_TYPE_CODE)
            .action(DEFAULT_ACTION)
            .status(DEFAULT_STATUS);
        return ordContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContract createUpdatedEntity(EntityManager em) {
        OrdContract ordContract = new OrdContract()
            .contractId(UPDATED_CONTRACT_ID)
            .languageId(UPDATED_LANGUAGE_ID)
            .termTypeCode(UPDATED_TERM_TYPE_CODE)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS);
        return ordContract;
    }

    @BeforeEach
    public void initTest() {
        ordContract = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdContract() throws Exception {
        int databaseSizeBeforeCreate = ordContractRepository.findAll().size();
        // Create the OrdContract
        restOrdContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContract)))
            .andExpect(status().isCreated());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeCreate + 1);
        OrdContract testOrdContract = ordContractList.get(ordContractList.size() - 1);
        assertThat(testOrdContract.getContractId()).isEqualTo(DEFAULT_CONTRACT_ID);
        assertThat(testOrdContract.getLanguageId()).isEqualTo(DEFAULT_LANGUAGE_ID);
        assertThat(testOrdContract.getTermTypeCode()).isEqualTo(DEFAULT_TERM_TYPE_CODE);
        assertThat(testOrdContract.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testOrdContract.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrdContractWithExistingId() throws Exception {
        // Create the OrdContract with an existing ID
        ordContract.setId(1L);

        int databaseSizeBeforeCreate = ordContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContract)))
            .andExpect(status().isBadRequest());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdContracts() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        // Get all the ordContractList
        restOrdContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractId").value(hasItem(DEFAULT_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].languageId").value(hasItem(DEFAULT_LANGUAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].termTypeCode").value(hasItem(DEFAULT_TERM_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getOrdContract() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        // Get the ordContract
        restOrdContractMockMvc
            .perform(get(ENTITY_API_URL_ID, ordContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordContract.getId().intValue()))
            .andExpect(jsonPath("$.contractId").value(DEFAULT_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.languageId").value(DEFAULT_LANGUAGE_ID.intValue()))
            .andExpect(jsonPath("$.termTypeCode").value(DEFAULT_TERM_TYPE_CODE))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingOrdContract() throws Exception {
        // Get the ordContract
        restOrdContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdContract() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();

        // Update the ordContract
        OrdContract updatedOrdContract = ordContractRepository.findById(ordContract.getId()).get();
        // Disconnect from session so that the updates on updatedOrdContract are not directly saved in db
        em.detach(updatedOrdContract);
        updatedOrdContract
            .contractId(UPDATED_CONTRACT_ID)
            .languageId(UPDATED_LANGUAGE_ID)
            .termTypeCode(UPDATED_TERM_TYPE_CODE)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS);

        restOrdContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdContract))
            )
            .andExpect(status().isOk());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
        OrdContract testOrdContract = ordContractList.get(ordContractList.size() - 1);
        assertThat(testOrdContract.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testOrdContract.getLanguageId()).isEqualTo(UPDATED_LANGUAGE_ID);
        assertThat(testOrdContract.getTermTypeCode()).isEqualTo(UPDATED_TERM_TYPE_CODE);
        assertThat(testOrdContract.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdContract.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdContractWithPatch() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();

        // Update the ordContract using partial update
        OrdContract partialUpdatedOrdContract = new OrdContract();
        partialUpdatedOrdContract.setId(ordContract.getId());

        partialUpdatedOrdContract.contractId(UPDATED_CONTRACT_ID).status(UPDATED_STATUS);

        restOrdContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContract))
            )
            .andExpect(status().isOk());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
        OrdContract testOrdContract = ordContractList.get(ordContractList.size() - 1);
        assertThat(testOrdContract.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testOrdContract.getLanguageId()).isEqualTo(DEFAULT_LANGUAGE_ID);
        assertThat(testOrdContract.getTermTypeCode()).isEqualTo(DEFAULT_TERM_TYPE_CODE);
        assertThat(testOrdContract.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testOrdContract.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrdContractWithPatch() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();

        // Update the ordContract using partial update
        OrdContract partialUpdatedOrdContract = new OrdContract();
        partialUpdatedOrdContract.setId(ordContract.getId());

        partialUpdatedOrdContract
            .contractId(UPDATED_CONTRACT_ID)
            .languageId(UPDATED_LANGUAGE_ID)
            .termTypeCode(UPDATED_TERM_TYPE_CODE)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS);

        restOrdContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContract))
            )
            .andExpect(status().isOk());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
        OrdContract testOrdContract = ordContractList.get(ordContractList.size() - 1);
        assertThat(testOrdContract.getContractId()).isEqualTo(UPDATED_CONTRACT_ID);
        assertThat(testOrdContract.getLanguageId()).isEqualTo(UPDATED_LANGUAGE_ID);
        assertThat(testOrdContract.getTermTypeCode()).isEqualTo(UPDATED_TERM_TYPE_CODE);
        assertThat(testOrdContract.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdContract.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdContract() throws Exception {
        int databaseSizeBeforeUpdate = ordContractRepository.findAll().size();
        ordContract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContractMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordContract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContract in the database
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdContract() throws Exception {
        // Initialize the database
        ordContractRepository.saveAndFlush(ordContract);

        int databaseSizeBeforeDelete = ordContractRepository.findAll().size();

        // Delete the ordContract
        restOrdContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdContract> ordContractList = ordContractRepository.findAll();
        assertThat(ordContractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

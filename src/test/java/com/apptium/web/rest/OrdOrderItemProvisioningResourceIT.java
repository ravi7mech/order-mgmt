package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdOrderItemProvisioning;
import com.apptium.repository.OrdOrderItemProvisioningRepository;
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
 * Integration tests for the {@link OrdOrderItemProvisioningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdOrderItemProvisioningResourceIT {

    private static final Long DEFAULT_PROVISIONING_ID = 1L;
    private static final Long UPDATED_PROVISIONING_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-order-item-provisionings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdOrderItemProvisioningRepository ordOrderItemProvisioningRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdOrderItemProvisioningMockMvc;

    private OrdOrderItemProvisioning ordOrderItemProvisioning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemProvisioning createEntity(EntityManager em) {
        OrdOrderItemProvisioning ordOrderItemProvisioning = new OrdOrderItemProvisioning()
            .provisioningId(DEFAULT_PROVISIONING_ID)
            .status(DEFAULT_STATUS);
        return ordOrderItemProvisioning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemProvisioning createUpdatedEntity(EntityManager em) {
        OrdOrderItemProvisioning ordOrderItemProvisioning = new OrdOrderItemProvisioning()
            .provisioningId(UPDATED_PROVISIONING_ID)
            .status(UPDATED_STATUS);
        return ordOrderItemProvisioning;
    }

    @BeforeEach
    public void initTest() {
        ordOrderItemProvisioning = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeCreate = ordOrderItemProvisioningRepository.findAll().size();
        // Create the OrdOrderItemProvisioning
        restOrdOrderItemProvisioningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isCreated());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeCreate + 1);
        OrdOrderItemProvisioning testOrdOrderItemProvisioning = ordOrderItemProvisioningList.get(ordOrderItemProvisioningList.size() - 1);
        assertThat(testOrdOrderItemProvisioning.getProvisioningId()).isEqualTo(DEFAULT_PROVISIONING_ID);
        assertThat(testOrdOrderItemProvisioning.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrdOrderItemProvisioningWithExistingId() throws Exception {
        // Create the OrdOrderItemProvisioning with an existing ID
        ordOrderItemProvisioning.setId(1L);

        int databaseSizeBeforeCreate = ordOrderItemProvisioningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdOrderItemProvisioningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdOrderItemProvisionings() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        // Get all the ordOrderItemProvisioningList
        restOrdOrderItemProvisioningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordOrderItemProvisioning.getId().intValue())))
            .andExpect(jsonPath("$.[*].provisioningId").value(hasItem(DEFAULT_PROVISIONING_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getOrdOrderItemProvisioning() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        // Get the ordOrderItemProvisioning
        restOrdOrderItemProvisioningMockMvc
            .perform(get(ENTITY_API_URL_ID, ordOrderItemProvisioning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordOrderItemProvisioning.getId().intValue()))
            .andExpect(jsonPath("$.provisioningId").value(DEFAULT_PROVISIONING_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingOrdOrderItemProvisioning() throws Exception {
        // Get the ordOrderItemProvisioning
        restOrdOrderItemProvisioningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdOrderItemProvisioning() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();

        // Update the ordOrderItemProvisioning
        OrdOrderItemProvisioning updatedOrdOrderItemProvisioning = ordOrderItemProvisioningRepository
            .findById(ordOrderItemProvisioning.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrdOrderItemProvisioning are not directly saved in db
        em.detach(updatedOrdOrderItemProvisioning);
        updatedOrdOrderItemProvisioning.provisioningId(UPDATED_PROVISIONING_ID).status(UPDATED_STATUS);

        restOrdOrderItemProvisioningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdOrderItemProvisioning.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdOrderItemProvisioning))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemProvisioning testOrdOrderItemProvisioning = ordOrderItemProvisioningList.get(ordOrderItemProvisioningList.size() - 1);
        assertThat(testOrdOrderItemProvisioning.getProvisioningId()).isEqualTo(UPDATED_PROVISIONING_ID);
        assertThat(testOrdOrderItemProvisioning.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordOrderItemProvisioning.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdOrderItemProvisioningWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();

        // Update the ordOrderItemProvisioning using partial update
        OrdOrderItemProvisioning partialUpdatedOrdOrderItemProvisioning = new OrdOrderItemProvisioning();
        partialUpdatedOrdOrderItemProvisioning.setId(ordOrderItemProvisioning.getId());

        partialUpdatedOrdOrderItemProvisioning.status(UPDATED_STATUS);

        restOrdOrderItemProvisioningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemProvisioning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemProvisioning))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemProvisioning testOrdOrderItemProvisioning = ordOrderItemProvisioningList.get(ordOrderItemProvisioningList.size() - 1);
        assertThat(testOrdOrderItemProvisioning.getProvisioningId()).isEqualTo(DEFAULT_PROVISIONING_ID);
        assertThat(testOrdOrderItemProvisioning.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrdOrderItemProvisioningWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();

        // Update the ordOrderItemProvisioning using partial update
        OrdOrderItemProvisioning partialUpdatedOrdOrderItemProvisioning = new OrdOrderItemProvisioning();
        partialUpdatedOrdOrderItemProvisioning.setId(ordOrderItemProvisioning.getId());

        partialUpdatedOrdOrderItemProvisioning.provisioningId(UPDATED_PROVISIONING_ID).status(UPDATED_STATUS);

        restOrdOrderItemProvisioningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemProvisioning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemProvisioning))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemProvisioning testOrdOrderItemProvisioning = ordOrderItemProvisioningList.get(ordOrderItemProvisioningList.size() - 1);
        assertThat(testOrdOrderItemProvisioning.getProvisioningId()).isEqualTo(UPDATED_PROVISIONING_ID);
        assertThat(testOrdOrderItemProvisioning.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordOrderItemProvisioning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdOrderItemProvisioning() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemProvisioningRepository.findAll().size();
        ordOrderItemProvisioning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemProvisioningMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemProvisioning))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemProvisioning in the database
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdOrderItemProvisioning() throws Exception {
        // Initialize the database
        ordOrderItemProvisioningRepository.saveAndFlush(ordOrderItemProvisioning);

        int databaseSizeBeforeDelete = ordOrderItemProvisioningRepository.findAll().size();

        // Delete the ordOrderItemProvisioning
        restOrdOrderItemProvisioningMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordOrderItemProvisioning.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdOrderItemProvisioning> ordOrderItemProvisioningList = ordOrderItemProvisioningRepository.findAll();
        assertThat(ordOrderItemProvisioningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

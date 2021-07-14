package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdOrderItemRelationship;
import com.apptium.repository.OrdOrderItemRelationshipRepository;
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
 * Integration tests for the {@link OrdOrderItemRelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdOrderItemRelationshipResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_PRIMARY_ORDER_ITEM_ID = 1L;
    private static final Long UPDATED_PRIMARY_ORDER_ITEM_ID = 2L;

    private static final Long DEFAULT_SECONDARY_ORDER_ITEM_ID = 1L;
    private static final Long UPDATED_SECONDARY_ORDER_ITEM_ID = 2L;

    private static final String ENTITY_API_URL = "/api/ord-order-item-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdOrderItemRelationshipRepository ordOrderItemRelationshipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdOrderItemRelationshipMockMvc;

    private OrdOrderItemRelationship ordOrderItemRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemRelationship createEntity(EntityManager em) {
        OrdOrderItemRelationship ordOrderItemRelationship = new OrdOrderItemRelationship()
            .type(DEFAULT_TYPE)
            .primaryOrderItemId(DEFAULT_PRIMARY_ORDER_ITEM_ID)
            .secondaryOrderItemId(DEFAULT_SECONDARY_ORDER_ITEM_ID);
        return ordOrderItemRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItemRelationship createUpdatedEntity(EntityManager em) {
        OrdOrderItemRelationship ordOrderItemRelationship = new OrdOrderItemRelationship()
            .type(UPDATED_TYPE)
            .primaryOrderItemId(UPDATED_PRIMARY_ORDER_ITEM_ID)
            .secondaryOrderItemId(UPDATED_SECONDARY_ORDER_ITEM_ID);
        return ordOrderItemRelationship;
    }

    @BeforeEach
    public void initTest() {
        ordOrderItemRelationship = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeCreate = ordOrderItemRelationshipRepository.findAll().size();
        // Create the OrdOrderItemRelationship
        restOrdOrderItemRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isCreated());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        OrdOrderItemRelationship testOrdOrderItemRelationship = ordOrderItemRelationshipList.get(ordOrderItemRelationshipList.size() - 1);
        assertThat(testOrdOrderItemRelationship.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrdOrderItemRelationship.getPrimaryOrderItemId()).isEqualTo(DEFAULT_PRIMARY_ORDER_ITEM_ID);
        assertThat(testOrdOrderItemRelationship.getSecondaryOrderItemId()).isEqualTo(DEFAULT_SECONDARY_ORDER_ITEM_ID);
    }

    @Test
    @Transactional
    void createOrdOrderItemRelationshipWithExistingId() throws Exception {
        // Create the OrdOrderItemRelationship with an existing ID
        ordOrderItemRelationship.setId(1L);

        int databaseSizeBeforeCreate = ordOrderItemRelationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdOrderItemRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdOrderItemRelationships() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        // Get all the ordOrderItemRelationshipList
        restOrdOrderItemRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordOrderItemRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].primaryOrderItemId").value(hasItem(DEFAULT_PRIMARY_ORDER_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].secondaryOrderItemId").value(hasItem(DEFAULT_SECONDARY_ORDER_ITEM_ID.intValue())));
    }

    @Test
    @Transactional
    void getOrdOrderItemRelationship() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        // Get the ordOrderItemRelationship
        restOrdOrderItemRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, ordOrderItemRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordOrderItemRelationship.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.primaryOrderItemId").value(DEFAULT_PRIMARY_ORDER_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.secondaryOrderItemId").value(DEFAULT_SECONDARY_ORDER_ITEM_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdOrderItemRelationship() throws Exception {
        // Get the ordOrderItemRelationship
        restOrdOrderItemRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdOrderItemRelationship() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();

        // Update the ordOrderItemRelationship
        OrdOrderItemRelationship updatedOrdOrderItemRelationship = ordOrderItemRelationshipRepository
            .findById(ordOrderItemRelationship.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrdOrderItemRelationship are not directly saved in db
        em.detach(updatedOrdOrderItemRelationship);
        updatedOrdOrderItemRelationship
            .type(UPDATED_TYPE)
            .primaryOrderItemId(UPDATED_PRIMARY_ORDER_ITEM_ID)
            .secondaryOrderItemId(UPDATED_SECONDARY_ORDER_ITEM_ID);

        restOrdOrderItemRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdOrderItemRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdOrderItemRelationship))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemRelationship testOrdOrderItemRelationship = ordOrderItemRelationshipList.get(ordOrderItemRelationshipList.size() - 1);
        assertThat(testOrdOrderItemRelationship.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdOrderItemRelationship.getPrimaryOrderItemId()).isEqualTo(UPDATED_PRIMARY_ORDER_ITEM_ID);
        assertThat(testOrdOrderItemRelationship.getSecondaryOrderItemId()).isEqualTo(UPDATED_SECONDARY_ORDER_ITEM_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordOrderItemRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdOrderItemRelationshipWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();

        // Update the ordOrderItemRelationship using partial update
        OrdOrderItemRelationship partialUpdatedOrdOrderItemRelationship = new OrdOrderItemRelationship();
        partialUpdatedOrdOrderItemRelationship.setId(ordOrderItemRelationship.getId());

        partialUpdatedOrdOrderItemRelationship
            .type(UPDATED_TYPE)
            .primaryOrderItemId(UPDATED_PRIMARY_ORDER_ITEM_ID)
            .secondaryOrderItemId(UPDATED_SECONDARY_ORDER_ITEM_ID);

        restOrdOrderItemRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemRelationship))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemRelationship testOrdOrderItemRelationship = ordOrderItemRelationshipList.get(ordOrderItemRelationshipList.size() - 1);
        assertThat(testOrdOrderItemRelationship.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdOrderItemRelationship.getPrimaryOrderItemId()).isEqualTo(UPDATED_PRIMARY_ORDER_ITEM_ID);
        assertThat(testOrdOrderItemRelationship.getSecondaryOrderItemId()).isEqualTo(UPDATED_SECONDARY_ORDER_ITEM_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrdOrderItemRelationshipWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();

        // Update the ordOrderItemRelationship using partial update
        OrdOrderItemRelationship partialUpdatedOrdOrderItemRelationship = new OrdOrderItemRelationship();
        partialUpdatedOrdOrderItemRelationship.setId(ordOrderItemRelationship.getId());

        partialUpdatedOrdOrderItemRelationship
            .type(UPDATED_TYPE)
            .primaryOrderItemId(UPDATED_PRIMARY_ORDER_ITEM_ID)
            .secondaryOrderItemId(UPDATED_SECONDARY_ORDER_ITEM_ID);

        restOrdOrderItemRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItemRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItemRelationship))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItemRelationship testOrdOrderItemRelationship = ordOrderItemRelationshipList.get(ordOrderItemRelationshipList.size() - 1);
        assertThat(testOrdOrderItemRelationship.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdOrderItemRelationship.getPrimaryOrderItemId()).isEqualTo(UPDATED_PRIMARY_ORDER_ITEM_ID);
        assertThat(testOrdOrderItemRelationship.getSecondaryOrderItemId()).isEqualTo(UPDATED_SECONDARY_ORDER_ITEM_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordOrderItemRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdOrderItemRelationship() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRelationshipRepository.findAll().size();
        ordOrderItemRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItemRelationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItemRelationship in the database
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdOrderItemRelationship() throws Exception {
        // Initialize the database
        ordOrderItemRelationshipRepository.saveAndFlush(ordOrderItemRelationship);

        int databaseSizeBeforeDelete = ordOrderItemRelationshipRepository.findAll().size();

        // Delete the ordOrderItemRelationship
        restOrdOrderItemRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordOrderItemRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdOrderItemRelationship> ordOrderItemRelationshipList = ordOrderItemRelationshipRepository.findAll();
        assertThat(ordOrderItemRelationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdOrderItem;
import com.apptium.repository.OrdOrderItemRepository;
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
 * Integration tests for the {@link OrdOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdOrderItemResourceIT {

    private static final Long DEFAULT_BILLER_ID = 1L;
    private static final Long UPDATED_BILLER_ID = 2L;

    private static final Long DEFAULT_FULLFILLMENT_ID = 1L;
    private static final Long UPDATED_FULLFILLMENT_ID = 2L;

    private static final Long DEFAULT_ACQUISITION_ID = 1L;
    private static final Long UPDATED_ACQUISITION_ID = 2L;

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final String DEFAULT_ITEM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_CART_ITEM_ID = 1L;
    private static final Long UPDATED_CART_ITEM_ID = 2L;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ord-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdOrderItemRepository ordOrderItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdOrderItemMockMvc;

    private OrdOrderItem ordOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItem createEntity(EntityManager em) {
        OrdOrderItem ordOrderItem = new OrdOrderItem()
            .billerId(DEFAULT_BILLER_ID)
            .fullfillmentId(DEFAULT_FULLFILLMENT_ID)
            .acquisitionId(DEFAULT_ACQUISITION_ID)
            .action(DEFAULT_ACTION)
            .state(DEFAULT_STATE)
            .quantity(DEFAULT_QUANTITY)
            .itemType(DEFAULT_ITEM_TYPE)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .cartItemId(DEFAULT_CART_ITEM_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return ordOrderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderItem createUpdatedEntity(EntityManager em) {
        OrdOrderItem ordOrderItem = new OrdOrderItem()
            .billerId(UPDATED_BILLER_ID)
            .fullfillmentId(UPDATED_FULLFILLMENT_ID)
            .acquisitionId(UPDATED_ACQUISITION_ID)
            .action(UPDATED_ACTION)
            .state(UPDATED_STATE)
            .quantity(UPDATED_QUANTITY)
            .itemType(UPDATED_ITEM_TYPE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .cartItemId(UPDATED_CART_ITEM_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);
        return ordOrderItem;
    }

    @BeforeEach
    public void initTest() {
        ordOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdOrderItem() throws Exception {
        int databaseSizeBeforeCreate = ordOrderItemRepository.findAll().size();
        // Create the OrdOrderItem
        restOrdOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItem)))
            .andExpect(status().isCreated());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrdOrderItem testOrdOrderItem = ordOrderItemList.get(ordOrderItemList.size() - 1);
        assertThat(testOrdOrderItem.getBillerId()).isEqualTo(DEFAULT_BILLER_ID);
        assertThat(testOrdOrderItem.getFullfillmentId()).isEqualTo(DEFAULT_FULLFILLMENT_ID);
        assertThat(testOrdOrderItem.getAcquisitionId()).isEqualTo(DEFAULT_ACQUISITION_ID);
        assertThat(testOrdOrderItem.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testOrdOrderItem.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOrdOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrdOrderItem.getItemType()).isEqualTo(DEFAULT_ITEM_TYPE);
        assertThat(testOrdOrderItem.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testOrdOrderItem.getCartItemId()).isEqualTo(DEFAULT_CART_ITEM_ID);
        assertThat(testOrdOrderItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdOrderItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdOrderItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOrdOrderItem.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createOrdOrderItemWithExistingId() throws Exception {
        // Create the OrdOrderItem with an existing ID
        ordOrderItem.setId(1L);

        int databaseSizeBeforeCreate = ordOrderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdOrderItems() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        // Get all the ordOrderItemList
        restOrdOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].billerId").value(hasItem(DEFAULT_BILLER_ID.intValue())))
            .andExpect(jsonPath("$.[*].fullfillmentId").value(hasItem(DEFAULT_FULLFILLMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].acquisitionId").value(hasItem(DEFAULT_ACQUISITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cartItemId").value(hasItem(DEFAULT_CART_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdOrderItem() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        // Get the ordOrderItem
        restOrdOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, ordOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.billerId").value(DEFAULT_BILLER_ID.intValue()))
            .andExpect(jsonPath("$.fullfillmentId").value(DEFAULT_FULLFILLMENT_ID.intValue()))
            .andExpect(jsonPath("$.acquisitionId").value(DEFAULT_ACQUISITION_ID.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.cartItemId").value(DEFAULT_CART_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdOrderItem() throws Exception {
        // Get the ordOrderItem
        restOrdOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdOrderItem() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();

        // Update the ordOrderItem
        OrdOrderItem updatedOrdOrderItem = ordOrderItemRepository.findById(ordOrderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrdOrderItem are not directly saved in db
        em.detach(updatedOrdOrderItem);
        updatedOrdOrderItem
            .billerId(UPDATED_BILLER_ID)
            .fullfillmentId(UPDATED_FULLFILLMENT_ID)
            .acquisitionId(UPDATED_ACQUISITION_ID)
            .action(UPDATED_ACTION)
            .state(UPDATED_STATE)
            .quantity(UPDATED_QUANTITY)
            .itemType(UPDATED_ITEM_TYPE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .cartItemId(UPDATED_CART_ITEM_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restOrdOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItem testOrdOrderItem = ordOrderItemList.get(ordOrderItemList.size() - 1);
        assertThat(testOrdOrderItem.getBillerId()).isEqualTo(UPDATED_BILLER_ID);
        assertThat(testOrdOrderItem.getFullfillmentId()).isEqualTo(UPDATED_FULLFILLMENT_ID);
        assertThat(testOrdOrderItem.getAcquisitionId()).isEqualTo(UPDATED_ACQUISITION_ID);
        assertThat(testOrdOrderItem.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdOrderItem.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrdOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrdOrderItem.getItemType()).isEqualTo(UPDATED_ITEM_TYPE);
        assertThat(testOrdOrderItem.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testOrdOrderItem.getCartItemId()).isEqualTo(UPDATED_CART_ITEM_ID);
        assertThat(testOrdOrderItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdOrderItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdOrderItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOrdOrderItem.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdOrderItemWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();

        // Update the ordOrderItem using partial update
        OrdOrderItem partialUpdatedOrdOrderItem = new OrdOrderItem();
        partialUpdatedOrdOrderItem.setId(ordOrderItem.getId());

        partialUpdatedOrdOrderItem
            .fullfillmentId(UPDATED_FULLFILLMENT_ID)
            .action(UPDATED_ACTION)
            .state(UPDATED_STATE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION);

        restOrdOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItem testOrdOrderItem = ordOrderItemList.get(ordOrderItemList.size() - 1);
        assertThat(testOrdOrderItem.getBillerId()).isEqualTo(DEFAULT_BILLER_ID);
        assertThat(testOrdOrderItem.getFullfillmentId()).isEqualTo(UPDATED_FULLFILLMENT_ID);
        assertThat(testOrdOrderItem.getAcquisitionId()).isEqualTo(DEFAULT_ACQUISITION_ID);
        assertThat(testOrdOrderItem.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdOrderItem.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrdOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrdOrderItem.getItemType()).isEqualTo(DEFAULT_ITEM_TYPE);
        assertThat(testOrdOrderItem.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testOrdOrderItem.getCartItemId()).isEqualTo(DEFAULT_CART_ITEM_ID);
        assertThat(testOrdOrderItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdOrderItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdOrderItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOrdOrderItem.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdOrderItemWithPatch() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();

        // Update the ordOrderItem using partial update
        OrdOrderItem partialUpdatedOrdOrderItem = new OrdOrderItem();
        partialUpdatedOrdOrderItem.setId(ordOrderItem.getId());

        partialUpdatedOrdOrderItem
            .billerId(UPDATED_BILLER_ID)
            .fullfillmentId(UPDATED_FULLFILLMENT_ID)
            .acquisitionId(UPDATED_ACQUISITION_ID)
            .action(UPDATED_ACTION)
            .state(UPDATED_STATE)
            .quantity(UPDATED_QUANTITY)
            .itemType(UPDATED_ITEM_TYPE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .cartItemId(UPDATED_CART_ITEM_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restOrdOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderItem testOrdOrderItem = ordOrderItemList.get(ordOrderItemList.size() - 1);
        assertThat(testOrdOrderItem.getBillerId()).isEqualTo(UPDATED_BILLER_ID);
        assertThat(testOrdOrderItem.getFullfillmentId()).isEqualTo(UPDATED_FULLFILLMENT_ID);
        assertThat(testOrdOrderItem.getAcquisitionId()).isEqualTo(UPDATED_ACQUISITION_ID);
        assertThat(testOrdOrderItem.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdOrderItem.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrdOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrdOrderItem.getItemType()).isEqualTo(UPDATED_ITEM_TYPE);
        assertThat(testOrdOrderItem.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testOrdOrderItem.getCartItemId()).isEqualTo(UPDATED_CART_ITEM_ID);
        assertThat(testOrdOrderItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdOrderItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdOrderItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOrdOrderItem.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderItemRepository.findAll().size();
        ordOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordOrderItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderItem in the database
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdOrderItem() throws Exception {
        // Initialize the database
        ordOrderItemRepository.saveAndFlush(ordOrderItem);

        int databaseSizeBeforeDelete = ordOrderItemRepository.findAll().size();

        // Delete the ordOrderItem
        restOrdOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdOrderItem> ordOrderItemList = ordOrderItemRepository.findAll();
        assertThat(ordOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

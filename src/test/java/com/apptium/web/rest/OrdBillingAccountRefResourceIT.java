package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdBillingAccountRef;
import com.apptium.repository.OrdBillingAccountRefRepository;
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
 * Integration tests for the {@link OrdBillingAccountRefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdBillingAccountRefResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HREF = "AAAAAAAAAA";
    private static final String UPDATED_HREF = "BBBBBBBBBB";

    private static final Long DEFAULT_CART_PRICE_ID = 1L;
    private static final Long UPDATED_CART_PRICE_ID = 2L;

    private static final Long DEFAULT_BILLING_ACCOUNT_ID = 1L;
    private static final Long UPDATED_BILLING_ACCOUNT_ID = 2L;

    private static final String DEFAULT_BILLING_SYSTEM = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_SYSTEM = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_QUOTE_ID = 1L;
    private static final Long UPDATED_QUOTE_ID = 2L;

    private static final Long DEFAULT_SALES_ORDER_ID = 1L;
    private static final Long UPDATED_SALES_ORDER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/ord-billing-account-refs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdBillingAccountRefRepository ordBillingAccountRefRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdBillingAccountRefMockMvc;

    private OrdBillingAccountRef ordBillingAccountRef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdBillingAccountRef createEntity(EntityManager em) {
        OrdBillingAccountRef ordBillingAccountRef = new OrdBillingAccountRef()
            .name(DEFAULT_NAME)
            .href(DEFAULT_HREF)
            .cartPriceId(DEFAULT_CART_PRICE_ID)
            .billingAccountId(DEFAULT_BILLING_ACCOUNT_ID)
            .billingSystem(DEFAULT_BILLING_SYSTEM)
            .deliveryMethod(DEFAULT_DELIVERY_METHOD)
            .billingAddress(DEFAULT_BILLING_ADDRESS)
            .status(DEFAULT_STATUS)
            .quoteId(DEFAULT_QUOTE_ID)
            .salesOrderId(DEFAULT_SALES_ORDER_ID);
        return ordBillingAccountRef;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdBillingAccountRef createUpdatedEntity(EntityManager em) {
        OrdBillingAccountRef ordBillingAccountRef = new OrdBillingAccountRef()
            .name(UPDATED_NAME)
            .href(UPDATED_HREF)
            .cartPriceId(UPDATED_CART_PRICE_ID)
            .billingAccountId(UPDATED_BILLING_ACCOUNT_ID)
            .billingSystem(UPDATED_BILLING_SYSTEM)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .status(UPDATED_STATUS)
            .quoteId(UPDATED_QUOTE_ID)
            .salesOrderId(UPDATED_SALES_ORDER_ID);
        return ordBillingAccountRef;
    }

    @BeforeEach
    public void initTest() {
        ordBillingAccountRef = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeCreate = ordBillingAccountRefRepository.findAll().size();
        // Create the OrdBillingAccountRef
        restOrdBillingAccountRefMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isCreated());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeCreate + 1);
        OrdBillingAccountRef testOrdBillingAccountRef = ordBillingAccountRefList.get(ordBillingAccountRefList.size() - 1);
        assertThat(testOrdBillingAccountRef.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdBillingAccountRef.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdBillingAccountRef.getCartPriceId()).isEqualTo(DEFAULT_CART_PRICE_ID);
        assertThat(testOrdBillingAccountRef.getBillingAccountId()).isEqualTo(DEFAULT_BILLING_ACCOUNT_ID);
        assertThat(testOrdBillingAccountRef.getBillingSystem()).isEqualTo(DEFAULT_BILLING_SYSTEM);
        assertThat(testOrdBillingAccountRef.getDeliveryMethod()).isEqualTo(DEFAULT_DELIVERY_METHOD);
        assertThat(testOrdBillingAccountRef.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
        assertThat(testOrdBillingAccountRef.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdBillingAccountRef.getQuoteId()).isEqualTo(DEFAULT_QUOTE_ID);
        assertThat(testOrdBillingAccountRef.getSalesOrderId()).isEqualTo(DEFAULT_SALES_ORDER_ID);
    }

    @Test
    @Transactional
    void createOrdBillingAccountRefWithExistingId() throws Exception {
        // Create the OrdBillingAccountRef with an existing ID
        ordBillingAccountRef.setId(1L);

        int databaseSizeBeforeCreate = ordBillingAccountRefRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdBillingAccountRefMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdBillingAccountRefs() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        // Get all the ordBillingAccountRefList
        restOrdBillingAccountRefMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordBillingAccountRef.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].href").value(hasItem(DEFAULT_HREF)))
            .andExpect(jsonPath("$.[*].cartPriceId").value(hasItem(DEFAULT_CART_PRICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].billingAccountId").value(hasItem(DEFAULT_BILLING_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].billingSystem").value(hasItem(DEFAULT_BILLING_SYSTEM)))
            .andExpect(jsonPath("$.[*].deliveryMethod").value(hasItem(DEFAULT_DELIVERY_METHOD)))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].quoteId").value(hasItem(DEFAULT_QUOTE_ID.intValue())))
            .andExpect(jsonPath("$.[*].salesOrderId").value(hasItem(DEFAULT_SALES_ORDER_ID.intValue())));
    }

    @Test
    @Transactional
    void getOrdBillingAccountRef() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        // Get the ordBillingAccountRef
        restOrdBillingAccountRefMockMvc
            .perform(get(ENTITY_API_URL_ID, ordBillingAccountRef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordBillingAccountRef.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.href").value(DEFAULT_HREF))
            .andExpect(jsonPath("$.cartPriceId").value(DEFAULT_CART_PRICE_ID.intValue()))
            .andExpect(jsonPath("$.billingAccountId").value(DEFAULT_BILLING_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.billingSystem").value(DEFAULT_BILLING_SYSTEM))
            .andExpect(jsonPath("$.deliveryMethod").value(DEFAULT_DELIVERY_METHOD))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.quoteId").value(DEFAULT_QUOTE_ID.intValue()))
            .andExpect(jsonPath("$.salesOrderId").value(DEFAULT_SALES_ORDER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdBillingAccountRef() throws Exception {
        // Get the ordBillingAccountRef
        restOrdBillingAccountRefMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdBillingAccountRef() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();

        // Update the ordBillingAccountRef
        OrdBillingAccountRef updatedOrdBillingAccountRef = ordBillingAccountRefRepository.findById(ordBillingAccountRef.getId()).get();
        // Disconnect from session so that the updates on updatedOrdBillingAccountRef are not directly saved in db
        em.detach(updatedOrdBillingAccountRef);
        updatedOrdBillingAccountRef
            .name(UPDATED_NAME)
            .href(UPDATED_HREF)
            .cartPriceId(UPDATED_CART_PRICE_ID)
            .billingAccountId(UPDATED_BILLING_ACCOUNT_ID)
            .billingSystem(UPDATED_BILLING_SYSTEM)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .status(UPDATED_STATUS)
            .quoteId(UPDATED_QUOTE_ID)
            .salesOrderId(UPDATED_SALES_ORDER_ID);

        restOrdBillingAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdBillingAccountRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdBillingAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
        OrdBillingAccountRef testOrdBillingAccountRef = ordBillingAccountRefList.get(ordBillingAccountRefList.size() - 1);
        assertThat(testOrdBillingAccountRef.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdBillingAccountRef.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdBillingAccountRef.getCartPriceId()).isEqualTo(UPDATED_CART_PRICE_ID);
        assertThat(testOrdBillingAccountRef.getBillingAccountId()).isEqualTo(UPDATED_BILLING_ACCOUNT_ID);
        assertThat(testOrdBillingAccountRef.getBillingSystem()).isEqualTo(UPDATED_BILLING_SYSTEM);
        assertThat(testOrdBillingAccountRef.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrdBillingAccountRef.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testOrdBillingAccountRef.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdBillingAccountRef.getQuoteId()).isEqualTo(UPDATED_QUOTE_ID);
        assertThat(testOrdBillingAccountRef.getSalesOrderId()).isEqualTo(UPDATED_SALES_ORDER_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordBillingAccountRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdBillingAccountRefWithPatch() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();

        // Update the ordBillingAccountRef using partial update
        OrdBillingAccountRef partialUpdatedOrdBillingAccountRef = new OrdBillingAccountRef();
        partialUpdatedOrdBillingAccountRef.setId(ordBillingAccountRef.getId());

        partialUpdatedOrdBillingAccountRef
            .billingSystem(UPDATED_BILLING_SYSTEM)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .status(UPDATED_STATUS)
            .salesOrderId(UPDATED_SALES_ORDER_ID);

        restOrdBillingAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdBillingAccountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdBillingAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
        OrdBillingAccountRef testOrdBillingAccountRef = ordBillingAccountRefList.get(ordBillingAccountRefList.size() - 1);
        assertThat(testOrdBillingAccountRef.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdBillingAccountRef.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdBillingAccountRef.getCartPriceId()).isEqualTo(DEFAULT_CART_PRICE_ID);
        assertThat(testOrdBillingAccountRef.getBillingAccountId()).isEqualTo(DEFAULT_BILLING_ACCOUNT_ID);
        assertThat(testOrdBillingAccountRef.getBillingSystem()).isEqualTo(UPDATED_BILLING_SYSTEM);
        assertThat(testOrdBillingAccountRef.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrdBillingAccountRef.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
        assertThat(testOrdBillingAccountRef.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdBillingAccountRef.getQuoteId()).isEqualTo(DEFAULT_QUOTE_ID);
        assertThat(testOrdBillingAccountRef.getSalesOrderId()).isEqualTo(UPDATED_SALES_ORDER_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrdBillingAccountRefWithPatch() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();

        // Update the ordBillingAccountRef using partial update
        OrdBillingAccountRef partialUpdatedOrdBillingAccountRef = new OrdBillingAccountRef();
        partialUpdatedOrdBillingAccountRef.setId(ordBillingAccountRef.getId());

        partialUpdatedOrdBillingAccountRef
            .name(UPDATED_NAME)
            .href(UPDATED_HREF)
            .cartPriceId(UPDATED_CART_PRICE_ID)
            .billingAccountId(UPDATED_BILLING_ACCOUNT_ID)
            .billingSystem(UPDATED_BILLING_SYSTEM)
            .deliveryMethod(UPDATED_DELIVERY_METHOD)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .status(UPDATED_STATUS)
            .quoteId(UPDATED_QUOTE_ID)
            .salesOrderId(UPDATED_SALES_ORDER_ID);

        restOrdBillingAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdBillingAccountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdBillingAccountRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
        OrdBillingAccountRef testOrdBillingAccountRef = ordBillingAccountRefList.get(ordBillingAccountRefList.size() - 1);
        assertThat(testOrdBillingAccountRef.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdBillingAccountRef.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdBillingAccountRef.getCartPriceId()).isEqualTo(UPDATED_CART_PRICE_ID);
        assertThat(testOrdBillingAccountRef.getBillingAccountId()).isEqualTo(UPDATED_BILLING_ACCOUNT_ID);
        assertThat(testOrdBillingAccountRef.getBillingSystem()).isEqualTo(UPDATED_BILLING_SYSTEM);
        assertThat(testOrdBillingAccountRef.getDeliveryMethod()).isEqualTo(UPDATED_DELIVERY_METHOD);
        assertThat(testOrdBillingAccountRef.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testOrdBillingAccountRef.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdBillingAccountRef.getQuoteId()).isEqualTo(UPDATED_QUOTE_ID);
        assertThat(testOrdBillingAccountRef.getSalesOrderId()).isEqualTo(UPDATED_SALES_ORDER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordBillingAccountRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdBillingAccountRef() throws Exception {
        int databaseSizeBeforeUpdate = ordBillingAccountRefRepository.findAll().size();
        ordBillingAccountRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdBillingAccountRefMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordBillingAccountRef))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdBillingAccountRef in the database
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdBillingAccountRef() throws Exception {
        // Initialize the database
        ordBillingAccountRefRepository.saveAndFlush(ordBillingAccountRef);

        int databaseSizeBeforeDelete = ordBillingAccountRefRepository.findAll().size();

        // Delete the ordBillingAccountRef
        restOrdBillingAccountRefMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordBillingAccountRef.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdBillingAccountRef> ordBillingAccountRefList = ordBillingAccountRefRepository.findAll();
        assertThat(ordBillingAccountRefList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

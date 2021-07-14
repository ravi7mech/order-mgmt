package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdProductOrder;
import com.apptium.repository.OrdProductOrderRepository;
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
 * Integration tests for the {@link OrdProductOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdProductOrderResourceIT {

    private static final String DEFAULT_HREF = "AAAAAAAAAA";
    private static final String UPDATED_HREF = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REQUESTED_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REQUESTED_COMPLETION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_COMPLETION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECTED_COMPLETION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_COMPLETION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTIFICATION_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_CONTACT = "BBBBBBBBBB";

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final Integer DEFAULT_SHOPPING_CART_ID = 1;
    private static final Integer UPDATED_SHOPPING_CART_ID = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_LOCATION_ID = 1L;
    private static final Long UPDATED_LOCATION_ID = 2L;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ord-product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdProductOrderRepository ordProductOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdProductOrderMockMvc;

    private OrdProductOrder ordProductOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProductOrder createEntity(EntityManager em) {
        OrdProductOrder ordProductOrder = new OrdProductOrder()
            .href(DEFAULT_HREF)
            .externalId(DEFAULT_EXTERNAL_ID)
            .priority(DEFAULT_PRIORITY)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .status(DEFAULT_STATUS)
            .orderDate(DEFAULT_ORDER_DATE)
            .completionDate(DEFAULT_COMPLETION_DATE)
            .requestedStartDate(DEFAULT_REQUESTED_START_DATE)
            .requestedCompletionDate(DEFAULT_REQUESTED_COMPLETION_DATE)
            .expectedCompletionDate(DEFAULT_EXPECTED_COMPLETION_DATE)
            .notificationContact(DEFAULT_NOTIFICATION_CONTACT)
            .customerId(DEFAULT_CUSTOMER_ID)
            .shoppingCartId(DEFAULT_SHOPPING_CART_ID)
            .type(DEFAULT_TYPE)
            .locationId(DEFAULT_LOCATION_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return ordProductOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProductOrder createUpdatedEntity(EntityManager em) {
        OrdProductOrder ordProductOrder = new OrdProductOrder()
            .href(UPDATED_HREF)
            .externalId(UPDATED_EXTERNAL_ID)
            .priority(UPDATED_PRIORITY)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .requestedStartDate(UPDATED_REQUESTED_START_DATE)
            .requestedCompletionDate(UPDATED_REQUESTED_COMPLETION_DATE)
            .expectedCompletionDate(UPDATED_EXPECTED_COMPLETION_DATE)
            .notificationContact(UPDATED_NOTIFICATION_CONTACT)
            .customerId(UPDATED_CUSTOMER_ID)
            .shoppingCartId(UPDATED_SHOPPING_CART_ID)
            .type(UPDATED_TYPE)
            .locationId(UPDATED_LOCATION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);
        return ordProductOrder;
    }

    @BeforeEach
    public void initTest() {
        ordProductOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdProductOrder() throws Exception {
        int databaseSizeBeforeCreate = ordProductOrderRepository.findAll().size();
        // Create the OrdProductOrder
        restOrdProductOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isCreated());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeCreate + 1);
        OrdProductOrder testOrdProductOrder = ordProductOrderList.get(ordProductOrderList.size() - 1);
        assertThat(testOrdProductOrder.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdProductOrder.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testOrdProductOrder.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testOrdProductOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrdProductOrder.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testOrdProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdProductOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrdProductOrder.getCompletionDate()).isEqualTo(DEFAULT_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getRequestedStartDate()).isEqualTo(DEFAULT_REQUESTED_START_DATE);
        assertThat(testOrdProductOrder.getRequestedCompletionDate()).isEqualTo(DEFAULT_REQUESTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getExpectedCompletionDate()).isEqualTo(DEFAULT_EXPECTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getNotificationContact()).isEqualTo(DEFAULT_NOTIFICATION_CONTACT);
        assertThat(testOrdProductOrder.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testOrdProductOrder.getShoppingCartId()).isEqualTo(DEFAULT_SHOPPING_CART_ID);
        assertThat(testOrdProductOrder.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrdProductOrder.getLocationId()).isEqualTo(DEFAULT_LOCATION_ID);
        assertThat(testOrdProductOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdProductOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdProductOrder.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOrdProductOrder.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createOrdProductOrderWithExistingId() throws Exception {
        // Create the OrdProductOrder with an existing ID
        ordProductOrder.setId(1L);

        int databaseSizeBeforeCreate = ordProductOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdProductOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdProductOrders() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        // Get all the ordProductOrderList
        restOrdProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordProductOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].href").value(hasItem(DEFAULT_HREF)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestedStartDate").value(hasItem(DEFAULT_REQUESTED_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestedCompletionDate").value(hasItem(DEFAULT_REQUESTED_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedCompletionDate").value(hasItem(DEFAULT_EXPECTED_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].notificationContact").value(hasItem(DEFAULT_NOTIFICATION_CONTACT)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].shoppingCartId").value(hasItem(DEFAULT_SHOPPING_CART_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].locationId").value(hasItem(DEFAULT_LOCATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdProductOrder() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        // Get the ordProductOrder
        restOrdProductOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, ordProductOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordProductOrder.getId().intValue()))
            .andExpect(jsonPath("$.href").value(DEFAULT_HREF))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.completionDate").value(DEFAULT_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.requestedStartDate").value(DEFAULT_REQUESTED_START_DATE.toString()))
            .andExpect(jsonPath("$.requestedCompletionDate").value(DEFAULT_REQUESTED_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.expectedCompletionDate").value(DEFAULT_EXPECTED_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.notificationContact").value(DEFAULT_NOTIFICATION_CONTACT))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.shoppingCartId").value(DEFAULT_SHOPPING_CART_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.locationId").value(DEFAULT_LOCATION_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdProductOrder() throws Exception {
        // Get the ordProductOrder
        restOrdProductOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdProductOrder() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();

        // Update the ordProductOrder
        OrdProductOrder updatedOrdProductOrder = ordProductOrderRepository.findById(ordProductOrder.getId()).get();
        // Disconnect from session so that the updates on updatedOrdProductOrder are not directly saved in db
        em.detach(updatedOrdProductOrder);
        updatedOrdProductOrder
            .href(UPDATED_HREF)
            .externalId(UPDATED_EXTERNAL_ID)
            .priority(UPDATED_PRIORITY)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .requestedStartDate(UPDATED_REQUESTED_START_DATE)
            .requestedCompletionDate(UPDATED_REQUESTED_COMPLETION_DATE)
            .expectedCompletionDate(UPDATED_EXPECTED_COMPLETION_DATE)
            .notificationContact(UPDATED_NOTIFICATION_CONTACT)
            .customerId(UPDATED_CUSTOMER_ID)
            .shoppingCartId(UPDATED_SHOPPING_CART_ID)
            .type(UPDATED_TYPE)
            .locationId(UPDATED_LOCATION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restOrdProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdProductOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
        OrdProductOrder testOrdProductOrder = ordProductOrderList.get(ordProductOrderList.size() - 1);
        assertThat(testOrdProductOrder.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdProductOrder.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testOrdProductOrder.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testOrdProductOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdProductOrder.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testOrdProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdProductOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrdProductOrder.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getRequestedStartDate()).isEqualTo(UPDATED_REQUESTED_START_DATE);
        assertThat(testOrdProductOrder.getRequestedCompletionDate()).isEqualTo(UPDATED_REQUESTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getExpectedCompletionDate()).isEqualTo(UPDATED_EXPECTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getNotificationContact()).isEqualTo(UPDATED_NOTIFICATION_CONTACT);
        assertThat(testOrdProductOrder.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testOrdProductOrder.getShoppingCartId()).isEqualTo(UPDATED_SHOPPING_CART_ID);
        assertThat(testOrdProductOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdProductOrder.getLocationId()).isEqualTo(UPDATED_LOCATION_ID);
        assertThat(testOrdProductOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdProductOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdProductOrder.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOrdProductOrder.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordProductOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdProductOrderWithPatch() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();

        // Update the ordProductOrder using partial update
        OrdProductOrder partialUpdatedOrdProductOrder = new OrdProductOrder();
        partialUpdatedOrdProductOrder.setId(ordProductOrder.getId());

        partialUpdatedOrdProductOrder
            .href(UPDATED_HREF)
            .priority(UPDATED_PRIORITY)
            .description(UPDATED_DESCRIPTION)
            .completionDate(UPDATED_COMPLETION_DATE)
            .requestedStartDate(UPDATED_REQUESTED_START_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .type(UPDATED_TYPE)
            .locationId(UPDATED_LOCATION_ID);

        restOrdProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
        OrdProductOrder testOrdProductOrder = ordProductOrderList.get(ordProductOrderList.size() - 1);
        assertThat(testOrdProductOrder.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdProductOrder.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testOrdProductOrder.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testOrdProductOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdProductOrder.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testOrdProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdProductOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrdProductOrder.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getRequestedStartDate()).isEqualTo(UPDATED_REQUESTED_START_DATE);
        assertThat(testOrdProductOrder.getRequestedCompletionDate()).isEqualTo(DEFAULT_REQUESTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getExpectedCompletionDate()).isEqualTo(DEFAULT_EXPECTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getNotificationContact()).isEqualTo(DEFAULT_NOTIFICATION_CONTACT);
        assertThat(testOrdProductOrder.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testOrdProductOrder.getShoppingCartId()).isEqualTo(DEFAULT_SHOPPING_CART_ID);
        assertThat(testOrdProductOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdProductOrder.getLocationId()).isEqualTo(UPDATED_LOCATION_ID);
        assertThat(testOrdProductOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdProductOrder.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdProductOrder.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOrdProductOrder.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdProductOrderWithPatch() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();

        // Update the ordProductOrder using partial update
        OrdProductOrder partialUpdatedOrdProductOrder = new OrdProductOrder();
        partialUpdatedOrdProductOrder.setId(ordProductOrder.getId());

        partialUpdatedOrdProductOrder
            .href(UPDATED_HREF)
            .externalId(UPDATED_EXTERNAL_ID)
            .priority(UPDATED_PRIORITY)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .completionDate(UPDATED_COMPLETION_DATE)
            .requestedStartDate(UPDATED_REQUESTED_START_DATE)
            .requestedCompletionDate(UPDATED_REQUESTED_COMPLETION_DATE)
            .expectedCompletionDate(UPDATED_EXPECTED_COMPLETION_DATE)
            .notificationContact(UPDATED_NOTIFICATION_CONTACT)
            .customerId(UPDATED_CUSTOMER_ID)
            .shoppingCartId(UPDATED_SHOPPING_CART_ID)
            .type(UPDATED_TYPE)
            .locationId(UPDATED_LOCATION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restOrdProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
        OrdProductOrder testOrdProductOrder = ordProductOrderList.get(ordProductOrderList.size() - 1);
        assertThat(testOrdProductOrder.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdProductOrder.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testOrdProductOrder.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testOrdProductOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdProductOrder.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testOrdProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdProductOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrdProductOrder.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getRequestedStartDate()).isEqualTo(UPDATED_REQUESTED_START_DATE);
        assertThat(testOrdProductOrder.getRequestedCompletionDate()).isEqualTo(UPDATED_REQUESTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getExpectedCompletionDate()).isEqualTo(UPDATED_EXPECTED_COMPLETION_DATE);
        assertThat(testOrdProductOrder.getNotificationContact()).isEqualTo(UPDATED_NOTIFICATION_CONTACT);
        assertThat(testOrdProductOrder.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testOrdProductOrder.getShoppingCartId()).isEqualTo(UPDATED_SHOPPING_CART_ID);
        assertThat(testOrdProductOrder.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrdProductOrder.getLocationId()).isEqualTo(UPDATED_LOCATION_ID);
        assertThat(testOrdProductOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdProductOrder.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdProductOrder.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOrdProductOrder.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = ordProductOrderRepository.findAll().size();
        ordProductOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProductOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProductOrder in the database
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdProductOrder() throws Exception {
        // Initialize the database
        ordProductOrderRepository.saveAndFlush(ordProductOrder);

        int databaseSizeBeforeDelete = ordProductOrderRepository.findAll().size();

        // Delete the ordProductOrder
        restOrdProductOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordProductOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdProductOrder> ordProductOrderList = ordProductOrderRepository.findAll();
        assertThat(ordProductOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

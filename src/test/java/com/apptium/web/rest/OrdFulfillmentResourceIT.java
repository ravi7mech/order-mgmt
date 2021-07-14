package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdFulfillment;
import com.apptium.repository.OrdFulfillmentRepository;
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
 * Integration tests for the {@link OrdFulfillmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdFulfillmentResourceIT {

    private static final Long DEFAULT_WORKORDER_ID = 1L;
    private static final Long UPDATED_WORKORDER_ID = 2L;

    private static final Long DEFAULT_APPOINTMENT_ID = 1L;
    private static final Long UPDATED_APPOINTMENT_ID = 2L;

    private static final String DEFAULT_ORDER_FULFILLMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_FULFILLMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_SHIPPING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_SHIPPING_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_CALL_AHEAD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_CALL_AHEAD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_JOB_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_JOB_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-fulfillments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdFulfillmentRepository ordFulfillmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdFulfillmentMockMvc;

    private OrdFulfillment ordFulfillment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdFulfillment createEntity(EntityManager em) {
        OrdFulfillment ordFulfillment = new OrdFulfillment()
            .workorderId(DEFAULT_WORKORDER_ID)
            .appointmentId(DEFAULT_APPOINTMENT_ID)
            .orderFulfillmentType(DEFAULT_ORDER_FULFILLMENT_TYPE)
            .alternateShippingAddress(DEFAULT_ALTERNATE_SHIPPING_ADDRESS)
            .orderCallAheadNumber(DEFAULT_ORDER_CALL_AHEAD_NUMBER)
            .orderJobComments(DEFAULT_ORDER_JOB_COMMENTS)
            .status(DEFAULT_STATUS);
        return ordFulfillment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdFulfillment createUpdatedEntity(EntityManager em) {
        OrdFulfillment ordFulfillment = new OrdFulfillment()
            .workorderId(UPDATED_WORKORDER_ID)
            .appointmentId(UPDATED_APPOINTMENT_ID)
            .orderFulfillmentType(UPDATED_ORDER_FULFILLMENT_TYPE)
            .alternateShippingAddress(UPDATED_ALTERNATE_SHIPPING_ADDRESS)
            .orderCallAheadNumber(UPDATED_ORDER_CALL_AHEAD_NUMBER)
            .orderJobComments(UPDATED_ORDER_JOB_COMMENTS)
            .status(UPDATED_STATUS);
        return ordFulfillment;
    }

    @BeforeEach
    public void initTest() {
        ordFulfillment = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdFulfillment() throws Exception {
        int databaseSizeBeforeCreate = ordFulfillmentRepository.findAll().size();
        // Create the OrdFulfillment
        restOrdFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isCreated());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeCreate + 1);
        OrdFulfillment testOrdFulfillment = ordFulfillmentList.get(ordFulfillmentList.size() - 1);
        assertThat(testOrdFulfillment.getWorkorderId()).isEqualTo(DEFAULT_WORKORDER_ID);
        assertThat(testOrdFulfillment.getAppointmentId()).isEqualTo(DEFAULT_APPOINTMENT_ID);
        assertThat(testOrdFulfillment.getOrderFulfillmentType()).isEqualTo(DEFAULT_ORDER_FULFILLMENT_TYPE);
        assertThat(testOrdFulfillment.getAlternateShippingAddress()).isEqualTo(DEFAULT_ALTERNATE_SHIPPING_ADDRESS);
        assertThat(testOrdFulfillment.getOrderCallAheadNumber()).isEqualTo(DEFAULT_ORDER_CALL_AHEAD_NUMBER);
        assertThat(testOrdFulfillment.getOrderJobComments()).isEqualTo(DEFAULT_ORDER_JOB_COMMENTS);
        assertThat(testOrdFulfillment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOrdFulfillmentWithExistingId() throws Exception {
        // Create the OrdFulfillment with an existing ID
        ordFulfillment.setId(1L);

        int databaseSizeBeforeCreate = ordFulfillmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdFulfillments() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        // Get all the ordFulfillmentList
        restOrdFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordFulfillment.getId().intValue())))
            .andExpect(jsonPath("$.[*].workorderId").value(hasItem(DEFAULT_WORKORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].appointmentId").value(hasItem(DEFAULT_APPOINTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderFulfillmentType").value(hasItem(DEFAULT_ORDER_FULFILLMENT_TYPE)))
            .andExpect(jsonPath("$.[*].alternateShippingAddress").value(hasItem(DEFAULT_ALTERNATE_SHIPPING_ADDRESS)))
            .andExpect(jsonPath("$.[*].orderCallAheadNumber").value(hasItem(DEFAULT_ORDER_CALL_AHEAD_NUMBER)))
            .andExpect(jsonPath("$.[*].orderJobComments").value(hasItem(DEFAULT_ORDER_JOB_COMMENTS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getOrdFulfillment() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        // Get the ordFulfillment
        restOrdFulfillmentMockMvc
            .perform(get(ENTITY_API_URL_ID, ordFulfillment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordFulfillment.getId().intValue()))
            .andExpect(jsonPath("$.workorderId").value(DEFAULT_WORKORDER_ID.intValue()))
            .andExpect(jsonPath("$.appointmentId").value(DEFAULT_APPOINTMENT_ID.intValue()))
            .andExpect(jsonPath("$.orderFulfillmentType").value(DEFAULT_ORDER_FULFILLMENT_TYPE))
            .andExpect(jsonPath("$.alternateShippingAddress").value(DEFAULT_ALTERNATE_SHIPPING_ADDRESS))
            .andExpect(jsonPath("$.orderCallAheadNumber").value(DEFAULT_ORDER_CALL_AHEAD_NUMBER))
            .andExpect(jsonPath("$.orderJobComments").value(DEFAULT_ORDER_JOB_COMMENTS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingOrdFulfillment() throws Exception {
        // Get the ordFulfillment
        restOrdFulfillmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdFulfillment() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();

        // Update the ordFulfillment
        OrdFulfillment updatedOrdFulfillment = ordFulfillmentRepository.findById(ordFulfillment.getId()).get();
        // Disconnect from session so that the updates on updatedOrdFulfillment are not directly saved in db
        em.detach(updatedOrdFulfillment);
        updatedOrdFulfillment
            .workorderId(UPDATED_WORKORDER_ID)
            .appointmentId(UPDATED_APPOINTMENT_ID)
            .orderFulfillmentType(UPDATED_ORDER_FULFILLMENT_TYPE)
            .alternateShippingAddress(UPDATED_ALTERNATE_SHIPPING_ADDRESS)
            .orderCallAheadNumber(UPDATED_ORDER_CALL_AHEAD_NUMBER)
            .orderJobComments(UPDATED_ORDER_JOB_COMMENTS)
            .status(UPDATED_STATUS);

        restOrdFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdFulfillment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdFulfillment))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillment testOrdFulfillment = ordFulfillmentList.get(ordFulfillmentList.size() - 1);
        assertThat(testOrdFulfillment.getWorkorderId()).isEqualTo(UPDATED_WORKORDER_ID);
        assertThat(testOrdFulfillment.getAppointmentId()).isEqualTo(UPDATED_APPOINTMENT_ID);
        assertThat(testOrdFulfillment.getOrderFulfillmentType()).isEqualTo(UPDATED_ORDER_FULFILLMENT_TYPE);
        assertThat(testOrdFulfillment.getAlternateShippingAddress()).isEqualTo(UPDATED_ALTERNATE_SHIPPING_ADDRESS);
        assertThat(testOrdFulfillment.getOrderCallAheadNumber()).isEqualTo(UPDATED_ORDER_CALL_AHEAD_NUMBER);
        assertThat(testOrdFulfillment.getOrderJobComments()).isEqualTo(UPDATED_ORDER_JOB_COMMENTS);
        assertThat(testOrdFulfillment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordFulfillment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordFulfillment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdFulfillmentWithPatch() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();

        // Update the ordFulfillment using partial update
        OrdFulfillment partialUpdatedOrdFulfillment = new OrdFulfillment();
        partialUpdatedOrdFulfillment.setId(ordFulfillment.getId());

        partialUpdatedOrdFulfillment.orderJobComments(UPDATED_ORDER_JOB_COMMENTS);

        restOrdFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdFulfillment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdFulfillment))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillment testOrdFulfillment = ordFulfillmentList.get(ordFulfillmentList.size() - 1);
        assertThat(testOrdFulfillment.getWorkorderId()).isEqualTo(DEFAULT_WORKORDER_ID);
        assertThat(testOrdFulfillment.getAppointmentId()).isEqualTo(DEFAULT_APPOINTMENT_ID);
        assertThat(testOrdFulfillment.getOrderFulfillmentType()).isEqualTo(DEFAULT_ORDER_FULFILLMENT_TYPE);
        assertThat(testOrdFulfillment.getAlternateShippingAddress()).isEqualTo(DEFAULT_ALTERNATE_SHIPPING_ADDRESS);
        assertThat(testOrdFulfillment.getOrderCallAheadNumber()).isEqualTo(DEFAULT_ORDER_CALL_AHEAD_NUMBER);
        assertThat(testOrdFulfillment.getOrderJobComments()).isEqualTo(UPDATED_ORDER_JOB_COMMENTS);
        assertThat(testOrdFulfillment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOrdFulfillmentWithPatch() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();

        // Update the ordFulfillment using partial update
        OrdFulfillment partialUpdatedOrdFulfillment = new OrdFulfillment();
        partialUpdatedOrdFulfillment.setId(ordFulfillment.getId());

        partialUpdatedOrdFulfillment
            .workorderId(UPDATED_WORKORDER_ID)
            .appointmentId(UPDATED_APPOINTMENT_ID)
            .orderFulfillmentType(UPDATED_ORDER_FULFILLMENT_TYPE)
            .alternateShippingAddress(UPDATED_ALTERNATE_SHIPPING_ADDRESS)
            .orderCallAheadNumber(UPDATED_ORDER_CALL_AHEAD_NUMBER)
            .orderJobComments(UPDATED_ORDER_JOB_COMMENTS)
            .status(UPDATED_STATUS);

        restOrdFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdFulfillment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdFulfillment))
            )
            .andExpect(status().isOk());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
        OrdFulfillment testOrdFulfillment = ordFulfillmentList.get(ordFulfillmentList.size() - 1);
        assertThat(testOrdFulfillment.getWorkorderId()).isEqualTo(UPDATED_WORKORDER_ID);
        assertThat(testOrdFulfillment.getAppointmentId()).isEqualTo(UPDATED_APPOINTMENT_ID);
        assertThat(testOrdFulfillment.getOrderFulfillmentType()).isEqualTo(UPDATED_ORDER_FULFILLMENT_TYPE);
        assertThat(testOrdFulfillment.getAlternateShippingAddress()).isEqualTo(UPDATED_ALTERNATE_SHIPPING_ADDRESS);
        assertThat(testOrdFulfillment.getOrderCallAheadNumber()).isEqualTo(UPDATED_ORDER_CALL_AHEAD_NUMBER);
        assertThat(testOrdFulfillment.getOrderJobComments()).isEqualTo(UPDATED_ORDER_JOB_COMMENTS);
        assertThat(testOrdFulfillment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordFulfillment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = ordFulfillmentRepository.findAll().size();
        ordFulfillment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordFulfillment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdFulfillment in the database
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdFulfillment() throws Exception {
        // Initialize the database
        ordFulfillmentRepository.saveAndFlush(ordFulfillment);

        int databaseSizeBeforeDelete = ordFulfillmentRepository.findAll().size();

        // Delete the ordFulfillment
        restOrdFulfillmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordFulfillment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdFulfillment> ordFulfillmentList = ordFulfillmentRepository.findAll();
        assertThat(ordFulfillmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

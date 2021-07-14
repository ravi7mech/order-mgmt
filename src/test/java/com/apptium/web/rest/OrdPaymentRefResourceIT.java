package com.apptium.web.rest;

import static com.apptium.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdPaymentRef;
import com.apptium.repository.OrdPaymentRefRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link OrdPaymentRefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdPaymentRefResourceIT {

    private static final Long DEFAULT_PAYMENT_ID = 1L;
    private static final Long UPDATED_PAYMENT_ID = 2L;

    private static final String DEFAULT_HREF = "AAAAAAAAAA";
    private static final String UPDATED_HREF = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_ENROL_RECURRING = "AAAAAAAAAA";
    private static final String UPDATED_ENROL_RECURRING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-payment-refs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdPaymentRefRepository ordPaymentRefRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdPaymentRefMockMvc;

    private OrdPaymentRef ordPaymentRef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPaymentRef createEntity(EntityManager em) {
        OrdPaymentRef ordPaymentRef = new OrdPaymentRef()
            .paymentId(DEFAULT_PAYMENT_ID)
            .href(DEFAULT_HREF)
            .name(DEFAULT_NAME)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .action(DEFAULT_ACTION)
            .status(DEFAULT_STATUS)
            .enrolRecurring(DEFAULT_ENROL_RECURRING);
        return ordPaymentRef;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPaymentRef createUpdatedEntity(EntityManager em) {
        OrdPaymentRef ordPaymentRef = new OrdPaymentRef()
            .paymentId(UPDATED_PAYMENT_ID)
            .href(UPDATED_HREF)
            .name(UPDATED_NAME)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .enrolRecurring(UPDATED_ENROL_RECURRING);
        return ordPaymentRef;
    }

    @BeforeEach
    public void initTest() {
        ordPaymentRef = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdPaymentRef() throws Exception {
        int databaseSizeBeforeCreate = ordPaymentRefRepository.findAll().size();
        // Create the OrdPaymentRef
        restOrdPaymentRefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPaymentRef)))
            .andExpect(status().isCreated());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeCreate + 1);
        OrdPaymentRef testOrdPaymentRef = ordPaymentRefList.get(ordPaymentRefList.size() - 1);
        assertThat(testOrdPaymentRef.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testOrdPaymentRef.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdPaymentRef.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdPaymentRef.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testOrdPaymentRef.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testOrdPaymentRef.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdPaymentRef.getEnrolRecurring()).isEqualTo(DEFAULT_ENROL_RECURRING);
    }

    @Test
    @Transactional
    void createOrdPaymentRefWithExistingId() throws Exception {
        // Create the OrdPaymentRef with an existing ID
        ordPaymentRef.setId(1L);

        int databaseSizeBeforeCreate = ordPaymentRefRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdPaymentRefMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPaymentRef)))
            .andExpect(status().isBadRequest());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdPaymentRefs() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        // Get all the ordPaymentRefList
        restOrdPaymentRefMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordPaymentRef.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].href").value(hasItem(DEFAULT_HREF)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].enrolRecurring").value(hasItem(DEFAULT_ENROL_RECURRING)));
    }

    @Test
    @Transactional
    void getOrdPaymentRef() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        // Get the ordPaymentRef
        restOrdPaymentRefMockMvc
            .perform(get(ENTITY_API_URL_ID, ordPaymentRef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordPaymentRef.getId().intValue()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID.intValue()))
            .andExpect(jsonPath("$.href").value(DEFAULT_HREF))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.enrolRecurring").value(DEFAULT_ENROL_RECURRING));
    }

    @Test
    @Transactional
    void getNonExistingOrdPaymentRef() throws Exception {
        // Get the ordPaymentRef
        restOrdPaymentRefMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdPaymentRef() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();

        // Update the ordPaymentRef
        OrdPaymentRef updatedOrdPaymentRef = ordPaymentRefRepository.findById(ordPaymentRef.getId()).get();
        // Disconnect from session so that the updates on updatedOrdPaymentRef are not directly saved in db
        em.detach(updatedOrdPaymentRef);
        updatedOrdPaymentRef
            .paymentId(UPDATED_PAYMENT_ID)
            .href(UPDATED_HREF)
            .name(UPDATED_NAME)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .enrolRecurring(UPDATED_ENROL_RECURRING);

        restOrdPaymentRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdPaymentRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdPaymentRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
        OrdPaymentRef testOrdPaymentRef = ordPaymentRefList.get(ordPaymentRefList.size() - 1);
        assertThat(testOrdPaymentRef.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testOrdPaymentRef.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPaymentRef.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPaymentRef.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testOrdPaymentRef.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdPaymentRef.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdPaymentRef.getEnrolRecurring()).isEqualTo(UPDATED_ENROL_RECURRING);
    }

    @Test
    @Transactional
    void putNonExistingOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordPaymentRef.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPaymentRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPaymentRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPaymentRef)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdPaymentRefWithPatch() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();

        // Update the ordPaymentRef using partial update
        OrdPaymentRef partialUpdatedOrdPaymentRef = new OrdPaymentRef();
        partialUpdatedOrdPaymentRef.setId(ordPaymentRef.getId());

        partialUpdatedOrdPaymentRef.paymentId(UPDATED_PAYMENT_ID).href(UPDATED_HREF);

        restOrdPaymentRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPaymentRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPaymentRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
        OrdPaymentRef testOrdPaymentRef = ordPaymentRefList.get(ordPaymentRefList.size() - 1);
        assertThat(testOrdPaymentRef.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testOrdPaymentRef.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPaymentRef.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdPaymentRef.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testOrdPaymentRef.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testOrdPaymentRef.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdPaymentRef.getEnrolRecurring()).isEqualTo(DEFAULT_ENROL_RECURRING);
    }

    @Test
    @Transactional
    void fullUpdateOrdPaymentRefWithPatch() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();

        // Update the ordPaymentRef using partial update
        OrdPaymentRef partialUpdatedOrdPaymentRef = new OrdPaymentRef();
        partialUpdatedOrdPaymentRef.setId(ordPaymentRef.getId());

        partialUpdatedOrdPaymentRef
            .paymentId(UPDATED_PAYMENT_ID)
            .href(UPDATED_HREF)
            .name(UPDATED_NAME)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .action(UPDATED_ACTION)
            .status(UPDATED_STATUS)
            .enrolRecurring(UPDATED_ENROL_RECURRING);

        restOrdPaymentRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPaymentRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPaymentRef))
            )
            .andExpect(status().isOk());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
        OrdPaymentRef testOrdPaymentRef = ordPaymentRefList.get(ordPaymentRefList.size() - 1);
        assertThat(testOrdPaymentRef.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testOrdPaymentRef.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPaymentRef.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPaymentRef.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testOrdPaymentRef.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testOrdPaymentRef.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdPaymentRef.getEnrolRecurring()).isEqualTo(UPDATED_ENROL_RECURRING);
    }

    @Test
    @Transactional
    void patchNonExistingOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordPaymentRef.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPaymentRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPaymentRef))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdPaymentRef() throws Exception {
        int databaseSizeBeforeUpdate = ordPaymentRefRepository.findAll().size();
        ordPaymentRef.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPaymentRefMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordPaymentRef))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPaymentRef in the database
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdPaymentRef() throws Exception {
        // Initialize the database
        ordPaymentRefRepository.saveAndFlush(ordPaymentRef);

        int databaseSizeBeforeDelete = ordPaymentRefRepository.findAll().size();

        // Delete the ordPaymentRef
        restOrdPaymentRefMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordPaymentRef.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdPaymentRef> ordPaymentRefList = ordPaymentRefRepository.findAll();
        assertThat(ordPaymentRefList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

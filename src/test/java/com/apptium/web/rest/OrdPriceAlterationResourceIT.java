package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdPriceAlteration;
import com.apptium.repository.OrdPriceAlterationRepository;
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
 * Integration tests for the {@link OrdPriceAlterationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdPriceAlterationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_OF_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE = "BBBBBBBBBB";

    private static final String DEFAULT_RECURRING_CHARGE_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_RECURRING_CHARGE_PERIOD = "BBBBBBBBBB";

    private static final String DEFAULT_APPLICATION_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_DURATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-price-alterations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdPriceAlterationRepository ordPriceAlterationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdPriceAlterationMockMvc;

    private OrdPriceAlteration ordPriceAlteration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPriceAlteration createEntity(EntityManager em) {
        OrdPriceAlteration ordPriceAlteration = new OrdPriceAlteration()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .priceType(DEFAULT_PRICE_TYPE)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE)
            .recurringChargePeriod(DEFAULT_RECURRING_CHARGE_PERIOD)
            .applicationDuration(DEFAULT_APPLICATION_DURATION)
            .priority(DEFAULT_PRIORITY);
        return ordPriceAlteration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPriceAlteration createUpdatedEntity(EntityManager em) {
        OrdPriceAlteration ordPriceAlteration = new OrdPriceAlteration()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .applicationDuration(UPDATED_APPLICATION_DURATION)
            .priority(UPDATED_PRIORITY);
        return ordPriceAlteration;
    }

    @BeforeEach
    public void initTest() {
        ordPriceAlteration = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeCreate = ordPriceAlterationRepository.findAll().size();
        // Create the OrdPriceAlteration
        restOrdPriceAlterationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isCreated());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeCreate + 1);
        OrdPriceAlteration testOrdPriceAlteration = ordPriceAlterationList.get(ordPriceAlterationList.size() - 1);
        assertThat(testOrdPriceAlteration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdPriceAlteration.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrdPriceAlteration.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);
        assertThat(testOrdPriceAlteration.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testOrdPriceAlteration.getRecurringChargePeriod()).isEqualTo(DEFAULT_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdPriceAlteration.getApplicationDuration()).isEqualTo(DEFAULT_APPLICATION_DURATION);
        assertThat(testOrdPriceAlteration.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createOrdPriceAlterationWithExistingId() throws Exception {
        // Create the OrdPriceAlteration with an existing ID
        ordPriceAlteration.setId(1L);

        int databaseSizeBeforeCreate = ordPriceAlterationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdPriceAlterationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdPriceAlterations() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        // Get all the ordPriceAlterationList
        restOrdPriceAlterationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordPriceAlteration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE)))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE)))
            .andExpect(jsonPath("$.[*].recurringChargePeriod").value(hasItem(DEFAULT_RECURRING_CHARGE_PERIOD)))
            .andExpect(jsonPath("$.[*].applicationDuration").value(hasItem(DEFAULT_APPLICATION_DURATION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)));
    }

    @Test
    @Transactional
    void getOrdPriceAlteration() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        // Get the ordPriceAlteration
        restOrdPriceAlterationMockMvc
            .perform(get(ENTITY_API_URL_ID, ordPriceAlteration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordPriceAlteration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priceType").value(DEFAULT_PRICE_TYPE))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE))
            .andExpect(jsonPath("$.recurringChargePeriod").value(DEFAULT_RECURRING_CHARGE_PERIOD))
            .andExpect(jsonPath("$.applicationDuration").value(DEFAULT_APPLICATION_DURATION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingOrdPriceAlteration() throws Exception {
        // Get the ordPriceAlteration
        restOrdPriceAlterationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdPriceAlteration() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();

        // Update the ordPriceAlteration
        OrdPriceAlteration updatedOrdPriceAlteration = ordPriceAlterationRepository.findById(ordPriceAlteration.getId()).get();
        // Disconnect from session so that the updates on updatedOrdPriceAlteration are not directly saved in db
        em.detach(updatedOrdPriceAlteration);
        updatedOrdPriceAlteration
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .applicationDuration(UPDATED_APPLICATION_DURATION)
            .priority(UPDATED_PRIORITY);

        restOrdPriceAlterationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdPriceAlteration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdPriceAlteration))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAlteration testOrdPriceAlteration = ordPriceAlterationList.get(ordPriceAlterationList.size() - 1);
        assertThat(testOrdPriceAlteration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPriceAlteration.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdPriceAlteration.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testOrdPriceAlteration.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testOrdPriceAlteration.getRecurringChargePeriod()).isEqualTo(UPDATED_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdPriceAlteration.getApplicationDuration()).isEqualTo(UPDATED_APPLICATION_DURATION);
        assertThat(testOrdPriceAlteration.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordPriceAlteration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdPriceAlterationWithPatch() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();

        // Update the ordPriceAlteration using partial update
        OrdPriceAlteration partialUpdatedOrdPriceAlteration = new OrdPriceAlteration();
        partialUpdatedOrdPriceAlteration.setId(ordPriceAlteration.getId());

        partialUpdatedOrdPriceAlteration.name(UPDATED_NAME).priceType(UPDATED_PRICE_TYPE).unitOfMeasure(UPDATED_UNIT_OF_MEASURE);

        restOrdPriceAlterationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPriceAlteration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPriceAlteration))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAlteration testOrdPriceAlteration = ordPriceAlterationList.get(ordPriceAlterationList.size() - 1);
        assertThat(testOrdPriceAlteration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPriceAlteration.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrdPriceAlteration.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testOrdPriceAlteration.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testOrdPriceAlteration.getRecurringChargePeriod()).isEqualTo(DEFAULT_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdPriceAlteration.getApplicationDuration()).isEqualTo(DEFAULT_APPLICATION_DURATION);
        assertThat(testOrdPriceAlteration.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateOrdPriceAlterationWithPatch() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();

        // Update the ordPriceAlteration using partial update
        OrdPriceAlteration partialUpdatedOrdPriceAlteration = new OrdPriceAlteration();
        partialUpdatedOrdPriceAlteration.setId(ordPriceAlteration.getId());

        partialUpdatedOrdPriceAlteration
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .applicationDuration(UPDATED_APPLICATION_DURATION)
            .priority(UPDATED_PRIORITY);

        restOrdPriceAlterationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPriceAlteration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPriceAlteration))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAlteration testOrdPriceAlteration = ordPriceAlterationList.get(ordPriceAlterationList.size() - 1);
        assertThat(testOrdPriceAlteration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPriceAlteration.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdPriceAlteration.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testOrdPriceAlteration.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testOrdPriceAlteration.getRecurringChargePeriod()).isEqualTo(UPDATED_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdPriceAlteration.getApplicationDuration()).isEqualTo(UPDATED_APPLICATION_DURATION);
        assertThat(testOrdPriceAlteration.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordPriceAlteration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdPriceAlteration() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAlterationRepository.findAll().size();
        ordPriceAlteration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAlterationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAlteration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPriceAlteration in the database
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdPriceAlteration() throws Exception {
        // Initialize the database
        ordPriceAlterationRepository.saveAndFlush(ordPriceAlteration);

        int databaseSizeBeforeDelete = ordPriceAlterationRepository.findAll().size();

        // Delete the ordPriceAlteration
        restOrdPriceAlterationMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordPriceAlteration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdPriceAlteration> ordPriceAlterationList = ordPriceAlterationRepository.findAll();
        assertThat(ordPriceAlterationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

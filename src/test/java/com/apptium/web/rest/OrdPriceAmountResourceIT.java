package com.apptium.web.rest;

import static com.apptium.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdPriceAmount;
import com.apptium.repository.OrdPriceAmountRepository;
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
 * Integration tests for the {@link OrdPriceAmountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdPriceAmountResourceIT {

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TAX_INCLUDED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_INCLUDED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DUTY_FREE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DUTY_FREE_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENTAGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_RECURRING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_RECURRING_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_ONE_TIME_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_ONE_TIME_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/ord-price-amounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdPriceAmountRepository ordPriceAmountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdPriceAmountMockMvc;

    private OrdPriceAmount ordPriceAmount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPriceAmount createEntity(EntityManager em) {
        OrdPriceAmount ordPriceAmount = new OrdPriceAmount()
            .currencyCode(DEFAULT_CURRENCY_CODE)
            .taxIncludedAmount(DEFAULT_TAX_INCLUDED_AMOUNT)
            .dutyFreeAmount(DEFAULT_DUTY_FREE_AMOUNT)
            .taxRate(DEFAULT_TAX_RATE)
            .percentage(DEFAULT_PERCENTAGE)
            .totalRecurringPrice(DEFAULT_TOTAL_RECURRING_PRICE)
            .totalOneTimePrice(DEFAULT_TOTAL_ONE_TIME_PRICE);
        return ordPriceAmount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPriceAmount createUpdatedEntity(EntityManager em) {
        OrdPriceAmount ordPriceAmount = new OrdPriceAmount()
            .currencyCode(UPDATED_CURRENCY_CODE)
            .taxIncludedAmount(UPDATED_TAX_INCLUDED_AMOUNT)
            .dutyFreeAmount(UPDATED_DUTY_FREE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .percentage(UPDATED_PERCENTAGE)
            .totalRecurringPrice(UPDATED_TOTAL_RECURRING_PRICE)
            .totalOneTimePrice(UPDATED_TOTAL_ONE_TIME_PRICE);
        return ordPriceAmount;
    }

    @BeforeEach
    public void initTest() {
        ordPriceAmount = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdPriceAmount() throws Exception {
        int databaseSizeBeforeCreate = ordPriceAmountRepository.findAll().size();
        // Create the OrdPriceAmount
        restOrdPriceAmountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isCreated());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeCreate + 1);
        OrdPriceAmount testOrdPriceAmount = ordPriceAmountList.get(ordPriceAmountList.size() - 1);
        assertThat(testOrdPriceAmount.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testOrdPriceAmount.getTaxIncludedAmount()).isEqualByComparingTo(DEFAULT_TAX_INCLUDED_AMOUNT);
        assertThat(testOrdPriceAmount.getDutyFreeAmount()).isEqualByComparingTo(DEFAULT_DUTY_FREE_AMOUNT);
        assertThat(testOrdPriceAmount.getTaxRate()).isEqualByComparingTo(DEFAULT_TAX_RATE);
        assertThat(testOrdPriceAmount.getPercentage()).isEqualByComparingTo(DEFAULT_PERCENTAGE);
        assertThat(testOrdPriceAmount.getTotalRecurringPrice()).isEqualByComparingTo(DEFAULT_TOTAL_RECURRING_PRICE);
        assertThat(testOrdPriceAmount.getTotalOneTimePrice()).isEqualByComparingTo(DEFAULT_TOTAL_ONE_TIME_PRICE);
    }

    @Test
    @Transactional
    void createOrdPriceAmountWithExistingId() throws Exception {
        // Create the OrdPriceAmount with an existing ID
        ordPriceAmount.setId(1L);

        int databaseSizeBeforeCreate = ordPriceAmountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdPriceAmountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdPriceAmounts() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        // Get all the ordPriceAmountList
        restOrdPriceAmountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordPriceAmount.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].taxIncludedAmount").value(hasItem(sameNumber(DEFAULT_TAX_INCLUDED_AMOUNT))))
            .andExpect(jsonPath("$.[*].dutyFreeAmount").value(hasItem(sameNumber(DEFAULT_DUTY_FREE_AMOUNT))))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(sameNumber(DEFAULT_TAX_RATE))))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(sameNumber(DEFAULT_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].totalRecurringPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_RECURRING_PRICE))))
            .andExpect(jsonPath("$.[*].totalOneTimePrice").value(hasItem(sameNumber(DEFAULT_TOTAL_ONE_TIME_PRICE))));
    }

    @Test
    @Transactional
    void getOrdPriceAmount() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        // Get the ordPriceAmount
        restOrdPriceAmountMockMvc
            .perform(get(ENTITY_API_URL_ID, ordPriceAmount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordPriceAmount.getId().intValue()))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
            .andExpect(jsonPath("$.taxIncludedAmount").value(sameNumber(DEFAULT_TAX_INCLUDED_AMOUNT)))
            .andExpect(jsonPath("$.dutyFreeAmount").value(sameNumber(DEFAULT_DUTY_FREE_AMOUNT)))
            .andExpect(jsonPath("$.taxRate").value(sameNumber(DEFAULT_TAX_RATE)))
            .andExpect(jsonPath("$.percentage").value(sameNumber(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.totalRecurringPrice").value(sameNumber(DEFAULT_TOTAL_RECURRING_PRICE)))
            .andExpect(jsonPath("$.totalOneTimePrice").value(sameNumber(DEFAULT_TOTAL_ONE_TIME_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingOrdPriceAmount() throws Exception {
        // Get the ordPriceAmount
        restOrdPriceAmountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdPriceAmount() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();

        // Update the ordPriceAmount
        OrdPriceAmount updatedOrdPriceAmount = ordPriceAmountRepository.findById(ordPriceAmount.getId()).get();
        // Disconnect from session so that the updates on updatedOrdPriceAmount are not directly saved in db
        em.detach(updatedOrdPriceAmount);
        updatedOrdPriceAmount
            .currencyCode(UPDATED_CURRENCY_CODE)
            .taxIncludedAmount(UPDATED_TAX_INCLUDED_AMOUNT)
            .dutyFreeAmount(UPDATED_DUTY_FREE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .percentage(UPDATED_PERCENTAGE)
            .totalRecurringPrice(UPDATED_TOTAL_RECURRING_PRICE)
            .totalOneTimePrice(UPDATED_TOTAL_ONE_TIME_PRICE);

        restOrdPriceAmountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdPriceAmount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdPriceAmount))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAmount testOrdPriceAmount = ordPriceAmountList.get(ordPriceAmountList.size() - 1);
        assertThat(testOrdPriceAmount.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testOrdPriceAmount.getTaxIncludedAmount()).isEqualTo(UPDATED_TAX_INCLUDED_AMOUNT);
        assertThat(testOrdPriceAmount.getDutyFreeAmount()).isEqualTo(UPDATED_DUTY_FREE_AMOUNT);
        assertThat(testOrdPriceAmount.getTaxRate()).isEqualTo(UPDATED_TAX_RATE);
        assertThat(testOrdPriceAmount.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testOrdPriceAmount.getTotalRecurringPrice()).isEqualTo(UPDATED_TOTAL_RECURRING_PRICE);
        assertThat(testOrdPriceAmount.getTotalOneTimePrice()).isEqualTo(UPDATED_TOTAL_ONE_TIME_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordPriceAmount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPriceAmount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdPriceAmountWithPatch() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();

        // Update the ordPriceAmount using partial update
        OrdPriceAmount partialUpdatedOrdPriceAmount = new OrdPriceAmount();
        partialUpdatedOrdPriceAmount.setId(ordPriceAmount.getId());

        partialUpdatedOrdPriceAmount.currencyCode(UPDATED_CURRENCY_CODE).taxRate(UPDATED_TAX_RATE);

        restOrdPriceAmountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPriceAmount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPriceAmount))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAmount testOrdPriceAmount = ordPriceAmountList.get(ordPriceAmountList.size() - 1);
        assertThat(testOrdPriceAmount.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testOrdPriceAmount.getTaxIncludedAmount()).isEqualByComparingTo(DEFAULT_TAX_INCLUDED_AMOUNT);
        assertThat(testOrdPriceAmount.getDutyFreeAmount()).isEqualByComparingTo(DEFAULT_DUTY_FREE_AMOUNT);
        assertThat(testOrdPriceAmount.getTaxRate()).isEqualByComparingTo(UPDATED_TAX_RATE);
        assertThat(testOrdPriceAmount.getPercentage()).isEqualByComparingTo(DEFAULT_PERCENTAGE);
        assertThat(testOrdPriceAmount.getTotalRecurringPrice()).isEqualByComparingTo(DEFAULT_TOTAL_RECURRING_PRICE);
        assertThat(testOrdPriceAmount.getTotalOneTimePrice()).isEqualByComparingTo(DEFAULT_TOTAL_ONE_TIME_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateOrdPriceAmountWithPatch() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();

        // Update the ordPriceAmount using partial update
        OrdPriceAmount partialUpdatedOrdPriceAmount = new OrdPriceAmount();
        partialUpdatedOrdPriceAmount.setId(ordPriceAmount.getId());

        partialUpdatedOrdPriceAmount
            .currencyCode(UPDATED_CURRENCY_CODE)
            .taxIncludedAmount(UPDATED_TAX_INCLUDED_AMOUNT)
            .dutyFreeAmount(UPDATED_DUTY_FREE_AMOUNT)
            .taxRate(UPDATED_TAX_RATE)
            .percentage(UPDATED_PERCENTAGE)
            .totalRecurringPrice(UPDATED_TOTAL_RECURRING_PRICE)
            .totalOneTimePrice(UPDATED_TOTAL_ONE_TIME_PRICE);

        restOrdPriceAmountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPriceAmount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPriceAmount))
            )
            .andExpect(status().isOk());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
        OrdPriceAmount testOrdPriceAmount = ordPriceAmountList.get(ordPriceAmountList.size() - 1);
        assertThat(testOrdPriceAmount.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testOrdPriceAmount.getTaxIncludedAmount()).isEqualByComparingTo(UPDATED_TAX_INCLUDED_AMOUNT);
        assertThat(testOrdPriceAmount.getDutyFreeAmount()).isEqualByComparingTo(UPDATED_DUTY_FREE_AMOUNT);
        assertThat(testOrdPriceAmount.getTaxRate()).isEqualByComparingTo(UPDATED_TAX_RATE);
        assertThat(testOrdPriceAmount.getPercentage()).isEqualByComparingTo(UPDATED_PERCENTAGE);
        assertThat(testOrdPriceAmount.getTotalRecurringPrice()).isEqualByComparingTo(UPDATED_TOTAL_RECURRING_PRICE);
        assertThat(testOrdPriceAmount.getTotalOneTimePrice()).isEqualByComparingTo(UPDATED_TOTAL_ONE_TIME_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordPriceAmount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdPriceAmount() throws Exception {
        int databaseSizeBeforeUpdate = ordPriceAmountRepository.findAll().size();
        ordPriceAmount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPriceAmountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordPriceAmount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPriceAmount in the database
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdPriceAmount() throws Exception {
        // Initialize the database
        ordPriceAmountRepository.saveAndFlush(ordPriceAmount);

        int databaseSizeBeforeDelete = ordPriceAmountRepository.findAll().size();

        // Delete the ordPriceAmount
        restOrdPriceAmountMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordPriceAmount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdPriceAmount> ordPriceAmountList = ordPriceAmountRepository.findAll();
        assertThat(ordPriceAmountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

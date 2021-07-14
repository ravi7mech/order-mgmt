package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdOrderPrice;
import com.apptium.repository.OrdOrderPriceRepository;
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
 * Integration tests for the {@link OrdOrderPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdOrderPriceResourceIT {

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

    private static final Long DEFAULT_PRICE_ID = 1L;
    private static final Long UPDATED_PRICE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/ord-order-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdOrderPriceRepository ordOrderPriceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdOrderPriceMockMvc;

    private OrdOrderPrice ordOrderPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderPrice createEntity(EntityManager em) {
        OrdOrderPrice ordOrderPrice = new OrdOrderPrice()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .priceType(DEFAULT_PRICE_TYPE)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE)
            .recurringChargePeriod(DEFAULT_RECURRING_CHARGE_PERIOD)
            .priceId(DEFAULT_PRICE_ID);
        return ordOrderPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdOrderPrice createUpdatedEntity(EntityManager em) {
        OrdOrderPrice ordOrderPrice = new OrdOrderPrice()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .priceId(UPDATED_PRICE_ID);
        return ordOrderPrice;
    }

    @BeforeEach
    public void initTest() {
        ordOrderPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdOrderPrice() throws Exception {
        int databaseSizeBeforeCreate = ordOrderPriceRepository.findAll().size();
        // Create the OrdOrderPrice
        restOrdOrderPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderPrice)))
            .andExpect(status().isCreated());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeCreate + 1);
        OrdOrderPrice testOrdOrderPrice = ordOrderPriceList.get(ordOrderPriceList.size() - 1);
        assertThat(testOrdOrderPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdOrderPrice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrdOrderPrice.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);
        assertThat(testOrdOrderPrice.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testOrdOrderPrice.getRecurringChargePeriod()).isEqualTo(DEFAULT_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdOrderPrice.getPriceId()).isEqualTo(DEFAULT_PRICE_ID);
    }

    @Test
    @Transactional
    void createOrdOrderPriceWithExistingId() throws Exception {
        // Create the OrdOrderPrice with an existing ID
        ordOrderPrice.setId(1L);

        int databaseSizeBeforeCreate = ordOrderPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdOrderPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderPrice)))
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdOrderPrices() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        // Get all the ordOrderPriceList
        restOrdOrderPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordOrderPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE)))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE)))
            .andExpect(jsonPath("$.[*].recurringChargePeriod").value(hasItem(DEFAULT_RECURRING_CHARGE_PERIOD)))
            .andExpect(jsonPath("$.[*].priceId").value(hasItem(DEFAULT_PRICE_ID.intValue())));
    }

    @Test
    @Transactional
    void getOrdOrderPrice() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        // Get the ordOrderPrice
        restOrdOrderPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, ordOrderPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordOrderPrice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priceType").value(DEFAULT_PRICE_TYPE))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE))
            .andExpect(jsonPath("$.recurringChargePeriod").value(DEFAULT_RECURRING_CHARGE_PERIOD))
            .andExpect(jsonPath("$.priceId").value(DEFAULT_PRICE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrdOrderPrice() throws Exception {
        // Get the ordOrderPrice
        restOrdOrderPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdOrderPrice() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();

        // Update the ordOrderPrice
        OrdOrderPrice updatedOrdOrderPrice = ordOrderPriceRepository.findById(ordOrderPrice.getId()).get();
        // Disconnect from session so that the updates on updatedOrdOrderPrice are not directly saved in db
        em.detach(updatedOrdOrderPrice);
        updatedOrdOrderPrice
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .priceId(UPDATED_PRICE_ID);

        restOrdOrderPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdOrderPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdOrderPrice))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderPrice testOrdOrderPrice = ordOrderPriceList.get(ordOrderPriceList.size() - 1);
        assertThat(testOrdOrderPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdOrderPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdOrderPrice.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testOrdOrderPrice.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testOrdOrderPrice.getRecurringChargePeriod()).isEqualTo(UPDATED_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdOrderPrice.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordOrderPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordOrderPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdOrderPriceWithPatch() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();

        // Update the ordOrderPrice using partial update
        OrdOrderPrice partialUpdatedOrdOrderPrice = new OrdOrderPrice();
        partialUpdatedOrdOrderPrice.setId(ordOrderPrice.getId());

        partialUpdatedOrdOrderPrice.description(UPDATED_DESCRIPTION).priceId(UPDATED_PRICE_ID);

        restOrdOrderPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderPrice))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderPrice testOrdOrderPrice = ordOrderPriceList.get(ordOrderPriceList.size() - 1);
        assertThat(testOrdOrderPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdOrderPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdOrderPrice.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);
        assertThat(testOrdOrderPrice.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testOrdOrderPrice.getRecurringChargePeriod()).isEqualTo(DEFAULT_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdOrderPrice.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrdOrderPriceWithPatch() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();

        // Update the ordOrderPrice using partial update
        OrdOrderPrice partialUpdatedOrdOrderPrice = new OrdOrderPrice();
        partialUpdatedOrdOrderPrice.setId(ordOrderPrice.getId());

        partialUpdatedOrdOrderPrice
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priceType(UPDATED_PRICE_TYPE)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .recurringChargePeriod(UPDATED_RECURRING_CHARGE_PERIOD)
            .priceId(UPDATED_PRICE_ID);

        restOrdOrderPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdOrderPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdOrderPrice))
            )
            .andExpect(status().isOk());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
        OrdOrderPrice testOrdOrderPrice = ordOrderPriceList.get(ordOrderPriceList.size() - 1);
        assertThat(testOrdOrderPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdOrderPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrdOrderPrice.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);
        assertThat(testOrdOrderPrice.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testOrdOrderPrice.getRecurringChargePeriod()).isEqualTo(UPDATED_RECURRING_CHARGE_PERIOD);
        assertThat(testOrdOrderPrice.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordOrderPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordOrderPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdOrderPrice() throws Exception {
        int databaseSizeBeforeUpdate = ordOrderPriceRepository.findAll().size();
        ordOrderPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdOrderPriceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordOrderPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdOrderPrice in the database
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdOrderPrice() throws Exception {
        // Initialize the database
        ordOrderPriceRepository.saveAndFlush(ordOrderPrice);

        int databaseSizeBeforeDelete = ordOrderPriceRepository.findAll().size();

        // Delete the ordOrderPrice
        restOrdOrderPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordOrderPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdOrderPrice> ordOrderPriceList = ordOrderPriceRepository.findAll();
        assertThat(ordOrderPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

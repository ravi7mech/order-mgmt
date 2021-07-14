package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdProduct;
import com.apptium.repository.OrdProductRepository;
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
 * Integration tests for the {@link OrdProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdProductResourceIT {

    private static final Long DEFAULT_VERSION_ID = 1L;
    private static final Long UPDATED_VERSION_ID = 2L;

    private static final Long DEFAULT_VARIATION_ID = 1L;
    private static final Long UPDATED_VARIATION_ID = 2L;

    private static final String DEFAULT_LINE_OF_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_OF_SERVICE = "BBBBBBBBBB";

    private static final Long DEFAULT_ASSET_ID = 1L;
    private static final Long UPDATED_ASSET_ID = 2L;

    private static final Long DEFAULT_SERIAL_NO = 1L;
    private static final Long UPDATED_SERIAL_NO = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdProductRepository ordProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdProductMockMvc;

    private OrdProduct ordProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProduct createEntity(EntityManager em) {
        OrdProduct ordProduct = new OrdProduct()
            .versionId(DEFAULT_VERSION_ID)
            .variationId(DEFAULT_VARIATION_ID)
            .lineOfService(DEFAULT_LINE_OF_SERVICE)
            .assetId(DEFAULT_ASSET_ID)
            .serialNo(DEFAULT_SERIAL_NO)
            .name(DEFAULT_NAME);
        return ordProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdProduct createUpdatedEntity(EntityManager em) {
        OrdProduct ordProduct = new OrdProduct()
            .versionId(UPDATED_VERSION_ID)
            .variationId(UPDATED_VARIATION_ID)
            .lineOfService(UPDATED_LINE_OF_SERVICE)
            .assetId(UPDATED_ASSET_ID)
            .serialNo(UPDATED_SERIAL_NO)
            .name(UPDATED_NAME);
        return ordProduct;
    }

    @BeforeEach
    public void initTest() {
        ordProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdProduct() throws Exception {
        int databaseSizeBeforeCreate = ordProductRepository.findAll().size();
        // Create the OrdProduct
        restOrdProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProduct)))
            .andExpect(status().isCreated());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeCreate + 1);
        OrdProduct testOrdProduct = ordProductList.get(ordProductList.size() - 1);
        assertThat(testOrdProduct.getVersionId()).isEqualTo(DEFAULT_VERSION_ID);
        assertThat(testOrdProduct.getVariationId()).isEqualTo(DEFAULT_VARIATION_ID);
        assertThat(testOrdProduct.getLineOfService()).isEqualTo(DEFAULT_LINE_OF_SERVICE);
        assertThat(testOrdProduct.getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
        assertThat(testOrdProduct.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testOrdProduct.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOrdProductWithExistingId() throws Exception {
        // Create the OrdProduct with an existing ID
        ordProduct.setId(1L);

        int databaseSizeBeforeCreate = ordProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProduct)))
            .andExpect(status().isBadRequest());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdProducts() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        // Get all the ordProductList
        restOrdProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionId").value(hasItem(DEFAULT_VERSION_ID.intValue())))
            .andExpect(jsonPath("$.[*].variationId").value(hasItem(DEFAULT_VARIATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineOfService").value(hasItem(DEFAULT_LINE_OF_SERVICE)))
            .andExpect(jsonPath("$.[*].assetId").value(hasItem(DEFAULT_ASSET_ID.intValue())))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getOrdProduct() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        // Get the ordProduct
        restOrdProductMockMvc
            .perform(get(ENTITY_API_URL_ID, ordProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordProduct.getId().intValue()))
            .andExpect(jsonPath("$.versionId").value(DEFAULT_VERSION_ID.intValue()))
            .andExpect(jsonPath("$.variationId").value(DEFAULT_VARIATION_ID.intValue()))
            .andExpect(jsonPath("$.lineOfService").value(DEFAULT_LINE_OF_SERVICE))
            .andExpect(jsonPath("$.assetId").value(DEFAULT_ASSET_ID.intValue()))
            .andExpect(jsonPath("$.serialNo").value(DEFAULT_SERIAL_NO.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOrdProduct() throws Exception {
        // Get the ordProduct
        restOrdProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdProduct() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();

        // Update the ordProduct
        OrdProduct updatedOrdProduct = ordProductRepository.findById(ordProduct.getId()).get();
        // Disconnect from session so that the updates on updatedOrdProduct are not directly saved in db
        em.detach(updatedOrdProduct);
        updatedOrdProduct
            .versionId(UPDATED_VERSION_ID)
            .variationId(UPDATED_VARIATION_ID)
            .lineOfService(UPDATED_LINE_OF_SERVICE)
            .assetId(UPDATED_ASSET_ID)
            .serialNo(UPDATED_SERIAL_NO)
            .name(UPDATED_NAME);

        restOrdProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
        OrdProduct testOrdProduct = ordProductList.get(ordProductList.size() - 1);
        assertThat(testOrdProduct.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testOrdProduct.getVariationId()).isEqualTo(UPDATED_VARIATION_ID);
        assertThat(testOrdProduct.getLineOfService()).isEqualTo(UPDATED_LINE_OF_SERVICE);
        assertThat(testOrdProduct.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testOrdProduct.getSerialNo()).isEqualTo(UPDATED_SERIAL_NO);
        assertThat(testOrdProduct.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdProductWithPatch() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();

        // Update the ordProduct using partial update
        OrdProduct partialUpdatedOrdProduct = new OrdProduct();
        partialUpdatedOrdProduct.setId(ordProduct.getId());

        partialUpdatedOrdProduct.versionId(UPDATED_VERSION_ID).variationId(UPDATED_VARIATION_ID).lineOfService(UPDATED_LINE_OF_SERVICE);

        restOrdProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
        OrdProduct testOrdProduct = ordProductList.get(ordProductList.size() - 1);
        assertThat(testOrdProduct.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testOrdProduct.getVariationId()).isEqualTo(UPDATED_VARIATION_ID);
        assertThat(testOrdProduct.getLineOfService()).isEqualTo(UPDATED_LINE_OF_SERVICE);
        assertThat(testOrdProduct.getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
        assertThat(testOrdProduct.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testOrdProduct.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOrdProductWithPatch() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();

        // Update the ordProduct using partial update
        OrdProduct partialUpdatedOrdProduct = new OrdProduct();
        partialUpdatedOrdProduct.setId(ordProduct.getId());

        partialUpdatedOrdProduct
            .versionId(UPDATED_VERSION_ID)
            .variationId(UPDATED_VARIATION_ID)
            .lineOfService(UPDATED_LINE_OF_SERVICE)
            .assetId(UPDATED_ASSET_ID)
            .serialNo(UPDATED_SERIAL_NO)
            .name(UPDATED_NAME);

        restOrdProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
        OrdProduct testOrdProduct = ordProductList.get(ordProductList.size() - 1);
        assertThat(testOrdProduct.getVersionId()).isEqualTo(UPDATED_VERSION_ID);
        assertThat(testOrdProduct.getVariationId()).isEqualTo(UPDATED_VARIATION_ID);
        assertThat(testOrdProduct.getLineOfService()).isEqualTo(UPDATED_LINE_OF_SERVICE);
        assertThat(testOrdProduct.getAssetId()).isEqualTo(UPDATED_ASSET_ID);
        assertThat(testOrdProduct.getSerialNo()).isEqualTo(UPDATED_SERIAL_NO);
        assertThat(testOrdProduct.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdProduct() throws Exception {
        int databaseSizeBeforeUpdate = ordProductRepository.findAll().size();
        ordProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdProduct in the database
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdProduct() throws Exception {
        // Initialize the database
        ordProductRepository.saveAndFlush(ordProduct);

        int databaseSizeBeforeDelete = ordProductRepository.findAll().size();

        // Delete the ordProduct
        restOrdProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdProduct> ordProductList = ordProductRepository.findAll();
        assertThat(ordProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

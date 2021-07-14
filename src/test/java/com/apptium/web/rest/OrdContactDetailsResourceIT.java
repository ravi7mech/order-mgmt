package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdContactDetails;
import com.apptium.repository.OrdContactDetailsRepository;
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
 * Integration tests for the {@link OrdContactDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdContactDetailsResourceIT {

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-contact-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdContactDetailsRepository ordContactDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdContactDetailsMockMvc;

    private OrdContactDetails ordContactDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContactDetails createEntity(EntityManager em) {
        OrdContactDetails ordContactDetails = new OrdContactDetails()
            .contactName(DEFAULT_CONTACT_NAME)
            .contactPhoneNumber(DEFAULT_CONTACT_PHONE_NUMBER)
            .contactEmailId(DEFAULT_CONTACT_EMAIL_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return ordContactDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdContactDetails createUpdatedEntity(EntityManager em) {
        OrdContactDetails ordContactDetails = new OrdContactDetails()
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhoneNumber(UPDATED_CONTACT_PHONE_NUMBER)
            .contactEmailId(UPDATED_CONTACT_EMAIL_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return ordContactDetails;
    }

    @BeforeEach
    public void initTest() {
        ordContactDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdContactDetails() throws Exception {
        int databaseSizeBeforeCreate = ordContactDetailsRepository.findAll().size();
        // Create the OrdContactDetails
        restOrdContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isCreated());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        OrdContactDetails testOrdContactDetails = ordContactDetailsList.get(ordContactDetailsList.size() - 1);
        assertThat(testOrdContactDetails.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testOrdContactDetails.getContactPhoneNumber()).isEqualTo(DEFAULT_CONTACT_PHONE_NUMBER);
        assertThat(testOrdContactDetails.getContactEmailId()).isEqualTo(DEFAULT_CONTACT_EMAIL_ID);
        assertThat(testOrdContactDetails.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOrdContactDetails.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createOrdContactDetailsWithExistingId() throws Exception {
        // Create the OrdContactDetails with an existing ID
        ordContactDetails.setId(1L);

        int databaseSizeBeforeCreate = ordContactDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdContactDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdContactDetails() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        // Get all the ordContactDetailsList
        restOrdContactDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordContactDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactPhoneNumber").value(hasItem(DEFAULT_CONTACT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].contactEmailId").value(hasItem(DEFAULT_CONTACT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    @Transactional
    void getOrdContactDetails() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        // Get the ordContactDetails
        restOrdContactDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, ordContactDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordContactDetails.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.contactPhoneNumber").value(DEFAULT_CONTACT_PHONE_NUMBER))
            .andExpect(jsonPath("$.contactEmailId").value(DEFAULT_CONTACT_EMAIL_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOrdContactDetails() throws Exception {
        // Get the ordContactDetails
        restOrdContactDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdContactDetails() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();

        // Update the ordContactDetails
        OrdContactDetails updatedOrdContactDetails = ordContactDetailsRepository.findById(ordContactDetails.getId()).get();
        // Disconnect from session so that the updates on updatedOrdContactDetails are not directly saved in db
        em.detach(updatedOrdContactDetails);
        updatedOrdContactDetails
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhoneNumber(UPDATED_CONTACT_PHONE_NUMBER)
            .contactEmailId(UPDATED_CONTACT_EMAIL_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restOrdContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdContactDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdContactDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrdContactDetails testOrdContactDetails = ordContactDetailsList.get(ordContactDetailsList.size() - 1);
        assertThat(testOrdContactDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOrdContactDetails.getContactPhoneNumber()).isEqualTo(UPDATED_CONTACT_PHONE_NUMBER);
        assertThat(testOrdContactDetails.getContactEmailId()).isEqualTo(UPDATED_CONTACT_EMAIL_ID);
        assertThat(testOrdContactDetails.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOrdContactDetails.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordContactDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdContactDetailsWithPatch() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();

        // Update the ordContactDetails using partial update
        OrdContactDetails partialUpdatedOrdContactDetails = new OrdContactDetails();
        partialUpdatedOrdContactDetails.setId(ordContactDetails.getId());

        partialUpdatedOrdContactDetails.firstName(UPDATED_FIRST_NAME);

        restOrdContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContactDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContactDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrdContactDetails testOrdContactDetails = ordContactDetailsList.get(ordContactDetailsList.size() - 1);
        assertThat(testOrdContactDetails.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testOrdContactDetails.getContactPhoneNumber()).isEqualTo(DEFAULT_CONTACT_PHONE_NUMBER);
        assertThat(testOrdContactDetails.getContactEmailId()).isEqualTo(DEFAULT_CONTACT_EMAIL_ID);
        assertThat(testOrdContactDetails.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOrdContactDetails.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOrdContactDetailsWithPatch() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();

        // Update the ordContactDetails using partial update
        OrdContactDetails partialUpdatedOrdContactDetails = new OrdContactDetails();
        partialUpdatedOrdContactDetails.setId(ordContactDetails.getId());

        partialUpdatedOrdContactDetails
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhoneNumber(UPDATED_CONTACT_PHONE_NUMBER)
            .contactEmailId(UPDATED_CONTACT_EMAIL_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restOrdContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdContactDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdContactDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrdContactDetails testOrdContactDetails = ordContactDetailsList.get(ordContactDetailsList.size() - 1);
        assertThat(testOrdContactDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testOrdContactDetails.getContactPhoneNumber()).isEqualTo(UPDATED_CONTACT_PHONE_NUMBER);
        assertThat(testOrdContactDetails.getContactEmailId()).isEqualTo(UPDATED_CONTACT_EMAIL_ID);
        assertThat(testOrdContactDetails.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOrdContactDetails.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordContactDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdContactDetails() throws Exception {
        int databaseSizeBeforeUpdate = ordContactDetailsRepository.findAll().size();
        ordContactDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdContactDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordContactDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdContactDetails in the database
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdContactDetails() throws Exception {
        // Initialize the database
        ordContactDetailsRepository.saveAndFlush(ordContactDetails);

        int databaseSizeBeforeDelete = ordContactDetailsRepository.findAll().size();

        // Delete the ordContactDetails
        restOrdContactDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordContactDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdContactDetails> ordContactDetailsList = ordContactDetailsRepository.findAll();
        assertThat(ordContactDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

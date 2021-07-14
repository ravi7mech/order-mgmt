package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdPlace;
import com.apptium.repository.OrdPlaceRepository;
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
 * Integration tests for the {@link OrdPlaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdPlaceResourceIT {

    private static final String DEFAULT_HREF = "AAAAAAAAAA";
    private static final String UPDATED_HREF = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdPlaceRepository ordPlaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdPlaceMockMvc;

    private OrdPlace ordPlace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPlace createEntity(EntityManager em) {
        OrdPlace ordPlace = new OrdPlace().href(DEFAULT_HREF).name(DEFAULT_NAME).role(DEFAULT_ROLE);
        return ordPlace;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdPlace createUpdatedEntity(EntityManager em) {
        OrdPlace ordPlace = new OrdPlace().href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);
        return ordPlace;
    }

    @BeforeEach
    public void initTest() {
        ordPlace = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdPlace() throws Exception {
        int databaseSizeBeforeCreate = ordPlaceRepository.findAll().size();
        // Create the OrdPlace
        restOrdPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPlace)))
            .andExpect(status().isCreated());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        OrdPlace testOrdPlace = ordPlaceList.get(ordPlaceList.size() - 1);
        assertThat(testOrdPlace.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdPlace.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createOrdPlaceWithExistingId() throws Exception {
        // Create the OrdPlace with an existing ID
        ordPlace.setId(1L);

        int databaseSizeBeforeCreate = ordPlaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPlace)))
            .andExpect(status().isBadRequest());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdPlaces() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        // Get all the ordPlaceList
        restOrdPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].href").value(hasItem(DEFAULT_HREF)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getOrdPlace() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        // Get the ordPlace
        restOrdPlaceMockMvc
            .perform(get(ENTITY_API_URL_ID, ordPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordPlace.getId().intValue()))
            .andExpect(jsonPath("$.href").value(DEFAULT_HREF))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingOrdPlace() throws Exception {
        // Get the ordPlace
        restOrdPlaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdPlace() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();

        // Update the ordPlace
        OrdPlace updatedOrdPlace = ordPlaceRepository.findById(ordPlace.getId()).get();
        // Disconnect from session so that the updates on updatedOrdPlace are not directly saved in db
        em.detach(updatedOrdPlace);
        updatedOrdPlace.href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);

        restOrdPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdPlace))
            )
            .andExpect(status().isOk());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
        OrdPlace testOrdPlace = ordPlaceList.get(ordPlaceList.size() - 1);
        assertThat(testOrdPlace.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPlace.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordPlace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdPlaceWithPatch() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();

        // Update the ordPlace using partial update
        OrdPlace partialUpdatedOrdPlace = new OrdPlace();
        partialUpdatedOrdPlace.setId(ordPlace.getId());

        partialUpdatedOrdPlace.href(UPDATED_HREF);

        restOrdPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPlace))
            )
            .andExpect(status().isOk());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
        OrdPlace testOrdPlace = ordPlaceList.get(ordPlaceList.size() - 1);
        assertThat(testOrdPlace.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdPlace.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateOrdPlaceWithPatch() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();

        // Update the ordPlace using partial update
        OrdPlace partialUpdatedOrdPlace = new OrdPlace();
        partialUpdatedOrdPlace.setId(ordPlace.getId());

        partialUpdatedOrdPlace.href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);

        restOrdPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdPlace))
            )
            .andExpect(status().isOk());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
        OrdPlace testOrdPlace = ordPlaceList.get(ordPlaceList.size() - 1);
        assertThat(testOrdPlace.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdPlace.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdPlace() throws Exception {
        int databaseSizeBeforeUpdate = ordPlaceRepository.findAll().size();
        ordPlace.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdPlaceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordPlace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdPlace in the database
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdPlace() throws Exception {
        // Initialize the database
        ordPlaceRepository.saveAndFlush(ordPlace);

        int databaseSizeBeforeDelete = ordPlaceRepository.findAll().size();

        // Delete the ordPlace
        restOrdPlaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordPlace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdPlace> ordPlaceList = ordPlaceRepository.findAll();
        assertThat(ordPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

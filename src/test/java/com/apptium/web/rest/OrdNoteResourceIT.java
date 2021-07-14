package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdNote;
import com.apptium.repository.OrdNoteRepository;
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
 * Integration tests for the {@link OrdNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdNoteResourceIT {

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ord-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdNoteRepository ordNoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdNoteMockMvc;

    private OrdNote ordNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdNote createEntity(EntityManager em) {
        OrdNote ordNote = new OrdNote().author(DEFAULT_AUTHOR).text(DEFAULT_TEXT).createdDate(DEFAULT_CREATED_DATE);
        return ordNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdNote createUpdatedEntity(EntityManager em) {
        OrdNote ordNote = new OrdNote().author(UPDATED_AUTHOR).text(UPDATED_TEXT).createdDate(UPDATED_CREATED_DATE);
        return ordNote;
    }

    @BeforeEach
    public void initTest() {
        ordNote = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdNote() throws Exception {
        int databaseSizeBeforeCreate = ordNoteRepository.findAll().size();
        // Create the OrdNote
        restOrdNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordNote)))
            .andExpect(status().isCreated());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeCreate + 1);
        OrdNote testOrdNote = ordNoteList.get(ordNoteList.size() - 1);
        assertThat(testOrdNote.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testOrdNote.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testOrdNote.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createOrdNoteWithExistingId() throws Exception {
        // Create the OrdNote with an existing ID
        ordNote.setId(1L);

        int databaseSizeBeforeCreate = ordNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordNote)))
            .andExpect(status().isBadRequest());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdNotes() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        // Get all the ordNoteList
        restOrdNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrdNote() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        // Get the ordNote
        restOrdNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, ordNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordNote.getId().intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrdNote() throws Exception {
        // Get the ordNote
        restOrdNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdNote() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();

        // Update the ordNote
        OrdNote updatedOrdNote = ordNoteRepository.findById(ordNote.getId()).get();
        // Disconnect from session so that the updates on updatedOrdNote are not directly saved in db
        em.detach(updatedOrdNote);
        updatedOrdNote.author(UPDATED_AUTHOR).text(UPDATED_TEXT).createdDate(UPDATED_CREATED_DATE);

        restOrdNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdNote))
            )
            .andExpect(status().isOk());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
        OrdNote testOrdNote = ordNoteList.get(ordNoteList.size() - 1);
        assertThat(testOrdNote.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testOrdNote.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testOrdNote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdNoteWithPatch() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();

        // Update the ordNote using partial update
        OrdNote partialUpdatedOrdNote = new OrdNote();
        partialUpdatedOrdNote.setId(ordNote.getId());

        partialUpdatedOrdNote.text(UPDATED_TEXT).createdDate(UPDATED_CREATED_DATE);

        restOrdNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdNote))
            )
            .andExpect(status().isOk());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
        OrdNote testOrdNote = ordNoteList.get(ordNoteList.size() - 1);
        assertThat(testOrdNote.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testOrdNote.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testOrdNote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdNoteWithPatch() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();

        // Update the ordNote using partial update
        OrdNote partialUpdatedOrdNote = new OrdNote();
        partialUpdatedOrdNote.setId(ordNote.getId());

        partialUpdatedOrdNote.author(UPDATED_AUTHOR).text(UPDATED_TEXT).createdDate(UPDATED_CREATED_DATE);

        restOrdNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdNote))
            )
            .andExpect(status().isOk());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
        OrdNote testOrdNote = ordNoteList.get(ordNoteList.size() - 1);
        assertThat(testOrdNote.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testOrdNote.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testOrdNote.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdNote() throws Exception {
        int databaseSizeBeforeUpdate = ordNoteRepository.findAll().size();
        ordNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdNoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdNote in the database
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdNote() throws Exception {
        // Initialize the database
        ordNoteRepository.saveAndFlush(ordNote);

        int databaseSizeBeforeDelete = ordNoteRepository.findAll().size();

        // Delete the ordNote
        restOrdNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdNote> ordNoteList = ordNoteRepository.findAll();
        assertThat(ordNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.apptium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apptium.IntegrationTest;
import com.apptium.domain.OrdChannel;
import com.apptium.repository.OrdChannelRepository;
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
 * Integration tests for the {@link OrdChannelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdChannelResourceIT {

    private static final String DEFAULT_HREF = "AAAAAAAAAA";
    private static final String UPDATED_HREF = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ord-channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdChannelRepository ordChannelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdChannelMockMvc;

    private OrdChannel ordChannel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdChannel createEntity(EntityManager em) {
        OrdChannel ordChannel = new OrdChannel().href(DEFAULT_HREF).name(DEFAULT_NAME).role(DEFAULT_ROLE);
        return ordChannel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdChannel createUpdatedEntity(EntityManager em) {
        OrdChannel ordChannel = new OrdChannel().href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);
        return ordChannel;
    }

    @BeforeEach
    public void initTest() {
        ordChannel = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdChannel() throws Exception {
        int databaseSizeBeforeCreate = ordChannelRepository.findAll().size();
        // Create the OrdChannel
        restOrdChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordChannel)))
            .andExpect(status().isCreated());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeCreate + 1);
        OrdChannel testOrdChannel = ordChannelList.get(ordChannelList.size() - 1);
        assertThat(testOrdChannel.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrdChannel.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createOrdChannelWithExistingId() throws Exception {
        // Create the OrdChannel with an existing ID
        ordChannel.setId(1L);

        int databaseSizeBeforeCreate = ordChannelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordChannel)))
            .andExpect(status().isBadRequest());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdChannels() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        // Get all the ordChannelList
        restOrdChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].href").value(hasItem(DEFAULT_HREF)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getOrdChannel() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        // Get the ordChannel
        restOrdChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, ordChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordChannel.getId().intValue()))
            .andExpect(jsonPath("$.href").value(DEFAULT_HREF))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingOrdChannel() throws Exception {
        // Get the ordChannel
        restOrdChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdChannel() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();

        // Update the ordChannel
        OrdChannel updatedOrdChannel = ordChannelRepository.findById(ordChannel.getId()).get();
        // Disconnect from session so that the updates on updatedOrdChannel are not directly saved in db
        em.detach(updatedOrdChannel);
        updatedOrdChannel.href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);

        restOrdChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdChannel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdChannel))
            )
            .andExpect(status().isOk());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
        OrdChannel testOrdChannel = ordChannelList.get(ordChannelList.size() - 1);
        assertThat(testOrdChannel.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdChannel.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordChannel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordChannel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdChannelWithPatch() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();

        // Update the ordChannel using partial update
        OrdChannel partialUpdatedOrdChannel = new OrdChannel();
        partialUpdatedOrdChannel.setId(ordChannel.getId());

        partialUpdatedOrdChannel.name(UPDATED_NAME);

        restOrdChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdChannel))
            )
            .andExpect(status().isOk());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
        OrdChannel testOrdChannel = ordChannelList.get(ordChannelList.size() - 1);
        assertThat(testOrdChannel.getHref()).isEqualTo(DEFAULT_HREF);
        assertThat(testOrdChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdChannel.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateOrdChannelWithPatch() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();

        // Update the ordChannel using partial update
        OrdChannel partialUpdatedOrdChannel = new OrdChannel();
        partialUpdatedOrdChannel.setId(ordChannel.getId());

        partialUpdatedOrdChannel.href(UPDATED_HREF).name(UPDATED_NAME).role(UPDATED_ROLE);

        restOrdChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdChannel))
            )
            .andExpect(status().isOk());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
        OrdChannel testOrdChannel = ordChannelList.get(ordChannelList.size() - 1);
        assertThat(testOrdChannel.getHref()).isEqualTo(UPDATED_HREF);
        assertThat(testOrdChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrdChannel.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordChannel))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdChannel() throws Exception {
        int databaseSizeBeforeUpdate = ordChannelRepository.findAll().size();
        ordChannel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdChannelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordChannel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdChannel in the database
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdChannel() throws Exception {
        // Initialize the database
        ordChannelRepository.saveAndFlush(ordChannel);

        int databaseSizeBeforeDelete = ordChannelRepository.findAll().size();

        // Delete the ordChannel
        restOrdChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordChannel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdChannel> ordChannelList = ordChannelRepository.findAll();
        assertThat(ordChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdChannel;
import com.apptium.repository.OrdChannelRepository;
import com.apptium.service.OrdChannelService;
import com.apptium.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.apptium.domain.OrdChannel}.
 */
@RestController
@RequestMapping("/api")
public class OrdChannelResource {

    private final Logger log = LoggerFactory.getLogger(OrdChannelResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdChannel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdChannelService ordChannelService;

    private final OrdChannelRepository ordChannelRepository;

    public OrdChannelResource(OrdChannelService ordChannelService, OrdChannelRepository ordChannelRepository) {
        this.ordChannelService = ordChannelService;
        this.ordChannelRepository = ordChannelRepository;
    }

    /**
     * {@code POST  /ord-channels} : Create a new ordChannel.
     *
     * @param ordChannel the ordChannel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordChannel, or with status {@code 400 (Bad Request)} if the ordChannel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-channels")
    public ResponseEntity<OrdChannel> createOrdChannel(@RequestBody OrdChannel ordChannel) throws URISyntaxException {
        log.debug("REST request to save OrdChannel : {}", ordChannel);
        if (ordChannel.getId() != null) {
            throw new BadRequestAlertException("A new ordChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdChannel result = ordChannelService.save(ordChannel);
        return ResponseEntity
            .created(new URI("/api/ord-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-channels/:id} : Updates an existing ordChannel.
     *
     * @param id the id of the ordChannel to save.
     * @param ordChannel the ordChannel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordChannel,
     * or with status {@code 400 (Bad Request)} if the ordChannel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordChannel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-channels/{id}")
    public ResponseEntity<OrdChannel> updateOrdChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdChannel ordChannel
    ) throws URISyntaxException {
        log.debug("REST request to update OrdChannel : {}, {}", id, ordChannel);
        if (ordChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordChannel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdChannel result = ordChannelService.save(ordChannel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordChannel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-channels/:id} : Partial updates given fields of an existing ordChannel, field will ignore if it is null
     *
     * @param id the id of the ordChannel to save.
     * @param ordChannel the ordChannel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordChannel,
     * or with status {@code 400 (Bad Request)} if the ordChannel is not valid,
     * or with status {@code 404 (Not Found)} if the ordChannel is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordChannel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-channels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdChannel> partialUpdateOrdChannel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdChannel ordChannel
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdChannel partially : {}, {}", id, ordChannel);
        if (ordChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordChannel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordChannelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdChannel> result = ordChannelService.partialUpdate(ordChannel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordChannel.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-channels} : get all the ordChannels.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordChannels in body.
     */
    @GetMapping("/ord-channels")
    public List<OrdChannel> getAllOrdChannels(@RequestParam(required = false) String filter) {
        if ("ordproductorder-is-null".equals(filter)) {
            log.debug("REST request to get all OrdChannels where ordProductOrder is null");
            return ordChannelService.findAllWhereOrdProductOrderIsNull();
        }
        log.debug("REST request to get all OrdChannels");
        return ordChannelService.findAll();
    }

    /**
     * {@code GET  /ord-channels/:id} : get the "id" ordChannel.
     *
     * @param id the id of the ordChannel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordChannel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-channels/{id}")
    public ResponseEntity<OrdChannel> getOrdChannel(@PathVariable Long id) {
        log.debug("REST request to get OrdChannel : {}", id);
        Optional<OrdChannel> ordChannel = ordChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordChannel);
    }

    /**
     * {@code DELETE  /ord-channels/:id} : delete the "id" ordChannel.
     *
     * @param id the id of the ordChannel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-channels/{id}")
    public ResponseEntity<Void> deleteOrdChannel(@PathVariable Long id) {
        log.debug("REST request to delete OrdChannel : {}", id);
        ordChannelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

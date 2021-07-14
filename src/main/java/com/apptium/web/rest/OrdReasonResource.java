package com.apptium.web.rest;

import com.apptium.domain.OrdReason;
import com.apptium.repository.OrdReasonRepository;
import com.apptium.service.OrdReasonService;
import com.apptium.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.apptium.domain.OrdReason}.
 */
@RestController
@RequestMapping("/api")
public class OrdReasonResource {

    private final Logger log = LoggerFactory.getLogger(OrdReasonResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdReasonService ordReasonService;

    private final OrdReasonRepository ordReasonRepository;

    public OrdReasonResource(OrdReasonService ordReasonService, OrdReasonRepository ordReasonRepository) {
        this.ordReasonService = ordReasonService;
        this.ordReasonRepository = ordReasonRepository;
    }

    /**
     * {@code POST  /ord-reasons} : Create a new ordReason.
     *
     * @param ordReason the ordReason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordReason, or with status {@code 400 (Bad Request)} if the ordReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-reasons")
    public ResponseEntity<OrdReason> createOrdReason(@RequestBody OrdReason ordReason) throws URISyntaxException {
        log.debug("REST request to save OrdReason : {}", ordReason);
        if (ordReason.getId() != null) {
            throw new BadRequestAlertException("A new ordReason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdReason result = ordReasonService.save(ordReason);
        return ResponseEntity
            .created(new URI("/api/ord-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-reasons/:id} : Updates an existing ordReason.
     *
     * @param id the id of the ordReason to save.
     * @param ordReason the ordReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordReason,
     * or with status {@code 400 (Bad Request)} if the ordReason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-reasons/{id}")
    public ResponseEntity<OrdReason> updateOrdReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdReason ordReason
    ) throws URISyntaxException {
        log.debug("REST request to update OrdReason : {}, {}", id, ordReason);
        if (ordReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdReason result = ordReasonService.save(ordReason);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordReason.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-reasons/:id} : Partial updates given fields of an existing ordReason, field will ignore if it is null
     *
     * @param id the id of the ordReason to save.
     * @param ordReason the ordReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordReason,
     * or with status {@code 400 (Bad Request)} if the ordReason is not valid,
     * or with status {@code 404 (Not Found)} if the ordReason is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-reasons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdReason> partialUpdateOrdReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdReason ordReason
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdReason partially : {}, {}", id, ordReason);
        if (ordReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdReason> result = ordReasonService.partialUpdate(ordReason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordReason.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-reasons} : get all the ordReasons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordReasons in body.
     */
    @GetMapping("/ord-reasons")
    public List<OrdReason> getAllOrdReasons() {
        log.debug("REST request to get all OrdReasons");
        return ordReasonService.findAll();
    }

    /**
     * {@code GET  /ord-reasons/:id} : get the "id" ordReason.
     *
     * @param id the id of the ordReason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordReason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-reasons/{id}")
    public ResponseEntity<OrdReason> getOrdReason(@PathVariable Long id) {
        log.debug("REST request to get OrdReason : {}", id);
        Optional<OrdReason> ordReason = ordReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordReason);
    }

    /**
     * {@code DELETE  /ord-reasons/:id} : delete the "id" ordReason.
     *
     * @param id the id of the ordReason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-reasons/{id}")
    public ResponseEntity<Void> deleteOrdReason(@PathVariable Long id) {
        log.debug("REST request to delete OrdReason : {}", id);
        ordReasonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

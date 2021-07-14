package com.apptium.web.rest;

import com.apptium.domain.OrdAcquisitionChar;
import com.apptium.repository.OrdAcquisitionCharRepository;
import com.apptium.service.OrdAcquisitionCharService;
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
 * REST controller for managing {@link com.apptium.domain.OrdAcquisitionChar}.
 */
@RestController
@RequestMapping("/api")
public class OrdAcquisitionCharResource {

    private final Logger log = LoggerFactory.getLogger(OrdAcquisitionCharResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdAcquisitionChar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdAcquisitionCharService ordAcquisitionCharService;

    private final OrdAcquisitionCharRepository ordAcquisitionCharRepository;

    public OrdAcquisitionCharResource(
        OrdAcquisitionCharService ordAcquisitionCharService,
        OrdAcquisitionCharRepository ordAcquisitionCharRepository
    ) {
        this.ordAcquisitionCharService = ordAcquisitionCharService;
        this.ordAcquisitionCharRepository = ordAcquisitionCharRepository;
    }

    /**
     * {@code POST  /ord-acquisition-chars} : Create a new ordAcquisitionChar.
     *
     * @param ordAcquisitionChar the ordAcquisitionChar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordAcquisitionChar, or with status {@code 400 (Bad Request)} if the ordAcquisitionChar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-acquisition-chars")
    public ResponseEntity<OrdAcquisitionChar> createOrdAcquisitionChar(@RequestBody OrdAcquisitionChar ordAcquisitionChar)
        throws URISyntaxException {
        log.debug("REST request to save OrdAcquisitionChar : {}", ordAcquisitionChar);
        if (ordAcquisitionChar.getId() != null) {
            throw new BadRequestAlertException("A new ordAcquisitionChar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdAcquisitionChar result = ordAcquisitionCharService.save(ordAcquisitionChar);
        return ResponseEntity
            .created(new URI("/api/ord-acquisition-chars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-acquisition-chars/:id} : Updates an existing ordAcquisitionChar.
     *
     * @param id the id of the ordAcquisitionChar to save.
     * @param ordAcquisitionChar the ordAcquisitionChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordAcquisitionChar,
     * or with status {@code 400 (Bad Request)} if the ordAcquisitionChar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordAcquisitionChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-acquisition-chars/{id}")
    public ResponseEntity<OrdAcquisitionChar> updateOrdAcquisitionChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdAcquisitionChar ordAcquisitionChar
    ) throws URISyntaxException {
        log.debug("REST request to update OrdAcquisitionChar : {}, {}", id, ordAcquisitionChar);
        if (ordAcquisitionChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordAcquisitionChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordAcquisitionCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdAcquisitionChar result = ordAcquisitionCharService.save(ordAcquisitionChar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordAcquisitionChar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-acquisition-chars/:id} : Partial updates given fields of an existing ordAcquisitionChar, field will ignore if it is null
     *
     * @param id the id of the ordAcquisitionChar to save.
     * @param ordAcquisitionChar the ordAcquisitionChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordAcquisitionChar,
     * or with status {@code 400 (Bad Request)} if the ordAcquisitionChar is not valid,
     * or with status {@code 404 (Not Found)} if the ordAcquisitionChar is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordAcquisitionChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-acquisition-chars/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdAcquisitionChar> partialUpdateOrdAcquisitionChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdAcquisitionChar ordAcquisitionChar
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdAcquisitionChar partially : {}, {}", id, ordAcquisitionChar);
        if (ordAcquisitionChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordAcquisitionChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordAcquisitionCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdAcquisitionChar> result = ordAcquisitionCharService.partialUpdate(ordAcquisitionChar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordAcquisitionChar.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-acquisition-chars} : get all the ordAcquisitionChars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordAcquisitionChars in body.
     */
    @GetMapping("/ord-acquisition-chars")
    public List<OrdAcquisitionChar> getAllOrdAcquisitionChars() {
        log.debug("REST request to get all OrdAcquisitionChars");
        return ordAcquisitionCharService.findAll();
    }

    /**
     * {@code GET  /ord-acquisition-chars/:id} : get the "id" ordAcquisitionChar.
     *
     * @param id the id of the ordAcquisitionChar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordAcquisitionChar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-acquisition-chars/{id}")
    public ResponseEntity<OrdAcquisitionChar> getOrdAcquisitionChar(@PathVariable Long id) {
        log.debug("REST request to get OrdAcquisitionChar : {}", id);
        Optional<OrdAcquisitionChar> ordAcquisitionChar = ordAcquisitionCharService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordAcquisitionChar);
    }

    /**
     * {@code DELETE  /ord-acquisition-chars/:id} : delete the "id" ordAcquisitionChar.
     *
     * @param id the id of the ordAcquisitionChar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-acquisition-chars/{id}")
    public ResponseEntity<Void> deleteOrdAcquisitionChar(@PathVariable Long id) {
        log.debug("REST request to delete OrdAcquisitionChar : {}", id);
        ordAcquisitionCharService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

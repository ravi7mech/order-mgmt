package com.apptium.web.rest;

import com.apptium.domain.OrdFulfillmentChar;
import com.apptium.repository.OrdFulfillmentCharRepository;
import com.apptium.service.OrdFulfillmentCharService;
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
 * REST controller for managing {@link com.apptium.domain.OrdFulfillmentChar}.
 */
@RestController
@RequestMapping("/api")
public class OrdFulfillmentCharResource {

    private final Logger log = LoggerFactory.getLogger(OrdFulfillmentCharResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdFulfillmentChar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdFulfillmentCharService ordFulfillmentCharService;

    private final OrdFulfillmentCharRepository ordFulfillmentCharRepository;

    public OrdFulfillmentCharResource(
        OrdFulfillmentCharService ordFulfillmentCharService,
        OrdFulfillmentCharRepository ordFulfillmentCharRepository
    ) {
        this.ordFulfillmentCharService = ordFulfillmentCharService;
        this.ordFulfillmentCharRepository = ordFulfillmentCharRepository;
    }

    /**
     * {@code POST  /ord-fulfillment-chars} : Create a new ordFulfillmentChar.
     *
     * @param ordFulfillmentChar the ordFulfillmentChar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordFulfillmentChar, or with status {@code 400 (Bad Request)} if the ordFulfillmentChar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-fulfillment-chars")
    public ResponseEntity<OrdFulfillmentChar> createOrdFulfillmentChar(@RequestBody OrdFulfillmentChar ordFulfillmentChar)
        throws URISyntaxException {
        log.debug("REST request to save OrdFulfillmentChar : {}", ordFulfillmentChar);
        if (ordFulfillmentChar.getId() != null) {
            throw new BadRequestAlertException("A new ordFulfillmentChar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdFulfillmentChar result = ordFulfillmentCharService.save(ordFulfillmentChar);
        return ResponseEntity
            .created(new URI("/api/ord-fulfillment-chars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-fulfillment-chars/:id} : Updates an existing ordFulfillmentChar.
     *
     * @param id the id of the ordFulfillmentChar to save.
     * @param ordFulfillmentChar the ordFulfillmentChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordFulfillmentChar,
     * or with status {@code 400 (Bad Request)} if the ordFulfillmentChar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordFulfillmentChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-fulfillment-chars/{id}")
    public ResponseEntity<OrdFulfillmentChar> updateOrdFulfillmentChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdFulfillmentChar ordFulfillmentChar
    ) throws URISyntaxException {
        log.debug("REST request to update OrdFulfillmentChar : {}, {}", id, ordFulfillmentChar);
        if (ordFulfillmentChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordFulfillmentChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordFulfillmentCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdFulfillmentChar result = ordFulfillmentCharService.save(ordFulfillmentChar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordFulfillmentChar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-fulfillment-chars/:id} : Partial updates given fields of an existing ordFulfillmentChar, field will ignore if it is null
     *
     * @param id the id of the ordFulfillmentChar to save.
     * @param ordFulfillmentChar the ordFulfillmentChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordFulfillmentChar,
     * or with status {@code 400 (Bad Request)} if the ordFulfillmentChar is not valid,
     * or with status {@code 404 (Not Found)} if the ordFulfillmentChar is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordFulfillmentChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-fulfillment-chars/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdFulfillmentChar> partialUpdateOrdFulfillmentChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdFulfillmentChar ordFulfillmentChar
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdFulfillmentChar partially : {}, {}", id, ordFulfillmentChar);
        if (ordFulfillmentChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordFulfillmentChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordFulfillmentCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdFulfillmentChar> result = ordFulfillmentCharService.partialUpdate(ordFulfillmentChar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordFulfillmentChar.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-fulfillment-chars} : get all the ordFulfillmentChars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordFulfillmentChars in body.
     */
    @GetMapping("/ord-fulfillment-chars")
    public List<OrdFulfillmentChar> getAllOrdFulfillmentChars() {
        log.debug("REST request to get all OrdFulfillmentChars");
        return ordFulfillmentCharService.findAll();
    }

    /**
     * {@code GET  /ord-fulfillment-chars/:id} : get the "id" ordFulfillmentChar.
     *
     * @param id the id of the ordFulfillmentChar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordFulfillmentChar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-fulfillment-chars/{id}")
    public ResponseEntity<OrdFulfillmentChar> getOrdFulfillmentChar(@PathVariable Long id) {
        log.debug("REST request to get OrdFulfillmentChar : {}", id);
        Optional<OrdFulfillmentChar> ordFulfillmentChar = ordFulfillmentCharService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordFulfillmentChar);
    }

    /**
     * {@code DELETE  /ord-fulfillment-chars/:id} : delete the "id" ordFulfillmentChar.
     *
     * @param id the id of the ordFulfillmentChar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-fulfillment-chars/{id}")
    public ResponseEntity<Void> deleteOrdFulfillmentChar(@PathVariable Long id) {
        log.debug("REST request to delete OrdFulfillmentChar : {}", id);
        ordFulfillmentCharService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

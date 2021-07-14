package com.apptium.web.rest;

import com.apptium.domain.OrdOrderItemChar;
import com.apptium.repository.OrdOrderItemCharRepository;
import com.apptium.service.OrdOrderItemCharService;
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
 * REST controller for managing {@link com.apptium.domain.OrdOrderItemChar}.
 */
@RestController
@RequestMapping("/api")
public class OrdOrderItemCharResource {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemCharResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdOrderItemChar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdOrderItemCharService ordOrderItemCharService;

    private final OrdOrderItemCharRepository ordOrderItemCharRepository;

    public OrdOrderItemCharResource(
        OrdOrderItemCharService ordOrderItemCharService,
        OrdOrderItemCharRepository ordOrderItemCharRepository
    ) {
        this.ordOrderItemCharService = ordOrderItemCharService;
        this.ordOrderItemCharRepository = ordOrderItemCharRepository;
    }

    /**
     * {@code POST  /ord-order-item-chars} : Create a new ordOrderItemChar.
     *
     * @param ordOrderItemChar the ordOrderItemChar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordOrderItemChar, or with status {@code 400 (Bad Request)} if the ordOrderItemChar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-order-item-chars")
    public ResponseEntity<OrdOrderItemChar> createOrdOrderItemChar(@RequestBody OrdOrderItemChar ordOrderItemChar)
        throws URISyntaxException {
        log.debug("REST request to save OrdOrderItemChar : {}", ordOrderItemChar);
        if (ordOrderItemChar.getId() != null) {
            throw new BadRequestAlertException("A new ordOrderItemChar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdOrderItemChar result = ordOrderItemCharService.save(ordOrderItemChar);
        return ResponseEntity
            .created(new URI("/api/ord-order-item-chars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-order-item-chars/:id} : Updates an existing ordOrderItemChar.
     *
     * @param id the id of the ordOrderItemChar to save.
     * @param ordOrderItemChar the ordOrderItemChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemChar,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemChar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-order-item-chars/{id}")
    public ResponseEntity<OrdOrderItemChar> updateOrdOrderItemChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemChar ordOrderItemChar
    ) throws URISyntaxException {
        log.debug("REST request to update OrdOrderItemChar : {}, {}", id, ordOrderItemChar);
        if (ordOrderItemChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdOrderItemChar result = ordOrderItemCharService.save(ordOrderItemChar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemChar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-order-item-chars/:id} : Partial updates given fields of an existing ordOrderItemChar, field will ignore if it is null
     *
     * @param id the id of the ordOrderItemChar to save.
     * @param ordOrderItemChar the ordOrderItemChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemChar,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemChar is not valid,
     * or with status {@code 404 (Not Found)} if the ordOrderItemChar is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-order-item-chars/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdOrderItemChar> partialUpdateOrdOrderItemChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemChar ordOrderItemChar
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdOrderItemChar partially : {}, {}", id, ordOrderItemChar);
        if (ordOrderItemChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdOrderItemChar> result = ordOrderItemCharService.partialUpdate(ordOrderItemChar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemChar.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-order-item-chars} : get all the ordOrderItemChars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordOrderItemChars in body.
     */
    @GetMapping("/ord-order-item-chars")
    public List<OrdOrderItemChar> getAllOrdOrderItemChars() {
        log.debug("REST request to get all OrdOrderItemChars");
        return ordOrderItemCharService.findAll();
    }

    /**
     * {@code GET  /ord-order-item-chars/:id} : get the "id" ordOrderItemChar.
     *
     * @param id the id of the ordOrderItemChar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordOrderItemChar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-order-item-chars/{id}")
    public ResponseEntity<OrdOrderItemChar> getOrdOrderItemChar(@PathVariable Long id) {
        log.debug("REST request to get OrdOrderItemChar : {}", id);
        Optional<OrdOrderItemChar> ordOrderItemChar = ordOrderItemCharService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordOrderItemChar);
    }

    /**
     * {@code DELETE  /ord-order-item-chars/:id} : delete the "id" ordOrderItemChar.
     *
     * @param id the id of the ordOrderItemChar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-order-item-chars/{id}")
    public ResponseEntity<Void> deleteOrdOrderItemChar(@PathVariable Long id) {
        log.debug("REST request to delete OrdOrderItemChar : {}", id);
        ordOrderItemCharService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdProvisiongChar;
import com.apptium.repository.OrdProvisiongCharRepository;
import com.apptium.service.OrdProvisiongCharService;
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
 * REST controller for managing {@link com.apptium.domain.OrdProvisiongChar}.
 */
@RestController
@RequestMapping("/api")
public class OrdProvisiongCharResource {

    private final Logger log = LoggerFactory.getLogger(OrdProvisiongCharResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdProvisiongChar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdProvisiongCharService ordProvisiongCharService;

    private final OrdProvisiongCharRepository ordProvisiongCharRepository;

    public OrdProvisiongCharResource(
        OrdProvisiongCharService ordProvisiongCharService,
        OrdProvisiongCharRepository ordProvisiongCharRepository
    ) {
        this.ordProvisiongCharService = ordProvisiongCharService;
        this.ordProvisiongCharRepository = ordProvisiongCharRepository;
    }

    /**
     * {@code POST  /ord-provisiong-chars} : Create a new ordProvisiongChar.
     *
     * @param ordProvisiongChar the ordProvisiongChar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordProvisiongChar, or with status {@code 400 (Bad Request)} if the ordProvisiongChar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-provisiong-chars")
    public ResponseEntity<OrdProvisiongChar> createOrdProvisiongChar(@RequestBody OrdProvisiongChar ordProvisiongChar)
        throws URISyntaxException {
        log.debug("REST request to save OrdProvisiongChar : {}", ordProvisiongChar);
        if (ordProvisiongChar.getId() != null) {
            throw new BadRequestAlertException("A new ordProvisiongChar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdProvisiongChar result = ordProvisiongCharService.save(ordProvisiongChar);
        return ResponseEntity
            .created(new URI("/api/ord-provisiong-chars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-provisiong-chars/:id} : Updates an existing ordProvisiongChar.
     *
     * @param id the id of the ordProvisiongChar to save.
     * @param ordProvisiongChar the ordProvisiongChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProvisiongChar,
     * or with status {@code 400 (Bad Request)} if the ordProvisiongChar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordProvisiongChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-provisiong-chars/{id}")
    public ResponseEntity<OrdProvisiongChar> updateOrdProvisiongChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProvisiongChar ordProvisiongChar
    ) throws URISyntaxException {
        log.debug("REST request to update OrdProvisiongChar : {}, {}", id, ordProvisiongChar);
        if (ordProvisiongChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProvisiongChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProvisiongCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdProvisiongChar result = ordProvisiongCharService.save(ordProvisiongChar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProvisiongChar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-provisiong-chars/:id} : Partial updates given fields of an existing ordProvisiongChar, field will ignore if it is null
     *
     * @param id the id of the ordProvisiongChar to save.
     * @param ordProvisiongChar the ordProvisiongChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProvisiongChar,
     * or with status {@code 400 (Bad Request)} if the ordProvisiongChar is not valid,
     * or with status {@code 404 (Not Found)} if the ordProvisiongChar is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordProvisiongChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-provisiong-chars/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdProvisiongChar> partialUpdateOrdProvisiongChar(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProvisiongChar ordProvisiongChar
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdProvisiongChar partially : {}, {}", id, ordProvisiongChar);
        if (ordProvisiongChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProvisiongChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProvisiongCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdProvisiongChar> result = ordProvisiongCharService.partialUpdate(ordProvisiongChar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProvisiongChar.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-provisiong-chars} : get all the ordProvisiongChars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordProvisiongChars in body.
     */
    @GetMapping("/ord-provisiong-chars")
    public List<OrdProvisiongChar> getAllOrdProvisiongChars() {
        log.debug("REST request to get all OrdProvisiongChars");
        return ordProvisiongCharService.findAll();
    }

    /**
     * {@code GET  /ord-provisiong-chars/:id} : get the "id" ordProvisiongChar.
     *
     * @param id the id of the ordProvisiongChar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordProvisiongChar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-provisiong-chars/{id}")
    public ResponseEntity<OrdProvisiongChar> getOrdProvisiongChar(@PathVariable Long id) {
        log.debug("REST request to get OrdProvisiongChar : {}", id);
        Optional<OrdProvisiongChar> ordProvisiongChar = ordProvisiongCharService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordProvisiongChar);
    }

    /**
     * {@code DELETE  /ord-provisiong-chars/:id} : delete the "id" ordProvisiongChar.
     *
     * @param id the id of the ordProvisiongChar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-provisiong-chars/{id}")
    public ResponseEntity<Void> deleteOrdProvisiongChar(@PathVariable Long id) {
        log.debug("REST request to delete OrdProvisiongChar : {}", id);
        ordProvisiongCharService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

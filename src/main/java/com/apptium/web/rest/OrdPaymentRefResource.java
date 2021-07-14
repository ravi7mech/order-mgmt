package com.apptium.web.rest;

import com.apptium.domain.OrdPaymentRef;
import com.apptium.repository.OrdPaymentRefRepository;
import com.apptium.service.OrdPaymentRefService;
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
 * REST controller for managing {@link com.apptium.domain.OrdPaymentRef}.
 */
@RestController
@RequestMapping("/api")
public class OrdPaymentRefResource {

    private final Logger log = LoggerFactory.getLogger(OrdPaymentRefResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdPaymentRef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdPaymentRefService ordPaymentRefService;

    private final OrdPaymentRefRepository ordPaymentRefRepository;

    public OrdPaymentRefResource(OrdPaymentRefService ordPaymentRefService, OrdPaymentRefRepository ordPaymentRefRepository) {
        this.ordPaymentRefService = ordPaymentRefService;
        this.ordPaymentRefRepository = ordPaymentRefRepository;
    }

    /**
     * {@code POST  /ord-payment-refs} : Create a new ordPaymentRef.
     *
     * @param ordPaymentRef the ordPaymentRef to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordPaymentRef, or with status {@code 400 (Bad Request)} if the ordPaymentRef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-payment-refs")
    public ResponseEntity<OrdPaymentRef> createOrdPaymentRef(@RequestBody OrdPaymentRef ordPaymentRef) throws URISyntaxException {
        log.debug("REST request to save OrdPaymentRef : {}", ordPaymentRef);
        if (ordPaymentRef.getId() != null) {
            throw new BadRequestAlertException("A new ordPaymentRef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdPaymentRef result = ordPaymentRefService.save(ordPaymentRef);
        return ResponseEntity
            .created(new URI("/api/ord-payment-refs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-payment-refs/:id} : Updates an existing ordPaymentRef.
     *
     * @param id the id of the ordPaymentRef to save.
     * @param ordPaymentRef the ordPaymentRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPaymentRef,
     * or with status {@code 400 (Bad Request)} if the ordPaymentRef is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordPaymentRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-payment-refs/{id}")
    public ResponseEntity<OrdPaymentRef> updateOrdPaymentRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPaymentRef ordPaymentRef
    ) throws URISyntaxException {
        log.debug("REST request to update OrdPaymentRef : {}, {}", id, ordPaymentRef);
        if (ordPaymentRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPaymentRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPaymentRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdPaymentRef result = ordPaymentRefService.save(ordPaymentRef);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPaymentRef.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-payment-refs/:id} : Partial updates given fields of an existing ordPaymentRef, field will ignore if it is null
     *
     * @param id the id of the ordPaymentRef to save.
     * @param ordPaymentRef the ordPaymentRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPaymentRef,
     * or with status {@code 400 (Bad Request)} if the ordPaymentRef is not valid,
     * or with status {@code 404 (Not Found)} if the ordPaymentRef is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordPaymentRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-payment-refs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdPaymentRef> partialUpdateOrdPaymentRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPaymentRef ordPaymentRef
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdPaymentRef partially : {}, {}", id, ordPaymentRef);
        if (ordPaymentRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPaymentRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPaymentRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdPaymentRef> result = ordPaymentRefService.partialUpdate(ordPaymentRef);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPaymentRef.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-payment-refs} : get all the ordPaymentRefs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordPaymentRefs in body.
     */
    @GetMapping("/ord-payment-refs")
    public List<OrdPaymentRef> getAllOrdPaymentRefs() {
        log.debug("REST request to get all OrdPaymentRefs");
        return ordPaymentRefService.findAll();
    }

    /**
     * {@code GET  /ord-payment-refs/:id} : get the "id" ordPaymentRef.
     *
     * @param id the id of the ordPaymentRef to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordPaymentRef, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-payment-refs/{id}")
    public ResponseEntity<OrdPaymentRef> getOrdPaymentRef(@PathVariable Long id) {
        log.debug("REST request to get OrdPaymentRef : {}", id);
        Optional<OrdPaymentRef> ordPaymentRef = ordPaymentRefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordPaymentRef);
    }

    /**
     * {@code DELETE  /ord-payment-refs/:id} : delete the "id" ordPaymentRef.
     *
     * @param id the id of the ordPaymentRef to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-payment-refs/{id}")
    public ResponseEntity<Void> deleteOrdPaymentRef(@PathVariable Long id) {
        log.debug("REST request to delete OrdPaymentRef : {}", id);
        ordPaymentRefService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

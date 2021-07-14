package com.apptium.web.rest;

import com.apptium.domain.OrdBillingAccountRef;
import com.apptium.repository.OrdBillingAccountRefRepository;
import com.apptium.service.OrdBillingAccountRefService;
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
 * REST controller for managing {@link com.apptium.domain.OrdBillingAccountRef}.
 */
@RestController
@RequestMapping("/api")
public class OrdBillingAccountRefResource {

    private final Logger log = LoggerFactory.getLogger(OrdBillingAccountRefResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdBillingAccountRef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdBillingAccountRefService ordBillingAccountRefService;

    private final OrdBillingAccountRefRepository ordBillingAccountRefRepository;

    public OrdBillingAccountRefResource(
        OrdBillingAccountRefService ordBillingAccountRefService,
        OrdBillingAccountRefRepository ordBillingAccountRefRepository
    ) {
        this.ordBillingAccountRefService = ordBillingAccountRefService;
        this.ordBillingAccountRefRepository = ordBillingAccountRefRepository;
    }

    /**
     * {@code POST  /ord-billing-account-refs} : Create a new ordBillingAccountRef.
     *
     * @param ordBillingAccountRef the ordBillingAccountRef to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordBillingAccountRef, or with status {@code 400 (Bad Request)} if the ordBillingAccountRef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-billing-account-refs")
    public ResponseEntity<OrdBillingAccountRef> createOrdBillingAccountRef(@RequestBody OrdBillingAccountRef ordBillingAccountRef)
        throws URISyntaxException {
        log.debug("REST request to save OrdBillingAccountRef : {}", ordBillingAccountRef);
        if (ordBillingAccountRef.getId() != null) {
            throw new BadRequestAlertException("A new ordBillingAccountRef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdBillingAccountRef result = ordBillingAccountRefService.save(ordBillingAccountRef);
        return ResponseEntity
            .created(new URI("/api/ord-billing-account-refs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-billing-account-refs/:id} : Updates an existing ordBillingAccountRef.
     *
     * @param id the id of the ordBillingAccountRef to save.
     * @param ordBillingAccountRef the ordBillingAccountRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordBillingAccountRef,
     * or with status {@code 400 (Bad Request)} if the ordBillingAccountRef is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordBillingAccountRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-billing-account-refs/{id}")
    public ResponseEntity<OrdBillingAccountRef> updateOrdBillingAccountRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdBillingAccountRef ordBillingAccountRef
    ) throws URISyntaxException {
        log.debug("REST request to update OrdBillingAccountRef : {}, {}", id, ordBillingAccountRef);
        if (ordBillingAccountRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordBillingAccountRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordBillingAccountRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdBillingAccountRef result = ordBillingAccountRefService.save(ordBillingAccountRef);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordBillingAccountRef.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-billing-account-refs/:id} : Partial updates given fields of an existing ordBillingAccountRef, field will ignore if it is null
     *
     * @param id the id of the ordBillingAccountRef to save.
     * @param ordBillingAccountRef the ordBillingAccountRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordBillingAccountRef,
     * or with status {@code 400 (Bad Request)} if the ordBillingAccountRef is not valid,
     * or with status {@code 404 (Not Found)} if the ordBillingAccountRef is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordBillingAccountRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-billing-account-refs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdBillingAccountRef> partialUpdateOrdBillingAccountRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdBillingAccountRef ordBillingAccountRef
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdBillingAccountRef partially : {}, {}", id, ordBillingAccountRef);
        if (ordBillingAccountRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordBillingAccountRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordBillingAccountRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdBillingAccountRef> result = ordBillingAccountRefService.partialUpdate(ordBillingAccountRef);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordBillingAccountRef.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-billing-account-refs} : get all the ordBillingAccountRefs.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordBillingAccountRefs in body.
     */
    @GetMapping("/ord-billing-account-refs")
    public List<OrdBillingAccountRef> getAllOrdBillingAccountRefs(@RequestParam(required = false) String filter) {
        if ("ordproductorder-is-null".equals(filter)) {
            log.debug("REST request to get all OrdBillingAccountRefs where ordProductOrder is null");
            return ordBillingAccountRefService.findAllWhereOrdProductOrderIsNull();
        }
        log.debug("REST request to get all OrdBillingAccountRefs");
        return ordBillingAccountRefService.findAll();
    }

    /**
     * {@code GET  /ord-billing-account-refs/:id} : get the "id" ordBillingAccountRef.
     *
     * @param id the id of the ordBillingAccountRef to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordBillingAccountRef, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-billing-account-refs/{id}")
    public ResponseEntity<OrdBillingAccountRef> getOrdBillingAccountRef(@PathVariable Long id) {
        log.debug("REST request to get OrdBillingAccountRef : {}", id);
        Optional<OrdBillingAccountRef> ordBillingAccountRef = ordBillingAccountRefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordBillingAccountRef);
    }

    /**
     * {@code DELETE  /ord-billing-account-refs/:id} : delete the "id" ordBillingAccountRef.
     *
     * @param id the id of the ordBillingAccountRef to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-billing-account-refs/{id}")
    public ResponseEntity<Void> deleteOrdBillingAccountRef(@PathVariable Long id) {
        log.debug("REST request to delete OrdBillingAccountRef : {}", id);
        ordBillingAccountRefService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdProductOfferingRef;
import com.apptium.repository.OrdProductOfferingRefRepository;
import com.apptium.service.OrdProductOfferingRefService;
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
 * REST controller for managing {@link com.apptium.domain.OrdProductOfferingRef}.
 */
@RestController
@RequestMapping("/api")
public class OrdProductOfferingRefResource {

    private final Logger log = LoggerFactory.getLogger(OrdProductOfferingRefResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdProductOfferingRef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdProductOfferingRefService ordProductOfferingRefService;

    private final OrdProductOfferingRefRepository ordProductOfferingRefRepository;

    public OrdProductOfferingRefResource(
        OrdProductOfferingRefService ordProductOfferingRefService,
        OrdProductOfferingRefRepository ordProductOfferingRefRepository
    ) {
        this.ordProductOfferingRefService = ordProductOfferingRefService;
        this.ordProductOfferingRefRepository = ordProductOfferingRefRepository;
    }

    /**
     * {@code POST  /ord-product-offering-refs} : Create a new ordProductOfferingRef.
     *
     * @param ordProductOfferingRef the ordProductOfferingRef to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordProductOfferingRef, or with status {@code 400 (Bad Request)} if the ordProductOfferingRef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-product-offering-refs")
    public ResponseEntity<OrdProductOfferingRef> createOrdProductOfferingRef(@RequestBody OrdProductOfferingRef ordProductOfferingRef)
        throws URISyntaxException {
        log.debug("REST request to save OrdProductOfferingRef : {}", ordProductOfferingRef);
        if (ordProductOfferingRef.getId() != null) {
            throw new BadRequestAlertException("A new ordProductOfferingRef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdProductOfferingRef result = ordProductOfferingRefService.save(ordProductOfferingRef);
        return ResponseEntity
            .created(new URI("/api/ord-product-offering-refs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-product-offering-refs/:id} : Updates an existing ordProductOfferingRef.
     *
     * @param id the id of the ordProductOfferingRef to save.
     * @param ordProductOfferingRef the ordProductOfferingRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductOfferingRef,
     * or with status {@code 400 (Bad Request)} if the ordProductOfferingRef is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordProductOfferingRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-product-offering-refs/{id}")
    public ResponseEntity<OrdProductOfferingRef> updateOrdProductOfferingRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductOfferingRef ordProductOfferingRef
    ) throws URISyntaxException {
        log.debug("REST request to update OrdProductOfferingRef : {}, {}", id, ordProductOfferingRef);
        if (ordProductOfferingRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductOfferingRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductOfferingRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdProductOfferingRef result = ordProductOfferingRefService.save(ordProductOfferingRef);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductOfferingRef.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-product-offering-refs/:id} : Partial updates given fields of an existing ordProductOfferingRef, field will ignore if it is null
     *
     * @param id the id of the ordProductOfferingRef to save.
     * @param ordProductOfferingRef the ordProductOfferingRef to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductOfferingRef,
     * or with status {@code 400 (Bad Request)} if the ordProductOfferingRef is not valid,
     * or with status {@code 404 (Not Found)} if the ordProductOfferingRef is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordProductOfferingRef couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-product-offering-refs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdProductOfferingRef> partialUpdateOrdProductOfferingRef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductOfferingRef ordProductOfferingRef
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdProductOfferingRef partially : {}, {}", id, ordProductOfferingRef);
        if (ordProductOfferingRef.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductOfferingRef.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductOfferingRefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdProductOfferingRef> result = ordProductOfferingRefService.partialUpdate(ordProductOfferingRef);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductOfferingRef.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-product-offering-refs} : get all the ordProductOfferingRefs.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordProductOfferingRefs in body.
     */
    @GetMapping("/ord-product-offering-refs")
    public List<OrdProductOfferingRef> getAllOrdProductOfferingRefs(@RequestParam(required = false) String filter) {
        if ("ordorderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrdProductOfferingRefs where ordOrderItem is null");
            return ordProductOfferingRefService.findAllWhereOrdOrderItemIsNull();
        }
        log.debug("REST request to get all OrdProductOfferingRefs");
        return ordProductOfferingRefService.findAll();
    }

    /**
     * {@code GET  /ord-product-offering-refs/:id} : get the "id" ordProductOfferingRef.
     *
     * @param id the id of the ordProductOfferingRef to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordProductOfferingRef, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-product-offering-refs/{id}")
    public ResponseEntity<OrdProductOfferingRef> getOrdProductOfferingRef(@PathVariable Long id) {
        log.debug("REST request to get OrdProductOfferingRef : {}", id);
        Optional<OrdProductOfferingRef> ordProductOfferingRef = ordProductOfferingRefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordProductOfferingRef);
    }

    /**
     * {@code DELETE  /ord-product-offering-refs/:id} : delete the "id" ordProductOfferingRef.
     *
     * @param id the id of the ordProductOfferingRef to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-product-offering-refs/{id}")
    public ResponseEntity<Void> deleteOrdProductOfferingRef(@PathVariable Long id) {
        log.debug("REST request to delete OrdProductOfferingRef : {}", id);
        ordProductOfferingRefService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

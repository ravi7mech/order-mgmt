package com.apptium.web.rest;

import com.apptium.domain.OrdFulfillment;
import com.apptium.repository.OrdFulfillmentRepository;
import com.apptium.service.OrdFulfillmentService;
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
 * REST controller for managing {@link com.apptium.domain.OrdFulfillment}.
 */
@RestController
@RequestMapping("/api")
public class OrdFulfillmentResource {

    private final Logger log = LoggerFactory.getLogger(OrdFulfillmentResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdFulfillment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdFulfillmentService ordFulfillmentService;

    private final OrdFulfillmentRepository ordFulfillmentRepository;

    public OrdFulfillmentResource(OrdFulfillmentService ordFulfillmentService, OrdFulfillmentRepository ordFulfillmentRepository) {
        this.ordFulfillmentService = ordFulfillmentService;
        this.ordFulfillmentRepository = ordFulfillmentRepository;
    }

    /**
     * {@code POST  /ord-fulfillments} : Create a new ordFulfillment.
     *
     * @param ordFulfillment the ordFulfillment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordFulfillment, or with status {@code 400 (Bad Request)} if the ordFulfillment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-fulfillments")
    public ResponseEntity<OrdFulfillment> createOrdFulfillment(@RequestBody OrdFulfillment ordFulfillment) throws URISyntaxException {
        log.debug("REST request to save OrdFulfillment : {}", ordFulfillment);
        if (ordFulfillment.getId() != null) {
            throw new BadRequestAlertException("A new ordFulfillment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdFulfillment result = ordFulfillmentService.save(ordFulfillment);
        return ResponseEntity
            .created(new URI("/api/ord-fulfillments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-fulfillments/:id} : Updates an existing ordFulfillment.
     *
     * @param id the id of the ordFulfillment to save.
     * @param ordFulfillment the ordFulfillment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordFulfillment,
     * or with status {@code 400 (Bad Request)} if the ordFulfillment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordFulfillment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-fulfillments/{id}")
    public ResponseEntity<OrdFulfillment> updateOrdFulfillment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdFulfillment ordFulfillment
    ) throws URISyntaxException {
        log.debug("REST request to update OrdFulfillment : {}, {}", id, ordFulfillment);
        if (ordFulfillment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordFulfillment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordFulfillmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdFulfillment result = ordFulfillmentService.save(ordFulfillment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordFulfillment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-fulfillments/:id} : Partial updates given fields of an existing ordFulfillment, field will ignore if it is null
     *
     * @param id the id of the ordFulfillment to save.
     * @param ordFulfillment the ordFulfillment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordFulfillment,
     * or with status {@code 400 (Bad Request)} if the ordFulfillment is not valid,
     * or with status {@code 404 (Not Found)} if the ordFulfillment is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordFulfillment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-fulfillments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdFulfillment> partialUpdateOrdFulfillment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdFulfillment ordFulfillment
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdFulfillment partially : {}, {}", id, ordFulfillment);
        if (ordFulfillment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordFulfillment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordFulfillmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdFulfillment> result = ordFulfillmentService.partialUpdate(ordFulfillment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordFulfillment.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-fulfillments} : get all the ordFulfillments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordFulfillments in body.
     */
    @GetMapping("/ord-fulfillments")
    public List<OrdFulfillment> getAllOrdFulfillments() {
        log.debug("REST request to get all OrdFulfillments");
        return ordFulfillmentService.findAll();
    }

    /**
     * {@code GET  /ord-fulfillments/:id} : get the "id" ordFulfillment.
     *
     * @param id the id of the ordFulfillment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordFulfillment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-fulfillments/{id}")
    public ResponseEntity<OrdFulfillment> getOrdFulfillment(@PathVariable Long id) {
        log.debug("REST request to get OrdFulfillment : {}", id);
        Optional<OrdFulfillment> ordFulfillment = ordFulfillmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordFulfillment);
    }

    /**
     * {@code DELETE  /ord-fulfillments/:id} : delete the "id" ordFulfillment.
     *
     * @param id the id of the ordFulfillment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-fulfillments/{id}")
    public ResponseEntity<Void> deleteOrdFulfillment(@PathVariable Long id) {
        log.debug("REST request to delete OrdFulfillment : {}", id);
        ordFulfillmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

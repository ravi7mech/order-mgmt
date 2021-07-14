package com.apptium.web.rest;

import com.apptium.domain.OrdOrderItemRelationship;
import com.apptium.repository.OrdOrderItemRelationshipRepository;
import com.apptium.service.OrdOrderItemRelationshipService;
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
 * REST controller for managing {@link com.apptium.domain.OrdOrderItemRelationship}.
 */
@RestController
@RequestMapping("/api")
public class OrdOrderItemRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemRelationshipResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdOrderItemRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdOrderItemRelationshipService ordOrderItemRelationshipService;

    private final OrdOrderItemRelationshipRepository ordOrderItemRelationshipRepository;

    public OrdOrderItemRelationshipResource(
        OrdOrderItemRelationshipService ordOrderItemRelationshipService,
        OrdOrderItemRelationshipRepository ordOrderItemRelationshipRepository
    ) {
        this.ordOrderItemRelationshipService = ordOrderItemRelationshipService;
        this.ordOrderItemRelationshipRepository = ordOrderItemRelationshipRepository;
    }

    /**
     * {@code POST  /ord-order-item-relationships} : Create a new ordOrderItemRelationship.
     *
     * @param ordOrderItemRelationship the ordOrderItemRelationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordOrderItemRelationship, or with status {@code 400 (Bad Request)} if the ordOrderItemRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-order-item-relationships")
    public ResponseEntity<OrdOrderItemRelationship> createOrdOrderItemRelationship(
        @RequestBody OrdOrderItemRelationship ordOrderItemRelationship
    ) throws URISyntaxException {
        log.debug("REST request to save OrdOrderItemRelationship : {}", ordOrderItemRelationship);
        if (ordOrderItemRelationship.getId() != null) {
            throw new BadRequestAlertException("A new ordOrderItemRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdOrderItemRelationship result = ordOrderItemRelationshipService.save(ordOrderItemRelationship);
        return ResponseEntity
            .created(new URI("/api/ord-order-item-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-order-item-relationships/:id} : Updates an existing ordOrderItemRelationship.
     *
     * @param id the id of the ordOrderItemRelationship to save.
     * @param ordOrderItemRelationship the ordOrderItemRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemRelationship,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemRelationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-order-item-relationships/{id}")
    public ResponseEntity<OrdOrderItemRelationship> updateOrdOrderItemRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemRelationship ordOrderItemRelationship
    ) throws URISyntaxException {
        log.debug("REST request to update OrdOrderItemRelationship : {}, {}", id, ordOrderItemRelationship);
        if (ordOrderItemRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdOrderItemRelationship result = ordOrderItemRelationshipService.save(ordOrderItemRelationship);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemRelationship.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-order-item-relationships/:id} : Partial updates given fields of an existing ordOrderItemRelationship, field will ignore if it is null
     *
     * @param id the id of the ordOrderItemRelationship to save.
     * @param ordOrderItemRelationship the ordOrderItemRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemRelationship,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemRelationship is not valid,
     * or with status {@code 404 (Not Found)} if the ordOrderItemRelationship is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-order-item-relationships/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdOrderItemRelationship> partialUpdateOrdOrderItemRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemRelationship ordOrderItemRelationship
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdOrderItemRelationship partially : {}, {}", id, ordOrderItemRelationship);
        if (ordOrderItemRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdOrderItemRelationship> result = ordOrderItemRelationshipService.partialUpdate(ordOrderItemRelationship);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemRelationship.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-order-item-relationships} : get all the ordOrderItemRelationships.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordOrderItemRelationships in body.
     */
    @GetMapping("/ord-order-item-relationships")
    public List<OrdOrderItemRelationship> getAllOrdOrderItemRelationships(@RequestParam(required = false) String filter) {
        if ("ordorderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrdOrderItemRelationships where ordOrderItem is null");
            return ordOrderItemRelationshipService.findAllWhereOrdOrderItemIsNull();
        }
        log.debug("REST request to get all OrdOrderItemRelationships");
        return ordOrderItemRelationshipService.findAll();
    }

    /**
     * {@code GET  /ord-order-item-relationships/:id} : get the "id" ordOrderItemRelationship.
     *
     * @param id the id of the ordOrderItemRelationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordOrderItemRelationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-order-item-relationships/{id}")
    public ResponseEntity<OrdOrderItemRelationship> getOrdOrderItemRelationship(@PathVariable Long id) {
        log.debug("REST request to get OrdOrderItemRelationship : {}", id);
        Optional<OrdOrderItemRelationship> ordOrderItemRelationship = ordOrderItemRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordOrderItemRelationship);
    }

    /**
     * {@code DELETE  /ord-order-item-relationships/:id} : delete the "id" ordOrderItemRelationship.
     *
     * @param id the id of the ordOrderItemRelationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-order-item-relationships/{id}")
    public ResponseEntity<Void> deleteOrdOrderItemRelationship(@PathVariable Long id) {
        log.debug("REST request to delete OrdOrderItemRelationship : {}", id);
        ordOrderItemRelationshipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

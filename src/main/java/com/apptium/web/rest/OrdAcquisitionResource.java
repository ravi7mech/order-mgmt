package com.apptium.web.rest;

import com.apptium.domain.OrdAcquisition;
import com.apptium.repository.OrdAcquisitionRepository;
import com.apptium.service.OrdAcquisitionService;
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
 * REST controller for managing {@link com.apptium.domain.OrdAcquisition}.
 */
@RestController
@RequestMapping("/api")
public class OrdAcquisitionResource {

    private final Logger log = LoggerFactory.getLogger(OrdAcquisitionResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdAcquisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdAcquisitionService ordAcquisitionService;

    private final OrdAcquisitionRepository ordAcquisitionRepository;

    public OrdAcquisitionResource(OrdAcquisitionService ordAcquisitionService, OrdAcquisitionRepository ordAcquisitionRepository) {
        this.ordAcquisitionService = ordAcquisitionService;
        this.ordAcquisitionRepository = ordAcquisitionRepository;
    }

    /**
     * {@code POST  /ord-acquisitions} : Create a new ordAcquisition.
     *
     * @param ordAcquisition the ordAcquisition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordAcquisition, or with status {@code 400 (Bad Request)} if the ordAcquisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-acquisitions")
    public ResponseEntity<OrdAcquisition> createOrdAcquisition(@RequestBody OrdAcquisition ordAcquisition) throws URISyntaxException {
        log.debug("REST request to save OrdAcquisition : {}", ordAcquisition);
        if (ordAcquisition.getId() != null) {
            throw new BadRequestAlertException("A new ordAcquisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdAcquisition result = ordAcquisitionService.save(ordAcquisition);
        return ResponseEntity
            .created(new URI("/api/ord-acquisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-acquisitions/:id} : Updates an existing ordAcquisition.
     *
     * @param id the id of the ordAcquisition to save.
     * @param ordAcquisition the ordAcquisition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordAcquisition,
     * or with status {@code 400 (Bad Request)} if the ordAcquisition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordAcquisition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-acquisitions/{id}")
    public ResponseEntity<OrdAcquisition> updateOrdAcquisition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdAcquisition ordAcquisition
    ) throws URISyntaxException {
        log.debug("REST request to update OrdAcquisition : {}, {}", id, ordAcquisition);
        if (ordAcquisition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordAcquisition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordAcquisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdAcquisition result = ordAcquisitionService.save(ordAcquisition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordAcquisition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-acquisitions/:id} : Partial updates given fields of an existing ordAcquisition, field will ignore if it is null
     *
     * @param id the id of the ordAcquisition to save.
     * @param ordAcquisition the ordAcquisition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordAcquisition,
     * or with status {@code 400 (Bad Request)} if the ordAcquisition is not valid,
     * or with status {@code 404 (Not Found)} if the ordAcquisition is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordAcquisition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-acquisitions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdAcquisition> partialUpdateOrdAcquisition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdAcquisition ordAcquisition
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdAcquisition partially : {}, {}", id, ordAcquisition);
        if (ordAcquisition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordAcquisition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordAcquisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdAcquisition> result = ordAcquisitionService.partialUpdate(ordAcquisition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordAcquisition.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-acquisitions} : get all the ordAcquisitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordAcquisitions in body.
     */
    @GetMapping("/ord-acquisitions")
    public List<OrdAcquisition> getAllOrdAcquisitions() {
        log.debug("REST request to get all OrdAcquisitions");
        return ordAcquisitionService.findAll();
    }

    /**
     * {@code GET  /ord-acquisitions/:id} : get the "id" ordAcquisition.
     *
     * @param id the id of the ordAcquisition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordAcquisition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-acquisitions/{id}")
    public ResponseEntity<OrdAcquisition> getOrdAcquisition(@PathVariable Long id) {
        log.debug("REST request to get OrdAcquisition : {}", id);
        Optional<OrdAcquisition> ordAcquisition = ordAcquisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordAcquisition);
    }

    /**
     * {@code DELETE  /ord-acquisitions/:id} : delete the "id" ordAcquisition.
     *
     * @param id the id of the ordAcquisition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-acquisitions/{id}")
    public ResponseEntity<Void> deleteOrdAcquisition(@PathVariable Long id) {
        log.debug("REST request to delete OrdAcquisition : {}", id);
        ordAcquisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

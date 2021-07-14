package com.apptium.web.rest;

import com.apptium.domain.OrdContractCharacteristics;
import com.apptium.repository.OrdContractCharacteristicsRepository;
import com.apptium.service.OrdContractCharacteristicsService;
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
 * REST controller for managing {@link com.apptium.domain.OrdContractCharacteristics}.
 */
@RestController
@RequestMapping("/api")
public class OrdContractCharacteristicsResource {

    private final Logger log = LoggerFactory.getLogger(OrdContractCharacteristicsResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdContractCharacteristics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdContractCharacteristicsService ordContractCharacteristicsService;

    private final OrdContractCharacteristicsRepository ordContractCharacteristicsRepository;

    public OrdContractCharacteristicsResource(
        OrdContractCharacteristicsService ordContractCharacteristicsService,
        OrdContractCharacteristicsRepository ordContractCharacteristicsRepository
    ) {
        this.ordContractCharacteristicsService = ordContractCharacteristicsService;
        this.ordContractCharacteristicsRepository = ordContractCharacteristicsRepository;
    }

    /**
     * {@code POST  /ord-contract-characteristics} : Create a new ordContractCharacteristics.
     *
     * @param ordContractCharacteristics the ordContractCharacteristics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordContractCharacteristics, or with status {@code 400 (Bad Request)} if the ordContractCharacteristics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-contract-characteristics")
    public ResponseEntity<OrdContractCharacteristics> createOrdContractCharacteristics(
        @RequestBody OrdContractCharacteristics ordContractCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to save OrdContractCharacteristics : {}", ordContractCharacteristics);
        if (ordContractCharacteristics.getId() != null) {
            throw new BadRequestAlertException("A new ordContractCharacteristics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdContractCharacteristics result = ordContractCharacteristicsService.save(ordContractCharacteristics);
        return ResponseEntity
            .created(new URI("/api/ord-contract-characteristics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-contract-characteristics/:id} : Updates an existing ordContractCharacteristics.
     *
     * @param id the id of the ordContractCharacteristics to save.
     * @param ordContractCharacteristics the ordContractCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContractCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordContractCharacteristics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordContractCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-contract-characteristics/{id}")
    public ResponseEntity<OrdContractCharacteristics> updateOrdContractCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContractCharacteristics ordContractCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to update OrdContractCharacteristics : {}, {}", id, ordContractCharacteristics);
        if (ordContractCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContractCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContractCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdContractCharacteristics result = ordContractCharacteristicsService.save(ordContractCharacteristics);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContractCharacteristics.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-contract-characteristics/:id} : Partial updates given fields of an existing ordContractCharacteristics, field will ignore if it is null
     *
     * @param id the id of the ordContractCharacteristics to save.
     * @param ordContractCharacteristics the ordContractCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContractCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordContractCharacteristics is not valid,
     * or with status {@code 404 (Not Found)} if the ordContractCharacteristics is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordContractCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-contract-characteristics/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdContractCharacteristics> partialUpdateOrdContractCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContractCharacteristics ordContractCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdContractCharacteristics partially : {}, {}", id, ordContractCharacteristics);
        if (ordContractCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContractCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContractCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdContractCharacteristics> result = ordContractCharacteristicsService.partialUpdate(ordContractCharacteristics);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContractCharacteristics.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-contract-characteristics} : get all the ordContractCharacteristics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordContractCharacteristics in body.
     */
    @GetMapping("/ord-contract-characteristics")
    public List<OrdContractCharacteristics> getAllOrdContractCharacteristics() {
        log.debug("REST request to get all OrdContractCharacteristics");
        return ordContractCharacteristicsService.findAll();
    }

    /**
     * {@code GET  /ord-contract-characteristics/:id} : get the "id" ordContractCharacteristics.
     *
     * @param id the id of the ordContractCharacteristics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordContractCharacteristics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-contract-characteristics/{id}")
    public ResponseEntity<OrdContractCharacteristics> getOrdContractCharacteristics(@PathVariable Long id) {
        log.debug("REST request to get OrdContractCharacteristics : {}", id);
        Optional<OrdContractCharacteristics> ordContractCharacteristics = ordContractCharacteristicsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordContractCharacteristics);
    }

    /**
     * {@code DELETE  /ord-contract-characteristics/:id} : delete the "id" ordContractCharacteristics.
     *
     * @param id the id of the ordContractCharacteristics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-contract-characteristics/{id}")
    public ResponseEntity<Void> deleteOrdContractCharacteristics(@PathVariable Long id) {
        log.debug("REST request to delete OrdContractCharacteristics : {}", id);
        ordContractCharacteristicsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdProductCharacteristics;
import com.apptium.repository.OrdProductCharacteristicsRepository;
import com.apptium.service.OrdProductCharacteristicsService;
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
 * REST controller for managing {@link com.apptium.domain.OrdProductCharacteristics}.
 */
@RestController
@RequestMapping("/api")
public class OrdProductCharacteristicsResource {

    private final Logger log = LoggerFactory.getLogger(OrdProductCharacteristicsResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdProductCharacteristics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdProductCharacteristicsService ordProductCharacteristicsService;

    private final OrdProductCharacteristicsRepository ordProductCharacteristicsRepository;

    public OrdProductCharacteristicsResource(
        OrdProductCharacteristicsService ordProductCharacteristicsService,
        OrdProductCharacteristicsRepository ordProductCharacteristicsRepository
    ) {
        this.ordProductCharacteristicsService = ordProductCharacteristicsService;
        this.ordProductCharacteristicsRepository = ordProductCharacteristicsRepository;
    }

    /**
     * {@code POST  /ord-product-characteristics} : Create a new ordProductCharacteristics.
     *
     * @param ordProductCharacteristics the ordProductCharacteristics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordProductCharacteristics, or with status {@code 400 (Bad Request)} if the ordProductCharacteristics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-product-characteristics")
    public ResponseEntity<OrdProductCharacteristics> createOrdProductCharacteristics(
        @RequestBody OrdProductCharacteristics ordProductCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to save OrdProductCharacteristics : {}", ordProductCharacteristics);
        if (ordProductCharacteristics.getId() != null) {
            throw new BadRequestAlertException("A new ordProductCharacteristics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdProductCharacteristics result = ordProductCharacteristicsService.save(ordProductCharacteristics);
        return ResponseEntity
            .created(new URI("/api/ord-product-characteristics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-product-characteristics/:id} : Updates an existing ordProductCharacteristics.
     *
     * @param id the id of the ordProductCharacteristics to save.
     * @param ordProductCharacteristics the ordProductCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordProductCharacteristics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordProductCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-product-characteristics/{id}")
    public ResponseEntity<OrdProductCharacteristics> updateOrdProductCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductCharacteristics ordProductCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to update OrdProductCharacteristics : {}, {}", id, ordProductCharacteristics);
        if (ordProductCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdProductCharacteristics result = ordProductCharacteristicsService.save(ordProductCharacteristics);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductCharacteristics.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-product-characteristics/:id} : Partial updates given fields of an existing ordProductCharacteristics, field will ignore if it is null
     *
     * @param id the id of the ordProductCharacteristics to save.
     * @param ordProductCharacteristics the ordProductCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordProductCharacteristics is not valid,
     * or with status {@code 404 (Not Found)} if the ordProductCharacteristics is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordProductCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-product-characteristics/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdProductCharacteristics> partialUpdateOrdProductCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductCharacteristics ordProductCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdProductCharacteristics partially : {}, {}", id, ordProductCharacteristics);
        if (ordProductCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdProductCharacteristics> result = ordProductCharacteristicsService.partialUpdate(ordProductCharacteristics);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductCharacteristics.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-product-characteristics} : get all the ordProductCharacteristics.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordProductCharacteristics in body.
     */
    @GetMapping("/ord-product-characteristics")
    public List<OrdProductCharacteristics> getAllOrdProductCharacteristics(@RequestParam(required = false) String filter) {
        if ("ordproduct-is-null".equals(filter)) {
            log.debug("REST request to get all OrdProductCharacteristicss where ordProduct is null");
            return ordProductCharacteristicsService.findAllWhereOrdProductIsNull();
        }
        log.debug("REST request to get all OrdProductCharacteristics");
        return ordProductCharacteristicsService.findAll();
    }

    /**
     * {@code GET  /ord-product-characteristics/:id} : get the "id" ordProductCharacteristics.
     *
     * @param id the id of the ordProductCharacteristics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordProductCharacteristics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-product-characteristics/{id}")
    public ResponseEntity<OrdProductCharacteristics> getOrdProductCharacteristics(@PathVariable Long id) {
        log.debug("REST request to get OrdProductCharacteristics : {}", id);
        Optional<OrdProductCharacteristics> ordProductCharacteristics = ordProductCharacteristicsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordProductCharacteristics);
    }

    /**
     * {@code DELETE  /ord-product-characteristics/:id} : delete the "id" ordProductCharacteristics.
     *
     * @param id the id of the ordProductCharacteristics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-product-characteristics/{id}")
    public ResponseEntity<Void> deleteOrdProductCharacteristics(@PathVariable Long id) {
        log.debug("REST request to delete OrdProductCharacteristics : {}", id);
        ordProductCharacteristicsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

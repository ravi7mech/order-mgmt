package com.apptium.web.rest;

import com.apptium.domain.OrdCharacteristics;
import com.apptium.repository.OrdCharacteristicsRepository;
import com.apptium.service.OrdCharacteristicsService;
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
 * REST controller for managing {@link com.apptium.domain.OrdCharacteristics}.
 */
@RestController
@RequestMapping("/api")
public class OrdCharacteristicsResource {

    private final Logger log = LoggerFactory.getLogger(OrdCharacteristicsResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdCharacteristics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdCharacteristicsService ordCharacteristicsService;

    private final OrdCharacteristicsRepository ordCharacteristicsRepository;

    public OrdCharacteristicsResource(
        OrdCharacteristicsService ordCharacteristicsService,
        OrdCharacteristicsRepository ordCharacteristicsRepository
    ) {
        this.ordCharacteristicsService = ordCharacteristicsService;
        this.ordCharacteristicsRepository = ordCharacteristicsRepository;
    }

    /**
     * {@code POST  /ord-characteristics} : Create a new ordCharacteristics.
     *
     * @param ordCharacteristics the ordCharacteristics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordCharacteristics, or with status {@code 400 (Bad Request)} if the ordCharacteristics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-characteristics")
    public ResponseEntity<OrdCharacteristics> createOrdCharacteristics(@RequestBody OrdCharacteristics ordCharacteristics)
        throws URISyntaxException {
        log.debug("REST request to save OrdCharacteristics : {}", ordCharacteristics);
        if (ordCharacteristics.getId() != null) {
            throw new BadRequestAlertException("A new ordCharacteristics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdCharacteristics result = ordCharacteristicsService.save(ordCharacteristics);
        return ResponseEntity
            .created(new URI("/api/ord-characteristics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-characteristics/:id} : Updates an existing ordCharacteristics.
     *
     * @param id the id of the ordCharacteristics to save.
     * @param ordCharacteristics the ordCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordCharacteristics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-characteristics/{id}")
    public ResponseEntity<OrdCharacteristics> updateOrdCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdCharacteristics ordCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to update OrdCharacteristics : {}, {}", id, ordCharacteristics);
        if (ordCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdCharacteristics result = ordCharacteristicsService.save(ordCharacteristics);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordCharacteristics.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-characteristics/:id} : Partial updates given fields of an existing ordCharacteristics, field will ignore if it is null
     *
     * @param id the id of the ordCharacteristics to save.
     * @param ordCharacteristics the ordCharacteristics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordCharacteristics,
     * or with status {@code 400 (Bad Request)} if the ordCharacteristics is not valid,
     * or with status {@code 404 (Not Found)} if the ordCharacteristics is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordCharacteristics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-characteristics/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdCharacteristics> partialUpdateOrdCharacteristics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdCharacteristics ordCharacteristics
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdCharacteristics partially : {}, {}", id, ordCharacteristics);
        if (ordCharacteristics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordCharacteristics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordCharacteristicsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdCharacteristics> result = ordCharacteristicsService.partialUpdate(ordCharacteristics);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordCharacteristics.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-characteristics} : get all the ordCharacteristics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordCharacteristics in body.
     */
    @GetMapping("/ord-characteristics")
    public List<OrdCharacteristics> getAllOrdCharacteristics() {
        log.debug("REST request to get all OrdCharacteristics");
        return ordCharacteristicsService.findAll();
    }

    /**
     * {@code GET  /ord-characteristics/:id} : get the "id" ordCharacteristics.
     *
     * @param id the id of the ordCharacteristics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordCharacteristics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-characteristics/{id}")
    public ResponseEntity<OrdCharacteristics> getOrdCharacteristics(@PathVariable Long id) {
        log.debug("REST request to get OrdCharacteristics : {}", id);
        Optional<OrdCharacteristics> ordCharacteristics = ordCharacteristicsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordCharacteristics);
    }

    /**
     * {@code DELETE  /ord-characteristics/:id} : delete the "id" ordCharacteristics.
     *
     * @param id the id of the ordCharacteristics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-characteristics/{id}")
    public ResponseEntity<Void> deleteOrdCharacteristics(@PathVariable Long id) {
        log.debug("REST request to delete OrdCharacteristics : {}", id);
        ordCharacteristicsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdPriceAlteration;
import com.apptium.repository.OrdPriceAlterationRepository;
import com.apptium.service.OrdPriceAlterationService;
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
 * REST controller for managing {@link com.apptium.domain.OrdPriceAlteration}.
 */
@RestController
@RequestMapping("/api")
public class OrdPriceAlterationResource {

    private final Logger log = LoggerFactory.getLogger(OrdPriceAlterationResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdPriceAlteration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdPriceAlterationService ordPriceAlterationService;

    private final OrdPriceAlterationRepository ordPriceAlterationRepository;

    public OrdPriceAlterationResource(
        OrdPriceAlterationService ordPriceAlterationService,
        OrdPriceAlterationRepository ordPriceAlterationRepository
    ) {
        this.ordPriceAlterationService = ordPriceAlterationService;
        this.ordPriceAlterationRepository = ordPriceAlterationRepository;
    }

    /**
     * {@code POST  /ord-price-alterations} : Create a new ordPriceAlteration.
     *
     * @param ordPriceAlteration the ordPriceAlteration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordPriceAlteration, or with status {@code 400 (Bad Request)} if the ordPriceAlteration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-price-alterations")
    public ResponseEntity<OrdPriceAlteration> createOrdPriceAlteration(@RequestBody OrdPriceAlteration ordPriceAlteration)
        throws URISyntaxException {
        log.debug("REST request to save OrdPriceAlteration : {}", ordPriceAlteration);
        if (ordPriceAlteration.getId() != null) {
            throw new BadRequestAlertException("A new ordPriceAlteration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdPriceAlteration result = ordPriceAlterationService.save(ordPriceAlteration);
        return ResponseEntity
            .created(new URI("/api/ord-price-alterations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-price-alterations/:id} : Updates an existing ordPriceAlteration.
     *
     * @param id the id of the ordPriceAlteration to save.
     * @param ordPriceAlteration the ordPriceAlteration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPriceAlteration,
     * or with status {@code 400 (Bad Request)} if the ordPriceAlteration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordPriceAlteration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-price-alterations/{id}")
    public ResponseEntity<OrdPriceAlteration> updateOrdPriceAlteration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPriceAlteration ordPriceAlteration
    ) throws URISyntaxException {
        log.debug("REST request to update OrdPriceAlteration : {}, {}", id, ordPriceAlteration);
        if (ordPriceAlteration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPriceAlteration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPriceAlterationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdPriceAlteration result = ordPriceAlterationService.save(ordPriceAlteration);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPriceAlteration.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-price-alterations/:id} : Partial updates given fields of an existing ordPriceAlteration, field will ignore if it is null
     *
     * @param id the id of the ordPriceAlteration to save.
     * @param ordPriceAlteration the ordPriceAlteration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPriceAlteration,
     * or with status {@code 400 (Bad Request)} if the ordPriceAlteration is not valid,
     * or with status {@code 404 (Not Found)} if the ordPriceAlteration is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordPriceAlteration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-price-alterations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdPriceAlteration> partialUpdateOrdPriceAlteration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPriceAlteration ordPriceAlteration
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdPriceAlteration partially : {}, {}", id, ordPriceAlteration);
        if (ordPriceAlteration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPriceAlteration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPriceAlterationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdPriceAlteration> result = ordPriceAlterationService.partialUpdate(ordPriceAlteration);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPriceAlteration.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-price-alterations} : get all the ordPriceAlterations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordPriceAlterations in body.
     */
    @GetMapping("/ord-price-alterations")
    public List<OrdPriceAlteration> getAllOrdPriceAlterations() {
        log.debug("REST request to get all OrdPriceAlterations");
        return ordPriceAlterationService.findAll();
    }

    /**
     * {@code GET  /ord-price-alterations/:id} : get the "id" ordPriceAlteration.
     *
     * @param id the id of the ordPriceAlteration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordPriceAlteration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-price-alterations/{id}")
    public ResponseEntity<OrdPriceAlteration> getOrdPriceAlteration(@PathVariable Long id) {
        log.debug("REST request to get OrdPriceAlteration : {}", id);
        Optional<OrdPriceAlteration> ordPriceAlteration = ordPriceAlterationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordPriceAlteration);
    }

    /**
     * {@code DELETE  /ord-price-alterations/:id} : delete the "id" ordPriceAlteration.
     *
     * @param id the id of the ordPriceAlteration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-price-alterations/{id}")
    public ResponseEntity<Void> deleteOrdPriceAlteration(@PathVariable Long id) {
        log.debug("REST request to delete OrdPriceAlteration : {}", id);
        ordPriceAlterationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

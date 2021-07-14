package com.apptium.web.rest;

import com.apptium.domain.OrdContactDetails;
import com.apptium.repository.OrdContactDetailsRepository;
import com.apptium.service.OrdContactDetailsService;
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
 * REST controller for managing {@link com.apptium.domain.OrdContactDetails}.
 */
@RestController
@RequestMapping("/api")
public class OrdContactDetailsResource {

    private final Logger log = LoggerFactory.getLogger(OrdContactDetailsResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdContactDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdContactDetailsService ordContactDetailsService;

    private final OrdContactDetailsRepository ordContactDetailsRepository;

    public OrdContactDetailsResource(
        OrdContactDetailsService ordContactDetailsService,
        OrdContactDetailsRepository ordContactDetailsRepository
    ) {
        this.ordContactDetailsService = ordContactDetailsService;
        this.ordContactDetailsRepository = ordContactDetailsRepository;
    }

    /**
     * {@code POST  /ord-contact-details} : Create a new ordContactDetails.
     *
     * @param ordContactDetails the ordContactDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordContactDetails, or with status {@code 400 (Bad Request)} if the ordContactDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-contact-details")
    public ResponseEntity<OrdContactDetails> createOrdContactDetails(@RequestBody OrdContactDetails ordContactDetails)
        throws URISyntaxException {
        log.debug("REST request to save OrdContactDetails : {}", ordContactDetails);
        if (ordContactDetails.getId() != null) {
            throw new BadRequestAlertException("A new ordContactDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdContactDetails result = ordContactDetailsService.save(ordContactDetails);
        return ResponseEntity
            .created(new URI("/api/ord-contact-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-contact-details/:id} : Updates an existing ordContactDetails.
     *
     * @param id the id of the ordContactDetails to save.
     * @param ordContactDetails the ordContactDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContactDetails,
     * or with status {@code 400 (Bad Request)} if the ordContactDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordContactDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-contact-details/{id}")
    public ResponseEntity<OrdContactDetails> updateOrdContactDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContactDetails ordContactDetails
    ) throws URISyntaxException {
        log.debug("REST request to update OrdContactDetails : {}, {}", id, ordContactDetails);
        if (ordContactDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContactDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContactDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdContactDetails result = ordContactDetailsService.save(ordContactDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContactDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-contact-details/:id} : Partial updates given fields of an existing ordContactDetails, field will ignore if it is null
     *
     * @param id the id of the ordContactDetails to save.
     * @param ordContactDetails the ordContactDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContactDetails,
     * or with status {@code 400 (Bad Request)} if the ordContactDetails is not valid,
     * or with status {@code 404 (Not Found)} if the ordContactDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordContactDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-contact-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdContactDetails> partialUpdateOrdContactDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContactDetails ordContactDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdContactDetails partially : {}, {}", id, ordContactDetails);
        if (ordContactDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContactDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContactDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdContactDetails> result = ordContactDetailsService.partialUpdate(ordContactDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContactDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-contact-details} : get all the ordContactDetails.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordContactDetails in body.
     */
    @GetMapping("/ord-contact-details")
    public List<OrdContactDetails> getAllOrdContactDetails(@RequestParam(required = false) String filter) {
        if ("ordproductorder-is-null".equals(filter)) {
            log.debug("REST request to get all OrdContactDetailss where ordProductOrder is null");
            return ordContactDetailsService.findAllWhereOrdProductOrderIsNull();
        }
        log.debug("REST request to get all OrdContactDetails");
        return ordContactDetailsService.findAll();
    }

    /**
     * {@code GET  /ord-contact-details/:id} : get the "id" ordContactDetails.
     *
     * @param id the id of the ordContactDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordContactDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-contact-details/{id}")
    public ResponseEntity<OrdContactDetails> getOrdContactDetails(@PathVariable Long id) {
        log.debug("REST request to get OrdContactDetails : {}", id);
        Optional<OrdContactDetails> ordContactDetails = ordContactDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordContactDetails);
    }

    /**
     * {@code DELETE  /ord-contact-details/:id} : delete the "id" ordContactDetails.
     *
     * @param id the id of the ordContactDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-contact-details/{id}")
    public ResponseEntity<Void> deleteOrdContactDetails(@PathVariable Long id) {
        log.debug("REST request to delete OrdContactDetails : {}", id);
        ordContactDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

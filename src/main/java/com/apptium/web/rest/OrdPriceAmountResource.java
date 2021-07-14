package com.apptium.web.rest;

import com.apptium.domain.OrdPriceAmount;
import com.apptium.repository.OrdPriceAmountRepository;
import com.apptium.service.OrdPriceAmountService;
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
 * REST controller for managing {@link com.apptium.domain.OrdPriceAmount}.
 */
@RestController
@RequestMapping("/api")
public class OrdPriceAmountResource {

    private final Logger log = LoggerFactory.getLogger(OrdPriceAmountResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdPriceAmount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdPriceAmountService ordPriceAmountService;

    private final OrdPriceAmountRepository ordPriceAmountRepository;

    public OrdPriceAmountResource(OrdPriceAmountService ordPriceAmountService, OrdPriceAmountRepository ordPriceAmountRepository) {
        this.ordPriceAmountService = ordPriceAmountService;
        this.ordPriceAmountRepository = ordPriceAmountRepository;
    }

    /**
     * {@code POST  /ord-price-amounts} : Create a new ordPriceAmount.
     *
     * @param ordPriceAmount the ordPriceAmount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordPriceAmount, or with status {@code 400 (Bad Request)} if the ordPriceAmount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-price-amounts")
    public ResponseEntity<OrdPriceAmount> createOrdPriceAmount(@RequestBody OrdPriceAmount ordPriceAmount) throws URISyntaxException {
        log.debug("REST request to save OrdPriceAmount : {}", ordPriceAmount);
        if (ordPriceAmount.getId() != null) {
            throw new BadRequestAlertException("A new ordPriceAmount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdPriceAmount result = ordPriceAmountService.save(ordPriceAmount);
        return ResponseEntity
            .created(new URI("/api/ord-price-amounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-price-amounts/:id} : Updates an existing ordPriceAmount.
     *
     * @param id the id of the ordPriceAmount to save.
     * @param ordPriceAmount the ordPriceAmount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPriceAmount,
     * or with status {@code 400 (Bad Request)} if the ordPriceAmount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordPriceAmount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-price-amounts/{id}")
    public ResponseEntity<OrdPriceAmount> updateOrdPriceAmount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPriceAmount ordPriceAmount
    ) throws URISyntaxException {
        log.debug("REST request to update OrdPriceAmount : {}, {}", id, ordPriceAmount);
        if (ordPriceAmount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPriceAmount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPriceAmountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdPriceAmount result = ordPriceAmountService.save(ordPriceAmount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPriceAmount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-price-amounts/:id} : Partial updates given fields of an existing ordPriceAmount, field will ignore if it is null
     *
     * @param id the id of the ordPriceAmount to save.
     * @param ordPriceAmount the ordPriceAmount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPriceAmount,
     * or with status {@code 400 (Bad Request)} if the ordPriceAmount is not valid,
     * or with status {@code 404 (Not Found)} if the ordPriceAmount is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordPriceAmount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-price-amounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdPriceAmount> partialUpdateOrdPriceAmount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPriceAmount ordPriceAmount
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdPriceAmount partially : {}, {}", id, ordPriceAmount);
        if (ordPriceAmount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPriceAmount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPriceAmountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdPriceAmount> result = ordPriceAmountService.partialUpdate(ordPriceAmount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPriceAmount.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-price-amounts} : get all the ordPriceAmounts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordPriceAmounts in body.
     */
    @GetMapping("/ord-price-amounts")
    public List<OrdPriceAmount> getAllOrdPriceAmounts(@RequestParam(required = false) String filter) {
        if ("ordorderprice-is-null".equals(filter)) {
            log.debug("REST request to get all OrdPriceAmounts where ordOrderPrice is null");
            return ordPriceAmountService.findAllWhereOrdOrderPriceIsNull();
        }

        if ("ordpricealteration-is-null".equals(filter)) {
            log.debug("REST request to get all OrdPriceAmounts where ordPriceAlteration is null");
            return ordPriceAmountService.findAllWhereOrdPriceAlterationIsNull();
        }
        log.debug("REST request to get all OrdPriceAmounts");
        return ordPriceAmountService.findAll();
    }

    /**
     * {@code GET  /ord-price-amounts/:id} : get the "id" ordPriceAmount.
     *
     * @param id the id of the ordPriceAmount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordPriceAmount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-price-amounts/{id}")
    public ResponseEntity<OrdPriceAmount> getOrdPriceAmount(@PathVariable Long id) {
        log.debug("REST request to get OrdPriceAmount : {}", id);
        Optional<OrdPriceAmount> ordPriceAmount = ordPriceAmountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordPriceAmount);
    }

    /**
     * {@code DELETE  /ord-price-amounts/:id} : delete the "id" ordPriceAmount.
     *
     * @param id the id of the ordPriceAmount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-price-amounts/{id}")
    public ResponseEntity<Void> deleteOrdPriceAmount(@PathVariable Long id) {
        log.debug("REST request to delete OrdPriceAmount : {}", id);
        ordPriceAmountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdOrderPrice;
import com.apptium.repository.OrdOrderPriceRepository;
import com.apptium.service.OrdOrderPriceService;
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
 * REST controller for managing {@link com.apptium.domain.OrdOrderPrice}.
 */
@RestController
@RequestMapping("/api")
public class OrdOrderPriceResource {

    private final Logger log = LoggerFactory.getLogger(OrdOrderPriceResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdOrderPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdOrderPriceService ordOrderPriceService;

    private final OrdOrderPriceRepository ordOrderPriceRepository;

    public OrdOrderPriceResource(OrdOrderPriceService ordOrderPriceService, OrdOrderPriceRepository ordOrderPriceRepository) {
        this.ordOrderPriceService = ordOrderPriceService;
        this.ordOrderPriceRepository = ordOrderPriceRepository;
    }

    /**
     * {@code POST  /ord-order-prices} : Create a new ordOrderPrice.
     *
     * @param ordOrderPrice the ordOrderPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordOrderPrice, or with status {@code 400 (Bad Request)} if the ordOrderPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-order-prices")
    public ResponseEntity<OrdOrderPrice> createOrdOrderPrice(@RequestBody OrdOrderPrice ordOrderPrice) throws URISyntaxException {
        log.debug("REST request to save OrdOrderPrice : {}", ordOrderPrice);
        if (ordOrderPrice.getId() != null) {
            throw new BadRequestAlertException("A new ordOrderPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdOrderPrice result = ordOrderPriceService.save(ordOrderPrice);
        return ResponseEntity
            .created(new URI("/api/ord-order-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-order-prices/:id} : Updates an existing ordOrderPrice.
     *
     * @param id the id of the ordOrderPrice to save.
     * @param ordOrderPrice the ordOrderPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderPrice,
     * or with status {@code 400 (Bad Request)} if the ordOrderPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-order-prices/{id}")
    public ResponseEntity<OrdOrderPrice> updateOrdOrderPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderPrice ordOrderPrice
    ) throws URISyntaxException {
        log.debug("REST request to update OrdOrderPrice : {}, {}", id, ordOrderPrice);
        if (ordOrderPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdOrderPrice result = ordOrderPriceService.save(ordOrderPrice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-order-prices/:id} : Partial updates given fields of an existing ordOrderPrice, field will ignore if it is null
     *
     * @param id the id of the ordOrderPrice to save.
     * @param ordOrderPrice the ordOrderPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderPrice,
     * or with status {@code 400 (Bad Request)} if the ordOrderPrice is not valid,
     * or with status {@code 404 (Not Found)} if the ordOrderPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-order-prices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdOrderPrice> partialUpdateOrdOrderPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderPrice ordOrderPrice
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdOrderPrice partially : {}, {}", id, ordOrderPrice);
        if (ordOrderPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdOrderPrice> result = ordOrderPriceService.partialUpdate(ordOrderPrice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-order-prices} : get all the ordOrderPrices.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordOrderPrices in body.
     */
    @GetMapping("/ord-order-prices")
    public List<OrdOrderPrice> getAllOrdOrderPrices(@RequestParam(required = false) String filter) {
        if ("ordproductorder-is-null".equals(filter)) {
            log.debug("REST request to get all OrdOrderPrices where ordProductOrder is null");
            return ordOrderPriceService.findAllWhereOrdProductOrderIsNull();
        }

        if ("ordorderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrdOrderPrices where ordOrderItem is null");
            return ordOrderPriceService.findAllWhereOrdOrderItemIsNull();
        }
        log.debug("REST request to get all OrdOrderPrices");
        return ordOrderPriceService.findAll();
    }

    /**
     * {@code GET  /ord-order-prices/:id} : get the "id" ordOrderPrice.
     *
     * @param id the id of the ordOrderPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordOrderPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-order-prices/{id}")
    public ResponseEntity<OrdOrderPrice> getOrdOrderPrice(@PathVariable Long id) {
        log.debug("REST request to get OrdOrderPrice : {}", id);
        Optional<OrdOrderPrice> ordOrderPrice = ordOrderPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordOrderPrice);
    }

    /**
     * {@code DELETE  /ord-order-prices/:id} : delete the "id" ordOrderPrice.
     *
     * @param id the id of the ordOrderPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-order-prices/{id}")
    public ResponseEntity<Void> deleteOrdOrderPrice(@PathVariable Long id) {
        log.debug("REST request to delete OrdOrderPrice : {}", id);
        ordOrderPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

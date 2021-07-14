package com.apptium.web.rest;

import com.apptium.domain.OrdProductOrder;
import com.apptium.repository.OrdProductOrderRepository;
import com.apptium.service.OrdProductOrderService;
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
 * REST controller for managing {@link com.apptium.domain.OrdProductOrder}.
 */
@RestController
@RequestMapping("/api")
public class OrdProductOrderResource {

    private final Logger log = LoggerFactory.getLogger(OrdProductOrderResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdProductOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdProductOrderService ordProductOrderService;

    private final OrdProductOrderRepository ordProductOrderRepository;

    public OrdProductOrderResource(OrdProductOrderService ordProductOrderService, OrdProductOrderRepository ordProductOrderRepository) {
        this.ordProductOrderService = ordProductOrderService;
        this.ordProductOrderRepository = ordProductOrderRepository;
    }

    /**
     * {@code POST  /ord-product-orders} : Create a new ordProductOrder.
     *
     * @param ordProductOrder the ordProductOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordProductOrder, or with status {@code 400 (Bad Request)} if the ordProductOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-product-orders")
    public ResponseEntity<OrdProductOrder> createOrdProductOrder(@RequestBody OrdProductOrder ordProductOrder) throws URISyntaxException {
        log.debug("REST request to save OrdProductOrder : {}", ordProductOrder);
        if (ordProductOrder.getId() != null) {
            throw new BadRequestAlertException("A new ordProductOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdProductOrder result = ordProductOrderService.save(ordProductOrder);
        return ResponseEntity
            .created(new URI("/api/ord-product-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-product-orders/:id} : Updates an existing ordProductOrder.
     *
     * @param id the id of the ordProductOrder to save.
     * @param ordProductOrder the ordProductOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductOrder,
     * or with status {@code 400 (Bad Request)} if the ordProductOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordProductOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-product-orders/{id}")
    public ResponseEntity<OrdProductOrder> updateOrdProductOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductOrder ordProductOrder
    ) throws URISyntaxException {
        log.debug("REST request to update OrdProductOrder : {}, {}", id, ordProductOrder);
        if (ordProductOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdProductOrder result = ordProductOrderService.save(ordProductOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-product-orders/:id} : Partial updates given fields of an existing ordProductOrder, field will ignore if it is null
     *
     * @param id the id of the ordProductOrder to save.
     * @param ordProductOrder the ordProductOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProductOrder,
     * or with status {@code 400 (Bad Request)} if the ordProductOrder is not valid,
     * or with status {@code 404 (Not Found)} if the ordProductOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordProductOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-product-orders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdProductOrder> partialUpdateOrdProductOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProductOrder ordProductOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdProductOrder partially : {}, {}", id, ordProductOrder);
        if (ordProductOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProductOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdProductOrder> result = ordProductOrderService.partialUpdate(ordProductOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProductOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-product-orders} : get all the ordProductOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordProductOrders in body.
     */
    @GetMapping("/ord-product-orders")
    public List<OrdProductOrder> getAllOrdProductOrders() {
        log.debug("REST request to get all OrdProductOrders");
        return ordProductOrderService.findAll();
    }

    /**
     * {@code GET  /ord-product-orders/:id} : get the "id" ordProductOrder.
     *
     * @param id the id of the ordProductOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordProductOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-product-orders/{id}")
    public ResponseEntity<OrdProductOrder> getOrdProductOrder(@PathVariable Long id) {
        log.debug("REST request to get OrdProductOrder : {}", id);
        Optional<OrdProductOrder> ordProductOrder = ordProductOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordProductOrder);
    }

    /**
     * {@code DELETE  /ord-product-orders/:id} : delete the "id" ordProductOrder.
     *
     * @param id the id of the ordProductOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-product-orders/{id}")
    public ResponseEntity<Void> deleteOrdProductOrder(@PathVariable Long id) {
        log.debug("REST request to delete OrdProductOrder : {}", id);
        ordProductOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

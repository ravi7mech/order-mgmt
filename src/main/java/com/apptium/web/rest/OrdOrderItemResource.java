package com.apptium.web.rest;

import com.apptium.domain.OrdOrderItem;
import com.apptium.repository.OrdOrderItemRepository;
import com.apptium.service.OrdOrderItemService;
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
 * REST controller for managing {@link com.apptium.domain.OrdOrderItem}.
 */
@RestController
@RequestMapping("/api")
public class OrdOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdOrderItemService ordOrderItemService;

    private final OrdOrderItemRepository ordOrderItemRepository;

    public OrdOrderItemResource(OrdOrderItemService ordOrderItemService, OrdOrderItemRepository ordOrderItemRepository) {
        this.ordOrderItemService = ordOrderItemService;
        this.ordOrderItemRepository = ordOrderItemRepository;
    }

    /**
     * {@code POST  /ord-order-items} : Create a new ordOrderItem.
     *
     * @param ordOrderItem the ordOrderItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordOrderItem, or with status {@code 400 (Bad Request)} if the ordOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-order-items")
    public ResponseEntity<OrdOrderItem> createOrdOrderItem(@RequestBody OrdOrderItem ordOrderItem) throws URISyntaxException {
        log.debug("REST request to save OrdOrderItem : {}", ordOrderItem);
        if (ordOrderItem.getId() != null) {
            throw new BadRequestAlertException("A new ordOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdOrderItem result = ordOrderItemService.save(ordOrderItem);
        return ResponseEntity
            .created(new URI("/api/ord-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-order-items/:id} : Updates an existing ordOrderItem.
     *
     * @param id the id of the ordOrderItem to save.
     * @param ordOrderItem the ordOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItem,
     * or with status {@code 400 (Bad Request)} if the ordOrderItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-order-items/{id}")
    public ResponseEntity<OrdOrderItem> updateOrdOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItem ordOrderItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrdOrderItem : {}, {}", id, ordOrderItem);
        if (ordOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdOrderItem result = ordOrderItemService.save(ordOrderItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-order-items/:id} : Partial updates given fields of an existing ordOrderItem, field will ignore if it is null
     *
     * @param id the id of the ordOrderItem to save.
     * @param ordOrderItem the ordOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItem,
     * or with status {@code 400 (Bad Request)} if the ordOrderItem is not valid,
     * or with status {@code 404 (Not Found)} if the ordOrderItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-order-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdOrderItem> partialUpdateOrdOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItem ordOrderItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdOrderItem partially : {}, {}", id, ordOrderItem);
        if (ordOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdOrderItem> result = ordOrderItemService.partialUpdate(ordOrderItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItem.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-order-items} : get all the ordOrderItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordOrderItems in body.
     */
    @GetMapping("/ord-order-items")
    public List<OrdOrderItem> getAllOrdOrderItems() {
        log.debug("REST request to get all OrdOrderItems");
        return ordOrderItemService.findAll();
    }

    /**
     * {@code GET  /ord-order-items/:id} : get the "id" ordOrderItem.
     *
     * @param id the id of the ordOrderItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordOrderItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-order-items/{id}")
    public ResponseEntity<OrdOrderItem> getOrdOrderItem(@PathVariable Long id) {
        log.debug("REST request to get OrdOrderItem : {}", id);
        Optional<OrdOrderItem> ordOrderItem = ordOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordOrderItem);
    }

    /**
     * {@code DELETE  /ord-order-items/:id} : delete the "id" ordOrderItem.
     *
     * @param id the id of the ordOrderItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-order-items/{id}")
    public ResponseEntity<Void> deleteOrdOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete OrdOrderItem : {}", id);
        ordOrderItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdProduct;
import com.apptium.repository.OrdProductRepository;
import com.apptium.service.OrdProductService;
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
 * REST controller for managing {@link com.apptium.domain.OrdProduct}.
 */
@RestController
@RequestMapping("/api")
public class OrdProductResource {

    private final Logger log = LoggerFactory.getLogger(OrdProductResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdProductService ordProductService;

    private final OrdProductRepository ordProductRepository;

    public OrdProductResource(OrdProductService ordProductService, OrdProductRepository ordProductRepository) {
        this.ordProductService = ordProductService;
        this.ordProductRepository = ordProductRepository;
    }

    /**
     * {@code POST  /ord-products} : Create a new ordProduct.
     *
     * @param ordProduct the ordProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordProduct, or with status {@code 400 (Bad Request)} if the ordProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-products")
    public ResponseEntity<OrdProduct> createOrdProduct(@RequestBody OrdProduct ordProduct) throws URISyntaxException {
        log.debug("REST request to save OrdProduct : {}", ordProduct);
        if (ordProduct.getId() != null) {
            throw new BadRequestAlertException("A new ordProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdProduct result = ordProductService.save(ordProduct);
        return ResponseEntity
            .created(new URI("/api/ord-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-products/:id} : Updates an existing ordProduct.
     *
     * @param id the id of the ordProduct to save.
     * @param ordProduct the ordProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProduct,
     * or with status {@code 400 (Bad Request)} if the ordProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-products/{id}")
    public ResponseEntity<OrdProduct> updateOrdProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProduct ordProduct
    ) throws URISyntaxException {
        log.debug("REST request to update OrdProduct : {}, {}", id, ordProduct);
        if (ordProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdProduct result = ordProductService.save(ordProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-products/:id} : Partial updates given fields of an existing ordProduct, field will ignore if it is null
     *
     * @param id the id of the ordProduct to save.
     * @param ordProduct the ordProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordProduct,
     * or with status {@code 400 (Bad Request)} if the ordProduct is not valid,
     * or with status {@code 404 (Not Found)} if the ordProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-products/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdProduct> partialUpdateOrdProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdProduct ordProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdProduct partially : {}, {}", id, ordProduct);
        if (ordProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdProduct> result = ordProductService.partialUpdate(ordProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-products} : get all the ordProducts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordProducts in body.
     */
    @GetMapping("/ord-products")
    public List<OrdProduct> getAllOrdProducts(@RequestParam(required = false) String filter) {
        if ("ordorderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrdProducts where ordOrderItem is null");
            return ordProductService.findAllWhereOrdOrderItemIsNull();
        }
        log.debug("REST request to get all OrdProducts");
        return ordProductService.findAll();
    }

    /**
     * {@code GET  /ord-products/:id} : get the "id" ordProduct.
     *
     * @param id the id of the ordProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-products/{id}")
    public ResponseEntity<OrdProduct> getOrdProduct(@PathVariable Long id) {
        log.debug("REST request to get OrdProduct : {}", id);
        Optional<OrdProduct> ordProduct = ordProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordProduct);
    }

    /**
     * {@code DELETE  /ord-products/:id} : delete the "id" ordProduct.
     *
     * @param id the id of the ordProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-products/{id}")
    public ResponseEntity<Void> deleteOrdProduct(@PathVariable Long id) {
        log.debug("REST request to delete OrdProduct : {}", id);
        ordProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

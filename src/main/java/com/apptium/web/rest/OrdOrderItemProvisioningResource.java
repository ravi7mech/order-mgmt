package com.apptium.web.rest;

import com.apptium.domain.OrdOrderItemProvisioning;
import com.apptium.repository.OrdOrderItemProvisioningRepository;
import com.apptium.service.OrdOrderItemProvisioningService;
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
 * REST controller for managing {@link com.apptium.domain.OrdOrderItemProvisioning}.
 */
@RestController
@RequestMapping("/api")
public class OrdOrderItemProvisioningResource {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemProvisioningResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdOrderItemProvisioning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdOrderItemProvisioningService ordOrderItemProvisioningService;

    private final OrdOrderItemProvisioningRepository ordOrderItemProvisioningRepository;

    public OrdOrderItemProvisioningResource(
        OrdOrderItemProvisioningService ordOrderItemProvisioningService,
        OrdOrderItemProvisioningRepository ordOrderItemProvisioningRepository
    ) {
        this.ordOrderItemProvisioningService = ordOrderItemProvisioningService;
        this.ordOrderItemProvisioningRepository = ordOrderItemProvisioningRepository;
    }

    /**
     * {@code POST  /ord-order-item-provisionings} : Create a new ordOrderItemProvisioning.
     *
     * @param ordOrderItemProvisioning the ordOrderItemProvisioning to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordOrderItemProvisioning, or with status {@code 400 (Bad Request)} if the ordOrderItemProvisioning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-order-item-provisionings")
    public ResponseEntity<OrdOrderItemProvisioning> createOrdOrderItemProvisioning(
        @RequestBody OrdOrderItemProvisioning ordOrderItemProvisioning
    ) throws URISyntaxException {
        log.debug("REST request to save OrdOrderItemProvisioning : {}", ordOrderItemProvisioning);
        if (ordOrderItemProvisioning.getId() != null) {
            throw new BadRequestAlertException("A new ordOrderItemProvisioning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdOrderItemProvisioning result = ordOrderItemProvisioningService.save(ordOrderItemProvisioning);
        return ResponseEntity
            .created(new URI("/api/ord-order-item-provisionings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-order-item-provisionings/:id} : Updates an existing ordOrderItemProvisioning.
     *
     * @param id the id of the ordOrderItemProvisioning to save.
     * @param ordOrderItemProvisioning the ordOrderItemProvisioning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemProvisioning,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemProvisioning is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemProvisioning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-order-item-provisionings/{id}")
    public ResponseEntity<OrdOrderItemProvisioning> updateOrdOrderItemProvisioning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemProvisioning ordOrderItemProvisioning
    ) throws URISyntaxException {
        log.debug("REST request to update OrdOrderItemProvisioning : {}, {}", id, ordOrderItemProvisioning);
        if (ordOrderItemProvisioning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemProvisioning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemProvisioningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdOrderItemProvisioning result = ordOrderItemProvisioningService.save(ordOrderItemProvisioning);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemProvisioning.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-order-item-provisionings/:id} : Partial updates given fields of an existing ordOrderItemProvisioning, field will ignore if it is null
     *
     * @param id the id of the ordOrderItemProvisioning to save.
     * @param ordOrderItemProvisioning the ordOrderItemProvisioning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordOrderItemProvisioning,
     * or with status {@code 400 (Bad Request)} if the ordOrderItemProvisioning is not valid,
     * or with status {@code 404 (Not Found)} if the ordOrderItemProvisioning is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordOrderItemProvisioning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-order-item-provisionings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdOrderItemProvisioning> partialUpdateOrdOrderItemProvisioning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdOrderItemProvisioning ordOrderItemProvisioning
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdOrderItemProvisioning partially : {}, {}", id, ordOrderItemProvisioning);
        if (ordOrderItemProvisioning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordOrderItemProvisioning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordOrderItemProvisioningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdOrderItemProvisioning> result = ordOrderItemProvisioningService.partialUpdate(ordOrderItemProvisioning);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordOrderItemProvisioning.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-order-item-provisionings} : get all the ordOrderItemProvisionings.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordOrderItemProvisionings in body.
     */
    @GetMapping("/ord-order-item-provisionings")
    public List<OrdOrderItemProvisioning> getAllOrdOrderItemProvisionings(@RequestParam(required = false) String filter) {
        if ("ordorderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrdOrderItemProvisionings where ordOrderItem is null");
            return ordOrderItemProvisioningService.findAllWhereOrdOrderItemIsNull();
        }
        log.debug("REST request to get all OrdOrderItemProvisionings");
        return ordOrderItemProvisioningService.findAll();
    }

    /**
     * {@code GET  /ord-order-item-provisionings/:id} : get the "id" ordOrderItemProvisioning.
     *
     * @param id the id of the ordOrderItemProvisioning to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordOrderItemProvisioning, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-order-item-provisionings/{id}")
    public ResponseEntity<OrdOrderItemProvisioning> getOrdOrderItemProvisioning(@PathVariable Long id) {
        log.debug("REST request to get OrdOrderItemProvisioning : {}", id);
        Optional<OrdOrderItemProvisioning> ordOrderItemProvisioning = ordOrderItemProvisioningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordOrderItemProvisioning);
    }

    /**
     * {@code DELETE  /ord-order-item-provisionings/:id} : delete the "id" ordOrderItemProvisioning.
     *
     * @param id the id of the ordOrderItemProvisioning to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-order-item-provisionings/{id}")
    public ResponseEntity<Void> deleteOrdOrderItemProvisioning(@PathVariable Long id) {
        log.debug("REST request to delete OrdOrderItemProvisioning : {}", id);
        ordOrderItemProvisioningService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

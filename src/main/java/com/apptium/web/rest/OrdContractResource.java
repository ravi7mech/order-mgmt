package com.apptium.web.rest;

import com.apptium.domain.OrdContract;
import com.apptium.repository.OrdContractRepository;
import com.apptium.service.OrdContractService;
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
 * REST controller for managing {@link com.apptium.domain.OrdContract}.
 */
@RestController
@RequestMapping("/api")
public class OrdContractResource {

    private final Logger log = LoggerFactory.getLogger(OrdContractResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdContractService ordContractService;

    private final OrdContractRepository ordContractRepository;

    public OrdContractResource(OrdContractService ordContractService, OrdContractRepository ordContractRepository) {
        this.ordContractService = ordContractService;
        this.ordContractRepository = ordContractRepository;
    }

    /**
     * {@code POST  /ord-contracts} : Create a new ordContract.
     *
     * @param ordContract the ordContract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordContract, or with status {@code 400 (Bad Request)} if the ordContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-contracts")
    public ResponseEntity<OrdContract> createOrdContract(@RequestBody OrdContract ordContract) throws URISyntaxException {
        log.debug("REST request to save OrdContract : {}", ordContract);
        if (ordContract.getId() != null) {
            throw new BadRequestAlertException("A new ordContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdContract result = ordContractService.save(ordContract);
        return ResponseEntity
            .created(new URI("/api/ord-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-contracts/:id} : Updates an existing ordContract.
     *
     * @param id the id of the ordContract to save.
     * @param ordContract the ordContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContract,
     * or with status {@code 400 (Bad Request)} if the ordContract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-contracts/{id}")
    public ResponseEntity<OrdContract> updateOrdContract(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContract ordContract
    ) throws URISyntaxException {
        log.debug("REST request to update OrdContract : {}, {}", id, ordContract);
        if (ordContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdContract result = ordContractService.save(ordContract);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContract.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-contracts/:id} : Partial updates given fields of an existing ordContract, field will ignore if it is null
     *
     * @param id the id of the ordContract to save.
     * @param ordContract the ordContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordContract,
     * or with status {@code 400 (Bad Request)} if the ordContract is not valid,
     * or with status {@code 404 (Not Found)} if the ordContract is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-contracts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdContract> partialUpdateOrdContract(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdContract ordContract
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdContract partially : {}, {}", id, ordContract);
        if (ordContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdContract> result = ordContractService.partialUpdate(ordContract);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordContract.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-contracts} : get all the ordContracts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordContracts in body.
     */
    @GetMapping("/ord-contracts")
    public List<OrdContract> getAllOrdContracts() {
        log.debug("REST request to get all OrdContracts");
        return ordContractService.findAll();
    }

    /**
     * {@code GET  /ord-contracts/:id} : get the "id" ordContract.
     *
     * @param id the id of the ordContract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordContract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-contracts/{id}")
    public ResponseEntity<OrdContract> getOrdContract(@PathVariable Long id) {
        log.debug("REST request to get OrdContract : {}", id);
        Optional<OrdContract> ordContract = ordContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordContract);
    }

    /**
     * {@code DELETE  /ord-contracts/:id} : delete the "id" ordContract.
     *
     * @param id the id of the ordContract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-contracts/{id}")
    public ResponseEntity<Void> deleteOrdContract(@PathVariable Long id) {
        log.debug("REST request to delete OrdContract : {}", id);
        ordContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

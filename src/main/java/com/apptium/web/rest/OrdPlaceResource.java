package com.apptium.web.rest;

import com.apptium.domain.OrdPlace;
import com.apptium.repository.OrdPlaceRepository;
import com.apptium.service.OrdPlaceService;
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
 * REST controller for managing {@link com.apptium.domain.OrdPlace}.
 */
@RestController
@RequestMapping("/api")
public class OrdPlaceResource {

    private final Logger log = LoggerFactory.getLogger(OrdPlaceResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdPlace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdPlaceService ordPlaceService;

    private final OrdPlaceRepository ordPlaceRepository;

    public OrdPlaceResource(OrdPlaceService ordPlaceService, OrdPlaceRepository ordPlaceRepository) {
        this.ordPlaceService = ordPlaceService;
        this.ordPlaceRepository = ordPlaceRepository;
    }

    /**
     * {@code POST  /ord-places} : Create a new ordPlace.
     *
     * @param ordPlace the ordPlace to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordPlace, or with status {@code 400 (Bad Request)} if the ordPlace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-places")
    public ResponseEntity<OrdPlace> createOrdPlace(@RequestBody OrdPlace ordPlace) throws URISyntaxException {
        log.debug("REST request to save OrdPlace : {}", ordPlace);
        if (ordPlace.getId() != null) {
            throw new BadRequestAlertException("A new ordPlace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdPlace result = ordPlaceService.save(ordPlace);
        return ResponseEntity
            .created(new URI("/api/ord-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-places/:id} : Updates an existing ordPlace.
     *
     * @param id the id of the ordPlace to save.
     * @param ordPlace the ordPlace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPlace,
     * or with status {@code 400 (Bad Request)} if the ordPlace is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordPlace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-places/{id}")
    public ResponseEntity<OrdPlace> updateOrdPlace(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPlace ordPlace
    ) throws URISyntaxException {
        log.debug("REST request to update OrdPlace : {}, {}", id, ordPlace);
        if (ordPlace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPlace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPlaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdPlace result = ordPlaceService.save(ordPlace);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPlace.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-places/:id} : Partial updates given fields of an existing ordPlace, field will ignore if it is null
     *
     * @param id the id of the ordPlace to save.
     * @param ordPlace the ordPlace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordPlace,
     * or with status {@code 400 (Bad Request)} if the ordPlace is not valid,
     * or with status {@code 404 (Not Found)} if the ordPlace is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordPlace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-places/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdPlace> partialUpdateOrdPlace(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdPlace ordPlace
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdPlace partially : {}, {}", id, ordPlace);
        if (ordPlace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordPlace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordPlaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdPlace> result = ordPlaceService.partialUpdate(ordPlace);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordPlace.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-places} : get all the ordPlaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordPlaces in body.
     */
    @GetMapping("/ord-places")
    public List<OrdPlace> getAllOrdPlaces() {
        log.debug("REST request to get all OrdPlaces");
        return ordPlaceService.findAll();
    }

    /**
     * {@code GET  /ord-places/:id} : get the "id" ordPlace.
     *
     * @param id the id of the ordPlace to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordPlace, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-places/{id}")
    public ResponseEntity<OrdPlace> getOrdPlace(@PathVariable Long id) {
        log.debug("REST request to get OrdPlace : {}", id);
        Optional<OrdPlace> ordPlace = ordPlaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordPlace);
    }

    /**
     * {@code DELETE  /ord-places/:id} : delete the "id" ordPlace.
     *
     * @param id the id of the ordPlace to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-places/{id}")
    public ResponseEntity<Void> deleteOrdPlace(@PathVariable Long id) {
        log.debug("REST request to delete OrdPlace : {}", id);
        ordPlaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.apptium.web.rest;

import com.apptium.domain.OrdNote;
import com.apptium.repository.OrdNoteRepository;
import com.apptium.service.OrdNoteService;
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
 * REST controller for managing {@link com.apptium.domain.OrdNote}.
 */
@RestController
@RequestMapping("/api")
public class OrdNoteResource {

    private final Logger log = LoggerFactory.getLogger(OrdNoteResource.class);

    private static final String ENTITY_NAME = "orderMgmtOrdNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdNoteService ordNoteService;

    private final OrdNoteRepository ordNoteRepository;

    public OrdNoteResource(OrdNoteService ordNoteService, OrdNoteRepository ordNoteRepository) {
        this.ordNoteService = ordNoteService;
        this.ordNoteRepository = ordNoteRepository;
    }

    /**
     * {@code POST  /ord-notes} : Create a new ordNote.
     *
     * @param ordNote the ordNote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordNote, or with status {@code 400 (Bad Request)} if the ordNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ord-notes")
    public ResponseEntity<OrdNote> createOrdNote(@RequestBody OrdNote ordNote) throws URISyntaxException {
        log.debug("REST request to save OrdNote : {}", ordNote);
        if (ordNote.getId() != null) {
            throw new BadRequestAlertException("A new ordNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdNote result = ordNoteService.save(ordNote);
        return ResponseEntity
            .created(new URI("/api/ord-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ord-notes/:id} : Updates an existing ordNote.
     *
     * @param id the id of the ordNote to save.
     * @param ordNote the ordNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordNote,
     * or with status {@code 400 (Bad Request)} if the ordNote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ord-notes/{id}")
    public ResponseEntity<OrdNote> updateOrdNote(@PathVariable(value = "id", required = false) final Long id, @RequestBody OrdNote ordNote)
        throws URISyntaxException {
        log.debug("REST request to update OrdNote : {}, {}", id, ordNote);
        if (ordNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdNote result = ordNoteService.save(ordNote);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordNote.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ord-notes/:id} : Partial updates given fields of an existing ordNote, field will ignore if it is null
     *
     * @param id the id of the ordNote to save.
     * @param ordNote the ordNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordNote,
     * or with status {@code 400 (Bad Request)} if the ordNote is not valid,
     * or with status {@code 404 (Not Found)} if the ordNote is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ord-notes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrdNote> partialUpdateOrdNote(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdNote ordNote
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdNote partially : {}, {}", id, ordNote);
        if (ordNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdNote> result = ordNoteService.partialUpdate(ordNote);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordNote.getId().toString())
        );
    }

    /**
     * {@code GET  /ord-notes} : get all the ordNotes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordNotes in body.
     */
    @GetMapping("/ord-notes")
    public List<OrdNote> getAllOrdNotes(@RequestParam(required = false) String filter) {
        if ("ordproductorder-is-null".equals(filter)) {
            log.debug("REST request to get all OrdNotes where ordProductOrder is null");
            return ordNoteService.findAllWhereOrdProductOrderIsNull();
        }
        log.debug("REST request to get all OrdNotes");
        return ordNoteService.findAll();
    }

    /**
     * {@code GET  /ord-notes/:id} : get the "id" ordNote.
     *
     * @param id the id of the ordNote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordNote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ord-notes/{id}")
    public ResponseEntity<OrdNote> getOrdNote(@PathVariable Long id) {
        log.debug("REST request to get OrdNote : {}", id);
        Optional<OrdNote> ordNote = ordNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordNote);
    }

    /**
     * {@code DELETE  /ord-notes/:id} : delete the "id" ordNote.
     *
     * @param id the id of the ordNote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ord-notes/{id}")
    public ResponseEntity<Void> deleteOrdNote(@PathVariable Long id) {
        log.debug("REST request to delete OrdNote : {}", id);
        ordNoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

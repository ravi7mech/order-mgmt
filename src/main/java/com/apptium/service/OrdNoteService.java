package com.apptium.service;

import com.apptium.domain.OrdNote;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdNote}.
 */
public interface OrdNoteService {
    /**
     * Save a ordNote.
     *
     * @param ordNote the entity to save.
     * @return the persisted entity.
     */
    OrdNote save(OrdNote ordNote);

    /**
     * Partially updates a ordNote.
     *
     * @param ordNote the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdNote> partialUpdate(OrdNote ordNote);

    /**
     * Get all the ordNotes.
     *
     * @return the list of entities.
     */
    List<OrdNote> findAll();
    /**
     * Get all the OrdNote where OrdProductOrder is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdNote> findAllWhereOrdProductOrderIsNull();

    /**
     * Get the "id" ordNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdNote> findOne(Long id);

    /**
     * Delete the "id" ordNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

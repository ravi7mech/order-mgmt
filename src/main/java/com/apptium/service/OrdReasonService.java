package com.apptium.service;

import com.apptium.domain.OrdReason;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdReason}.
 */
public interface OrdReasonService {
    /**
     * Save a ordReason.
     *
     * @param ordReason the entity to save.
     * @return the persisted entity.
     */
    OrdReason save(OrdReason ordReason);

    /**
     * Partially updates a ordReason.
     *
     * @param ordReason the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdReason> partialUpdate(OrdReason ordReason);

    /**
     * Get all the ordReasons.
     *
     * @return the list of entities.
     */
    List<OrdReason> findAll();

    /**
     * Get the "id" ordReason.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdReason> findOne(Long id);

    /**
     * Delete the "id" ordReason.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

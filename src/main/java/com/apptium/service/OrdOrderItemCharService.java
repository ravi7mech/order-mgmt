package com.apptium.service;

import com.apptium.domain.OrdOrderItemChar;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdOrderItemChar}.
 */
public interface OrdOrderItemCharService {
    /**
     * Save a ordOrderItemChar.
     *
     * @param ordOrderItemChar the entity to save.
     * @return the persisted entity.
     */
    OrdOrderItemChar save(OrdOrderItemChar ordOrderItemChar);

    /**
     * Partially updates a ordOrderItemChar.
     *
     * @param ordOrderItemChar the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdOrderItemChar> partialUpdate(OrdOrderItemChar ordOrderItemChar);

    /**
     * Get all the ordOrderItemChars.
     *
     * @return the list of entities.
     */
    List<OrdOrderItemChar> findAll();

    /**
     * Get the "id" ordOrderItemChar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdOrderItemChar> findOne(Long id);

    /**
     * Delete the "id" ordOrderItemChar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

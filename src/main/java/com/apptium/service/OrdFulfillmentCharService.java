package com.apptium.service;

import com.apptium.domain.OrdFulfillmentChar;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdFulfillmentChar}.
 */
public interface OrdFulfillmentCharService {
    /**
     * Save a ordFulfillmentChar.
     *
     * @param ordFulfillmentChar the entity to save.
     * @return the persisted entity.
     */
    OrdFulfillmentChar save(OrdFulfillmentChar ordFulfillmentChar);

    /**
     * Partially updates a ordFulfillmentChar.
     *
     * @param ordFulfillmentChar the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdFulfillmentChar> partialUpdate(OrdFulfillmentChar ordFulfillmentChar);

    /**
     * Get all the ordFulfillmentChars.
     *
     * @return the list of entities.
     */
    List<OrdFulfillmentChar> findAll();

    /**
     * Get the "id" ordFulfillmentChar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdFulfillmentChar> findOne(Long id);

    /**
     * Delete the "id" ordFulfillmentChar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

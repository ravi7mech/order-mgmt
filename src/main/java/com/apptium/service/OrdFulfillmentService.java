package com.apptium.service;

import com.apptium.domain.OrdFulfillment;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdFulfillment}.
 */
public interface OrdFulfillmentService {
    /**
     * Save a ordFulfillment.
     *
     * @param ordFulfillment the entity to save.
     * @return the persisted entity.
     */
    OrdFulfillment save(OrdFulfillment ordFulfillment);

    /**
     * Partially updates a ordFulfillment.
     *
     * @param ordFulfillment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdFulfillment> partialUpdate(OrdFulfillment ordFulfillment);

    /**
     * Get all the ordFulfillments.
     *
     * @return the list of entities.
     */
    List<OrdFulfillment> findAll();

    /**
     * Get the "id" ordFulfillment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdFulfillment> findOne(Long id);

    /**
     * Delete the "id" ordFulfillment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.apptium.service;

import com.apptium.domain.OrdOrderItem;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdOrderItem}.
 */
public interface OrdOrderItemService {
    /**
     * Save a ordOrderItem.
     *
     * @param ordOrderItem the entity to save.
     * @return the persisted entity.
     */
    OrdOrderItem save(OrdOrderItem ordOrderItem);

    /**
     * Partially updates a ordOrderItem.
     *
     * @param ordOrderItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdOrderItem> partialUpdate(OrdOrderItem ordOrderItem);

    /**
     * Get all the ordOrderItems.
     *
     * @return the list of entities.
     */
    List<OrdOrderItem> findAll();

    /**
     * Get the "id" ordOrderItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdOrderItem> findOne(Long id);

    /**
     * Delete the "id" ordOrderItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

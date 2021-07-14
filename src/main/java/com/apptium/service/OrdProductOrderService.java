package com.apptium.service;

import com.apptium.domain.OrdProductOrder;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdProductOrder}.
 */
public interface OrdProductOrderService {
    /**
     * Save a ordProductOrder.
     *
     * @param ordProductOrder the entity to save.
     * @return the persisted entity.
     */
    OrdProductOrder save(OrdProductOrder ordProductOrder);

    /**
     * Partially updates a ordProductOrder.
     *
     * @param ordProductOrder the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdProductOrder> partialUpdate(OrdProductOrder ordProductOrder);

    /**
     * Get all the ordProductOrders.
     *
     * @return the list of entities.
     */
    List<OrdProductOrder> findAll();

    /**
     * Get the "id" ordProductOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdProductOrder> findOne(Long id);

    /**
     * Delete the "id" ordProductOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

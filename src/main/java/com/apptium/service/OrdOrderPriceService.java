package com.apptium.service;

import com.apptium.domain.OrdOrderPrice;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdOrderPrice}.
 */
public interface OrdOrderPriceService {
    /**
     * Save a ordOrderPrice.
     *
     * @param ordOrderPrice the entity to save.
     * @return the persisted entity.
     */
    OrdOrderPrice save(OrdOrderPrice ordOrderPrice);

    /**
     * Partially updates a ordOrderPrice.
     *
     * @param ordOrderPrice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdOrderPrice> partialUpdate(OrdOrderPrice ordOrderPrice);

    /**
     * Get all the ordOrderPrices.
     *
     * @return the list of entities.
     */
    List<OrdOrderPrice> findAll();
    /**
     * Get all the OrdOrderPrice where OrdProductOrder is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdOrderPrice> findAllWhereOrdProductOrderIsNull();
    /**
     * Get all the OrdOrderPrice where OrdOrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdOrderPrice> findAllWhereOrdOrderItemIsNull();

    /**
     * Get the "id" ordOrderPrice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdOrderPrice> findOne(Long id);

    /**
     * Delete the "id" ordOrderPrice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

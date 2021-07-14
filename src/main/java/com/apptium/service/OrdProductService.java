package com.apptium.service;

import com.apptium.domain.OrdProduct;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdProduct}.
 */
public interface OrdProductService {
    /**
     * Save a ordProduct.
     *
     * @param ordProduct the entity to save.
     * @return the persisted entity.
     */
    OrdProduct save(OrdProduct ordProduct);

    /**
     * Partially updates a ordProduct.
     *
     * @param ordProduct the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdProduct> partialUpdate(OrdProduct ordProduct);

    /**
     * Get all the ordProducts.
     *
     * @return the list of entities.
     */
    List<OrdProduct> findAll();
    /**
     * Get all the OrdProduct where OrdOrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdProduct> findAllWhereOrdOrderItemIsNull();

    /**
     * Get the "id" ordProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdProduct> findOne(Long id);

    /**
     * Delete the "id" ordProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

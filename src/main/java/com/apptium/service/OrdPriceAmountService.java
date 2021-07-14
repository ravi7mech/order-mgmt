package com.apptium.service;

import com.apptium.domain.OrdPriceAmount;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdPriceAmount}.
 */
public interface OrdPriceAmountService {
    /**
     * Save a ordPriceAmount.
     *
     * @param ordPriceAmount the entity to save.
     * @return the persisted entity.
     */
    OrdPriceAmount save(OrdPriceAmount ordPriceAmount);

    /**
     * Partially updates a ordPriceAmount.
     *
     * @param ordPriceAmount the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdPriceAmount> partialUpdate(OrdPriceAmount ordPriceAmount);

    /**
     * Get all the ordPriceAmounts.
     *
     * @return the list of entities.
     */
    List<OrdPriceAmount> findAll();
    /**
     * Get all the OrdPriceAmount where OrdOrderPrice is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdPriceAmount> findAllWhereOrdOrderPriceIsNull();
    /**
     * Get all the OrdPriceAmount where OrdPriceAlteration is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdPriceAmount> findAllWhereOrdPriceAlterationIsNull();

    /**
     * Get the "id" ordPriceAmount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdPriceAmount> findOne(Long id);

    /**
     * Delete the "id" ordPriceAmount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

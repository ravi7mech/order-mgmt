package com.apptium.service;

import com.apptium.domain.OrdPriceAlteration;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdPriceAlteration}.
 */
public interface OrdPriceAlterationService {
    /**
     * Save a ordPriceAlteration.
     *
     * @param ordPriceAlteration the entity to save.
     * @return the persisted entity.
     */
    OrdPriceAlteration save(OrdPriceAlteration ordPriceAlteration);

    /**
     * Partially updates a ordPriceAlteration.
     *
     * @param ordPriceAlteration the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdPriceAlteration> partialUpdate(OrdPriceAlteration ordPriceAlteration);

    /**
     * Get all the ordPriceAlterations.
     *
     * @return the list of entities.
     */
    List<OrdPriceAlteration> findAll();

    /**
     * Get the "id" ordPriceAlteration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdPriceAlteration> findOne(Long id);

    /**
     * Delete the "id" ordPriceAlteration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

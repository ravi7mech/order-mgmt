package com.apptium.service;

import com.apptium.domain.OrdCharacteristics;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdCharacteristics}.
 */
public interface OrdCharacteristicsService {
    /**
     * Save a ordCharacteristics.
     *
     * @param ordCharacteristics the entity to save.
     * @return the persisted entity.
     */
    OrdCharacteristics save(OrdCharacteristics ordCharacteristics);

    /**
     * Partially updates a ordCharacteristics.
     *
     * @param ordCharacteristics the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdCharacteristics> partialUpdate(OrdCharacteristics ordCharacteristics);

    /**
     * Get all the ordCharacteristics.
     *
     * @return the list of entities.
     */
    List<OrdCharacteristics> findAll();

    /**
     * Get the "id" ordCharacteristics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdCharacteristics> findOne(Long id);

    /**
     * Delete the "id" ordCharacteristics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

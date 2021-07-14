package com.apptium.service;

import com.apptium.domain.OrdProductCharacteristics;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdProductCharacteristics}.
 */
public interface OrdProductCharacteristicsService {
    /**
     * Save a ordProductCharacteristics.
     *
     * @param ordProductCharacteristics the entity to save.
     * @return the persisted entity.
     */
    OrdProductCharacteristics save(OrdProductCharacteristics ordProductCharacteristics);

    /**
     * Partially updates a ordProductCharacteristics.
     *
     * @param ordProductCharacteristics the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdProductCharacteristics> partialUpdate(OrdProductCharacteristics ordProductCharacteristics);

    /**
     * Get all the ordProductCharacteristics.
     *
     * @return the list of entities.
     */
    List<OrdProductCharacteristics> findAll();
    /**
     * Get all the OrdProductCharacteristics where OrdProduct is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdProductCharacteristics> findAllWhereOrdProductIsNull();

    /**
     * Get the "id" ordProductCharacteristics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdProductCharacteristics> findOne(Long id);

    /**
     * Delete the "id" ordProductCharacteristics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

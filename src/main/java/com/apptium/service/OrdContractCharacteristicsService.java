package com.apptium.service;

import com.apptium.domain.OrdContractCharacteristics;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdContractCharacteristics}.
 */
public interface OrdContractCharacteristicsService {
    /**
     * Save a ordContractCharacteristics.
     *
     * @param ordContractCharacteristics the entity to save.
     * @return the persisted entity.
     */
    OrdContractCharacteristics save(OrdContractCharacteristics ordContractCharacteristics);

    /**
     * Partially updates a ordContractCharacteristics.
     *
     * @param ordContractCharacteristics the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdContractCharacteristics> partialUpdate(OrdContractCharacteristics ordContractCharacteristics);

    /**
     * Get all the ordContractCharacteristics.
     *
     * @return the list of entities.
     */
    List<OrdContractCharacteristics> findAll();

    /**
     * Get the "id" ordContractCharacteristics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdContractCharacteristics> findOne(Long id);

    /**
     * Delete the "id" ordContractCharacteristics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

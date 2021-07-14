package com.apptium.service;

import com.apptium.domain.OrdContract;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdContract}.
 */
public interface OrdContractService {
    /**
     * Save a ordContract.
     *
     * @param ordContract the entity to save.
     * @return the persisted entity.
     */
    OrdContract save(OrdContract ordContract);

    /**
     * Partially updates a ordContract.
     *
     * @param ordContract the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdContract> partialUpdate(OrdContract ordContract);

    /**
     * Get all the ordContracts.
     *
     * @return the list of entities.
     */
    List<OrdContract> findAll();

    /**
     * Get the "id" ordContract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdContract> findOne(Long id);

    /**
     * Delete the "id" ordContract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

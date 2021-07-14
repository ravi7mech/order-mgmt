package com.apptium.service;

import com.apptium.domain.OrdAcquisition;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdAcquisition}.
 */
public interface OrdAcquisitionService {
    /**
     * Save a ordAcquisition.
     *
     * @param ordAcquisition the entity to save.
     * @return the persisted entity.
     */
    OrdAcquisition save(OrdAcquisition ordAcquisition);

    /**
     * Partially updates a ordAcquisition.
     *
     * @param ordAcquisition the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdAcquisition> partialUpdate(OrdAcquisition ordAcquisition);

    /**
     * Get all the ordAcquisitions.
     *
     * @return the list of entities.
     */
    List<OrdAcquisition> findAll();

    /**
     * Get the "id" ordAcquisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdAcquisition> findOne(Long id);

    /**
     * Delete the "id" ordAcquisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

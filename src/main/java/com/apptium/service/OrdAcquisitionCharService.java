package com.apptium.service;

import com.apptium.domain.OrdAcquisitionChar;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdAcquisitionChar}.
 */
public interface OrdAcquisitionCharService {
    /**
     * Save a ordAcquisitionChar.
     *
     * @param ordAcquisitionChar the entity to save.
     * @return the persisted entity.
     */
    OrdAcquisitionChar save(OrdAcquisitionChar ordAcquisitionChar);

    /**
     * Partially updates a ordAcquisitionChar.
     *
     * @param ordAcquisitionChar the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdAcquisitionChar> partialUpdate(OrdAcquisitionChar ordAcquisitionChar);

    /**
     * Get all the ordAcquisitionChars.
     *
     * @return the list of entities.
     */
    List<OrdAcquisitionChar> findAll();

    /**
     * Get the "id" ordAcquisitionChar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdAcquisitionChar> findOne(Long id);

    /**
     * Delete the "id" ordAcquisitionChar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.apptium.service;

import com.apptium.domain.OrdProvisiongChar;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdProvisiongChar}.
 */
public interface OrdProvisiongCharService {
    /**
     * Save a ordProvisiongChar.
     *
     * @param ordProvisiongChar the entity to save.
     * @return the persisted entity.
     */
    OrdProvisiongChar save(OrdProvisiongChar ordProvisiongChar);

    /**
     * Partially updates a ordProvisiongChar.
     *
     * @param ordProvisiongChar the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdProvisiongChar> partialUpdate(OrdProvisiongChar ordProvisiongChar);

    /**
     * Get all the ordProvisiongChars.
     *
     * @return the list of entities.
     */
    List<OrdProvisiongChar> findAll();

    /**
     * Get the "id" ordProvisiongChar.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdProvisiongChar> findOne(Long id);

    /**
     * Delete the "id" ordProvisiongChar.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

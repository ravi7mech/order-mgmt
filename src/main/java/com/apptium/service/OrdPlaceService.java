package com.apptium.service;

import com.apptium.domain.OrdPlace;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdPlace}.
 */
public interface OrdPlaceService {
    /**
     * Save a ordPlace.
     *
     * @param ordPlace the entity to save.
     * @return the persisted entity.
     */
    OrdPlace save(OrdPlace ordPlace);

    /**
     * Partially updates a ordPlace.
     *
     * @param ordPlace the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdPlace> partialUpdate(OrdPlace ordPlace);

    /**
     * Get all the ordPlaces.
     *
     * @return the list of entities.
     */
    List<OrdPlace> findAll();

    /**
     * Get the "id" ordPlace.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdPlace> findOne(Long id);

    /**
     * Delete the "id" ordPlace.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

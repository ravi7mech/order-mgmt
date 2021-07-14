package com.apptium.service;

import com.apptium.domain.OrdContactDetails;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdContactDetails}.
 */
public interface OrdContactDetailsService {
    /**
     * Save a ordContactDetails.
     *
     * @param ordContactDetails the entity to save.
     * @return the persisted entity.
     */
    OrdContactDetails save(OrdContactDetails ordContactDetails);

    /**
     * Partially updates a ordContactDetails.
     *
     * @param ordContactDetails the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdContactDetails> partialUpdate(OrdContactDetails ordContactDetails);

    /**
     * Get all the ordContactDetails.
     *
     * @return the list of entities.
     */
    List<OrdContactDetails> findAll();
    /**
     * Get all the OrdContactDetails where OrdProductOrder is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdContactDetails> findAllWhereOrdProductOrderIsNull();

    /**
     * Get the "id" ordContactDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdContactDetails> findOne(Long id);

    /**
     * Delete the "id" ordContactDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

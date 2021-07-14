package com.apptium.service;

import com.apptium.domain.OrdPaymentRef;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdPaymentRef}.
 */
public interface OrdPaymentRefService {
    /**
     * Save a ordPaymentRef.
     *
     * @param ordPaymentRef the entity to save.
     * @return the persisted entity.
     */
    OrdPaymentRef save(OrdPaymentRef ordPaymentRef);

    /**
     * Partially updates a ordPaymentRef.
     *
     * @param ordPaymentRef the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdPaymentRef> partialUpdate(OrdPaymentRef ordPaymentRef);

    /**
     * Get all the ordPaymentRefs.
     *
     * @return the list of entities.
     */
    List<OrdPaymentRef> findAll();

    /**
     * Get the "id" ordPaymentRef.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdPaymentRef> findOne(Long id);

    /**
     * Delete the "id" ordPaymentRef.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

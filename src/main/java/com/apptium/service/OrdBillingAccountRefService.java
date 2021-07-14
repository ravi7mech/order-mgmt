package com.apptium.service;

import com.apptium.domain.OrdBillingAccountRef;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdBillingAccountRef}.
 */
public interface OrdBillingAccountRefService {
    /**
     * Save a ordBillingAccountRef.
     *
     * @param ordBillingAccountRef the entity to save.
     * @return the persisted entity.
     */
    OrdBillingAccountRef save(OrdBillingAccountRef ordBillingAccountRef);

    /**
     * Partially updates a ordBillingAccountRef.
     *
     * @param ordBillingAccountRef the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdBillingAccountRef> partialUpdate(OrdBillingAccountRef ordBillingAccountRef);

    /**
     * Get all the ordBillingAccountRefs.
     *
     * @return the list of entities.
     */
    List<OrdBillingAccountRef> findAll();
    /**
     * Get all the OrdBillingAccountRef where OrdProductOrder is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdBillingAccountRef> findAllWhereOrdProductOrderIsNull();

    /**
     * Get the "id" ordBillingAccountRef.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdBillingAccountRef> findOne(Long id);

    /**
     * Delete the "id" ordBillingAccountRef.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

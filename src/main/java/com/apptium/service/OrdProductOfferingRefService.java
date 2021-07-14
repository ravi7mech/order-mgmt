package com.apptium.service;

import com.apptium.domain.OrdProductOfferingRef;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdProductOfferingRef}.
 */
public interface OrdProductOfferingRefService {
    /**
     * Save a ordProductOfferingRef.
     *
     * @param ordProductOfferingRef the entity to save.
     * @return the persisted entity.
     */
    OrdProductOfferingRef save(OrdProductOfferingRef ordProductOfferingRef);

    /**
     * Partially updates a ordProductOfferingRef.
     *
     * @param ordProductOfferingRef the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdProductOfferingRef> partialUpdate(OrdProductOfferingRef ordProductOfferingRef);

    /**
     * Get all the ordProductOfferingRefs.
     *
     * @return the list of entities.
     */
    List<OrdProductOfferingRef> findAll();
    /**
     * Get all the OrdProductOfferingRef where OrdOrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdProductOfferingRef> findAllWhereOrdOrderItemIsNull();

    /**
     * Get the "id" ordProductOfferingRef.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdProductOfferingRef> findOne(Long id);

    /**
     * Delete the "id" ordProductOfferingRef.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

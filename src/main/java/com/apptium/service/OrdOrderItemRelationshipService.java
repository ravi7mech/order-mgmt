package com.apptium.service;

import com.apptium.domain.OrdOrderItemRelationship;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdOrderItemRelationship}.
 */
public interface OrdOrderItemRelationshipService {
    /**
     * Save a ordOrderItemRelationship.
     *
     * @param ordOrderItemRelationship the entity to save.
     * @return the persisted entity.
     */
    OrdOrderItemRelationship save(OrdOrderItemRelationship ordOrderItemRelationship);

    /**
     * Partially updates a ordOrderItemRelationship.
     *
     * @param ordOrderItemRelationship the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdOrderItemRelationship> partialUpdate(OrdOrderItemRelationship ordOrderItemRelationship);

    /**
     * Get all the ordOrderItemRelationships.
     *
     * @return the list of entities.
     */
    List<OrdOrderItemRelationship> findAll();
    /**
     * Get all the OrdOrderItemRelationship where OrdOrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdOrderItemRelationship> findAllWhereOrdOrderItemIsNull();

    /**
     * Get the "id" ordOrderItemRelationship.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdOrderItemRelationship> findOne(Long id);

    /**
     * Delete the "id" ordOrderItemRelationship.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

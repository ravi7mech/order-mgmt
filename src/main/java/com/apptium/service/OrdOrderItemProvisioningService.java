package com.apptium.service;

import com.apptium.domain.OrdOrderItemProvisioning;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdOrderItemProvisioning}.
 */
public interface OrdOrderItemProvisioningService {
    /**
     * Save a ordOrderItemProvisioning.
     *
     * @param ordOrderItemProvisioning the entity to save.
     * @return the persisted entity.
     */
    OrdOrderItemProvisioning save(OrdOrderItemProvisioning ordOrderItemProvisioning);

    /**
     * Partially updates a ordOrderItemProvisioning.
     *
     * @param ordOrderItemProvisioning the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdOrderItemProvisioning> partialUpdate(OrdOrderItemProvisioning ordOrderItemProvisioning);

    /**
     * Get all the ordOrderItemProvisionings.
     *
     * @return the list of entities.
     */
    List<OrdOrderItemProvisioning> findAll();
    /**
     * Get all the OrdOrderItemProvisioning where OrdOrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdOrderItemProvisioning> findAllWhereOrdOrderItemIsNull();

    /**
     * Get the "id" ordOrderItemProvisioning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdOrderItemProvisioning> findOne(Long id);

    /**
     * Delete the "id" ordOrderItemProvisioning.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

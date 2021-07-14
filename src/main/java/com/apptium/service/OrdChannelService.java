package com.apptium.service;

import com.apptium.domain.OrdChannel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrdChannel}.
 */
public interface OrdChannelService {
    /**
     * Save a ordChannel.
     *
     * @param ordChannel the entity to save.
     * @return the persisted entity.
     */
    OrdChannel save(OrdChannel ordChannel);

    /**
     * Partially updates a ordChannel.
     *
     * @param ordChannel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdChannel> partialUpdate(OrdChannel ordChannel);

    /**
     * Get all the ordChannels.
     *
     * @return the list of entities.
     */
    List<OrdChannel> findAll();
    /**
     * Get all the OrdChannel where OrdProductOrder is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrdChannel> findAllWhereOrdProductOrderIsNull();

    /**
     * Get the "id" ordChannel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdChannel> findOne(Long id);

    /**
     * Delete the "id" ordChannel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

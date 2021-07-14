package com.apptium.service.impl;

import com.apptium.domain.OrdOrderItemRelationship;
import com.apptium.repository.OrdOrderItemRelationshipRepository;
import com.apptium.service.OrdOrderItemRelationshipService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdOrderItemRelationship}.
 */
@Service
@Transactional
public class OrdOrderItemRelationshipServiceImpl implements OrdOrderItemRelationshipService {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemRelationshipServiceImpl.class);

    private final OrdOrderItemRelationshipRepository ordOrderItemRelationshipRepository;

    public OrdOrderItemRelationshipServiceImpl(OrdOrderItemRelationshipRepository ordOrderItemRelationshipRepository) {
        this.ordOrderItemRelationshipRepository = ordOrderItemRelationshipRepository;
    }

    @Override
    public OrdOrderItemRelationship save(OrdOrderItemRelationship ordOrderItemRelationship) {
        log.debug("Request to save OrdOrderItemRelationship : {}", ordOrderItemRelationship);
        return ordOrderItemRelationshipRepository.save(ordOrderItemRelationship);
    }

    @Override
    public Optional<OrdOrderItemRelationship> partialUpdate(OrdOrderItemRelationship ordOrderItemRelationship) {
        log.debug("Request to partially update OrdOrderItemRelationship : {}", ordOrderItemRelationship);

        return ordOrderItemRelationshipRepository
            .findById(ordOrderItemRelationship.getId())
            .map(
                existingOrdOrderItemRelationship -> {
                    if (ordOrderItemRelationship.getType() != null) {
                        existingOrdOrderItemRelationship.setType(ordOrderItemRelationship.getType());
                    }
                    if (ordOrderItemRelationship.getPrimaryOrderItemId() != null) {
                        existingOrdOrderItemRelationship.setPrimaryOrderItemId(ordOrderItemRelationship.getPrimaryOrderItemId());
                    }
                    if (ordOrderItemRelationship.getSecondaryOrderItemId() != null) {
                        existingOrdOrderItemRelationship.setSecondaryOrderItemId(ordOrderItemRelationship.getSecondaryOrderItemId());
                    }

                    return existingOrdOrderItemRelationship;
                }
            )
            .map(ordOrderItemRelationshipRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdOrderItemRelationship> findAll() {
        log.debug("Request to get all OrdOrderItemRelationships");
        return ordOrderItemRelationshipRepository.findAll();
    }

    /**
     *  Get all the ordOrderItemRelationships where OrdOrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdOrderItemRelationship> findAllWhereOrdOrderItemIsNull() {
        log.debug("Request to get all ordOrderItemRelationships where OrdOrderItem is null");
        return StreamSupport
            .stream(ordOrderItemRelationshipRepository.findAll().spliterator(), false)
            .filter(ordOrderItemRelationship -> ordOrderItemRelationship.getOrdOrderItem() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdOrderItemRelationship> findOne(Long id) {
        log.debug("Request to get OrdOrderItemRelationship : {}", id);
        return ordOrderItemRelationshipRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdOrderItemRelationship : {}", id);
        ordOrderItemRelationshipRepository.deleteById(id);
    }
}

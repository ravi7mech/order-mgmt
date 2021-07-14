package com.apptium.service.impl;

import com.apptium.domain.OrdOrderItemProvisioning;
import com.apptium.repository.OrdOrderItemProvisioningRepository;
import com.apptium.service.OrdOrderItemProvisioningService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdOrderItemProvisioning}.
 */
@Service
@Transactional
public class OrdOrderItemProvisioningServiceImpl implements OrdOrderItemProvisioningService {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemProvisioningServiceImpl.class);

    private final OrdOrderItemProvisioningRepository ordOrderItemProvisioningRepository;

    public OrdOrderItemProvisioningServiceImpl(OrdOrderItemProvisioningRepository ordOrderItemProvisioningRepository) {
        this.ordOrderItemProvisioningRepository = ordOrderItemProvisioningRepository;
    }

    @Override
    public OrdOrderItemProvisioning save(OrdOrderItemProvisioning ordOrderItemProvisioning) {
        log.debug("Request to save OrdOrderItemProvisioning : {}", ordOrderItemProvisioning);
        return ordOrderItemProvisioningRepository.save(ordOrderItemProvisioning);
    }

    @Override
    public Optional<OrdOrderItemProvisioning> partialUpdate(OrdOrderItemProvisioning ordOrderItemProvisioning) {
        log.debug("Request to partially update OrdOrderItemProvisioning : {}", ordOrderItemProvisioning);

        return ordOrderItemProvisioningRepository
            .findById(ordOrderItemProvisioning.getId())
            .map(
                existingOrdOrderItemProvisioning -> {
                    if (ordOrderItemProvisioning.getProvisioningId() != null) {
                        existingOrdOrderItemProvisioning.setProvisioningId(ordOrderItemProvisioning.getProvisioningId());
                    }
                    if (ordOrderItemProvisioning.getStatus() != null) {
                        existingOrdOrderItemProvisioning.setStatus(ordOrderItemProvisioning.getStatus());
                    }

                    return existingOrdOrderItemProvisioning;
                }
            )
            .map(ordOrderItemProvisioningRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdOrderItemProvisioning> findAll() {
        log.debug("Request to get all OrdOrderItemProvisionings");
        return ordOrderItemProvisioningRepository.findAll();
    }

    /**
     *  Get all the ordOrderItemProvisionings where OrdOrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdOrderItemProvisioning> findAllWhereOrdOrderItemIsNull() {
        log.debug("Request to get all ordOrderItemProvisionings where OrdOrderItem is null");
        return StreamSupport
            .stream(ordOrderItemProvisioningRepository.findAll().spliterator(), false)
            .filter(ordOrderItemProvisioning -> ordOrderItemProvisioning.getOrdOrderItem() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdOrderItemProvisioning> findOne(Long id) {
        log.debug("Request to get OrdOrderItemProvisioning : {}", id);
        return ordOrderItemProvisioningRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdOrderItemProvisioning : {}", id);
        ordOrderItemProvisioningRepository.deleteById(id);
    }
}

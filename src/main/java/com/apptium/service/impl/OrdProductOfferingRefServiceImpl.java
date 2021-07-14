package com.apptium.service.impl;

import com.apptium.domain.OrdProductOfferingRef;
import com.apptium.repository.OrdProductOfferingRefRepository;
import com.apptium.service.OrdProductOfferingRefService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdProductOfferingRef}.
 */
@Service
@Transactional
public class OrdProductOfferingRefServiceImpl implements OrdProductOfferingRefService {

    private final Logger log = LoggerFactory.getLogger(OrdProductOfferingRefServiceImpl.class);

    private final OrdProductOfferingRefRepository ordProductOfferingRefRepository;

    public OrdProductOfferingRefServiceImpl(OrdProductOfferingRefRepository ordProductOfferingRefRepository) {
        this.ordProductOfferingRefRepository = ordProductOfferingRefRepository;
    }

    @Override
    public OrdProductOfferingRef save(OrdProductOfferingRef ordProductOfferingRef) {
        log.debug("Request to save OrdProductOfferingRef : {}", ordProductOfferingRef);
        return ordProductOfferingRefRepository.save(ordProductOfferingRef);
    }

    @Override
    public Optional<OrdProductOfferingRef> partialUpdate(OrdProductOfferingRef ordProductOfferingRef) {
        log.debug("Request to partially update OrdProductOfferingRef : {}", ordProductOfferingRef);

        return ordProductOfferingRefRepository
            .findById(ordProductOfferingRef.getId())
            .map(
                existingOrdProductOfferingRef -> {
                    if (ordProductOfferingRef.getHref() != null) {
                        existingOrdProductOfferingRef.setHref(ordProductOfferingRef.getHref());
                    }
                    if (ordProductOfferingRef.getName() != null) {
                        existingOrdProductOfferingRef.setName(ordProductOfferingRef.getName());
                    }
                    if (ordProductOfferingRef.getProductGuid() != null) {
                        existingOrdProductOfferingRef.setProductGuid(ordProductOfferingRef.getProductGuid());
                    }

                    return existingOrdProductOfferingRef;
                }
            )
            .map(ordProductOfferingRefRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdProductOfferingRef> findAll() {
        log.debug("Request to get all OrdProductOfferingRefs");
        return ordProductOfferingRefRepository.findAll();
    }

    /**
     *  Get all the ordProductOfferingRefs where OrdOrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdProductOfferingRef> findAllWhereOrdOrderItemIsNull() {
        log.debug("Request to get all ordProductOfferingRefs where OrdOrderItem is null");
        return StreamSupport
            .stream(ordProductOfferingRefRepository.findAll().spliterator(), false)
            .filter(ordProductOfferingRef -> ordProductOfferingRef.getOrdOrderItem() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdProductOfferingRef> findOne(Long id) {
        log.debug("Request to get OrdProductOfferingRef : {}", id);
        return ordProductOfferingRefRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdProductOfferingRef : {}", id);
        ordProductOfferingRefRepository.deleteById(id);
    }
}

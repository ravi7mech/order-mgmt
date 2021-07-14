package com.apptium.service.impl;

import com.apptium.domain.OrdPaymentRef;
import com.apptium.repository.OrdPaymentRefRepository;
import com.apptium.service.OrdPaymentRefService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdPaymentRef}.
 */
@Service
@Transactional
public class OrdPaymentRefServiceImpl implements OrdPaymentRefService {

    private final Logger log = LoggerFactory.getLogger(OrdPaymentRefServiceImpl.class);

    private final OrdPaymentRefRepository ordPaymentRefRepository;

    public OrdPaymentRefServiceImpl(OrdPaymentRefRepository ordPaymentRefRepository) {
        this.ordPaymentRefRepository = ordPaymentRefRepository;
    }

    @Override
    public OrdPaymentRef save(OrdPaymentRef ordPaymentRef) {
        log.debug("Request to save OrdPaymentRef : {}", ordPaymentRef);
        return ordPaymentRefRepository.save(ordPaymentRef);
    }

    @Override
    public Optional<OrdPaymentRef> partialUpdate(OrdPaymentRef ordPaymentRef) {
        log.debug("Request to partially update OrdPaymentRef : {}", ordPaymentRef);

        return ordPaymentRefRepository
            .findById(ordPaymentRef.getId())
            .map(
                existingOrdPaymentRef -> {
                    if (ordPaymentRef.getPaymentId() != null) {
                        existingOrdPaymentRef.setPaymentId(ordPaymentRef.getPaymentId());
                    }
                    if (ordPaymentRef.getHref() != null) {
                        existingOrdPaymentRef.setHref(ordPaymentRef.getHref());
                    }
                    if (ordPaymentRef.getName() != null) {
                        existingOrdPaymentRef.setName(ordPaymentRef.getName());
                    }
                    if (ordPaymentRef.getPaymentAmount() != null) {
                        existingOrdPaymentRef.setPaymentAmount(ordPaymentRef.getPaymentAmount());
                    }
                    if (ordPaymentRef.getAction() != null) {
                        existingOrdPaymentRef.setAction(ordPaymentRef.getAction());
                    }
                    if (ordPaymentRef.getStatus() != null) {
                        existingOrdPaymentRef.setStatus(ordPaymentRef.getStatus());
                    }
                    if (ordPaymentRef.getEnrolRecurring() != null) {
                        existingOrdPaymentRef.setEnrolRecurring(ordPaymentRef.getEnrolRecurring());
                    }

                    return existingOrdPaymentRef;
                }
            )
            .map(ordPaymentRefRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdPaymentRef> findAll() {
        log.debug("Request to get all OrdPaymentRefs");
        return ordPaymentRefRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdPaymentRef> findOne(Long id) {
        log.debug("Request to get OrdPaymentRef : {}", id);
        return ordPaymentRefRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdPaymentRef : {}", id);
        ordPaymentRefRepository.deleteById(id);
    }
}

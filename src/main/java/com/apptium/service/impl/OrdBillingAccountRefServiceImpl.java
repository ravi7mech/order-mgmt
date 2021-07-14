package com.apptium.service.impl;

import com.apptium.domain.OrdBillingAccountRef;
import com.apptium.repository.OrdBillingAccountRefRepository;
import com.apptium.service.OrdBillingAccountRefService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdBillingAccountRef}.
 */
@Service
@Transactional
public class OrdBillingAccountRefServiceImpl implements OrdBillingAccountRefService {

    private final Logger log = LoggerFactory.getLogger(OrdBillingAccountRefServiceImpl.class);

    private final OrdBillingAccountRefRepository ordBillingAccountRefRepository;

    public OrdBillingAccountRefServiceImpl(OrdBillingAccountRefRepository ordBillingAccountRefRepository) {
        this.ordBillingAccountRefRepository = ordBillingAccountRefRepository;
    }

    @Override
    public OrdBillingAccountRef save(OrdBillingAccountRef ordBillingAccountRef) {
        log.debug("Request to save OrdBillingAccountRef : {}", ordBillingAccountRef);
        return ordBillingAccountRefRepository.save(ordBillingAccountRef);
    }

    @Override
    public Optional<OrdBillingAccountRef> partialUpdate(OrdBillingAccountRef ordBillingAccountRef) {
        log.debug("Request to partially update OrdBillingAccountRef : {}", ordBillingAccountRef);

        return ordBillingAccountRefRepository
            .findById(ordBillingAccountRef.getId())
            .map(
                existingOrdBillingAccountRef -> {
                    if (ordBillingAccountRef.getName() != null) {
                        existingOrdBillingAccountRef.setName(ordBillingAccountRef.getName());
                    }
                    if (ordBillingAccountRef.getHref() != null) {
                        existingOrdBillingAccountRef.setHref(ordBillingAccountRef.getHref());
                    }
                    if (ordBillingAccountRef.getCartPriceId() != null) {
                        existingOrdBillingAccountRef.setCartPriceId(ordBillingAccountRef.getCartPriceId());
                    }
                    if (ordBillingAccountRef.getBillingAccountId() != null) {
                        existingOrdBillingAccountRef.setBillingAccountId(ordBillingAccountRef.getBillingAccountId());
                    }
                    if (ordBillingAccountRef.getBillingSystem() != null) {
                        existingOrdBillingAccountRef.setBillingSystem(ordBillingAccountRef.getBillingSystem());
                    }
                    if (ordBillingAccountRef.getDeliveryMethod() != null) {
                        existingOrdBillingAccountRef.setDeliveryMethod(ordBillingAccountRef.getDeliveryMethod());
                    }
                    if (ordBillingAccountRef.getBillingAddress() != null) {
                        existingOrdBillingAccountRef.setBillingAddress(ordBillingAccountRef.getBillingAddress());
                    }
                    if (ordBillingAccountRef.getStatus() != null) {
                        existingOrdBillingAccountRef.setStatus(ordBillingAccountRef.getStatus());
                    }
                    if (ordBillingAccountRef.getQuoteId() != null) {
                        existingOrdBillingAccountRef.setQuoteId(ordBillingAccountRef.getQuoteId());
                    }
                    if (ordBillingAccountRef.getSalesOrderId() != null) {
                        existingOrdBillingAccountRef.setSalesOrderId(ordBillingAccountRef.getSalesOrderId());
                    }

                    return existingOrdBillingAccountRef;
                }
            )
            .map(ordBillingAccountRefRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdBillingAccountRef> findAll() {
        log.debug("Request to get all OrdBillingAccountRefs");
        return ordBillingAccountRefRepository.findAll();
    }

    /**
     *  Get all the ordBillingAccountRefs where OrdProductOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdBillingAccountRef> findAllWhereOrdProductOrderIsNull() {
        log.debug("Request to get all ordBillingAccountRefs where OrdProductOrder is null");
        return StreamSupport
            .stream(ordBillingAccountRefRepository.findAll().spliterator(), false)
            .filter(ordBillingAccountRef -> ordBillingAccountRef.getOrdProductOrder() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdBillingAccountRef> findOne(Long id) {
        log.debug("Request to get OrdBillingAccountRef : {}", id);
        return ordBillingAccountRefRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdBillingAccountRef : {}", id);
        ordBillingAccountRefRepository.deleteById(id);
    }
}

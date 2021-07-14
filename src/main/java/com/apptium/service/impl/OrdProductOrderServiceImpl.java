package com.apptium.service.impl;

import com.apptium.domain.OrdProductOrder;
import com.apptium.repository.OrdProductOrderRepository;
import com.apptium.service.OrdProductOrderService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdProductOrder}.
 */
@Service
@Transactional
public class OrdProductOrderServiceImpl implements OrdProductOrderService {

    private final Logger log = LoggerFactory.getLogger(OrdProductOrderServiceImpl.class);

    private final OrdProductOrderRepository ordProductOrderRepository;

    public OrdProductOrderServiceImpl(OrdProductOrderRepository ordProductOrderRepository) {
        this.ordProductOrderRepository = ordProductOrderRepository;
    }

    @Override
    public OrdProductOrder save(OrdProductOrder ordProductOrder) {
        log.debug("Request to save OrdProductOrder : {}", ordProductOrder);
        return ordProductOrderRepository.save(ordProductOrder);
    }

    @Override
    public Optional<OrdProductOrder> partialUpdate(OrdProductOrder ordProductOrder) {
        log.debug("Request to partially update OrdProductOrder : {}", ordProductOrder);

        return ordProductOrderRepository
            .findById(ordProductOrder.getId())
            .map(
                existingOrdProductOrder -> {
                    if (ordProductOrder.getHref() != null) {
                        existingOrdProductOrder.setHref(ordProductOrder.getHref());
                    }
                    if (ordProductOrder.getExternalId() != null) {
                        existingOrdProductOrder.setExternalId(ordProductOrder.getExternalId());
                    }
                    if (ordProductOrder.getPriority() != null) {
                        existingOrdProductOrder.setPriority(ordProductOrder.getPriority());
                    }
                    if (ordProductOrder.getDescription() != null) {
                        existingOrdProductOrder.setDescription(ordProductOrder.getDescription());
                    }
                    if (ordProductOrder.getCategory() != null) {
                        existingOrdProductOrder.setCategory(ordProductOrder.getCategory());
                    }
                    if (ordProductOrder.getStatus() != null) {
                        existingOrdProductOrder.setStatus(ordProductOrder.getStatus());
                    }
                    if (ordProductOrder.getOrderDate() != null) {
                        existingOrdProductOrder.setOrderDate(ordProductOrder.getOrderDate());
                    }
                    if (ordProductOrder.getCompletionDate() != null) {
                        existingOrdProductOrder.setCompletionDate(ordProductOrder.getCompletionDate());
                    }
                    if (ordProductOrder.getRequestedStartDate() != null) {
                        existingOrdProductOrder.setRequestedStartDate(ordProductOrder.getRequestedStartDate());
                    }
                    if (ordProductOrder.getRequestedCompletionDate() != null) {
                        existingOrdProductOrder.setRequestedCompletionDate(ordProductOrder.getRequestedCompletionDate());
                    }
                    if (ordProductOrder.getExpectedCompletionDate() != null) {
                        existingOrdProductOrder.setExpectedCompletionDate(ordProductOrder.getExpectedCompletionDate());
                    }
                    if (ordProductOrder.getNotificationContact() != null) {
                        existingOrdProductOrder.setNotificationContact(ordProductOrder.getNotificationContact());
                    }
                    if (ordProductOrder.getCustomerId() != null) {
                        existingOrdProductOrder.setCustomerId(ordProductOrder.getCustomerId());
                    }
                    if (ordProductOrder.getShoppingCartId() != null) {
                        existingOrdProductOrder.setShoppingCartId(ordProductOrder.getShoppingCartId());
                    }
                    if (ordProductOrder.getType() != null) {
                        existingOrdProductOrder.setType(ordProductOrder.getType());
                    }
                    if (ordProductOrder.getLocationId() != null) {
                        existingOrdProductOrder.setLocationId(ordProductOrder.getLocationId());
                    }
                    if (ordProductOrder.getCreatedBy() != null) {
                        existingOrdProductOrder.setCreatedBy(ordProductOrder.getCreatedBy());
                    }
                    if (ordProductOrder.getCreatedDate() != null) {
                        existingOrdProductOrder.setCreatedDate(ordProductOrder.getCreatedDate());
                    }
                    if (ordProductOrder.getUpdatedBy() != null) {
                        existingOrdProductOrder.setUpdatedBy(ordProductOrder.getUpdatedBy());
                    }
                    if (ordProductOrder.getUpdatedDate() != null) {
                        existingOrdProductOrder.setUpdatedDate(ordProductOrder.getUpdatedDate());
                    }

                    return existingOrdProductOrder;
                }
            )
            .map(ordProductOrderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdProductOrder> findAll() {
        log.debug("Request to get all OrdProductOrders");
        return ordProductOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdProductOrder> findOne(Long id) {
        log.debug("Request to get OrdProductOrder : {}", id);
        return ordProductOrderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdProductOrder : {}", id);
        ordProductOrderRepository.deleteById(id);
    }
}

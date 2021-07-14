package com.apptium.service.impl;

import com.apptium.domain.OrdOrderItem;
import com.apptium.repository.OrdOrderItemRepository;
import com.apptium.service.OrdOrderItemService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdOrderItem}.
 */
@Service
@Transactional
public class OrdOrderItemServiceImpl implements OrdOrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemServiceImpl.class);

    private final OrdOrderItemRepository ordOrderItemRepository;

    public OrdOrderItemServiceImpl(OrdOrderItemRepository ordOrderItemRepository) {
        this.ordOrderItemRepository = ordOrderItemRepository;
    }

    @Override
    public OrdOrderItem save(OrdOrderItem ordOrderItem) {
        log.debug("Request to save OrdOrderItem : {}", ordOrderItem);
        return ordOrderItemRepository.save(ordOrderItem);
    }

    @Override
    public Optional<OrdOrderItem> partialUpdate(OrdOrderItem ordOrderItem) {
        log.debug("Request to partially update OrdOrderItem : {}", ordOrderItem);

        return ordOrderItemRepository
            .findById(ordOrderItem.getId())
            .map(
                existingOrdOrderItem -> {
                    if (ordOrderItem.getBillerId() != null) {
                        existingOrdOrderItem.setBillerId(ordOrderItem.getBillerId());
                    }
                    if (ordOrderItem.getFullfillmentId() != null) {
                        existingOrdOrderItem.setFullfillmentId(ordOrderItem.getFullfillmentId());
                    }
                    if (ordOrderItem.getAcquisitionId() != null) {
                        existingOrdOrderItem.setAcquisitionId(ordOrderItem.getAcquisitionId());
                    }
                    if (ordOrderItem.getAction() != null) {
                        existingOrdOrderItem.setAction(ordOrderItem.getAction());
                    }
                    if (ordOrderItem.getState() != null) {
                        existingOrdOrderItem.setState(ordOrderItem.getState());
                    }
                    if (ordOrderItem.getQuantity() != null) {
                        existingOrdOrderItem.setQuantity(ordOrderItem.getQuantity());
                    }
                    if (ordOrderItem.getItemType() != null) {
                        existingOrdOrderItem.setItemType(ordOrderItem.getItemType());
                    }
                    if (ordOrderItem.getItemDescription() != null) {
                        existingOrdOrderItem.setItemDescription(ordOrderItem.getItemDescription());
                    }
                    if (ordOrderItem.getCartItemId() != null) {
                        existingOrdOrderItem.setCartItemId(ordOrderItem.getCartItemId());
                    }
                    if (ordOrderItem.getCreatedBy() != null) {
                        existingOrdOrderItem.setCreatedBy(ordOrderItem.getCreatedBy());
                    }
                    if (ordOrderItem.getCreatedDate() != null) {
                        existingOrdOrderItem.setCreatedDate(ordOrderItem.getCreatedDate());
                    }
                    if (ordOrderItem.getUpdatedBy() != null) {
                        existingOrdOrderItem.setUpdatedBy(ordOrderItem.getUpdatedBy());
                    }
                    if (ordOrderItem.getUpdatedDate() != null) {
                        existingOrdOrderItem.setUpdatedDate(ordOrderItem.getUpdatedDate());
                    }

                    return existingOrdOrderItem;
                }
            )
            .map(ordOrderItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdOrderItem> findAll() {
        log.debug("Request to get all OrdOrderItems");
        return ordOrderItemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdOrderItem> findOne(Long id) {
        log.debug("Request to get OrdOrderItem : {}", id);
        return ordOrderItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdOrderItem : {}", id);
        ordOrderItemRepository.deleteById(id);
    }
}

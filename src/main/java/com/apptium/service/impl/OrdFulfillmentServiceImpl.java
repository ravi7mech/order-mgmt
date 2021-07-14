package com.apptium.service.impl;

import com.apptium.domain.OrdFulfillment;
import com.apptium.repository.OrdFulfillmentRepository;
import com.apptium.service.OrdFulfillmentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdFulfillment}.
 */
@Service
@Transactional
public class OrdFulfillmentServiceImpl implements OrdFulfillmentService {

    private final Logger log = LoggerFactory.getLogger(OrdFulfillmentServiceImpl.class);

    private final OrdFulfillmentRepository ordFulfillmentRepository;

    public OrdFulfillmentServiceImpl(OrdFulfillmentRepository ordFulfillmentRepository) {
        this.ordFulfillmentRepository = ordFulfillmentRepository;
    }

    @Override
    public OrdFulfillment save(OrdFulfillment ordFulfillment) {
        log.debug("Request to save OrdFulfillment : {}", ordFulfillment);
        return ordFulfillmentRepository.save(ordFulfillment);
    }

    @Override
    public Optional<OrdFulfillment> partialUpdate(OrdFulfillment ordFulfillment) {
        log.debug("Request to partially update OrdFulfillment : {}", ordFulfillment);

        return ordFulfillmentRepository
            .findById(ordFulfillment.getId())
            .map(
                existingOrdFulfillment -> {
                    if (ordFulfillment.getWorkorderId() != null) {
                        existingOrdFulfillment.setWorkorderId(ordFulfillment.getWorkorderId());
                    }
                    if (ordFulfillment.getAppointmentId() != null) {
                        existingOrdFulfillment.setAppointmentId(ordFulfillment.getAppointmentId());
                    }
                    if (ordFulfillment.getOrderFulfillmentType() != null) {
                        existingOrdFulfillment.setOrderFulfillmentType(ordFulfillment.getOrderFulfillmentType());
                    }
                    if (ordFulfillment.getAlternateShippingAddress() != null) {
                        existingOrdFulfillment.setAlternateShippingAddress(ordFulfillment.getAlternateShippingAddress());
                    }
                    if (ordFulfillment.getOrderCallAheadNumber() != null) {
                        existingOrdFulfillment.setOrderCallAheadNumber(ordFulfillment.getOrderCallAheadNumber());
                    }
                    if (ordFulfillment.getOrderJobComments() != null) {
                        existingOrdFulfillment.setOrderJobComments(ordFulfillment.getOrderJobComments());
                    }
                    if (ordFulfillment.getStatus() != null) {
                        existingOrdFulfillment.setStatus(ordFulfillment.getStatus());
                    }

                    return existingOrdFulfillment;
                }
            )
            .map(ordFulfillmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdFulfillment> findAll() {
        log.debug("Request to get all OrdFulfillments");
        return ordFulfillmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdFulfillment> findOne(Long id) {
        log.debug("Request to get OrdFulfillment : {}", id);
        return ordFulfillmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdFulfillment : {}", id);
        ordFulfillmentRepository.deleteById(id);
    }
}

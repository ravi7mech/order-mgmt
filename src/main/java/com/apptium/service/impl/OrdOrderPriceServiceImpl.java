package com.apptium.service.impl;

import com.apptium.domain.OrdOrderPrice;
import com.apptium.repository.OrdOrderPriceRepository;
import com.apptium.service.OrdOrderPriceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdOrderPrice}.
 */
@Service
@Transactional
public class OrdOrderPriceServiceImpl implements OrdOrderPriceService {

    private final Logger log = LoggerFactory.getLogger(OrdOrderPriceServiceImpl.class);

    private final OrdOrderPriceRepository ordOrderPriceRepository;

    public OrdOrderPriceServiceImpl(OrdOrderPriceRepository ordOrderPriceRepository) {
        this.ordOrderPriceRepository = ordOrderPriceRepository;
    }

    @Override
    public OrdOrderPrice save(OrdOrderPrice ordOrderPrice) {
        log.debug("Request to save OrdOrderPrice : {}", ordOrderPrice);
        return ordOrderPriceRepository.save(ordOrderPrice);
    }

    @Override
    public Optional<OrdOrderPrice> partialUpdate(OrdOrderPrice ordOrderPrice) {
        log.debug("Request to partially update OrdOrderPrice : {}", ordOrderPrice);

        return ordOrderPriceRepository
            .findById(ordOrderPrice.getId())
            .map(
                existingOrdOrderPrice -> {
                    if (ordOrderPrice.getName() != null) {
                        existingOrdOrderPrice.setName(ordOrderPrice.getName());
                    }
                    if (ordOrderPrice.getDescription() != null) {
                        existingOrdOrderPrice.setDescription(ordOrderPrice.getDescription());
                    }
                    if (ordOrderPrice.getPriceType() != null) {
                        existingOrdOrderPrice.setPriceType(ordOrderPrice.getPriceType());
                    }
                    if (ordOrderPrice.getUnitOfMeasure() != null) {
                        existingOrdOrderPrice.setUnitOfMeasure(ordOrderPrice.getUnitOfMeasure());
                    }
                    if (ordOrderPrice.getRecurringChargePeriod() != null) {
                        existingOrdOrderPrice.setRecurringChargePeriod(ordOrderPrice.getRecurringChargePeriod());
                    }
                    if (ordOrderPrice.getPriceId() != null) {
                        existingOrdOrderPrice.setPriceId(ordOrderPrice.getPriceId());
                    }

                    return existingOrdOrderPrice;
                }
            )
            .map(ordOrderPriceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdOrderPrice> findAll() {
        log.debug("Request to get all OrdOrderPrices");
        return ordOrderPriceRepository.findAll();
    }

    /**
     *  Get all the ordOrderPrices where OrdProductOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdOrderPrice> findAllWhereOrdProductOrderIsNull() {
        log.debug("Request to get all ordOrderPrices where OrdProductOrder is null");
        return StreamSupport
            .stream(ordOrderPriceRepository.findAll().spliterator(), false)
            .filter(ordOrderPrice -> ordOrderPrice.getOrdProductOrder() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the ordOrderPrices where OrdOrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdOrderPrice> findAllWhereOrdOrderItemIsNull() {
        log.debug("Request to get all ordOrderPrices where OrdOrderItem is null");
        return StreamSupport
            .stream(ordOrderPriceRepository.findAll().spliterator(), false)
            .filter(ordOrderPrice -> ordOrderPrice.getOrdOrderItem() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdOrderPrice> findOne(Long id) {
        log.debug("Request to get OrdOrderPrice : {}", id);
        return ordOrderPriceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdOrderPrice : {}", id);
        ordOrderPriceRepository.deleteById(id);
    }
}

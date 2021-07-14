package com.apptium.service.impl;

import com.apptium.domain.OrdPriceAmount;
import com.apptium.repository.OrdPriceAmountRepository;
import com.apptium.service.OrdPriceAmountService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdPriceAmount}.
 */
@Service
@Transactional
public class OrdPriceAmountServiceImpl implements OrdPriceAmountService {

    private final Logger log = LoggerFactory.getLogger(OrdPriceAmountServiceImpl.class);

    private final OrdPriceAmountRepository ordPriceAmountRepository;

    public OrdPriceAmountServiceImpl(OrdPriceAmountRepository ordPriceAmountRepository) {
        this.ordPriceAmountRepository = ordPriceAmountRepository;
    }

    @Override
    public OrdPriceAmount save(OrdPriceAmount ordPriceAmount) {
        log.debug("Request to save OrdPriceAmount : {}", ordPriceAmount);
        return ordPriceAmountRepository.save(ordPriceAmount);
    }

    @Override
    public Optional<OrdPriceAmount> partialUpdate(OrdPriceAmount ordPriceAmount) {
        log.debug("Request to partially update OrdPriceAmount : {}", ordPriceAmount);

        return ordPriceAmountRepository
            .findById(ordPriceAmount.getId())
            .map(
                existingOrdPriceAmount -> {
                    if (ordPriceAmount.getCurrencyCode() != null) {
                        existingOrdPriceAmount.setCurrencyCode(ordPriceAmount.getCurrencyCode());
                    }
                    if (ordPriceAmount.getTaxIncludedAmount() != null) {
                        existingOrdPriceAmount.setTaxIncludedAmount(ordPriceAmount.getTaxIncludedAmount());
                    }
                    if (ordPriceAmount.getDutyFreeAmount() != null) {
                        existingOrdPriceAmount.setDutyFreeAmount(ordPriceAmount.getDutyFreeAmount());
                    }
                    if (ordPriceAmount.getTaxRate() != null) {
                        existingOrdPriceAmount.setTaxRate(ordPriceAmount.getTaxRate());
                    }
                    if (ordPriceAmount.getPercentage() != null) {
                        existingOrdPriceAmount.setPercentage(ordPriceAmount.getPercentage());
                    }
                    if (ordPriceAmount.getTotalRecurringPrice() != null) {
                        existingOrdPriceAmount.setTotalRecurringPrice(ordPriceAmount.getTotalRecurringPrice());
                    }
                    if (ordPriceAmount.getTotalOneTimePrice() != null) {
                        existingOrdPriceAmount.setTotalOneTimePrice(ordPriceAmount.getTotalOneTimePrice());
                    }

                    return existingOrdPriceAmount;
                }
            )
            .map(ordPriceAmountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdPriceAmount> findAll() {
        log.debug("Request to get all OrdPriceAmounts");
        return ordPriceAmountRepository.findAll();
    }

    /**
     *  Get all the ordPriceAmounts where OrdOrderPrice is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdPriceAmount> findAllWhereOrdOrderPriceIsNull() {
        log.debug("Request to get all ordPriceAmounts where OrdOrderPrice is null");
        return StreamSupport
            .stream(ordPriceAmountRepository.findAll().spliterator(), false)
            .filter(ordPriceAmount -> ordPriceAmount.getOrdOrderPrice() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the ordPriceAmounts where OrdPriceAlteration is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdPriceAmount> findAllWhereOrdPriceAlterationIsNull() {
        log.debug("Request to get all ordPriceAmounts where OrdPriceAlteration is null");
        return StreamSupport
            .stream(ordPriceAmountRepository.findAll().spliterator(), false)
            .filter(ordPriceAmount -> ordPriceAmount.getOrdPriceAlteration() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdPriceAmount> findOne(Long id) {
        log.debug("Request to get OrdPriceAmount : {}", id);
        return ordPriceAmountRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdPriceAmount : {}", id);
        ordPriceAmountRepository.deleteById(id);
    }
}

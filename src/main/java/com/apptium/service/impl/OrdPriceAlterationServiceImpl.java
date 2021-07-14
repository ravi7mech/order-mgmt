package com.apptium.service.impl;

import com.apptium.domain.OrdPriceAlteration;
import com.apptium.repository.OrdPriceAlterationRepository;
import com.apptium.service.OrdPriceAlterationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdPriceAlteration}.
 */
@Service
@Transactional
public class OrdPriceAlterationServiceImpl implements OrdPriceAlterationService {

    private final Logger log = LoggerFactory.getLogger(OrdPriceAlterationServiceImpl.class);

    private final OrdPriceAlterationRepository ordPriceAlterationRepository;

    public OrdPriceAlterationServiceImpl(OrdPriceAlterationRepository ordPriceAlterationRepository) {
        this.ordPriceAlterationRepository = ordPriceAlterationRepository;
    }

    @Override
    public OrdPriceAlteration save(OrdPriceAlteration ordPriceAlteration) {
        log.debug("Request to save OrdPriceAlteration : {}", ordPriceAlteration);
        return ordPriceAlterationRepository.save(ordPriceAlteration);
    }

    @Override
    public Optional<OrdPriceAlteration> partialUpdate(OrdPriceAlteration ordPriceAlteration) {
        log.debug("Request to partially update OrdPriceAlteration : {}", ordPriceAlteration);

        return ordPriceAlterationRepository
            .findById(ordPriceAlteration.getId())
            .map(
                existingOrdPriceAlteration -> {
                    if (ordPriceAlteration.getName() != null) {
                        existingOrdPriceAlteration.setName(ordPriceAlteration.getName());
                    }
                    if (ordPriceAlteration.getDescription() != null) {
                        existingOrdPriceAlteration.setDescription(ordPriceAlteration.getDescription());
                    }
                    if (ordPriceAlteration.getPriceType() != null) {
                        existingOrdPriceAlteration.setPriceType(ordPriceAlteration.getPriceType());
                    }
                    if (ordPriceAlteration.getUnitOfMeasure() != null) {
                        existingOrdPriceAlteration.setUnitOfMeasure(ordPriceAlteration.getUnitOfMeasure());
                    }
                    if (ordPriceAlteration.getRecurringChargePeriod() != null) {
                        existingOrdPriceAlteration.setRecurringChargePeriod(ordPriceAlteration.getRecurringChargePeriod());
                    }
                    if (ordPriceAlteration.getApplicationDuration() != null) {
                        existingOrdPriceAlteration.setApplicationDuration(ordPriceAlteration.getApplicationDuration());
                    }
                    if (ordPriceAlteration.getPriority() != null) {
                        existingOrdPriceAlteration.setPriority(ordPriceAlteration.getPriority());
                    }

                    return existingOrdPriceAlteration;
                }
            )
            .map(ordPriceAlterationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdPriceAlteration> findAll() {
        log.debug("Request to get all OrdPriceAlterations");
        return ordPriceAlterationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdPriceAlteration> findOne(Long id) {
        log.debug("Request to get OrdPriceAlteration : {}", id);
        return ordPriceAlterationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdPriceAlteration : {}", id);
        ordPriceAlterationRepository.deleteById(id);
    }
}

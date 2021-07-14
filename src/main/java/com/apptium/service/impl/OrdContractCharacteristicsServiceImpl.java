package com.apptium.service.impl;

import com.apptium.domain.OrdContractCharacteristics;
import com.apptium.repository.OrdContractCharacteristicsRepository;
import com.apptium.service.OrdContractCharacteristicsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdContractCharacteristics}.
 */
@Service
@Transactional
public class OrdContractCharacteristicsServiceImpl implements OrdContractCharacteristicsService {

    private final Logger log = LoggerFactory.getLogger(OrdContractCharacteristicsServiceImpl.class);

    private final OrdContractCharacteristicsRepository ordContractCharacteristicsRepository;

    public OrdContractCharacteristicsServiceImpl(OrdContractCharacteristicsRepository ordContractCharacteristicsRepository) {
        this.ordContractCharacteristicsRepository = ordContractCharacteristicsRepository;
    }

    @Override
    public OrdContractCharacteristics save(OrdContractCharacteristics ordContractCharacteristics) {
        log.debug("Request to save OrdContractCharacteristics : {}", ordContractCharacteristics);
        return ordContractCharacteristicsRepository.save(ordContractCharacteristics);
    }

    @Override
    public Optional<OrdContractCharacteristics> partialUpdate(OrdContractCharacteristics ordContractCharacteristics) {
        log.debug("Request to partially update OrdContractCharacteristics : {}", ordContractCharacteristics);

        return ordContractCharacteristicsRepository
            .findById(ordContractCharacteristics.getId())
            .map(
                existingOrdContractCharacteristics -> {
                    if (ordContractCharacteristics.getName() != null) {
                        existingOrdContractCharacteristics.setName(ordContractCharacteristics.getName());
                    }
                    if (ordContractCharacteristics.getValue() != null) {
                        existingOrdContractCharacteristics.setValue(ordContractCharacteristics.getValue());
                    }
                    if (ordContractCharacteristics.getValueType() != null) {
                        existingOrdContractCharacteristics.setValueType(ordContractCharacteristics.getValueType());
                    }
                    if (ordContractCharacteristics.getCreatedBy() != null) {
                        existingOrdContractCharacteristics.setCreatedBy(ordContractCharacteristics.getCreatedBy());
                    }
                    if (ordContractCharacteristics.getCreatedDate() != null) {
                        existingOrdContractCharacteristics.setCreatedDate(ordContractCharacteristics.getCreatedDate());
                    }

                    return existingOrdContractCharacteristics;
                }
            )
            .map(ordContractCharacteristicsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdContractCharacteristics> findAll() {
        log.debug("Request to get all OrdContractCharacteristics");
        return ordContractCharacteristicsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdContractCharacteristics> findOne(Long id) {
        log.debug("Request to get OrdContractCharacteristics : {}", id);
        return ordContractCharacteristicsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdContractCharacteristics : {}", id);
        ordContractCharacteristicsRepository.deleteById(id);
    }
}

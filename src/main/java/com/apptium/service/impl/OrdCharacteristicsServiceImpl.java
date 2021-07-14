package com.apptium.service.impl;

import com.apptium.domain.OrdCharacteristics;
import com.apptium.repository.OrdCharacteristicsRepository;
import com.apptium.service.OrdCharacteristicsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdCharacteristics}.
 */
@Service
@Transactional
public class OrdCharacteristicsServiceImpl implements OrdCharacteristicsService {

    private final Logger log = LoggerFactory.getLogger(OrdCharacteristicsServiceImpl.class);

    private final OrdCharacteristicsRepository ordCharacteristicsRepository;

    public OrdCharacteristicsServiceImpl(OrdCharacteristicsRepository ordCharacteristicsRepository) {
        this.ordCharacteristicsRepository = ordCharacteristicsRepository;
    }

    @Override
    public OrdCharacteristics save(OrdCharacteristics ordCharacteristics) {
        log.debug("Request to save OrdCharacteristics : {}", ordCharacteristics);
        return ordCharacteristicsRepository.save(ordCharacteristics);
    }

    @Override
    public Optional<OrdCharacteristics> partialUpdate(OrdCharacteristics ordCharacteristics) {
        log.debug("Request to partially update OrdCharacteristics : {}", ordCharacteristics);

        return ordCharacteristicsRepository
            .findById(ordCharacteristics.getId())
            .map(
                existingOrdCharacteristics -> {
                    if (ordCharacteristics.getName() != null) {
                        existingOrdCharacteristics.setName(ordCharacteristics.getName());
                    }
                    if (ordCharacteristics.getValue() != null) {
                        existingOrdCharacteristics.setValue(ordCharacteristics.getValue());
                    }
                    if (ordCharacteristics.getValueType() != null) {
                        existingOrdCharacteristics.setValueType(ordCharacteristics.getValueType());
                    }

                    return existingOrdCharacteristics;
                }
            )
            .map(ordCharacteristicsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdCharacteristics> findAll() {
        log.debug("Request to get all OrdCharacteristics");
        return ordCharacteristicsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdCharacteristics> findOne(Long id) {
        log.debug("Request to get OrdCharacteristics : {}", id);
        return ordCharacteristicsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdCharacteristics : {}", id);
        ordCharacteristicsRepository.deleteById(id);
    }
}

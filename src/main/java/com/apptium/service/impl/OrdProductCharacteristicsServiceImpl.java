package com.apptium.service.impl;

import com.apptium.domain.OrdProductCharacteristics;
import com.apptium.repository.OrdProductCharacteristicsRepository;
import com.apptium.service.OrdProductCharacteristicsService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdProductCharacteristics}.
 */
@Service
@Transactional
public class OrdProductCharacteristicsServiceImpl implements OrdProductCharacteristicsService {

    private final Logger log = LoggerFactory.getLogger(OrdProductCharacteristicsServiceImpl.class);

    private final OrdProductCharacteristicsRepository ordProductCharacteristicsRepository;

    public OrdProductCharacteristicsServiceImpl(OrdProductCharacteristicsRepository ordProductCharacteristicsRepository) {
        this.ordProductCharacteristicsRepository = ordProductCharacteristicsRepository;
    }

    @Override
    public OrdProductCharacteristics save(OrdProductCharacteristics ordProductCharacteristics) {
        log.debug("Request to save OrdProductCharacteristics : {}", ordProductCharacteristics);
        return ordProductCharacteristicsRepository.save(ordProductCharacteristics);
    }

    @Override
    public Optional<OrdProductCharacteristics> partialUpdate(OrdProductCharacteristics ordProductCharacteristics) {
        log.debug("Request to partially update OrdProductCharacteristics : {}", ordProductCharacteristics);

        return ordProductCharacteristicsRepository
            .findById(ordProductCharacteristics.getId())
            .map(
                existingOrdProductCharacteristics -> {
                    if (ordProductCharacteristics.getName() != null) {
                        existingOrdProductCharacteristics.setName(ordProductCharacteristics.getName());
                    }
                    if (ordProductCharacteristics.getValue() != null) {
                        existingOrdProductCharacteristics.setValue(ordProductCharacteristics.getValue());
                    }
                    if (ordProductCharacteristics.getValueType() != null) {
                        existingOrdProductCharacteristics.setValueType(ordProductCharacteristics.getValueType());
                    }

                    return existingOrdProductCharacteristics;
                }
            )
            .map(ordProductCharacteristicsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdProductCharacteristics> findAll() {
        log.debug("Request to get all OrdProductCharacteristics");
        return ordProductCharacteristicsRepository.findAll();
    }

    /**
     *  Get all the ordProductCharacteristics where OrdProduct is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdProductCharacteristics> findAllWhereOrdProductIsNull() {
        log.debug("Request to get all ordProductCharacteristics where OrdProduct is null");
        return StreamSupport
            .stream(ordProductCharacteristicsRepository.findAll().spliterator(), false)
            .filter(ordProductCharacteristics -> ordProductCharacteristics.getOrdProduct() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdProductCharacteristics> findOne(Long id) {
        log.debug("Request to get OrdProductCharacteristics : {}", id);
        return ordProductCharacteristicsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdProductCharacteristics : {}", id);
        ordProductCharacteristicsRepository.deleteById(id);
    }
}

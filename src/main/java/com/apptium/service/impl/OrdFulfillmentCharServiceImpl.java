package com.apptium.service.impl;

import com.apptium.domain.OrdFulfillmentChar;
import com.apptium.repository.OrdFulfillmentCharRepository;
import com.apptium.service.OrdFulfillmentCharService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdFulfillmentChar}.
 */
@Service
@Transactional
public class OrdFulfillmentCharServiceImpl implements OrdFulfillmentCharService {

    private final Logger log = LoggerFactory.getLogger(OrdFulfillmentCharServiceImpl.class);

    private final OrdFulfillmentCharRepository ordFulfillmentCharRepository;

    public OrdFulfillmentCharServiceImpl(OrdFulfillmentCharRepository ordFulfillmentCharRepository) {
        this.ordFulfillmentCharRepository = ordFulfillmentCharRepository;
    }

    @Override
    public OrdFulfillmentChar save(OrdFulfillmentChar ordFulfillmentChar) {
        log.debug("Request to save OrdFulfillmentChar : {}", ordFulfillmentChar);
        return ordFulfillmentCharRepository.save(ordFulfillmentChar);
    }

    @Override
    public Optional<OrdFulfillmentChar> partialUpdate(OrdFulfillmentChar ordFulfillmentChar) {
        log.debug("Request to partially update OrdFulfillmentChar : {}", ordFulfillmentChar);

        return ordFulfillmentCharRepository
            .findById(ordFulfillmentChar.getId())
            .map(
                existingOrdFulfillmentChar -> {
                    if (ordFulfillmentChar.getName() != null) {
                        existingOrdFulfillmentChar.setName(ordFulfillmentChar.getName());
                    }
                    if (ordFulfillmentChar.getValue() != null) {
                        existingOrdFulfillmentChar.setValue(ordFulfillmentChar.getValue());
                    }
                    if (ordFulfillmentChar.getValueType() != null) {
                        existingOrdFulfillmentChar.setValueType(ordFulfillmentChar.getValueType());
                    }
                    if (ordFulfillmentChar.getCreatedBy() != null) {
                        existingOrdFulfillmentChar.setCreatedBy(ordFulfillmentChar.getCreatedBy());
                    }
                    if (ordFulfillmentChar.getCreatedDate() != null) {
                        existingOrdFulfillmentChar.setCreatedDate(ordFulfillmentChar.getCreatedDate());
                    }

                    return existingOrdFulfillmentChar;
                }
            )
            .map(ordFulfillmentCharRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdFulfillmentChar> findAll() {
        log.debug("Request to get all OrdFulfillmentChars");
        return ordFulfillmentCharRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdFulfillmentChar> findOne(Long id) {
        log.debug("Request to get OrdFulfillmentChar : {}", id);
        return ordFulfillmentCharRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdFulfillmentChar : {}", id);
        ordFulfillmentCharRepository.deleteById(id);
    }
}

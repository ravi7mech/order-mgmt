package com.apptium.service.impl;

import com.apptium.domain.OrdProvisiongChar;
import com.apptium.repository.OrdProvisiongCharRepository;
import com.apptium.service.OrdProvisiongCharService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdProvisiongChar}.
 */
@Service
@Transactional
public class OrdProvisiongCharServiceImpl implements OrdProvisiongCharService {

    private final Logger log = LoggerFactory.getLogger(OrdProvisiongCharServiceImpl.class);

    private final OrdProvisiongCharRepository ordProvisiongCharRepository;

    public OrdProvisiongCharServiceImpl(OrdProvisiongCharRepository ordProvisiongCharRepository) {
        this.ordProvisiongCharRepository = ordProvisiongCharRepository;
    }

    @Override
    public OrdProvisiongChar save(OrdProvisiongChar ordProvisiongChar) {
        log.debug("Request to save OrdProvisiongChar : {}", ordProvisiongChar);
        return ordProvisiongCharRepository.save(ordProvisiongChar);
    }

    @Override
    public Optional<OrdProvisiongChar> partialUpdate(OrdProvisiongChar ordProvisiongChar) {
        log.debug("Request to partially update OrdProvisiongChar : {}", ordProvisiongChar);

        return ordProvisiongCharRepository
            .findById(ordProvisiongChar.getId())
            .map(
                existingOrdProvisiongChar -> {
                    if (ordProvisiongChar.getName() != null) {
                        existingOrdProvisiongChar.setName(ordProvisiongChar.getName());
                    }
                    if (ordProvisiongChar.getValue() != null) {
                        existingOrdProvisiongChar.setValue(ordProvisiongChar.getValue());
                    }
                    if (ordProvisiongChar.getValueType() != null) {
                        existingOrdProvisiongChar.setValueType(ordProvisiongChar.getValueType());
                    }
                    if (ordProvisiongChar.getCreatedBy() != null) {
                        existingOrdProvisiongChar.setCreatedBy(ordProvisiongChar.getCreatedBy());
                    }
                    if (ordProvisiongChar.getCreatedDate() != null) {
                        existingOrdProvisiongChar.setCreatedDate(ordProvisiongChar.getCreatedDate());
                    }

                    return existingOrdProvisiongChar;
                }
            )
            .map(ordProvisiongCharRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdProvisiongChar> findAll() {
        log.debug("Request to get all OrdProvisiongChars");
        return ordProvisiongCharRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdProvisiongChar> findOne(Long id) {
        log.debug("Request to get OrdProvisiongChar : {}", id);
        return ordProvisiongCharRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdProvisiongChar : {}", id);
        ordProvisiongCharRepository.deleteById(id);
    }
}

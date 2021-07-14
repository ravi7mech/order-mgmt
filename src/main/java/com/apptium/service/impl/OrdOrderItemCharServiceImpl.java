package com.apptium.service.impl;

import com.apptium.domain.OrdOrderItemChar;
import com.apptium.repository.OrdOrderItemCharRepository;
import com.apptium.service.OrdOrderItemCharService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdOrderItemChar}.
 */
@Service
@Transactional
public class OrdOrderItemCharServiceImpl implements OrdOrderItemCharService {

    private final Logger log = LoggerFactory.getLogger(OrdOrderItemCharServiceImpl.class);

    private final OrdOrderItemCharRepository ordOrderItemCharRepository;

    public OrdOrderItemCharServiceImpl(OrdOrderItemCharRepository ordOrderItemCharRepository) {
        this.ordOrderItemCharRepository = ordOrderItemCharRepository;
    }

    @Override
    public OrdOrderItemChar save(OrdOrderItemChar ordOrderItemChar) {
        log.debug("Request to save OrdOrderItemChar : {}", ordOrderItemChar);
        return ordOrderItemCharRepository.save(ordOrderItemChar);
    }

    @Override
    public Optional<OrdOrderItemChar> partialUpdate(OrdOrderItemChar ordOrderItemChar) {
        log.debug("Request to partially update OrdOrderItemChar : {}", ordOrderItemChar);

        return ordOrderItemCharRepository
            .findById(ordOrderItemChar.getId())
            .map(
                existingOrdOrderItemChar -> {
                    if (ordOrderItemChar.getName() != null) {
                        existingOrdOrderItemChar.setName(ordOrderItemChar.getName());
                    }
                    if (ordOrderItemChar.getValue() != null) {
                        existingOrdOrderItemChar.setValue(ordOrderItemChar.getValue());
                    }

                    return existingOrdOrderItemChar;
                }
            )
            .map(ordOrderItemCharRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdOrderItemChar> findAll() {
        log.debug("Request to get all OrdOrderItemChars");
        return ordOrderItemCharRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdOrderItemChar> findOne(Long id) {
        log.debug("Request to get OrdOrderItemChar : {}", id);
        return ordOrderItemCharRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdOrderItemChar : {}", id);
        ordOrderItemCharRepository.deleteById(id);
    }
}

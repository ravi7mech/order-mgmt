package com.apptium.service.impl;

import com.apptium.domain.OrdAcquisitionChar;
import com.apptium.repository.OrdAcquisitionCharRepository;
import com.apptium.service.OrdAcquisitionCharService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdAcquisitionChar}.
 */
@Service
@Transactional
public class OrdAcquisitionCharServiceImpl implements OrdAcquisitionCharService {

    private final Logger log = LoggerFactory.getLogger(OrdAcquisitionCharServiceImpl.class);

    private final OrdAcquisitionCharRepository ordAcquisitionCharRepository;

    public OrdAcquisitionCharServiceImpl(OrdAcquisitionCharRepository ordAcquisitionCharRepository) {
        this.ordAcquisitionCharRepository = ordAcquisitionCharRepository;
    }

    @Override
    public OrdAcquisitionChar save(OrdAcquisitionChar ordAcquisitionChar) {
        log.debug("Request to save OrdAcquisitionChar : {}", ordAcquisitionChar);
        return ordAcquisitionCharRepository.save(ordAcquisitionChar);
    }

    @Override
    public Optional<OrdAcquisitionChar> partialUpdate(OrdAcquisitionChar ordAcquisitionChar) {
        log.debug("Request to partially update OrdAcquisitionChar : {}", ordAcquisitionChar);

        return ordAcquisitionCharRepository
            .findById(ordAcquisitionChar.getId())
            .map(
                existingOrdAcquisitionChar -> {
                    if (ordAcquisitionChar.getName() != null) {
                        existingOrdAcquisitionChar.setName(ordAcquisitionChar.getName());
                    }
                    if (ordAcquisitionChar.getValue() != null) {
                        existingOrdAcquisitionChar.setValue(ordAcquisitionChar.getValue());
                    }
                    if (ordAcquisitionChar.getValueType() != null) {
                        existingOrdAcquisitionChar.setValueType(ordAcquisitionChar.getValueType());
                    }
                    if (ordAcquisitionChar.getCreatedBy() != null) {
                        existingOrdAcquisitionChar.setCreatedBy(ordAcquisitionChar.getCreatedBy());
                    }
                    if (ordAcquisitionChar.getCreatedDate() != null) {
                        existingOrdAcquisitionChar.setCreatedDate(ordAcquisitionChar.getCreatedDate());
                    }

                    return existingOrdAcquisitionChar;
                }
            )
            .map(ordAcquisitionCharRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdAcquisitionChar> findAll() {
        log.debug("Request to get all OrdAcquisitionChars");
        return ordAcquisitionCharRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdAcquisitionChar> findOne(Long id) {
        log.debug("Request to get OrdAcquisitionChar : {}", id);
        return ordAcquisitionCharRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdAcquisitionChar : {}", id);
        ordAcquisitionCharRepository.deleteById(id);
    }
}

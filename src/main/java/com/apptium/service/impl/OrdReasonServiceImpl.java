package com.apptium.service.impl;

import com.apptium.domain.OrdReason;
import com.apptium.repository.OrdReasonRepository;
import com.apptium.service.OrdReasonService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdReason}.
 */
@Service
@Transactional
public class OrdReasonServiceImpl implements OrdReasonService {

    private final Logger log = LoggerFactory.getLogger(OrdReasonServiceImpl.class);

    private final OrdReasonRepository ordReasonRepository;

    public OrdReasonServiceImpl(OrdReasonRepository ordReasonRepository) {
        this.ordReasonRepository = ordReasonRepository;
    }

    @Override
    public OrdReason save(OrdReason ordReason) {
        log.debug("Request to save OrdReason : {}", ordReason);
        return ordReasonRepository.save(ordReason);
    }

    @Override
    public Optional<OrdReason> partialUpdate(OrdReason ordReason) {
        log.debug("Request to partially update OrdReason : {}", ordReason);

        return ordReasonRepository
            .findById(ordReason.getId())
            .map(
                existingOrdReason -> {
                    if (ordReason.getReason() != null) {
                        existingOrdReason.setReason(ordReason.getReason());
                    }
                    if (ordReason.getDescription() != null) {
                        existingOrdReason.setDescription(ordReason.getDescription());
                    }

                    return existingOrdReason;
                }
            )
            .map(ordReasonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdReason> findAll() {
        log.debug("Request to get all OrdReasons");
        return ordReasonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdReason> findOne(Long id) {
        log.debug("Request to get OrdReason : {}", id);
        return ordReasonRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdReason : {}", id);
        ordReasonRepository.deleteById(id);
    }
}

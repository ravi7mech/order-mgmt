package com.apptium.service.impl;

import com.apptium.domain.OrdAcquisition;
import com.apptium.repository.OrdAcquisitionRepository;
import com.apptium.service.OrdAcquisitionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdAcquisition}.
 */
@Service
@Transactional
public class OrdAcquisitionServiceImpl implements OrdAcquisitionService {

    private final Logger log = LoggerFactory.getLogger(OrdAcquisitionServiceImpl.class);

    private final OrdAcquisitionRepository ordAcquisitionRepository;

    public OrdAcquisitionServiceImpl(OrdAcquisitionRepository ordAcquisitionRepository) {
        this.ordAcquisitionRepository = ordAcquisitionRepository;
    }

    @Override
    public OrdAcquisition save(OrdAcquisition ordAcquisition) {
        log.debug("Request to save OrdAcquisition : {}", ordAcquisition);
        return ordAcquisitionRepository.save(ordAcquisition);
    }

    @Override
    public Optional<OrdAcquisition> partialUpdate(OrdAcquisition ordAcquisition) {
        log.debug("Request to partially update OrdAcquisition : {}", ordAcquisition);

        return ordAcquisitionRepository
            .findById(ordAcquisition.getId())
            .map(
                existingOrdAcquisition -> {
                    if (ordAcquisition.getChannel() != null) {
                        existingOrdAcquisition.setChannel(ordAcquisition.getChannel());
                    }
                    if (ordAcquisition.getAffiliate() != null) {
                        existingOrdAcquisition.setAffiliate(ordAcquisition.getAffiliate());
                    }
                    if (ordAcquisition.getPartner() != null) {
                        existingOrdAcquisition.setPartner(ordAcquisition.getPartner());
                    }
                    if (ordAcquisition.getAcquisitionAgent() != null) {
                        existingOrdAcquisition.setAcquisitionAgent(ordAcquisition.getAcquisitionAgent());
                    }
                    if (ordAcquisition.getAction() != null) {
                        existingOrdAcquisition.setAction(ordAcquisition.getAction());
                    }

                    return existingOrdAcquisition;
                }
            )
            .map(ordAcquisitionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdAcquisition> findAll() {
        log.debug("Request to get all OrdAcquisitions");
        return ordAcquisitionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdAcquisition> findOne(Long id) {
        log.debug("Request to get OrdAcquisition : {}", id);
        return ordAcquisitionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdAcquisition : {}", id);
        ordAcquisitionRepository.deleteById(id);
    }
}

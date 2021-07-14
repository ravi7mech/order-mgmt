package com.apptium.service.impl;

import com.apptium.domain.OrdContract;
import com.apptium.repository.OrdContractRepository;
import com.apptium.service.OrdContractService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdContract}.
 */
@Service
@Transactional
public class OrdContractServiceImpl implements OrdContractService {

    private final Logger log = LoggerFactory.getLogger(OrdContractServiceImpl.class);

    private final OrdContractRepository ordContractRepository;

    public OrdContractServiceImpl(OrdContractRepository ordContractRepository) {
        this.ordContractRepository = ordContractRepository;
    }

    @Override
    public OrdContract save(OrdContract ordContract) {
        log.debug("Request to save OrdContract : {}", ordContract);
        return ordContractRepository.save(ordContract);
    }

    @Override
    public Optional<OrdContract> partialUpdate(OrdContract ordContract) {
        log.debug("Request to partially update OrdContract : {}", ordContract);

        return ordContractRepository
            .findById(ordContract.getId())
            .map(
                existingOrdContract -> {
                    if (ordContract.getContractId() != null) {
                        existingOrdContract.setContractId(ordContract.getContractId());
                    }
                    if (ordContract.getLanguageId() != null) {
                        existingOrdContract.setLanguageId(ordContract.getLanguageId());
                    }
                    if (ordContract.getTermTypeCode() != null) {
                        existingOrdContract.setTermTypeCode(ordContract.getTermTypeCode());
                    }
                    if (ordContract.getAction() != null) {
                        existingOrdContract.setAction(ordContract.getAction());
                    }
                    if (ordContract.getStatus() != null) {
                        existingOrdContract.setStatus(ordContract.getStatus());
                    }

                    return existingOrdContract;
                }
            )
            .map(ordContractRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdContract> findAll() {
        log.debug("Request to get all OrdContracts");
        return ordContractRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdContract> findOne(Long id) {
        log.debug("Request to get OrdContract : {}", id);
        return ordContractRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdContract : {}", id);
        ordContractRepository.deleteById(id);
    }
}

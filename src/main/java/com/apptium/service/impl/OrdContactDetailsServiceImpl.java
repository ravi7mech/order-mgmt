package com.apptium.service.impl;

import com.apptium.domain.OrdContactDetails;
import com.apptium.repository.OrdContactDetailsRepository;
import com.apptium.service.OrdContactDetailsService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdContactDetails}.
 */
@Service
@Transactional
public class OrdContactDetailsServiceImpl implements OrdContactDetailsService {

    private final Logger log = LoggerFactory.getLogger(OrdContactDetailsServiceImpl.class);

    private final OrdContactDetailsRepository ordContactDetailsRepository;

    public OrdContactDetailsServiceImpl(OrdContactDetailsRepository ordContactDetailsRepository) {
        this.ordContactDetailsRepository = ordContactDetailsRepository;
    }

    @Override
    public OrdContactDetails save(OrdContactDetails ordContactDetails) {
        log.debug("Request to save OrdContactDetails : {}", ordContactDetails);
        return ordContactDetailsRepository.save(ordContactDetails);
    }

    @Override
    public Optional<OrdContactDetails> partialUpdate(OrdContactDetails ordContactDetails) {
        log.debug("Request to partially update OrdContactDetails : {}", ordContactDetails);

        return ordContactDetailsRepository
            .findById(ordContactDetails.getId())
            .map(
                existingOrdContactDetails -> {
                    if (ordContactDetails.getContactName() != null) {
                        existingOrdContactDetails.setContactName(ordContactDetails.getContactName());
                    }
                    if (ordContactDetails.getContactPhoneNumber() != null) {
                        existingOrdContactDetails.setContactPhoneNumber(ordContactDetails.getContactPhoneNumber());
                    }
                    if (ordContactDetails.getContactEmailId() != null) {
                        existingOrdContactDetails.setContactEmailId(ordContactDetails.getContactEmailId());
                    }
                    if (ordContactDetails.getFirstName() != null) {
                        existingOrdContactDetails.setFirstName(ordContactDetails.getFirstName());
                    }
                    if (ordContactDetails.getLastName() != null) {
                        existingOrdContactDetails.setLastName(ordContactDetails.getLastName());
                    }

                    return existingOrdContactDetails;
                }
            )
            .map(ordContactDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdContactDetails> findAll() {
        log.debug("Request to get all OrdContactDetails");
        return ordContactDetailsRepository.findAll();
    }

    /**
     *  Get all the ordContactDetails where OrdProductOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdContactDetails> findAllWhereOrdProductOrderIsNull() {
        log.debug("Request to get all ordContactDetails where OrdProductOrder is null");
        return StreamSupport
            .stream(ordContactDetailsRepository.findAll().spliterator(), false)
            .filter(ordContactDetails -> ordContactDetails.getOrdProductOrder() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdContactDetails> findOne(Long id) {
        log.debug("Request to get OrdContactDetails : {}", id);
        return ordContactDetailsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdContactDetails : {}", id);
        ordContactDetailsRepository.deleteById(id);
    }
}

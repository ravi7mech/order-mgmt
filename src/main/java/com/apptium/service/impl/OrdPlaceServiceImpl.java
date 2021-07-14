package com.apptium.service.impl;

import com.apptium.domain.OrdPlace;
import com.apptium.repository.OrdPlaceRepository;
import com.apptium.service.OrdPlaceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdPlace}.
 */
@Service
@Transactional
public class OrdPlaceServiceImpl implements OrdPlaceService {

    private final Logger log = LoggerFactory.getLogger(OrdPlaceServiceImpl.class);

    private final OrdPlaceRepository ordPlaceRepository;

    public OrdPlaceServiceImpl(OrdPlaceRepository ordPlaceRepository) {
        this.ordPlaceRepository = ordPlaceRepository;
    }

    @Override
    public OrdPlace save(OrdPlace ordPlace) {
        log.debug("Request to save OrdPlace : {}", ordPlace);
        return ordPlaceRepository.save(ordPlace);
    }

    @Override
    public Optional<OrdPlace> partialUpdate(OrdPlace ordPlace) {
        log.debug("Request to partially update OrdPlace : {}", ordPlace);

        return ordPlaceRepository
            .findById(ordPlace.getId())
            .map(
                existingOrdPlace -> {
                    if (ordPlace.getHref() != null) {
                        existingOrdPlace.setHref(ordPlace.getHref());
                    }
                    if (ordPlace.getName() != null) {
                        existingOrdPlace.setName(ordPlace.getName());
                    }
                    if (ordPlace.getRole() != null) {
                        existingOrdPlace.setRole(ordPlace.getRole());
                    }

                    return existingOrdPlace;
                }
            )
            .map(ordPlaceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdPlace> findAll() {
        log.debug("Request to get all OrdPlaces");
        return ordPlaceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdPlace> findOne(Long id) {
        log.debug("Request to get OrdPlace : {}", id);
        return ordPlaceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdPlace : {}", id);
        ordPlaceRepository.deleteById(id);
    }
}

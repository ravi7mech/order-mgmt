package com.apptium.service.impl;

import com.apptium.domain.OrdChannel;
import com.apptium.repository.OrdChannelRepository;
import com.apptium.service.OrdChannelService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdChannel}.
 */
@Service
@Transactional
public class OrdChannelServiceImpl implements OrdChannelService {

    private final Logger log = LoggerFactory.getLogger(OrdChannelServiceImpl.class);

    private final OrdChannelRepository ordChannelRepository;

    public OrdChannelServiceImpl(OrdChannelRepository ordChannelRepository) {
        this.ordChannelRepository = ordChannelRepository;
    }

    @Override
    public OrdChannel save(OrdChannel ordChannel) {
        log.debug("Request to save OrdChannel : {}", ordChannel);
        return ordChannelRepository.save(ordChannel);
    }

    @Override
    public Optional<OrdChannel> partialUpdate(OrdChannel ordChannel) {
        log.debug("Request to partially update OrdChannel : {}", ordChannel);

        return ordChannelRepository
            .findById(ordChannel.getId())
            .map(
                existingOrdChannel -> {
                    if (ordChannel.getHref() != null) {
                        existingOrdChannel.setHref(ordChannel.getHref());
                    }
                    if (ordChannel.getName() != null) {
                        existingOrdChannel.setName(ordChannel.getName());
                    }
                    if (ordChannel.getRole() != null) {
                        existingOrdChannel.setRole(ordChannel.getRole());
                    }

                    return existingOrdChannel;
                }
            )
            .map(ordChannelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdChannel> findAll() {
        log.debug("Request to get all OrdChannels");
        return ordChannelRepository.findAll();
    }

    /**
     *  Get all the ordChannels where OrdProductOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdChannel> findAllWhereOrdProductOrderIsNull() {
        log.debug("Request to get all ordChannels where OrdProductOrder is null");
        return StreamSupport
            .stream(ordChannelRepository.findAll().spliterator(), false)
            .filter(ordChannel -> ordChannel.getOrdProductOrder() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdChannel> findOne(Long id) {
        log.debug("Request to get OrdChannel : {}", id);
        return ordChannelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdChannel : {}", id);
        ordChannelRepository.deleteById(id);
    }
}

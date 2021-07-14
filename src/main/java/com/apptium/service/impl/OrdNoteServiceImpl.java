package com.apptium.service.impl;

import com.apptium.domain.OrdNote;
import com.apptium.repository.OrdNoteRepository;
import com.apptium.service.OrdNoteService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdNote}.
 */
@Service
@Transactional
public class OrdNoteServiceImpl implements OrdNoteService {

    private final Logger log = LoggerFactory.getLogger(OrdNoteServiceImpl.class);

    private final OrdNoteRepository ordNoteRepository;

    public OrdNoteServiceImpl(OrdNoteRepository ordNoteRepository) {
        this.ordNoteRepository = ordNoteRepository;
    }

    @Override
    public OrdNote save(OrdNote ordNote) {
        log.debug("Request to save OrdNote : {}", ordNote);
        return ordNoteRepository.save(ordNote);
    }

    @Override
    public Optional<OrdNote> partialUpdate(OrdNote ordNote) {
        log.debug("Request to partially update OrdNote : {}", ordNote);

        return ordNoteRepository
            .findById(ordNote.getId())
            .map(
                existingOrdNote -> {
                    if (ordNote.getAuthor() != null) {
                        existingOrdNote.setAuthor(ordNote.getAuthor());
                    }
                    if (ordNote.getText() != null) {
                        existingOrdNote.setText(ordNote.getText());
                    }
                    if (ordNote.getCreatedDate() != null) {
                        existingOrdNote.setCreatedDate(ordNote.getCreatedDate());
                    }

                    return existingOrdNote;
                }
            )
            .map(ordNoteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdNote> findAll() {
        log.debug("Request to get all OrdNotes");
        return ordNoteRepository.findAll();
    }

    /**
     *  Get all the ordNotes where OrdProductOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdNote> findAllWhereOrdProductOrderIsNull() {
        log.debug("Request to get all ordNotes where OrdProductOrder is null");
        return StreamSupport
            .stream(ordNoteRepository.findAll().spliterator(), false)
            .filter(ordNote -> ordNote.getOrdProductOrder() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdNote> findOne(Long id) {
        log.debug("Request to get OrdNote : {}", id);
        return ordNoteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdNote : {}", id);
        ordNoteRepository.deleteById(id);
    }
}

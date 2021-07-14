package com.apptium.repository;

import com.apptium.domain.OrdNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdNoteRepository extends JpaRepository<OrdNote, Long> {}

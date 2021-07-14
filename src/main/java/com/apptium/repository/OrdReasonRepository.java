package com.apptium.repository;

import com.apptium.domain.OrdReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdReasonRepository extends JpaRepository<OrdReason, Long> {}

package com.apptium.repository;

import com.apptium.domain.OrdAcquisition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdAcquisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdAcquisitionRepository extends JpaRepository<OrdAcquisition, Long> {}

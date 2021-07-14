package com.apptium.repository;

import com.apptium.domain.OrdCharacteristics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdCharacteristics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdCharacteristicsRepository extends JpaRepository<OrdCharacteristics, Long> {}

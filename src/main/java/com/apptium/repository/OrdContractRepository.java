package com.apptium.repository;

import com.apptium.domain.OrdContract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdContractRepository extends JpaRepository<OrdContract, Long> {}

package com.apptium.repository;

import com.apptium.domain.OrdContactDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdContactDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdContactDetailsRepository extends JpaRepository<OrdContactDetails, Long> {}

package com.apptium.repository;

import com.apptium.domain.OrdProductOfferingRef;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdProductOfferingRef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdProductOfferingRefRepository extends JpaRepository<OrdProductOfferingRef, Long> {}

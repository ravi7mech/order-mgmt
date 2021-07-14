package com.apptium.repository;

import com.apptium.domain.OrdPaymentRef;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdPaymentRef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdPaymentRefRepository extends JpaRepository<OrdPaymentRef, Long> {}

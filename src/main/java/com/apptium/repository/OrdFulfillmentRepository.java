package com.apptium.repository;

import com.apptium.domain.OrdFulfillment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdFulfillment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdFulfillmentRepository extends JpaRepository<OrdFulfillment, Long> {}

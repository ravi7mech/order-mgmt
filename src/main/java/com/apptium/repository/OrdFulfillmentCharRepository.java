package com.apptium.repository;

import com.apptium.domain.OrdFulfillmentChar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdFulfillmentChar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdFulfillmentCharRepository extends JpaRepository<OrdFulfillmentChar, Long> {}

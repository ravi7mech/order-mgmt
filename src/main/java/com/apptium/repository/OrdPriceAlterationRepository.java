package com.apptium.repository;

import com.apptium.domain.OrdPriceAlteration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdPriceAlteration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdPriceAlterationRepository extends JpaRepository<OrdPriceAlteration, Long> {}

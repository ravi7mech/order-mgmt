package com.apptium.repository;

import com.apptium.domain.OrdPriceAmount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdPriceAmount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdPriceAmountRepository extends JpaRepository<OrdPriceAmount, Long> {}

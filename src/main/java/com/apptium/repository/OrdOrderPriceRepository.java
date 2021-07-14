package com.apptium.repository;

import com.apptium.domain.OrdOrderPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdOrderPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdOrderPriceRepository extends JpaRepository<OrdOrderPrice, Long> {}

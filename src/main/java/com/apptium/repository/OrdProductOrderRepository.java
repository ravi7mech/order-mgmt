package com.apptium.repository;

import com.apptium.domain.OrdProductOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdProductOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdProductOrderRepository extends JpaRepository<OrdProductOrder, Long> {}

package com.apptium.repository;

import com.apptium.domain.OrdOrderItemProvisioning;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdOrderItemProvisioning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdOrderItemProvisioningRepository extends JpaRepository<OrdOrderItemProvisioning, Long> {}

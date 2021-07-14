package com.apptium.repository;

import com.apptium.domain.OrdOrderItemChar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdOrderItemChar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdOrderItemCharRepository extends JpaRepository<OrdOrderItemChar, Long> {}

package com.apptium.repository;

import com.apptium.domain.OrdProvisiongChar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdProvisiongChar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdProvisiongCharRepository extends JpaRepository<OrdProvisiongChar, Long> {}

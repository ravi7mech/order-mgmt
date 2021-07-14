package com.apptium.repository;

import com.apptium.domain.OrdPlace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdPlaceRepository extends JpaRepository<OrdPlace, Long> {}

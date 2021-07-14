package com.apptium.repository;

import com.apptium.domain.OrdChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdChannelRepository extends JpaRepository<OrdChannel, Long> {}

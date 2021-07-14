package com.apptium.repository;

import com.apptium.domain.OrdOrderItemRelationship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrdOrderItemRelationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdOrderItemRelationshipRepository extends JpaRepository<OrdOrderItemRelationship, Long> {}

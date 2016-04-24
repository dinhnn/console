package com.nido.console.repository;

import com.nido.console.domain.ExternalService;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExternalService entity.
 */
public interface ExternalServiceRepository extends JpaRepository<ExternalService,Long> {

}

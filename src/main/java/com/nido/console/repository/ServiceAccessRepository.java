package com.nido.console.repository;

import com.nido.console.domain.ServiceAccess;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceAccess entity.
 */
public interface ServiceAccessRepository extends JpaRepository<ServiceAccess,Long> {

}

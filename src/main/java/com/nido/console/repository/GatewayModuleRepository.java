package com.nido.console.repository;

import com.nido.console.domain.GatewayModule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GatewayModule entity.
 */
public interface GatewayModuleRepository extends JpaRepository<GatewayModule,Long> {

}

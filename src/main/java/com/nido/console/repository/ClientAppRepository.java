package com.nido.console.repository;

import com.nido.console.domain.ClientApp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClientApp entity.
 */
public interface ClientAppRepository extends JpaRepository<ClientApp,Long> {

}

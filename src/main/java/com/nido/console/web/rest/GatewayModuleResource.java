package com.nido.console.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nido.console.domain.GatewayModule;
import com.nido.console.repository.GatewayModuleRepository;
import com.nido.console.repository.search.GatewayModuleSearchRepository;
import com.nido.console.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing GatewayModule.
 */
@RestController
@RequestMapping("/api")
public class GatewayModuleResource {

    private final Logger log = LoggerFactory.getLogger(GatewayModuleResource.class);
        
    @Inject
    private GatewayModuleRepository gatewayModuleRepository;
    
    @Inject
    private GatewayModuleSearchRepository gatewayModuleSearchRepository;
    
    /**
     * POST  /gateway-modules : Create a new gatewayModule.
     *
     * @param gatewayModule the gatewayModule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gatewayModule, or with status 400 (Bad Request) if the gatewayModule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gateway-modules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GatewayModule> createGatewayModule(@Valid @RequestBody GatewayModule gatewayModule) throws URISyntaxException {
        log.debug("REST request to save GatewayModule : {}", gatewayModule);
        if (gatewayModule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gatewayModule", "idexists", "A new gatewayModule cannot already have an ID")).body(null);
        }
        GatewayModule result = gatewayModuleRepository.save(gatewayModule);
        gatewayModuleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/gateway-modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gatewayModule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gateway-modules : Updates an existing gatewayModule.
     *
     * @param gatewayModule the gatewayModule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gatewayModule,
     * or with status 400 (Bad Request) if the gatewayModule is not valid,
     * or with status 500 (Internal Server Error) if the gatewayModule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gateway-modules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GatewayModule> updateGatewayModule(@Valid @RequestBody GatewayModule gatewayModule) throws URISyntaxException {
        log.debug("REST request to update GatewayModule : {}", gatewayModule);
        if (gatewayModule.getId() == null) {
            return createGatewayModule(gatewayModule);
        }
        GatewayModule result = gatewayModuleRepository.save(gatewayModule);
        gatewayModuleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gatewayModule", gatewayModule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gateway-modules : get all the gatewayModules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gatewayModules in body
     */
    @RequestMapping(value = "/gateway-modules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GatewayModule> getAllGatewayModules() {
        log.debug("REST request to get all GatewayModules");
        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        return gatewayModules;
    }

    /**
     * GET  /gateway-modules/:id : get the "id" gatewayModule.
     *
     * @param id the id of the gatewayModule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gatewayModule, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gateway-modules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GatewayModule> getGatewayModule(@PathVariable Long id) {
        log.debug("REST request to get GatewayModule : {}", id);
        GatewayModule gatewayModule = gatewayModuleRepository.findOne(id);
        return Optional.ofNullable(gatewayModule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gateway-modules/:id : delete the "id" gatewayModule.
     *
     * @param id the id of the gatewayModule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gateway-modules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGatewayModule(@PathVariable Long id) {
        log.debug("REST request to delete GatewayModule : {}", id);
        gatewayModuleRepository.delete(id);
        gatewayModuleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gatewayModule", id.toString())).build();
    }

    /**
     * SEARCH  /_search/gateway-modules?query=:query : search for the gatewayModule corresponding
     * to the query.
     *
     * @param query the query of the gatewayModule search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/gateway-modules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GatewayModule> searchGatewayModules(@RequestParam String query) {
        log.debug("REST request to search GatewayModules for query {}", query);
        return StreamSupport
            .stream(gatewayModuleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

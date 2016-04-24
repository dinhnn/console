package com.nido.console.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nido.console.domain.ServiceAccess;
import com.nido.console.repository.ServiceAccessRepository;
import com.nido.console.repository.search.ServiceAccessSearchRepository;
import com.nido.console.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ServiceAccess.
 */
@RestController
@RequestMapping("/api")
public class ServiceAccessResource {

    private final Logger log = LoggerFactory.getLogger(ServiceAccessResource.class);
        
    @Inject
    private ServiceAccessRepository serviceAccessRepository;
    
    @Inject
    private ServiceAccessSearchRepository serviceAccessSearchRepository;
    
    /**
     * POST  /service-accesses : Create a new serviceAccess.
     *
     * @param serviceAccess the serviceAccess to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceAccess, or with status 400 (Bad Request) if the serviceAccess has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-accesses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceAccess> createServiceAccess(@RequestBody ServiceAccess serviceAccess) throws URISyntaxException {
        log.debug("REST request to save ServiceAccess : {}", serviceAccess);
        if (serviceAccess.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceAccess", "idexists", "A new serviceAccess cannot already have an ID")).body(null);
        }
        ServiceAccess result = serviceAccessRepository.save(serviceAccess);
        serviceAccessSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/service-accesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceAccess", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-accesses : Updates an existing serviceAccess.
     *
     * @param serviceAccess the serviceAccess to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceAccess,
     * or with status 400 (Bad Request) if the serviceAccess is not valid,
     * or with status 500 (Internal Server Error) if the serviceAccess couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-accesses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceAccess> updateServiceAccess(@RequestBody ServiceAccess serviceAccess) throws URISyntaxException {
        log.debug("REST request to update ServiceAccess : {}", serviceAccess);
        if (serviceAccess.getId() == null) {
            return createServiceAccess(serviceAccess);
        }
        ServiceAccess result = serviceAccessRepository.save(serviceAccess);
        serviceAccessSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceAccess", serviceAccess.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-accesses : get all the serviceAccesses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceAccesses in body
     */
    @RequestMapping(value = "/service-accesses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceAccess> getAllServiceAccesses() {
        log.debug("REST request to get all ServiceAccesses");
        List<ServiceAccess> serviceAccesses = serviceAccessRepository.findAll();
        return serviceAccesses;
    }

    /**
     * GET  /service-accesses/:id : get the "id" serviceAccess.
     *
     * @param id the id of the serviceAccess to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceAccess, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-accesses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceAccess> getServiceAccess(@PathVariable Long id) {
        log.debug("REST request to get ServiceAccess : {}", id);
        ServiceAccess serviceAccess = serviceAccessRepository.findOne(id);
        return Optional.ofNullable(serviceAccess)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-accesses/:id : delete the "id" serviceAccess.
     *
     * @param id the id of the serviceAccess to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-accesses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceAccess(@PathVariable Long id) {
        log.debug("REST request to delete ServiceAccess : {}", id);
        serviceAccessRepository.delete(id);
        serviceAccessSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceAccess", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-accesses?query=:query : search for the serviceAccess corresponding
     * to the query.
     *
     * @param query the query of the serviceAccess search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-accesses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceAccess> searchServiceAccesses(@RequestParam String query) {
        log.debug("REST request to search ServiceAccesses for query {}", query);
        return StreamSupport
            .stream(serviceAccessSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

package com.nido.console.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nido.console.domain.ExternalService;
import com.nido.console.repository.ExternalServiceRepository;
import com.nido.console.repository.search.ExternalServiceSearchRepository;
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
 * REST controller for managing ExternalService.
 */
@RestController
@RequestMapping("/api")
public class ExternalServiceResource {

    private final Logger log = LoggerFactory.getLogger(ExternalServiceResource.class);
        
    @Inject
    private ExternalServiceRepository externalServiceRepository;
    
    @Inject
    private ExternalServiceSearchRepository externalServiceSearchRepository;
    
    /**
     * POST  /external-services : Create a new externalService.
     *
     * @param externalService the externalService to create
     * @return the ResponseEntity with status 201 (Created) and with body the new externalService, or with status 400 (Bad Request) if the externalService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/external-services",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExternalService> createExternalService(@Valid @RequestBody ExternalService externalService) throws URISyntaxException {
        log.debug("REST request to save ExternalService : {}", externalService);
        if (externalService.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("externalService", "idexists", "A new externalService cannot already have an ID")).body(null);
        }
        ExternalService result = externalServiceRepository.save(externalService);
        externalServiceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/external-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("externalService", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /external-services : Updates an existing externalService.
     *
     * @param externalService the externalService to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated externalService,
     * or with status 400 (Bad Request) if the externalService is not valid,
     * or with status 500 (Internal Server Error) if the externalService couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/external-services",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExternalService> updateExternalService(@Valid @RequestBody ExternalService externalService) throws URISyntaxException {
        log.debug("REST request to update ExternalService : {}", externalService);
        if (externalService.getId() == null) {
            return createExternalService(externalService);
        }
        ExternalService result = externalServiceRepository.save(externalService);
        externalServiceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("externalService", externalService.getId().toString()))
            .body(result);
    }

    /**
     * GET  /external-services : get all the externalServices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of externalServices in body
     */
    @RequestMapping(value = "/external-services",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExternalService> getAllExternalServices() {
        log.debug("REST request to get all ExternalServices");
        List<ExternalService> externalServices = externalServiceRepository.findAll();
        return externalServices;
    }

    /**
     * GET  /external-services/:id : get the "id" externalService.
     *
     * @param id the id of the externalService to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the externalService, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/external-services/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExternalService> getExternalService(@PathVariable Long id) {
        log.debug("REST request to get ExternalService : {}", id);
        ExternalService externalService = externalServiceRepository.findOne(id);
        return Optional.ofNullable(externalService)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /external-services/:id : delete the "id" externalService.
     *
     * @param id the id of the externalService to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/external-services/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExternalService(@PathVariable Long id) {
        log.debug("REST request to delete ExternalService : {}", id);
        externalServiceRepository.delete(id);
        externalServiceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("externalService", id.toString())).build();
    }

    /**
     * SEARCH  /_search/external-services?query=:query : search for the externalService corresponding
     * to the query.
     *
     * @param query the query of the externalService search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/external-services",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExternalService> searchExternalServices(@RequestParam String query) {
        log.debug("REST request to search ExternalServices for query {}", query);
        return StreamSupport
            .stream(externalServiceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

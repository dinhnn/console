package com.nido.console.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nido.console.domain.ClientApp;
import com.nido.console.repository.ClientAppRepository;
import com.nido.console.repository.search.ClientAppSearchRepository;
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
 * REST controller for managing ClientApp.
 */
@RestController
@RequestMapping("/api")
public class ClientAppResource {

    private final Logger log = LoggerFactory.getLogger(ClientAppResource.class);
        
    @Inject
    private ClientAppRepository clientAppRepository;
    
    @Inject
    private ClientAppSearchRepository clientAppSearchRepository;
    
    /**
     * POST  /client-apps : Create a new clientApp.
     *
     * @param clientApp the clientApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientApp, or with status 400 (Bad Request) if the clientApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-apps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientApp> createClientApp(@Valid @RequestBody ClientApp clientApp) throws URISyntaxException {
        log.debug("REST request to save ClientApp : {}", clientApp);
        if (clientApp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clientApp", "idexists", "A new clientApp cannot already have an ID")).body(null);
        }
        ClientApp result = clientAppRepository.save(clientApp);
        clientAppSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/client-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientApp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-apps : Updates an existing clientApp.
     *
     * @param clientApp the clientApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientApp,
     * or with status 400 (Bad Request) if the clientApp is not valid,
     * or with status 500 (Internal Server Error) if the clientApp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/client-apps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientApp> updateClientApp(@Valid @RequestBody ClientApp clientApp) throws URISyntaxException {
        log.debug("REST request to update ClientApp : {}", clientApp);
        if (clientApp.getId() == null) {
            return createClientApp(clientApp);
        }
        ClientApp result = clientAppRepository.save(clientApp);
        clientAppSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clientApp", clientApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-apps : get all the clientApps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientApps in body
     */
    @RequestMapping(value = "/client-apps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClientApp> getAllClientApps() {
        log.debug("REST request to get all ClientApps");
        List<ClientApp> clientApps = clientAppRepository.findAll();
        return clientApps;
    }

    /**
     * GET  /client-apps/:id : get the "id" clientApp.
     *
     * @param id the id of the clientApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientApp, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/client-apps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientApp> getClientApp(@PathVariable Long id) {
        log.debug("REST request to get ClientApp : {}", id);
        ClientApp clientApp = clientAppRepository.findOne(id);
        return Optional.ofNullable(clientApp)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /client-apps/:id : delete the "id" clientApp.
     *
     * @param id the id of the clientApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/client-apps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClientApp(@PathVariable Long id) {
        log.debug("REST request to delete ClientApp : {}", id);
        clientAppRepository.delete(id);
        clientAppSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clientApp", id.toString())).build();
    }

    /**
     * SEARCH  /_search/client-apps?query=:query : search for the clientApp corresponding
     * to the query.
     *
     * @param query the query of the clientApp search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/client-apps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClientApp> searchClientApps(@RequestParam String query) {
        log.debug("REST request to search ClientApps for query {}", query);
        return StreamSupport
            .stream(clientAppSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

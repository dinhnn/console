package com.nido.console.web.rest;

import com.nido.console.ConsoleApp;
import com.nido.console.domain.ExternalService;
import com.nido.console.repository.ExternalServiceRepository;
import com.nido.console.repository.search.ExternalServiceSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ExternalServiceResource REST controller.
 *
 * @see ExternalServiceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsoleApp.class)
@WebAppConfiguration
@IntegrationTest
public class ExternalServiceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CONTACT = "AAAAA";
    private static final String UPDATED_CONTACT = "BBBBB";
    private static final String DEFAULT_ENDPOINT = "AAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBB";
    private static final String DEFAULT_HEALTH_CHECK = "AAAAA";
    private static final String UPDATED_HEALTH_CHECK = "BBBBB";

    private static final Integer DEFAULT_RATE_LIMIT = 1;
    private static final Integer UPDATED_RATE_LIMIT = 2;
    private static final String DEFAULT_METHODS = "AAAAA";
    private static final String UPDATED_METHODS = "BBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    @Inject
    private ExternalServiceRepository externalServiceRepository;

    @Inject
    private ExternalServiceSearchRepository externalServiceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExternalServiceMockMvc;

    private ExternalService externalService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExternalServiceResource externalServiceResource = new ExternalServiceResource();
        ReflectionTestUtils.setField(externalServiceResource, "externalServiceSearchRepository", externalServiceSearchRepository);
        ReflectionTestUtils.setField(externalServiceResource, "externalServiceRepository", externalServiceRepository);
        this.restExternalServiceMockMvc = MockMvcBuilders.standaloneSetup(externalServiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        externalServiceSearchRepository.deleteAll();
        externalService = new ExternalService();
        externalService.setName(DEFAULT_NAME);
        externalService.setDescription(DEFAULT_DESCRIPTION);
        externalService.setContact(DEFAULT_CONTACT);
        externalService.setEndpoint(DEFAULT_ENDPOINT);
        externalService.setHealthCheck(DEFAULT_HEALTH_CHECK);
        externalService.setRateLimit(DEFAULT_RATE_LIMIT);
        externalService.setMethods(DEFAULT_METHODS);
        externalService.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createExternalService() throws Exception {
        int databaseSizeBeforeCreate = externalServiceRepository.findAll().size();

        // Create the ExternalService

        restExternalServiceMockMvc.perform(post("/api/external-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(externalService)))
                .andExpect(status().isCreated());

        // Validate the ExternalService in the database
        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeCreate + 1);
        ExternalService testExternalService = externalServices.get(externalServices.size() - 1);
        assertThat(testExternalService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExternalService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExternalService.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testExternalService.getEndpoint()).isEqualTo(DEFAULT_ENDPOINT);
        assertThat(testExternalService.getHealthCheck()).isEqualTo(DEFAULT_HEALTH_CHECK);
        assertThat(testExternalService.getRateLimit()).isEqualTo(DEFAULT_RATE_LIMIT);
        assertThat(testExternalService.getMethods()).isEqualTo(DEFAULT_METHODS);
        assertThat(testExternalService.getVersion()).isEqualTo(DEFAULT_VERSION);

        // Validate the ExternalService in ElasticSearch
        ExternalService externalServiceEs = externalServiceSearchRepository.findOne(testExternalService.getId());
        assertThat(externalServiceEs).isEqualToComparingFieldByField(testExternalService);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = externalServiceRepository.findAll().size();
        // set the field null
        externalService.setName(null);

        // Create the ExternalService, which fails.

        restExternalServiceMockMvc.perform(post("/api/external-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(externalService)))
                .andExpect(status().isBadRequest());

        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndpointIsRequired() throws Exception {
        int databaseSizeBeforeTest = externalServiceRepository.findAll().size();
        // set the field null
        externalService.setEndpoint(null);

        // Create the ExternalService, which fails.

        restExternalServiceMockMvc.perform(post("/api/external-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(externalService)))
                .andExpect(status().isBadRequest());

        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHealthCheckIsRequired() throws Exception {
        int databaseSizeBeforeTest = externalServiceRepository.findAll().size();
        // set the field null
        externalService.setHealthCheck(null);

        // Create the ExternalService, which fails.

        restExternalServiceMockMvc.perform(post("/api/external-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(externalService)))
                .andExpect(status().isBadRequest());

        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExternalServices() throws Exception {
        // Initialize the database
        externalServiceRepository.saveAndFlush(externalService);

        // Get all the externalServices
        restExternalServiceMockMvc.perform(get("/api/external-services?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(externalService.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT.toString())))
                .andExpect(jsonPath("$.[*].healthCheck").value(hasItem(DEFAULT_HEALTH_CHECK.toString())))
                .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT)))
                .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    public void getExternalService() throws Exception {
        // Initialize the database
        externalServiceRepository.saveAndFlush(externalService);

        // Get the externalService
        restExternalServiceMockMvc.perform(get("/api/external-services/{id}", externalService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(externalService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT.toString()))
            .andExpect(jsonPath("$.healthCheck").value(DEFAULT_HEALTH_CHECK.toString()))
            .andExpect(jsonPath("$.rateLimit").value(DEFAULT_RATE_LIMIT))
            .andExpect(jsonPath("$.methods").value(DEFAULT_METHODS.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    public void getNonExistingExternalService() throws Exception {
        // Get the externalService
        restExternalServiceMockMvc.perform(get("/api/external-services/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExternalService() throws Exception {
        // Initialize the database
        externalServiceRepository.saveAndFlush(externalService);
        externalServiceSearchRepository.save(externalService);
        int databaseSizeBeforeUpdate = externalServiceRepository.findAll().size();

        // Update the externalService
        ExternalService updatedExternalService = new ExternalService();
        updatedExternalService.setId(externalService.getId());
        updatedExternalService.setName(UPDATED_NAME);
        updatedExternalService.setDescription(UPDATED_DESCRIPTION);
        updatedExternalService.setContact(UPDATED_CONTACT);
        updatedExternalService.setEndpoint(UPDATED_ENDPOINT);
        updatedExternalService.setHealthCheck(UPDATED_HEALTH_CHECK);
        updatedExternalService.setRateLimit(UPDATED_RATE_LIMIT);
        updatedExternalService.setMethods(UPDATED_METHODS);
        updatedExternalService.setVersion(UPDATED_VERSION);

        restExternalServiceMockMvc.perform(put("/api/external-services")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedExternalService)))
                .andExpect(status().isOk());

        // Validate the ExternalService in the database
        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeUpdate);
        ExternalService testExternalService = externalServices.get(externalServices.size() - 1);
        assertThat(testExternalService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExternalService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExternalService.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testExternalService.getEndpoint()).isEqualTo(UPDATED_ENDPOINT);
        assertThat(testExternalService.getHealthCheck()).isEqualTo(UPDATED_HEALTH_CHECK);
        assertThat(testExternalService.getRateLimit()).isEqualTo(UPDATED_RATE_LIMIT);
        assertThat(testExternalService.getMethods()).isEqualTo(UPDATED_METHODS);
        assertThat(testExternalService.getVersion()).isEqualTo(UPDATED_VERSION);

        // Validate the ExternalService in ElasticSearch
        ExternalService externalServiceEs = externalServiceSearchRepository.findOne(testExternalService.getId());
        assertThat(externalServiceEs).isEqualToComparingFieldByField(testExternalService);
    }

    @Test
    @Transactional
    public void deleteExternalService() throws Exception {
        // Initialize the database
        externalServiceRepository.saveAndFlush(externalService);
        externalServiceSearchRepository.save(externalService);
        int databaseSizeBeforeDelete = externalServiceRepository.findAll().size();

        // Get the externalService
        restExternalServiceMockMvc.perform(delete("/api/external-services/{id}", externalService.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean externalServiceExistsInEs = externalServiceSearchRepository.exists(externalService.getId());
        assertThat(externalServiceExistsInEs).isFalse();

        // Validate the database is empty
        List<ExternalService> externalServices = externalServiceRepository.findAll();
        assertThat(externalServices).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExternalService() throws Exception {
        // Initialize the database
        externalServiceRepository.saveAndFlush(externalService);
        externalServiceSearchRepository.save(externalService);

        // Search the externalService
        restExternalServiceMockMvc.perform(get("/api/_search/external-services?query=id:" + externalService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT.toString())))
            .andExpect(jsonPath("$.[*].healthCheck").value(hasItem(DEFAULT_HEALTH_CHECK.toString())))
            .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT)))
            .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }
}

package com.nido.console.web.rest;

import com.nido.console.ConsoleApp;
import com.nido.console.domain.ServiceAccess;
import com.nido.console.repository.ServiceAccessRepository;
import com.nido.console.repository.search.ServiceAccessSearchRepository;

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
 * Test class for the ServiceAccessResource REST controller.
 *
 * @see ServiceAccessResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsoleApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceAccessResourceIntTest {


    private static final Integer DEFAULT_RATE_LIMIT = 1;
    private static final Integer UPDATED_RATE_LIMIT = 2;
    private static final String DEFAULT_METHODS = "AAAAA";
    private static final String UPDATED_METHODS = "BBBBB";

    @Inject
    private ServiceAccessRepository serviceAccessRepository;

    @Inject
    private ServiceAccessSearchRepository serviceAccessSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceAccessMockMvc;

    private ServiceAccess serviceAccess;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceAccessResource serviceAccessResource = new ServiceAccessResource();
        ReflectionTestUtils.setField(serviceAccessResource, "serviceAccessSearchRepository", serviceAccessSearchRepository);
        ReflectionTestUtils.setField(serviceAccessResource, "serviceAccessRepository", serviceAccessRepository);
        this.restServiceAccessMockMvc = MockMvcBuilders.standaloneSetup(serviceAccessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceAccessSearchRepository.deleteAll();
        serviceAccess = new ServiceAccess();
        serviceAccess.setRateLimit(DEFAULT_RATE_LIMIT);
        serviceAccess.setMethods(DEFAULT_METHODS);
    }

    @Test
    @Transactional
    public void createServiceAccess() throws Exception {
        int databaseSizeBeforeCreate = serviceAccessRepository.findAll().size();

        // Create the ServiceAccess

        restServiceAccessMockMvc.perform(post("/api/service-accesses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceAccess)))
                .andExpect(status().isCreated());

        // Validate the ServiceAccess in the database
        List<ServiceAccess> serviceAccesses = serviceAccessRepository.findAll();
        assertThat(serviceAccesses).hasSize(databaseSizeBeforeCreate + 1);
        ServiceAccess testServiceAccess = serviceAccesses.get(serviceAccesses.size() - 1);
        assertThat(testServiceAccess.getRateLimit()).isEqualTo(DEFAULT_RATE_LIMIT);
        assertThat(testServiceAccess.getMethods()).isEqualTo(DEFAULT_METHODS);

        // Validate the ServiceAccess in ElasticSearch
        ServiceAccess serviceAccessEs = serviceAccessSearchRepository.findOne(testServiceAccess.getId());
        assertThat(serviceAccessEs).isEqualToComparingFieldByField(testServiceAccess);
    }

    @Test
    @Transactional
    public void getAllServiceAccesses() throws Exception {
        // Initialize the database
        serviceAccessRepository.saveAndFlush(serviceAccess);

        // Get all the serviceAccesses
        restServiceAccessMockMvc.perform(get("/api/service-accesses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceAccess.getId().intValue())))
                .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT)))
                .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS.toString())));
    }

    @Test
    @Transactional
    public void getServiceAccess() throws Exception {
        // Initialize the database
        serviceAccessRepository.saveAndFlush(serviceAccess);

        // Get the serviceAccess
        restServiceAccessMockMvc.perform(get("/api/service-accesses/{id}", serviceAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceAccess.getId().intValue()))
            .andExpect(jsonPath("$.rateLimit").value(DEFAULT_RATE_LIMIT))
            .andExpect(jsonPath("$.methods").value(DEFAULT_METHODS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceAccess() throws Exception {
        // Get the serviceAccess
        restServiceAccessMockMvc.perform(get("/api/service-accesses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceAccess() throws Exception {
        // Initialize the database
        serviceAccessRepository.saveAndFlush(serviceAccess);
        serviceAccessSearchRepository.save(serviceAccess);
        int databaseSizeBeforeUpdate = serviceAccessRepository.findAll().size();

        // Update the serviceAccess
        ServiceAccess updatedServiceAccess = new ServiceAccess();
        updatedServiceAccess.setId(serviceAccess.getId());
        updatedServiceAccess.setRateLimit(UPDATED_RATE_LIMIT);
        updatedServiceAccess.setMethods(UPDATED_METHODS);

        restServiceAccessMockMvc.perform(put("/api/service-accesses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedServiceAccess)))
                .andExpect(status().isOk());

        // Validate the ServiceAccess in the database
        List<ServiceAccess> serviceAccesses = serviceAccessRepository.findAll();
        assertThat(serviceAccesses).hasSize(databaseSizeBeforeUpdate);
        ServiceAccess testServiceAccess = serviceAccesses.get(serviceAccesses.size() - 1);
        assertThat(testServiceAccess.getRateLimit()).isEqualTo(UPDATED_RATE_LIMIT);
        assertThat(testServiceAccess.getMethods()).isEqualTo(UPDATED_METHODS);

        // Validate the ServiceAccess in ElasticSearch
        ServiceAccess serviceAccessEs = serviceAccessSearchRepository.findOne(testServiceAccess.getId());
        assertThat(serviceAccessEs).isEqualToComparingFieldByField(testServiceAccess);
    }

    @Test
    @Transactional
    public void deleteServiceAccess() throws Exception {
        // Initialize the database
        serviceAccessRepository.saveAndFlush(serviceAccess);
        serviceAccessSearchRepository.save(serviceAccess);
        int databaseSizeBeforeDelete = serviceAccessRepository.findAll().size();

        // Get the serviceAccess
        restServiceAccessMockMvc.perform(delete("/api/service-accesses/{id}", serviceAccess.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceAccessExistsInEs = serviceAccessSearchRepository.exists(serviceAccess.getId());
        assertThat(serviceAccessExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceAccess> serviceAccesses = serviceAccessRepository.findAll();
        assertThat(serviceAccesses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceAccess() throws Exception {
        // Initialize the database
        serviceAccessRepository.saveAndFlush(serviceAccess);
        serviceAccessSearchRepository.save(serviceAccess);

        // Search the serviceAccess
        restServiceAccessMockMvc.perform(get("/api/_search/service-accesses?query=id:" + serviceAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateLimit").value(hasItem(DEFAULT_RATE_LIMIT)))
            .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS.toString())));
    }
}

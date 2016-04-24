package com.nido.console.web.rest;

import com.nido.console.ConsoleApp;
import com.nido.console.domain.ClientApp;
import com.nido.console.repository.ClientAppRepository;
import com.nido.console.repository.search.ClientAppSearchRepository;

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
 * Test class for the ClientAppResource REST controller.
 *
 * @see ClientAppResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsoleApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClientAppResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CONTACT = "AAAAA";
    private static final String UPDATED_CONTACT = "BBBBB";

    @Inject
    private ClientAppRepository clientAppRepository;

    @Inject
    private ClientAppSearchRepository clientAppSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientAppMockMvc;

    private ClientApp clientApp;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientAppResource clientAppResource = new ClientAppResource();
        ReflectionTestUtils.setField(clientAppResource, "clientAppSearchRepository", clientAppSearchRepository);
        ReflectionTestUtils.setField(clientAppResource, "clientAppRepository", clientAppRepository);
        this.restClientAppMockMvc = MockMvcBuilders.standaloneSetup(clientAppResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientAppSearchRepository.deleteAll();
        clientApp = new ClientApp();
        clientApp.setName(DEFAULT_NAME);
        clientApp.setDescription(DEFAULT_DESCRIPTION);
        clientApp.setContact(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void createClientApp() throws Exception {
        int databaseSizeBeforeCreate = clientAppRepository.findAll().size();

        // Create the ClientApp

        restClientAppMockMvc.perform(post("/api/client-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientApp)))
                .andExpect(status().isCreated());

        // Validate the ClientApp in the database
        List<ClientApp> clientApps = clientAppRepository.findAll();
        assertThat(clientApps).hasSize(databaseSizeBeforeCreate + 1);
        ClientApp testClientApp = clientApps.get(clientApps.size() - 1);
        assertThat(testClientApp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientApp.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClientApp.getContact()).isEqualTo(DEFAULT_CONTACT);

        // Validate the ClientApp in ElasticSearch
        ClientApp clientAppEs = clientAppSearchRepository.findOne(testClientApp.getId());
        assertThat(clientAppEs).isEqualToComparingFieldByField(testClientApp);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientAppRepository.findAll().size();
        // set the field null
        clientApp.setName(null);

        // Create the ClientApp, which fails.

        restClientAppMockMvc.perform(post("/api/client-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientApp)))
                .andExpect(status().isBadRequest());

        List<ClientApp> clientApps = clientAppRepository.findAll();
        assertThat(clientApps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientApps() throws Exception {
        // Initialize the database
        clientAppRepository.saveAndFlush(clientApp);

        // Get all the clientApps
        restClientAppMockMvc.perform(get("/api/client-apps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientApp.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }

    @Test
    @Transactional
    public void getClientApp() throws Exception {
        // Initialize the database
        clientAppRepository.saveAndFlush(clientApp);

        // Get the clientApp
        restClientAppMockMvc.perform(get("/api/client-apps/{id}", clientApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clientApp.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClientApp() throws Exception {
        // Get the clientApp
        restClientAppMockMvc.perform(get("/api/client-apps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientApp() throws Exception {
        // Initialize the database
        clientAppRepository.saveAndFlush(clientApp);
        clientAppSearchRepository.save(clientApp);
        int databaseSizeBeforeUpdate = clientAppRepository.findAll().size();

        // Update the clientApp
        ClientApp updatedClientApp = new ClientApp();
        updatedClientApp.setId(clientApp.getId());
        updatedClientApp.setName(UPDATED_NAME);
        updatedClientApp.setDescription(UPDATED_DESCRIPTION);
        updatedClientApp.setContact(UPDATED_CONTACT);

        restClientAppMockMvc.perform(put("/api/client-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClientApp)))
                .andExpect(status().isOk());

        // Validate the ClientApp in the database
        List<ClientApp> clientApps = clientAppRepository.findAll();
        assertThat(clientApps).hasSize(databaseSizeBeforeUpdate);
        ClientApp testClientApp = clientApps.get(clientApps.size() - 1);
        assertThat(testClientApp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientApp.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClientApp.getContact()).isEqualTo(UPDATED_CONTACT);

        // Validate the ClientApp in ElasticSearch
        ClientApp clientAppEs = clientAppSearchRepository.findOne(testClientApp.getId());
        assertThat(clientAppEs).isEqualToComparingFieldByField(testClientApp);
    }

    @Test
    @Transactional
    public void deleteClientApp() throws Exception {
        // Initialize the database
        clientAppRepository.saveAndFlush(clientApp);
        clientAppSearchRepository.save(clientApp);
        int databaseSizeBeforeDelete = clientAppRepository.findAll().size();

        // Get the clientApp
        restClientAppMockMvc.perform(delete("/api/client-apps/{id}", clientApp.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientAppExistsInEs = clientAppSearchRepository.exists(clientApp.getId());
        assertThat(clientAppExistsInEs).isFalse();

        // Validate the database is empty
        List<ClientApp> clientApps = clientAppRepository.findAll();
        assertThat(clientApps).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClientApp() throws Exception {
        // Initialize the database
        clientAppRepository.saveAndFlush(clientApp);
        clientAppSearchRepository.save(clientApp);

        // Search the clientApp
        restClientAppMockMvc.perform(get("/api/_search/client-apps?query=id:" + clientApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }
}

package com.nido.console.web.rest;

import com.nido.console.ConsoleApp;
import com.nido.console.domain.GatewayModule;
import com.nido.console.repository.GatewayModuleRepository;
import com.nido.console.repository.search.GatewayModuleSearchRepository;

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
 * Test class for the GatewayModuleResource REST controller.
 *
 * @see GatewayModuleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsoleApp.class)
@WebAppConfiguration
@IntegrationTest
public class GatewayModuleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ARTIFACT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ARTIFACT = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Integer DEFAULT_INSTANCES = 1;
    private static final Integer UPDATED_INSTANCES = 2;
    private static final String DEFAULT_SETTINGS = "AAAAA";
    private static final String UPDATED_SETTINGS = "BBBBB";

    @Inject
    private GatewayModuleRepository gatewayModuleRepository;

    @Inject
    private GatewayModuleSearchRepository gatewayModuleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGatewayModuleMockMvc;

    private GatewayModule gatewayModule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GatewayModuleResource gatewayModuleResource = new GatewayModuleResource();
        ReflectionTestUtils.setField(gatewayModuleResource, "gatewayModuleSearchRepository", gatewayModuleSearchRepository);
        ReflectionTestUtils.setField(gatewayModuleResource, "gatewayModuleRepository", gatewayModuleRepository);
        this.restGatewayModuleMockMvc = MockMvcBuilders.standaloneSetup(gatewayModuleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gatewayModuleSearchRepository.deleteAll();
        gatewayModule = new GatewayModule();
        gatewayModule.setName(DEFAULT_NAME);
        gatewayModule.setDescription(DEFAULT_DESCRIPTION);
        gatewayModule.setArtifact(DEFAULT_ARTIFACT);
        gatewayModule.setActivated(DEFAULT_ACTIVATED);
        gatewayModule.setInstances(DEFAULT_INSTANCES);
        gatewayModule.setSettings(DEFAULT_SETTINGS);
    }

    @Test
    @Transactional
    public void createGatewayModule() throws Exception {
        int databaseSizeBeforeCreate = gatewayModuleRepository.findAll().size();

        // Create the GatewayModule

        restGatewayModuleMockMvc.perform(post("/api/gateway-modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gatewayModule)))
                .andExpect(status().isCreated());

        // Validate the GatewayModule in the database
        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeCreate + 1);
        GatewayModule testGatewayModule = gatewayModules.get(gatewayModules.size() - 1);
        assertThat(testGatewayModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGatewayModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGatewayModule.getArtifact()).isEqualTo(DEFAULT_ARTIFACT);
        assertThat(testGatewayModule.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testGatewayModule.getInstances()).isEqualTo(DEFAULT_INSTANCES);
        assertThat(testGatewayModule.getSettings()).isEqualTo(DEFAULT_SETTINGS);

        // Validate the GatewayModule in ElasticSearch
        GatewayModule gatewayModuleEs = gatewayModuleSearchRepository.findOne(testGatewayModule.getId());
        assertThat(gatewayModuleEs).isEqualToComparingFieldByField(testGatewayModule);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayModuleRepository.findAll().size();
        // set the field null
        gatewayModule.setName(null);

        // Create the GatewayModule, which fails.

        restGatewayModuleMockMvc.perform(post("/api/gateway-modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gatewayModule)))
                .andExpect(status().isBadRequest());

        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArtifactIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayModuleRepository.findAll().size();
        // set the field null
        gatewayModule.setArtifact(null);

        // Create the GatewayModule, which fails.

        restGatewayModuleMockMvc.perform(post("/api/gateway-modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gatewayModule)))
                .andExpect(status().isBadRequest());

        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInstancesIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayModuleRepository.findAll().size();
        // set the field null
        gatewayModule.setInstances(null);

        // Create the GatewayModule, which fails.

        restGatewayModuleMockMvc.perform(post("/api/gateway-modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gatewayModule)))
                .andExpect(status().isBadRequest());

        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGatewayModules() throws Exception {
        // Initialize the database
        gatewayModuleRepository.saveAndFlush(gatewayModule);

        // Get all the gatewayModules
        restGatewayModuleMockMvc.perform(get("/api/gateway-modules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gatewayModule.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].artifact").value(hasItem(DEFAULT_ARTIFACT.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
                .andExpect(jsonPath("$.[*].instances").value(hasItem(DEFAULT_INSTANCES)))
                .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS.toString())));
    }

    @Test
    @Transactional
    public void getGatewayModule() throws Exception {
        // Initialize the database
        gatewayModuleRepository.saveAndFlush(gatewayModule);

        // Get the gatewayModule
        restGatewayModuleMockMvc.perform(get("/api/gateway-modules/{id}", gatewayModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gatewayModule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.artifact").value(DEFAULT_ARTIFACT.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.instances").value(DEFAULT_INSTANCES))
            .andExpect(jsonPath("$.settings").value(DEFAULT_SETTINGS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGatewayModule() throws Exception {
        // Get the gatewayModule
        restGatewayModuleMockMvc.perform(get("/api/gateway-modules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGatewayModule() throws Exception {
        // Initialize the database
        gatewayModuleRepository.saveAndFlush(gatewayModule);
        gatewayModuleSearchRepository.save(gatewayModule);
        int databaseSizeBeforeUpdate = gatewayModuleRepository.findAll().size();

        // Update the gatewayModule
        GatewayModule updatedGatewayModule = new GatewayModule();
        updatedGatewayModule.setId(gatewayModule.getId());
        updatedGatewayModule.setName(UPDATED_NAME);
        updatedGatewayModule.setDescription(UPDATED_DESCRIPTION);
        updatedGatewayModule.setArtifact(UPDATED_ARTIFACT);
        updatedGatewayModule.setActivated(UPDATED_ACTIVATED);
        updatedGatewayModule.setInstances(UPDATED_INSTANCES);
        updatedGatewayModule.setSettings(UPDATED_SETTINGS);

        restGatewayModuleMockMvc.perform(put("/api/gateway-modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGatewayModule)))
                .andExpect(status().isOk());

        // Validate the GatewayModule in the database
        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeUpdate);
        GatewayModule testGatewayModule = gatewayModules.get(gatewayModules.size() - 1);
        assertThat(testGatewayModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGatewayModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGatewayModule.getArtifact()).isEqualTo(UPDATED_ARTIFACT);
        assertThat(testGatewayModule.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testGatewayModule.getInstances()).isEqualTo(UPDATED_INSTANCES);
        assertThat(testGatewayModule.getSettings()).isEqualTo(UPDATED_SETTINGS);

        // Validate the GatewayModule in ElasticSearch
        GatewayModule gatewayModuleEs = gatewayModuleSearchRepository.findOne(testGatewayModule.getId());
        assertThat(gatewayModuleEs).isEqualToComparingFieldByField(testGatewayModule);
    }

    @Test
    @Transactional
    public void deleteGatewayModule() throws Exception {
        // Initialize the database
        gatewayModuleRepository.saveAndFlush(gatewayModule);
        gatewayModuleSearchRepository.save(gatewayModule);
        int databaseSizeBeforeDelete = gatewayModuleRepository.findAll().size();

        // Get the gatewayModule
        restGatewayModuleMockMvc.perform(delete("/api/gateway-modules/{id}", gatewayModule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gatewayModuleExistsInEs = gatewayModuleSearchRepository.exists(gatewayModule.getId());
        assertThat(gatewayModuleExistsInEs).isFalse();

        // Validate the database is empty
        List<GatewayModule> gatewayModules = gatewayModuleRepository.findAll();
        assertThat(gatewayModules).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGatewayModule() throws Exception {
        // Initialize the database
        gatewayModuleRepository.saveAndFlush(gatewayModule);
        gatewayModuleSearchRepository.save(gatewayModule);

        // Search the gatewayModule
        restGatewayModuleMockMvc.perform(get("/api/_search/gateway-modules?query=id:" + gatewayModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gatewayModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].artifact").value(hasItem(DEFAULT_ARTIFACT.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].instances").value(hasItem(DEFAULT_INSTANCES)))
            .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS.toString())));
    }
}

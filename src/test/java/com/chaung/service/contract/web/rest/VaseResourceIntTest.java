package com.chaung.service.contract.web.rest;

import com.chaung.service.contract.ServiceContractApp;

import com.chaung.service.contract.domain.Vase;
import com.chaung.service.contract.domain.Product;
import com.chaung.service.contract.repository.VaseRepository;
import com.chaung.service.contract.service.VaseService;
import com.chaung.service.contract.service.dto.VaseDTO;
import com.chaung.service.contract.service.mapper.VaseMapper;
import com.chaung.service.contract.web.rest.errors.ExceptionTranslator;
import com.chaung.service.contract.service.dto.VaseCriteria;
import com.chaung.service.contract.service.VaseQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.chaung.service.contract.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VaseResource REST controller.
 *
 * @see VaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceContractApp.class)
public class VaseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private VaseRepository vaseRepository;


    @Autowired
    private VaseMapper vaseMapper;
    

    @Autowired
    private VaseService vaseService;

    @Autowired
    private VaseQueryService vaseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVaseMockMvc;

    private Vase vase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VaseResource vaseResource = new VaseResource(vaseService, vaseQueryService);
        this.restVaseMockMvc = MockMvcBuilders.standaloneSetup(vaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vase createEntity(EntityManager em) {
        Vase vase = new Vase()
            .name(DEFAULT_NAME);
        return vase;
    }

    @Before
    public void initTest() {
        vase = createEntity(em);
    }

    @Test
    @Transactional
    public void createVase() throws Exception {
        int databaseSizeBeforeCreate = vaseRepository.findAll().size();

        // Create the Vase
        VaseDTO vaseDTO = vaseMapper.toDto(vase);
        restVaseMockMvc.perform(post("/api/vases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Vase in the database
        List<Vase> vaseList = vaseRepository.findAll();
        assertThat(vaseList).hasSize(databaseSizeBeforeCreate + 1);
        Vase testVase = vaseList.get(vaseList.size() - 1);
        assertThat(testVase.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createVaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vaseRepository.findAll().size();

        // Create the Vase with an existing ID
        vase.setId(1L);
        VaseDTO vaseDTO = vaseMapper.toDto(vase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaseMockMvc.perform(post("/api/vases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vase in the database
        List<Vase> vaseList = vaseRepository.findAll();
        assertThat(vaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVases() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        // Get all the vaseList
        restVaseMockMvc.perform(get("/api/vases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getVase() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        // Get the vase
        restVaseMockMvc.perform(get("/api/vases/{id}", vase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vase.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllVasesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        // Get all the vaseList where name equals to DEFAULT_NAME
        defaultVaseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vaseList where name equals to UPDATED_NAME
        defaultVaseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVasesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        // Get all the vaseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVaseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vaseList where name equals to UPDATED_NAME
        defaultVaseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllVasesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        // Get all the vaseList where name is not null
        defaultVaseShouldBeFound("name.specified=true");

        // Get all the vaseList where name is null
        defaultVaseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllVasesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        vase.setProduct(product);
        vaseRepository.saveAndFlush(vase);
        Long productId = product.getId();

        // Get all the vaseList where product equals to productId
        defaultVaseShouldBeFound("productId.equals=" + productId);

        // Get all the vaseList where product equals to productId + 1
        defaultVaseShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVaseShouldBeFound(String filter) throws Exception {
        restVaseMockMvc.perform(get("/api/vases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vase.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVaseShouldNotBeFound(String filter) throws Exception {
        restVaseMockMvc.perform(get("/api/vases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingVase() throws Exception {
        // Get the vase
        restVaseMockMvc.perform(get("/api/vases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVase() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        int databaseSizeBeforeUpdate = vaseRepository.findAll().size();

        // Update the vase
        Vase updatedVase = vaseRepository.findById(vase.getId()).get();
        // Disconnect from session so that the updates on updatedVase are not directly saved in db
        em.detach(updatedVase);
        updatedVase
            .name(UPDATED_NAME);
        VaseDTO vaseDTO = vaseMapper.toDto(updatedVase);

        restVaseMockMvc.perform(put("/api/vases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vaseDTO)))
            .andExpect(status().isOk());

        // Validate the Vase in the database
        List<Vase> vaseList = vaseRepository.findAll();
        assertThat(vaseList).hasSize(databaseSizeBeforeUpdate);
        Vase testVase = vaseList.get(vaseList.size() - 1);
        assertThat(testVase.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingVase() throws Exception {
        int databaseSizeBeforeUpdate = vaseRepository.findAll().size();

        // Create the Vase
        VaseDTO vaseDTO = vaseMapper.toDto(vase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restVaseMockMvc.perform(put("/api/vases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vase in the database
        List<Vase> vaseList = vaseRepository.findAll();
        assertThat(vaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVase() throws Exception {
        // Initialize the database
        vaseRepository.saveAndFlush(vase);

        int databaseSizeBeforeDelete = vaseRepository.findAll().size();

        // Get the vase
        restVaseMockMvc.perform(delete("/api/vases/{id}", vase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vase> vaseList = vaseRepository.findAll();
        assertThat(vaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vase.class);
        Vase vase1 = new Vase();
        vase1.setId(1L);
        Vase vase2 = new Vase();
        vase2.setId(vase1.getId());
        assertThat(vase1).isEqualTo(vase2);
        vase2.setId(2L);
        assertThat(vase1).isNotEqualTo(vase2);
        vase1.setId(null);
        assertThat(vase1).isNotEqualTo(vase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaseDTO.class);
        VaseDTO vaseDTO1 = new VaseDTO();
        vaseDTO1.setId(1L);
        VaseDTO vaseDTO2 = new VaseDTO();
        assertThat(vaseDTO1).isNotEqualTo(vaseDTO2);
        vaseDTO2.setId(vaseDTO1.getId());
        assertThat(vaseDTO1).isEqualTo(vaseDTO2);
        vaseDTO2.setId(2L);
        assertThat(vaseDTO1).isNotEqualTo(vaseDTO2);
        vaseDTO1.setId(null);
        assertThat(vaseDTO1).isNotEqualTo(vaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vaseMapper.fromId(null)).isNull();
    }
}

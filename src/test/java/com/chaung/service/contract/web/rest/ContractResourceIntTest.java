package com.chaung.service.contract.web.rest;

import com.chaung.service.contract.ServiceContractApp;

import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.domain.Product;
import com.chaung.service.contract.repository.ContractRepository;
import com.chaung.service.contract.service.ContractService;
import com.chaung.service.contract.service.dto.ContractDTO;
import com.chaung.service.contract.service.mapper.ContractMapper;
import com.chaung.service.contract.web.rest.errors.ExceptionTranslator;
import com.chaung.service.contract.service.dto.ContractCriteria;
import com.chaung.service.contract.service.ContractQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static com.chaung.service.contract.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContractResource REST controller.
 *
 * @see ContractResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceContractApp.class)
public class ContractResourceIntTest {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CUSTOMER_AGE = 1;
    private static final Integer UPDATED_CUSTOMER_AGE = 2;

    private static final String DEFAULT_CUSTOMER_DOC_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_DOC_ID = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private ContractRepository contractRepository;


    @Autowired
    private ContractMapper contractMapper;
    

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractQueryService contractQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContractResource contractResource = new ContractResource(contractService, contractQueryService);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
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
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .customerAge(DEFAULT_CUSTOMER_AGE)
            .customerDocId(DEFAULT_CUSTOMER_DOC_ID)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return contract;
    }

    @Before
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testContract.getCustomerAge()).isEqualTo(DEFAULT_CUSTOMER_AGE);
        assertThat(testContract.getCustomerDocId()).isEqualTo(DEFAULT_CUSTOMER_DOC_ID);
        assertThat(testContract.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testContract.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createContractWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].customerAge").value(hasItem(DEFAULT_CUSTOMER_AGE)))
            .andExpect(jsonPath("$.[*].customerDocId").value(hasItem(DEFAULT_CUSTOMER_DOC_ID.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }
    

    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()))
            .andExpect(jsonPath("$.customerAge").value(DEFAULT_CUSTOMER_AGE))
            .andExpect(jsonPath("$.customerDocId").value(DEFAULT_CUSTOMER_DOC_ID.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultContractShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the contractList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultContractShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultContractShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the contractList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultContractShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerName is not null
        defaultContractShouldBeFound("customerName.specified=true");

        // Get all the contractList where customerName is null
        defaultContractShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerAge equals to DEFAULT_CUSTOMER_AGE
        defaultContractShouldBeFound("customerAge.equals=" + DEFAULT_CUSTOMER_AGE);

        // Get all the contractList where customerAge equals to UPDATED_CUSTOMER_AGE
        defaultContractShouldNotBeFound("customerAge.equals=" + UPDATED_CUSTOMER_AGE);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerAgeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerAge in DEFAULT_CUSTOMER_AGE or UPDATED_CUSTOMER_AGE
        defaultContractShouldBeFound("customerAge.in=" + DEFAULT_CUSTOMER_AGE + "," + UPDATED_CUSTOMER_AGE);

        // Get all the contractList where customerAge equals to UPDATED_CUSTOMER_AGE
        defaultContractShouldNotBeFound("customerAge.in=" + UPDATED_CUSTOMER_AGE);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerAge is not null
        defaultContractShouldBeFound("customerAge.specified=true");

        // Get all the contractList where customerAge is null
        defaultContractShouldNotBeFound("customerAge.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerAge greater than or equals to DEFAULT_CUSTOMER_AGE
        defaultContractShouldBeFound("customerAge.greaterOrEqualThan=" + DEFAULT_CUSTOMER_AGE);

        // Get all the contractList where customerAge greater than or equals to UPDATED_CUSTOMER_AGE
        defaultContractShouldNotBeFound("customerAge.greaterOrEqualThan=" + UPDATED_CUSTOMER_AGE);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerAge less than or equals to DEFAULT_CUSTOMER_AGE
        defaultContractShouldNotBeFound("customerAge.lessThan=" + DEFAULT_CUSTOMER_AGE);

        // Get all the contractList where customerAge less than or equals to UPDATED_CUSTOMER_AGE
        defaultContractShouldBeFound("customerAge.lessThan=" + UPDATED_CUSTOMER_AGE);
    }


    @Test
    @Transactional
    public void getAllContractsByCustomerDocIdIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerDocId equals to DEFAULT_CUSTOMER_DOC_ID
        defaultContractShouldBeFound("customerDocId.equals=" + DEFAULT_CUSTOMER_DOC_ID);

        // Get all the contractList where customerDocId equals to UPDATED_CUSTOMER_DOC_ID
        defaultContractShouldNotBeFound("customerDocId.equals=" + UPDATED_CUSTOMER_DOC_ID);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerDocIdIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerDocId in DEFAULT_CUSTOMER_DOC_ID or UPDATED_CUSTOMER_DOC_ID
        defaultContractShouldBeFound("customerDocId.in=" + DEFAULT_CUSTOMER_DOC_ID + "," + UPDATED_CUSTOMER_DOC_ID);

        // Get all the contractList where customerDocId equals to UPDATED_CUSTOMER_DOC_ID
        defaultContractShouldNotBeFound("customerDocId.in=" + UPDATED_CUSTOMER_DOC_ID);
    }

    @Test
    @Transactional
    public void getAllContractsByCustomerDocIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where customerDocId is not null
        defaultContractShouldBeFound("customerDocId.specified=true");

        // Get all the contractList where customerDocId is null
        defaultContractShouldNotBeFound("customerDocId.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        contract.addProduct(product);
        contractRepository.saveAndFlush(contract);
        Long productId = product.getId();

        // Get all the contractList where product equals to productId
        defaultContractShouldBeFound("productId.equals=" + productId);

        // Get all the contractList where product equals to productId + 1
        defaultContractShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].customerAge").value(hasItem(DEFAULT_CUSTOMER_AGE)))
            .andExpect(jsonPath("$.[*].customerDocId").value(hasItem(DEFAULT_CUSTOMER_DOC_ID.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .customerName(UPDATED_CUSTOMER_NAME)
            .customerAge(UPDATED_CUSTOMER_AGE)
            .customerDocId(UPDATED_CUSTOMER_DOC_ID)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testContract.getCustomerAge()).isEqualTo(UPDATED_CUSTOMER_AGE);
        assertThat(testContract.getCustomerDocId()).isEqualTo(UPDATED_CUSTOMER_DOC_ID);
        assertThat(testContract.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContract.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Get the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = new Contract();
        contract1.setId(1L);
        Contract contract2 = new Contract();
        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);
        contract2.setId(2L);
        assertThat(contract1).isNotEqualTo(contract2);
        contract1.setId(null);
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractDTO.class);
        ContractDTO contractDTO1 = new ContractDTO();
        contractDTO1.setId(1L);
        ContractDTO contractDTO2 = new ContractDTO();
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO2.setId(contractDTO1.getId());
        assertThat(contractDTO1).isEqualTo(contractDTO2);
        contractDTO2.setId(2L);
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO1.setId(null);
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contractMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contractMapper.fromId(null)).isNull();
    }
}

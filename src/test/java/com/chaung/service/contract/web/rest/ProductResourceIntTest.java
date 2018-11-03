package com.chaung.service.contract.web.rest;

import com.chaung.service.contract.ServiceContractApp;

import com.chaung.service.contract.domain.Product;
import com.chaung.service.contract.domain.Vase;
import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.repository.ProductRepository;
import com.chaung.service.contract.service.ProductService;
import com.chaung.service.contract.service.dto.ProductDTO;
import com.chaung.service.contract.service.mapper.ProductMapper;
import com.chaung.service.contract.web.rest.errors.ExceptionTranslator;
import com.chaung.service.contract.service.dto.ProductCriteria;
import com.chaung.service.contract.service.ProductQueryService;

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
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceContractApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMMITMENT = 1;
    private static final Integer UPDATED_COMMITMENT = 2;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductMapper productMapper;
    

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productType(DEFAULT_PRODUCT_TYPE)
            .commitment(DEFAULT_COMMITMENT)
            .price(DEFAULT_PRICE)
            .name(DEFAULT_NAME)
            .device(DEFAULT_DEVICE)
            .serviceId(DEFAULT_SERVICE_ID);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testProduct.getCommitment()).isEqualTo(DEFAULT_COMMITMENT);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testProduct.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].commitment").value(hasItem(DEFAULT_COMMITMENT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())));
    }
    

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE.toString()))
            .andExpect(jsonPath("$.commitment").value(DEFAULT_COMMITMENT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()));
    }

    @Test
    @Transactional
    public void getAllProductsByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productType equals to DEFAULT_PRODUCT_TYPE
        defaultProductShouldBeFound("productType.equals=" + DEFAULT_PRODUCT_TYPE);

        // Get all the productList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductShouldNotBeFound("productType.equals=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductTypeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productType in DEFAULT_PRODUCT_TYPE or UPDATED_PRODUCT_TYPE
        defaultProductShouldBeFound("productType.in=" + DEFAULT_PRODUCT_TYPE + "," + UPDATED_PRODUCT_TYPE);

        // Get all the productList where productType equals to UPDATED_PRODUCT_TYPE
        defaultProductShouldNotBeFound("productType.in=" + UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void getAllProductsByProductTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productType is not null
        defaultProductShouldBeFound("productType.specified=true");

        // Get all the productList where productType is null
        defaultProductShouldNotBeFound("productType.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByCommitmentIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where commitment equals to DEFAULT_COMMITMENT
        defaultProductShouldBeFound("commitment.equals=" + DEFAULT_COMMITMENT);

        // Get all the productList where commitment equals to UPDATED_COMMITMENT
        defaultProductShouldNotBeFound("commitment.equals=" + UPDATED_COMMITMENT);
    }

    @Test
    @Transactional
    public void getAllProductsByCommitmentIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where commitment in DEFAULT_COMMITMENT or UPDATED_COMMITMENT
        defaultProductShouldBeFound("commitment.in=" + DEFAULT_COMMITMENT + "," + UPDATED_COMMITMENT);

        // Get all the productList where commitment equals to UPDATED_COMMITMENT
        defaultProductShouldNotBeFound("commitment.in=" + UPDATED_COMMITMENT);
    }

    @Test
    @Transactional
    public void getAllProductsByCommitmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where commitment is not null
        defaultProductShouldBeFound("commitment.specified=true");

        // Get all the productList where commitment is null
        defaultProductShouldNotBeFound("commitment.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByCommitmentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where commitment greater than or equals to DEFAULT_COMMITMENT
        defaultProductShouldBeFound("commitment.greaterOrEqualThan=" + DEFAULT_COMMITMENT);

        // Get all the productList where commitment greater than or equals to UPDATED_COMMITMENT
        defaultProductShouldNotBeFound("commitment.greaterOrEqualThan=" + UPDATED_COMMITMENT);
    }

    @Test
    @Transactional
    public void getAllProductsByCommitmentIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where commitment less than or equals to DEFAULT_COMMITMENT
        defaultProductShouldNotBeFound("commitment.lessThan=" + DEFAULT_COMMITMENT);

        // Get all the productList where commitment less than or equals to UPDATED_COMMITMENT
        defaultProductShouldBeFound("commitment.lessThan=" + UPDATED_COMMITMENT);
    }


    @Test
    @Transactional
    public void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where device equals to DEFAULT_DEVICE
        defaultProductShouldBeFound("device.equals=" + DEFAULT_DEVICE);

        // Get all the productList where device equals to UPDATED_DEVICE
        defaultProductShouldNotBeFound("device.equals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProductsByDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where device in DEFAULT_DEVICE or UPDATED_DEVICE
        defaultProductShouldBeFound("device.in=" + DEFAULT_DEVICE + "," + UPDATED_DEVICE);

        // Get all the productList where device equals to UPDATED_DEVICE
        defaultProductShouldNotBeFound("device.in=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProductsByDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where device is not null
        defaultProductShouldBeFound("device.specified=true");

        // Get all the productList where device is null
        defaultProductShouldNotBeFound("device.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serviceId equals to DEFAULT_SERVICE_ID
        defaultProductShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the productList where serviceId equals to UPDATED_SERVICE_ID
        defaultProductShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllProductsByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultProductShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the productList where serviceId equals to UPDATED_SERVICE_ID
        defaultProductShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllProductsByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where serviceId is not null
        defaultProductShouldBeFound("serviceId.specified=true");

        // Get all the productList where serviceId is null
        defaultProductShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByVaseIsEqualToSomething() throws Exception {
        // Initialize the database
        Vase vase = VaseResourceIntTest.createEntity(em);
        em.persist(vase);
        em.flush();
        product.addVase(vase);
        productRepository.saveAndFlush(product);
        Long vaseId = vase.getId();

        // Get all the productList where vase equals to vaseId
        defaultProductShouldBeFound("vaseId.equals=" + vaseId);

        // Get all the productList where vase equals to vaseId + 1
        defaultProductShouldNotBeFound("vaseId.equals=" + (vaseId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByContractIsEqualToSomething() throws Exception {
        // Initialize the database
        Contract contract = ContractResourceIntTest.createEntity(em);
        em.persist(contract);
        em.flush();
        product.setContract(contract);
        productRepository.saveAndFlush(product);
        Long contractId = contract.getId();

        // Get all the productList where contract equals to contractId
        defaultProductShouldBeFound("contractId.equals=" + contractId);

        // Get all the productList where contract equals to contractId + 1
        defaultProductShouldNotBeFound("contractId.equals=" + (contractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].commitment").value(hasItem(DEFAULT_COMMITMENT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productType(UPDATED_PRODUCT_TYPE)
            .commitment(UPDATED_COMMITMENT)
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .device(UPDATED_DEVICE)
            .serviceId(UPDATED_SERVICE_ID);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testProduct.getCommitment()).isEqualTo(UPDATED_COMMITMENT);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testProduct.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}

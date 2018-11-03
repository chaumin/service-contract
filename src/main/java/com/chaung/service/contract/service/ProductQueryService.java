package com.chaung.service.contract.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.chaung.service.contract.domain.Product;
import com.chaung.service.contract.domain.*; // for static metamodels
import com.chaung.service.contract.repository.ProductRepository;
import com.chaung.service.contract.service.dto.ProductCriteria;

import com.chaung.service.contract.service.dto.ProductDTO;
import com.chaung.service.contract.service.mapper.ProductMapper;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page)
            .map(productMapper::toDto);
    }

    /**
     * Function to convert ProductCriteria to a {@link Specification}
     */
    private Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getProductType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductType(), Product_.productType));
            }
            if (criteria.getCommitment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommitment(), Product_.commitment));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getDevice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDevice(), Product_.device));
            }
            if (criteria.getServiceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceId(), Product_.serviceId));
            }
            if (criteria.getVaseId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVaseId(), Product_.vases, Vase_.id));
            }
            if (criteria.getContractId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContractId(), Product_.contract, Contract_.id));
            }
        }
        return specification;
    }

}

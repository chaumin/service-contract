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

import com.chaung.service.contract.domain.Vase;
import com.chaung.service.contract.domain.*; // for static metamodels
import com.chaung.service.contract.repository.VaseRepository;
import com.chaung.service.contract.service.dto.VaseCriteria;

import com.chaung.service.contract.service.dto.VaseDTO;
import com.chaung.service.contract.service.mapper.VaseMapper;

/**
 * Service for executing complex queries for Vase entities in the database.
 * The main input is a {@link VaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VaseDTO} or a {@link Page} of {@link VaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VaseQueryService extends QueryService<Vase> {

    private final Logger log = LoggerFactory.getLogger(VaseQueryService.class);

    private final VaseRepository vaseRepository;

    private final VaseMapper vaseMapper;

    public VaseQueryService(VaseRepository vaseRepository, VaseMapper vaseMapper) {
        this.vaseRepository = vaseRepository;
        this.vaseMapper = vaseMapper;
    }

    /**
     * Return a {@link List} of {@link VaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VaseDTO> findByCriteria(VaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vase> specification = createSpecification(criteria);
        return vaseMapper.toDto(vaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VaseDTO> findByCriteria(VaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vase> specification = createSpecification(criteria);
        return vaseRepository.findAll(specification, page)
            .map(vaseMapper::toDto);
    }

    /**
     * Function to convert VaseCriteria to a {@link Specification}
     */
    private Specification<Vase> createSpecification(VaseCriteria criteria) {
        Specification<Vase> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Vase_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Vase_.name));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), Vase_.product, Product_.id));
            }
        }
        return specification;
    }

}

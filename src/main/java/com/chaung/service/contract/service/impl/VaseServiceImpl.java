package com.chaung.service.contract.service.impl;

import com.chaung.service.contract.service.VaseService;
import com.chaung.service.contract.domain.Vase;
import com.chaung.service.contract.repository.VaseRepository;
import com.chaung.service.contract.service.dto.VaseDTO;
import com.chaung.service.contract.service.mapper.VaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Vase.
 */
@Service
@Transactional
public class VaseServiceImpl implements VaseService {

    private final Logger log = LoggerFactory.getLogger(VaseServiceImpl.class);

    private final VaseRepository vaseRepository;

    private final VaseMapper vaseMapper;

    public VaseServiceImpl(VaseRepository vaseRepository, VaseMapper vaseMapper) {
        this.vaseRepository = vaseRepository;
        this.vaseMapper = vaseMapper;
    }

    /**
     * Save a vase.
     *
     * @param vaseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VaseDTO save(VaseDTO vaseDTO) {
        log.debug("Request to save Vase : {}", vaseDTO);
        Vase vase = vaseMapper.toEntity(vaseDTO);
        vase = vaseRepository.save(vase);
        return vaseMapper.toDto(vase);
    }

    /**
     * Get all the vases.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<VaseDTO> findAll() {
        log.debug("Request to get all Vases");
        return vaseRepository.findAll().stream()
            .map(vaseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one vase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VaseDTO> findOne(Long id) {
        log.debug("Request to get Vase : {}", id);
        return vaseRepository.findById(id)
            .map(vaseMapper::toDto);
    }

    /**
     * Delete the vase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vase : {}", id);
        vaseRepository.deleteById(id);
    }
}

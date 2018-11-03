package com.chaung.service.contract.service;

import com.chaung.service.contract.service.dto.VaseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Vase.
 */
public interface VaseService {

    /**
     * Save a vase.
     *
     * @param vaseDTO the entity to save
     * @return the persisted entity
     */
    VaseDTO save(VaseDTO vaseDTO);

    /**
     * Get all the vases.
     *
     * @return the list of entities
     */
    List<VaseDTO> findAll();


    /**
     * Get the "id" vase.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<VaseDTO> findOne(Long id);

    /**
     * Delete the "id" vase.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

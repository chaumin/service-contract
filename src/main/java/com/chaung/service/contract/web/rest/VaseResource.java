package com.chaung.service.contract.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chaung.service.contract.service.VaseService;
import com.chaung.service.contract.web.rest.errors.BadRequestAlertException;
import com.chaung.service.contract.web.rest.util.HeaderUtil;
import com.chaung.service.contract.service.dto.VaseDTO;
import com.chaung.service.contract.service.dto.VaseCriteria;
import com.chaung.service.contract.service.VaseQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Vase.
 */
@RestController
@RequestMapping("/api")
public class VaseResource {

    private final Logger log = LoggerFactory.getLogger(VaseResource.class);

    private static final String ENTITY_NAME = "vase";

    private final VaseService vaseService;

    private final VaseQueryService vaseQueryService;

    public VaseResource(VaseService vaseService, VaseQueryService vaseQueryService) {
        this.vaseService = vaseService;
        this.vaseQueryService = vaseQueryService;
    }

    /**
     * POST  /vases : Create a new vase.
     *
     * @param vaseDTO the vaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vaseDTO, or with status 400 (Bad Request) if the vase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vases")
    @Timed
    public ResponseEntity<VaseDTO> createVase(@RequestBody VaseDTO vaseDTO) throws URISyntaxException {
        log.debug("REST request to save Vase : {}", vaseDTO);
        if (vaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new vase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaseDTO result = vaseService.save(vaseDTO);
        return ResponseEntity.created(new URI("/api/vases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vases : Updates an existing vase.
     *
     * @param vaseDTO the vaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vaseDTO,
     * or with status 400 (Bad Request) if the vaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the vaseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vases")
    @Timed
    public ResponseEntity<VaseDTO> updateVase(@RequestBody VaseDTO vaseDTO) throws URISyntaxException {
        log.debug("REST request to update Vase : {}", vaseDTO);
        if (vaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VaseDTO result = vaseService.save(vaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vases : get all the vases.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of vases in body
     */
    @GetMapping("/vases")
    @Timed
    public ResponseEntity<List<VaseDTO>> getAllVases(VaseCriteria criteria) {
        log.debug("REST request to get Vases by criteria: {}", criteria);
        List<VaseDTO> entityList = vaseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /vases/:id : get the "id" vase.
     *
     * @param id the id of the vaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/vases/{id}")
    @Timed
    public ResponseEntity<VaseDTO> getVase(@PathVariable Long id) {
        log.debug("REST request to get Vase : {}", id);
        Optional<VaseDTO> vaseDTO = vaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaseDTO);
    }

    /**
     * DELETE  /vases/:id : delete the "id" vase.
     *
     * @param id the id of the vaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vases/{id}")
    @Timed
    public ResponseEntity<Void> deleteVase(@PathVariable Long id) {
        log.debug("REST request to delete Vase : {}", id);
        vaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

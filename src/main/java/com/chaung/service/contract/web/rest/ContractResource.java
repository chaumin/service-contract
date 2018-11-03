package com.chaung.service.contract.web.rest;

import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.service.dto.BasketDTO;
import com.codahale.metrics.annotation.Timed;
import com.chaung.service.contract.service.ContractService;
import com.chaung.service.contract.web.rest.errors.BadRequestAlertException;
import com.chaung.service.contract.web.rest.util.HeaderUtil;
import com.chaung.service.contract.web.rest.util.PaginationUtil;
import com.chaung.service.contract.service.dto.ContractDTO;
import com.chaung.service.contract.service.dto.ContractCriteria;
import com.chaung.service.contract.service.ContractQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contract.
 */
@RestController
@RequestMapping("/api")
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    private final ContractService contractService;

    private final ContractQueryService contractQueryService;

    public ContractResource(ContractService contractService, ContractQueryService contractQueryService) {
        this.contractService = contractService;
        this.contractQueryService = contractQueryService;
    }

    @PostMapping("/service-contract")
    @Timed
    public ResponseEntity<Contract> createServiceContract(@RequestBody BasketDTO basketDTO) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", basketDTO);
        Contract contract = contractService.saveContract(basketDTO);
        return ResponseEntity.created(new URI("/api/contracts/" + contract.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, contract.getId().toString()))
            .body(contract);
    }

    @GetMapping("/service-contract")
    @Timed
    @ApiImplicitParams({
        @ApiImplicitParam(name = "customerDocId.equals", value = "Customer Id", dataType = "String", paramType = "query")
    })
    public ResponseEntity<List<ContractDTO>> getContractsByCustomerDocId(@ApiIgnore ContractCriteria criteria, @ApiIgnore Pageable pageable) {
        log.debug("REST request to get Contracts by criteria: {}", criteria);
        Page<ContractDTO> page = contractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-contract");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST  /contracts : Create a new contract.
     *
     * @param contractDTO the contractDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractDTO, or with status 400 (Bad Request) if the contract has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contracts")
    @Timed
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contractDTO);
        if (contractDTO.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractDTO result = contractService.save(contractDTO);
        return ResponseEntity.created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contracts : Updates an existing contract.
     *
     * @param contractDTO the contractDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractDTO,
     * or with status 400 (Bad Request) if the contractDTO is not valid,
     * or with status 500 (Internal Server Error) if the contractDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contracts")
    @Timed
    public ResponseEntity<ContractDTO> updateContract(@RequestBody ContractDTO contractDTO) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contractDTO);
        if (contractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContractDTO result = contractService.save(contractDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contractDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contracts : get all the contracts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contracts in body
     */
    @GetMapping("/contracts")
    @Timed
    public ResponseEntity<List<ContractDTO>> getAllContracts(ContractCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contracts by criteria: {}", criteria);
        Page<ContractDTO> page = contractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contracts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contracts/:id : get the "id" contract.
     *
     * @param id the id of the contractDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contracts/{id}")
    @Timed
    public ResponseEntity<ContractDTO> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        Optional<ContractDTO> contractDTO = contractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractDTO);
    }

    /**
     * DELETE  /contracts/:id : delete the "id" contract.
     *
     * @param id the id of the contractDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contracts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

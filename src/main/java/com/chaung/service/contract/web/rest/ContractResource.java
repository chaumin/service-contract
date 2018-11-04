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
import io.swagger.annotations.Api;
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

}

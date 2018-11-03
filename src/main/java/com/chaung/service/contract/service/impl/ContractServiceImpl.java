package com.chaung.service.contract.service.impl;

import com.chaung.service.contract.assembler.ServiceContractAssembler;
import com.chaung.service.contract.service.ContractService;
import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.repository.ContractRepository;
import com.chaung.service.contract.service.dto.BasketDTO;
import com.chaung.service.contract.service.dto.ContractDTO;
import com.chaung.service.contract.service.file.ServiceContractGenerator;
import com.chaung.service.contract.service.mapper.ContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public Contract saveContract(BasketDTO basketDTO) {
        log.debug("Request to save Service Contract : {}", basketDTO);
        Contract contract = ServiceContractAssembler.assembleContract(basketDTO);
        contract.setContent(ServiceContractGenerator.generate(basketDTO, ServiceContractGenerator.PDF));
        contract.setContentContentType(ServiceContractGenerator.PDF);
        log.debug("Request to save Contract : {}", contract);
        contract = contractRepository.save(contract);
        return contract;
    }

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable)
            .map(contractMapper::toDto);
    }


    /**
     * Get one contract by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findById(id)
            .map(contractMapper::toDto);
    }

    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }
}

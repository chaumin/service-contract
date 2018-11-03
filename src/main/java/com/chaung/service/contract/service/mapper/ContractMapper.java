package com.chaung.service.contract.service.mapper;

import com.chaung.service.contract.domain.*;
import com.chaung.service.contract.service.dto.ContractDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {


    @Mapping(target = "products", ignore = true)
    Contract toEntity(ContractDTO contractDTO);

    default Contract fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}

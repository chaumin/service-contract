package com.chaung.service.contract.service.mapper;

import com.chaung.service.contract.domain.*;
import com.chaung.service.contract.service.dto.VaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vase and its DTO VaseDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface VaseMapper extends EntityMapper<VaseDTO, Vase> {

    @Mapping(source = "product.id", target = "productId")
    VaseDTO toDto(Vase vase);

    @Mapping(source = "productId", target = "product")
    Vase toEntity(VaseDTO vaseDTO);

    default Vase fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vase vase = new Vase();
        vase.setId(id);
        return vase;
    }
}

package com.chaung.service.contract.service.mapper;

import com.chaung.service.contract.domain.*;
import com.chaung.service.contract.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {ContractMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "contract.id", target = "contractId")
    ProductDTO toDto(Product product);

    @Mapping(target = "vases", ignore = true)
    @Mapping(source = "contractId", target = "contract")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}

package com.chaung.service.contract.assembler;

import com.chaung.service.contract.domain.Contract;
import com.chaung.service.contract.domain.Product;
import com.chaung.service.contract.domain.Vase;
import com.chaung.service.contract.service.dto.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class ServiceContractAssembler {

    public static Contract assembleContract(BasketDTO basketDTO) {
        Contract contract = new Contract();

        if (Optional.of(basketDTO).isPresent()) {
            ContractCustomerDTO customer = basketDTO.getCustomerInfo();
            if (Optional.of(customer).isPresent()) {
                contract
                    .customerName(customer.getName())
                    .customerAge(customer.getAge())
                    .customerDocId(customer.getDocId());
            }

            Set<ContractProductDTO> productDTOSet = basketDTO.getProducts();
            if (productDTOSet.size() > 0) {
                Set<Product> products = new HashSet<>();
                productDTOSet.stream().forEach(contractProductDTO -> {
                    Product product = new Product();
                    product
                        .contract(contract)
                        .productType(contractProductDTO.getProductType())
                        .commitment(contractProductDTO.getCommitment())
                        .price(contractProductDTO.getPrice())
                        .name(contractProductDTO.getName())
                        .device(contractProductDTO.getDevice())
                        .serviceId(contractProductDTO.getServiceId());
                    Set<ContractVaseDTO> vaseDTOSet = contractProductDTO.getVases();
                    if(vaseDTOSet.size() > 0) {
                        Set<Vase> vases = new HashSet<>();
                        vaseDTOSet.stream().forEach(vaseDTO -> {
                            Vase vase = new Vase();
                            vase.setProduct(product);
                            vase.setName(vaseDTO.getVas());
                            vases.add(vase);
                        });
                        product.setVases(vases);
                    }
                    products.add(product);
                });
                contract.setProducts(products);
            }
        }

        return contract;
    }
}

package com.chaung.service.contract.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class BasketDTO implements Serializable {


    private ContractCustomerDTO customerInfo;

    private Set<ContractProductDTO> products = new HashSet<>();

    public ContractCustomerDTO getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(ContractCustomerDTO customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Set<ContractProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ContractProductDTO> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "BasketDTO{" +
            "customerInfo=" + customerInfo +
            ", products=" + products +
            '}';
    }
}

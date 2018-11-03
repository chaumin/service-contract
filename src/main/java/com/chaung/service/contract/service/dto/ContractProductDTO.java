package com.chaung.service.contract.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ContractProductDTO implements Serializable {

    @JsonProperty("type")
    private String productType;

    private Integer commitment;

    private Float price;

    private String name;

    private String device;

    private String serviceId;

    private Set<ContractVaseDTO> vases = new HashSet<>();

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getCommitment() {
        return commitment;
    }

    public void setCommitment(Integer commitment) {
        this.commitment = commitment;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Set<ContractVaseDTO> getVases() {
        return vases;
    }

    public void setVases(Set<ContractVaseDTO> vases) {
        this.vases = vases;
    }

    @Override
    public String toString() {
        return "ContractProductDTO{" +
            "productType='" + productType + '\'' +
            ", commitment=" + commitment +
            ", price=" + price +
            ", name='" + name + '\'' +
            ", device='" + device + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", vases=" + vases +
            '}';
    }
}

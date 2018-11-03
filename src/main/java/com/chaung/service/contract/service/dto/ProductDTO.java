package com.chaung.service.contract.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String productType;

    private Integer commitment;

    private Float price;

    private String name;

    private String device;

    private String serviceId;

    private Long contractId;

    private Set<VaseDTO> vases = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Set<VaseDTO> getVases() {
        return vases;
    }

    public void setVases(Set<VaseDTO> vases) {
        this.vases = vases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + id +
            ", productType='" + productType + '\'' +
            ", commitment=" + commitment +
            ", price=" + price +
            ", name='" + name + '\'' +
            ", device='" + device + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", contractId=" + contractId +
            ", vases=" + vases +
            '}';
    }
}

package com.chaung.service.contract.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Product entity. This class is used in ProductResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /products?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter productType;

    private IntegerFilter commitment;

    private FloatFilter price;

    private StringFilter name;

    private StringFilter device;

    private StringFilter serviceId;

    private LongFilter vaseId;

    private LongFilter contractId;

    public ProductCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductType() {
        return productType;
    }

    public void setProductType(StringFilter productType) {
        this.productType = productType;
    }

    public IntegerFilter getCommitment() {
        return commitment;
    }

    public void setCommitment(IntegerFilter commitment) {
        this.commitment = commitment;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDevice() {
        return device;
    }

    public void setDevice(StringFilter device) {
        this.device = device;
    }

    public StringFilter getServiceId() {
        return serviceId;
    }

    public void setServiceId(StringFilter serviceId) {
        this.serviceId = serviceId;
    }

    public LongFilter getVaseId() {
        return vaseId;
    }

    public void setVaseId(LongFilter vaseId) {
        this.vaseId = vaseId;
    }

    public LongFilter getContractId() {
        return contractId;
    }

    public void setContractId(LongFilter contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (productType != null ? "productType=" + productType + ", " : "") +
                (commitment != null ? "commitment=" + commitment + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (device != null ? "device=" + device + ", " : "") +
                (serviceId != null ? "serviceId=" + serviceId + ", " : "") +
                (vaseId != null ? "vaseId=" + vaseId + ", " : "") +
                (contractId != null ? "contractId=" + contractId + ", " : "") +
            "}";
    }

}

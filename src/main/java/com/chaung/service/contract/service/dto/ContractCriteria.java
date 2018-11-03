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
 * Criteria class for the Contract entity. This class is used in ContractResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contracts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter customerName;

    private IntegerFilter customerAge;

    private StringFilter customerDocId;

    private LongFilter productId;

    public ContractCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public IntegerFilter getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(IntegerFilter customerAge) {
        this.customerAge = customerAge;
    }

    public StringFilter getCustomerDocId() {
        return customerDocId;
    }

    public void setCustomerDocId(StringFilter customerDocId) {
        this.customerDocId = customerDocId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ContractCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (customerName != null ? "customerName=" + customerName + ", " : "") +
                (customerAge != null ? "customerAge=" + customerAge + ", " : "") +
                (customerDocId != null ? "customerDocId=" + customerDocId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}

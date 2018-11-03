package com.chaung.service.contract.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Contract entity.
 */
public class ContractDTO implements Serializable {

    private Long id;

    private String customerName;

    private Integer customerAge;

    private String customerDocId;

    @Lob
    private byte[] content;
    private String contentContentType;

    private ZonedDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    public String getCustomerDocId() {
        return customerDocId;
    }

    public void setCustomerDocId(String customerDocId) {
        this.customerDocId = customerDocId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (contractDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + id +
            ", customerName='" + customerName + '\'' +
            ", customerAge=" + customerAge +
            ", customerDocId='" + customerDocId + '\'' +
           // ", content=" + Arrays.toString(content) +
            ", contentContentType='" + contentContentType + '\'' +
            ", createdDate=" + createdDate +
            '}';
    }
}

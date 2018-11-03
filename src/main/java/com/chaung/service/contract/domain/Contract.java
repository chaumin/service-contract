package com.chaung.service.contract.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_age")
    private Integer customerAge;

    @Column(name = "customer_doc_id")
    private String customerDocId;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Contract customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public Contract customerAge(Integer customerAge) {
        this.customerAge = customerAge;
        return this;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    public String getCustomerDocId() {
        return customerDocId;
    }

    public Contract customerDocId(String customerDocId) {
        this.customerDocId = customerDocId;
        return this;
    }

    public void setCustomerDocId(String customerDocId) {
        this.customerDocId = customerDocId;
    }

    public byte[] getContent() {
        return content;
    }

    public Contract content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public Contract contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Contract products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Contract addProduct(Product product) {
        this.products.add(product);
        product.setContract(this);
        return this;
    }

    public Contract removeProduct(Product product) {
        this.products.remove(product);
        product.setContract(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if (contract.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contract.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", customerAge=" + getCustomerAge() +
            ", customerDocId='" + getCustomerDocId() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            "}";
    }
}

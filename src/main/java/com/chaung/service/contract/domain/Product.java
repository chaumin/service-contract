package com.chaung.service.contract.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "commitment")
    private Integer commitment;

    @Column(name = "price")
    private Float price;

    @Column(name = "name")
    private String name;

    @Column(name = "device")
    private String device;

    @Column(name = "service_id")
    private String serviceId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vase> vases = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("products")
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public Product productType(String productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getCommitment() {
        return commitment;
    }

    public Product commitment(Integer commitment) {
        this.commitment = commitment;
        return this;
    }

    public void setCommitment(Integer commitment) {
        this.commitment = commitment;
    }

    public Float getPrice() {
        return price;
    }

    public Product price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice() {
        return device;
    }

    public Product device(String device) {
        this.device = device;
        return this;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Product serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Set<Vase> getVases() {
        return vases;
    }

    public Product vases(Set<Vase> vases) {
        this.vases = vases;
        return this;
    }

    public Product addVase(Vase vase) {
        this.vases.add(vase);
        vase.setProduct(this);
        return this;
    }

    public Product removeVase(Vase vase) {
        this.vases.remove(vase);
        vase.setProduct(null);
        return this;
    }

    public void setVases(Set<Vase> vases) {
        this.vases = vases;
    }

    public Contract getContract() {
        return contract;
    }

    public Product contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productType='" + getProductType() + "'" +
            ", commitment=" + getCommitment() +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", device='" + getDevice() + "'" +
            ", serviceId='" + getServiceId() + "'" +
            "}";
    }
}

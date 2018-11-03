package com.chaung.service.contract.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.objects.annotations.Property;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Vase entity.
 */
public class VaseDTO implements Serializable {

    private Long id;

    private String name;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VaseDTO vaseDTO = (VaseDTO) o;
        if (vaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VaseDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", productId=" + productId +
            '}';
    }
}

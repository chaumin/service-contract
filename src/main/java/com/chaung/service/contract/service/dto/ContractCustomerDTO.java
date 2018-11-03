package com.chaung.service.contract.service.dto;

import java.io.Serializable;

public class ContractCustomerDTO implements Serializable {

    private String name;
    private Integer age;
    private String docId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public String toString() {
        return "ContractCustomerDTO{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", docId='" + docId + '\'' +
            '}';
    }
}

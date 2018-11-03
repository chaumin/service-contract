package com.chaung.service.contract.service.dto;

import java.io.Serializable;

public class ContractVaseDTO implements Serializable {

    private String vas;

    public String getVas() {
        return vas;
    }

    public void setVas(String vas) {
        this.vas = vas;
    }

    @Override
    public String toString() {
        return "ContractVaseDTO{" +
            "vas='" + vas + '\'' +
            '}';
    }
}

package com.chaung.service.contract.repository;

import com.chaung.service.contract.domain.Contract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

    List<Contract> findByCustomerDocId(String customerDocId);
}

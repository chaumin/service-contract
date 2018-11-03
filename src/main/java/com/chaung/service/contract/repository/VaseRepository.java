package com.chaung.service.contract.repository;

import com.chaung.service.contract.domain.Vase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Vase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaseRepository extends JpaRepository<Vase, Long>, JpaSpecificationExecutor<Vase> {

}

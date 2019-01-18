package com.mf.repository;

import com.mf.entity.SalePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalePersonRepository extends JpaRepository<SalePerson, Integer>, JpaSpecificationExecutor<SalePerson> {

}

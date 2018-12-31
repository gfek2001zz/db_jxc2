package com.mf.repository;

import com.mf.entity.DictClassify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DictClassifyRepository extends JpaRepository<DictClassify, Integer>, JpaSpecificationExecutor<DictClassify> {
}

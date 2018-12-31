package com.mf.repository;


import com.mf.entity.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DictItemRepository extends JpaRepository<DictItem, Integer>, JpaSpecificationExecutor<DictItem> {

    List<DictItem> findItemByClassifyCode(String classifyCode);
}

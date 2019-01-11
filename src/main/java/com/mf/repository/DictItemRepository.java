package com.mf.repository;


import com.mf.entity.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DictItemRepository extends JpaRepository<DictItem, Integer>, JpaSpecificationExecutor<DictItem> {

    List<DictItem> findItemByClassifyCode(String classifyCode);

    @Query(value = "SELECT * FROM t_dict_item_list WHERE classify_code=?1 AND item_code=?2",nativeQuery=true)
    DictItem findItemByItemCode(String classifyCode, String itemCode);
}

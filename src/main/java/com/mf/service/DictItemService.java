package com.mf.service;

import com.mf.entity.DictItem;

import java.util.List;

public interface DictItemService {

    List<DictItem> list(DictItem dictItem);

    Long getCount(DictItem dictItem);

    void save(DictItem dictItem);

    void delete(Integer id);

    DictItem findById(Integer id);

    DictItem findItemByItemCode(String classifyCode, String itemCode);
}

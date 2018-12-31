package com.mf.service;

import com.mf.entity.DictClassify;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DictClassifyService {

    List<DictClassify> list(DictClassify searchVO, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    Long getCount(DictClassify dictClassify);

    void save(DictClassify dictClassify);

    void delete(Integer id);

    DictClassify findById(Integer id);
}

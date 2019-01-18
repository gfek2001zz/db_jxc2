package com.mf.service;

import com.mf.entity.SalePerson;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SalePersonSerivce {

    List<SalePerson> list(SalePerson salePerson, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    Long getCount(SalePerson salePerson);
}

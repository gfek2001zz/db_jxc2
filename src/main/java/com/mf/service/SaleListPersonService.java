package com.mf.service;

import com.mf.entity.SaleList;
import com.mf.entity.SaleListPerson;

import java.util.List;

public interface SaleListPersonService {

    List<SaleListPerson> findListBySaleListId(SaleList saleList);
}

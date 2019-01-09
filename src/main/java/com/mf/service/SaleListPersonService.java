package com.mf.service;

import com.mf.entity.SaleList;
import com.mf.entity.SaleListPerson;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleListPersonService {

    List<SaleListPerson> findListBySaleListId(SaleList saleList);

    void delete(Integer saleId);
}

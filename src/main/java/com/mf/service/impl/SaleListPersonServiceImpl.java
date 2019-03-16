package com.mf.service.impl;

import com.mf.entity.SaleList;
import com.mf.entity.SaleListPerson;
import com.mf.repository.SaleListPersonRepository;
import com.mf.service.SaleListPersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SaleListPersonServiceImpl implements SaleListPersonService {

    @Resource
    private SaleListPersonRepository saleListPersonRepository;


    @Override
    public List<SaleListPerson> findListBySaleListId(SaleList saleList) {
        return saleListPersonRepository.findListBySaleListId(saleList.getId());
    }

    @Override
    public Float findSaleAmountBySalePerson(Integer salePersonId) {
        return saleListPersonRepository.findSaleAmountBySalePerson(salePersonId);
    }

    @Override
    public void delete(Integer saleId) {
        saleListPersonRepository.deleteBySaleListId(saleId);
    }

    @Override
    public long countSalePersonBind(Integer saleId) {
        return saleListPersonRepository.countSalePersonBind(saleId);
    }
}

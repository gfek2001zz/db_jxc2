package com.mf.service.impl;

import com.mf.entity.SaleList;
import com.mf.entity.SaleListPayment;
import com.mf.repository.SaleListPaymentRepository;
import com.mf.service.SaleListPaymentService;
import com.mf.service.SaleListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("saleListPaymentServiceImpl")
public class SaleListPaymentServiceImpl implements SaleListPaymentService {

    @Resource
    private SaleListPaymentRepository saleListPaymentRepository;

    @Resource
    private SaleListService saleListService;

    @Override
    public boolean add(SaleListPayment saleListPayment) {
        boolean result = true;

        Float amount = saleListPaymentRepository.findPaymentAmountBySaleListId(saleListPayment.getSaleId());
        if (null == amount) {
            amount = saleListPayment.getAmount();
        } else {
            amount = amount + saleListPayment.getAmount();
        }

        SaleList saleList = saleListService.findById(saleListPayment.getSaleId());
        if (saleList.getAmountPaid() >= (amount + saleList.getAmountEarnest())) {
            saleListPaymentRepository.save(saleListPayment);
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public void delete(Integer saleId) {
        saleListPaymentRepository.deleteBySaleListId(saleId);
    }
}

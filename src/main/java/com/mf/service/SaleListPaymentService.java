package com.mf.service;

import com.mf.entity.SaleListPayment;

public interface SaleListPaymentService {

    boolean add(SaleListPayment saleListPayment);

    void delete(Integer saleId);
}

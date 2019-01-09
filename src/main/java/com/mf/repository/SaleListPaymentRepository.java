package com.mf.repository;

import com.mf.entity.SaleListPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SaleListPaymentRepository extends JpaRepository<SaleListPayment, Integer>, JpaSpecificationExecutor<SaleListPayment> {

    @Query(value="SELECT ifnull(sum(amount), 0) FROM t_sale_list_payment WHERE sale_id=?1 group by sale_id",nativeQuery=true)
    public Float findPaymentAmountBySaleListId(Integer saleId);




    /**
     * 根据销售单id删除所有销售单商品
     * @param saleListId
     * @return
     */
    @Transactional
    @Query(value="delete FROM t_sale_list_payment WHERE sale_id=?1",nativeQuery=true)
    @Modifying
    public void deleteBySaleListId(Integer saleListId);
}

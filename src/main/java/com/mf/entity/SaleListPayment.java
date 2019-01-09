package com.mf.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_sale_list_payment")
public class SaleListPayment {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer saleId;

    @Column
    private Float amount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}

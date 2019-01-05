package com.mf.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_sale_list_person")
public class SaleListPerson {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "saleId")
    private SaleList saleList;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @Column
    private Float amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SaleList getSaleList() {
        return saleList;
    }

    public void setSaleList(SaleList saleList) {
        this.saleList = saleList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}

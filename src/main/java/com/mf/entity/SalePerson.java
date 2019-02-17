package com.mf.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_sale_person")
public class SalePerson {
    @Id
    @GeneratedValue
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "shopId")
    private Shop shop;


    @Column
    private String name;


    @Transient
    private Float amount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}

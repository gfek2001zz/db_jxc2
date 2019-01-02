package com.mf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 银行卡实体
 * @author cj
 *
 */
@Entity
@Table(name="t_card")
public class Card {
	
	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@Column(length=200)
	private String name; //卡名称
	
	@Column(length=50)
	private Float money; // 金额
	
	@Column(length=50)
	private String number; // 卡号
	
	@Column(length=300)
	private String address; // 联系地址
	
	@Column(length=1000)
	private String remarks; // 备注 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", name=" + name + ", contact=" + money + ", number=" + number + ", address="
				+ address + ", remarks=" + remarks + "]";
	}
	
	
	
	
}

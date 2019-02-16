package com.mf.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mf.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 销售单实体
 * @author Administrator
 *
 */
@Entity
@Table(name="t_saleList")
public class SaleList {
	
	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	@Column(length=100)
	private String saleNumber; // 销售单号
	
	@ManyToOne
	@JoinColumn(name="customerId")
	private Customer customer; // 客户

	@ManyToOne
	@JoinColumn(name="shopId")
	private Shop shop; //门店

	@ManyToOne
	@JoinColumn(name="cardId")
	private Card card; //卡号
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date saleDate; // 销售日期
	
	@Transient
	private Date bSaleDate; // 起始日期 搜索用到
	
	@Transient
	private Date eSaleDate; // 结束日期 搜索用到
	
	private float amountPayable; // 应付金额
	
	private float amountPaid; // 实付金额

	@Transient
	private float amountDiscount; //优惠金额

	@Transient
	private String amountDiscountRate; //折扣

	private float amountEarnest; //实收定金

	private float amountFinalPayment; //待收余额

	private float amountCostPrice; //成本价

	@Transient
	private float amountBalance;

	private String contractNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date factoryDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deliveryDate;

	private String matchmaker;

	
	private Integer state; // 交易状态 1 已付 2 未付
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user; // 操作员
	
	@Column(length=1000)
	private String remarks; // 备注
	
	@Transient
	private List<SaleListGoods> saleListGoodsList=null; // 销售单商品集合

    @Transient
    private String saleListPerson;


    @Transient
    private Float grossProfit;

    @Transient
    private Float grossProfitRate;

    @Transient
	private String saleMonth;


    @Transient
	private String saleDay;

    private int receiverGoods;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonSerialize(using=CustomDateSerializer.class)
	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public float getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(float amountPayable) {
		this.amountPayable = amountPayable;
	}

	public float getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(float amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

	public Date getbSaleDate() {
		return bSaleDate;
	}

	public void setbSaleDate(Date bSaleDate) {
		this.bSaleDate = bSaleDate;
	}

	public Date geteSaleDate() {
		return eSaleDate;
	}

	public void seteSaleDate(Date eSaleDate) {
		this.eSaleDate = eSaleDate;
	}

	public String getAmountDiscountRate() {
		return amountDiscountRate;
	}

	public void setAmountDiscountRate(String amountDiscountRate) {
		this.amountDiscountRate = amountDiscountRate;
	}

	public List<SaleListGoods> getSaleListGoodsList() {
		return saleListGoodsList;
	}

	public void setSaleListGoodsList(List<SaleListGoods> saleListGoodsList) {
		this.saleListGoodsList = saleListGoodsList;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

    public String getSaleListPerson() {
        return saleListPerson;
    }

    public void setSaleListPerson(String saleListPerson) {
        this.saleListPerson = saleListPerson;
    }

    public float getAmountEarnest() {
		return amountEarnest;
	}

	public void setAmountEarnest(float amountEarnest) {
		this.amountEarnest = amountEarnest;
	}

	public float getAmountFinalPayment() {
		return amountFinalPayment;
	}

	public void setAmountFinalPayment(float amountFinalPayment) {
		this.amountFinalPayment = amountFinalPayment;
	}

	public float getAmountBalance() {
		return amountBalance;
	}

	public void setAmountBalance(float amountBalance) {
		this.amountBalance = amountBalance;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	@JsonSerialize(using=CustomDateSerializer.class)
	public Date getFactoryDate() {
		return factoryDate;
	}

	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}

	@JsonSerialize(using=CustomDateSerializer.class)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getMatchmaker() {
		return matchmaker;
	}

	public void setMatchmaker(String matchmaker) {
		this.matchmaker = matchmaker;
	}

    public float getAmountCostPrice() {
        return amountCostPrice;
    }

    public void setAmountCostPrice(float amountCostPrice) {
        this.amountCostPrice = amountCostPrice;
    }

    public Float getGrossProfit() {
	    if (grossProfit == null) {
	        grossProfit = amountPayable - amountCostPrice;
        }

        return grossProfit;
    }


    public void setGrossProfit(Float grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Float getGrossProfitRate() {
	    if (grossProfitRate == null) {
	        grossProfitRate = (getGrossProfit() / amountPayable) * 100.0F;
        }


        return grossProfitRate;
    }

    public void setGrossProfitRate(Float grossProfitRate) {
        this.grossProfitRate = grossProfitRate;
    }

	public String getSaleMonth() {

		if (saleMonth == null) {
			Date date = getSaleDate();
			SimpleDateFormat format = new SimpleDateFormat("MM");
			saleMonth = format.format(date);
		}

		return saleMonth;
	}

	public void setSaleMonth(String saleMonth) {
		this.saleMonth = saleMonth;
	}

	public String getSaleDay() {
		if (saleDay == null) {
			Date date = getSaleDate();
			SimpleDateFormat format = new SimpleDateFormat("dd");
			saleDay = format.format(date);
		}

		return saleDay;
	}

	public void setSaleDay(String saleDay) {
		this.saleDay = saleDay;
	}

	public float getAmountDiscount() {
		return amountDiscount;
	}

	public void setAmountDiscount(float amountDiscount) {
		this.amountDiscount = amountDiscount;
	}

	public int getReceiverGoods() {
		return receiverGoods;
	}

	public void setReceiverGoods(int receiverGoods) {
		this.receiverGoods = receiverGoods;
	}

	@Override
	public String toString() {
		return "SaleList [id=" + id + ", saleNumber=" + saleNumber + ", customer=" + customer
				+ ", saleDate=" + saleDate + ", amountPayable=" + amountPayable + ", amountPaid=" + amountPaid
				+ ", state=" + state + ", user=" + user + ", remarks=" + remarks + "]";
	}
	
	
}

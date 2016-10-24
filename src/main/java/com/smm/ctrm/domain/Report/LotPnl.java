package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class LotPnl extends HibernateEntity {
	private static final long serialVersionUID = 1461719402483L;
	/**
	 *
	 */
	@JsonProperty(value = "FullNo4S")
	private String FullNo4S;
	/**
	 *
	 */
	@JsonProperty(value = "BrandNames4S")
	private String BrandNames4S;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName4S")
	private String CustomerName4S;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceNo4S")
	private String InvoiceNo4S;
	
	/**
	 *
	 */
	@JsonProperty(value = "Quantity4S")
	private BigDecimal Quantity4S;
	/**
	 *
	 */
	@JsonProperty(value = "Price4S")
	private BigDecimal Price4S;
	/**
	 *
	 */
	@JsonProperty(value = "RealFee4S")
	private BigDecimal RealFee4S;
	/**
	 *
	 */
	@JsonProperty(value = "QuantityHedged4S")
	private BigDecimal QuantityHedged4S;
	/**
	 *
	 */
	@JsonProperty(value = "PriceHedged4S")
	private BigDecimal PriceHedged4S;
	/**
	 *
	 */
	@JsonProperty(value = "Profit4S")
	private BigDecimal Profit4S;
	/**
	 *
	 */
	@JsonProperty(value = "Price4B")
	private BigDecimal Price4B;
	/**
	 *
	 */
	@JsonProperty(value = "RealFee4B")
	private BigDecimal RealFee4B;
	/**
	 *
	 */
	@JsonProperty(value = "QuantityHedged4B")
	private BigDecimal QuantityHedged4B;
	/**
	 *
	 */
	@JsonProperty(value = "PriceHedged4B")
	private BigDecimal PriceHedged4B;
	/**
	 *
	 */
	@JsonProperty(value = "Cost4B")
	private BigDecimal Cost4B;
	/**
	 *
	 */
	@JsonProperty(value = "SumProfit4Goods")
	private BigDecimal SumProfit4Goods;	
	/**
	 *
	 */
	@JsonProperty(value = "SumProfit4Hedged")
	private BigDecimal SumProfit4Hedged;
	/**
	 *
	 */
	@JsonProperty(value = "SumProfit4All")
	private BigDecimal SumProfit4All;
	/**
	 *
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	
	
	public String getFullNo4S() {
		return FullNo4S;
	}
	public void setFullNo4S(String fullNo4S) {
		FullNo4S = fullNo4S;
	}
	public String getBrandNames4S() {
		return BrandNames4S;
	}
	public void setBrandNames4S(String brandNames4S) {
		BrandNames4S = brandNames4S;
	}
	public String getCustomerName4S() {
		return CustomerName4S;
	}
	public void setCustomerName4S(String customerName4S) {
		CustomerName4S = customerName4S;
	}
	public String getInvoiceNo4S() {
		return InvoiceNo4S;
	}
	public void setInvoiceNo4S(String invoiceNo4S) {
		InvoiceNo4S = invoiceNo4S;
	}
	public BigDecimal getQuantity4S() {
		return Quantity4S;
	}
	public void setQuantity4S(BigDecimal quantity4s) {
		Quantity4S = quantity4s;
	}
	public BigDecimal getPrice4S() {
		return Price4S;
	}
	public void setPrice4S(BigDecimal price4s) {
		Price4S = price4s;
	}
	public BigDecimal getRealFee4S() {
		return RealFee4S;
	}
	public void setRealFee4S(BigDecimal realFee4S) {
		RealFee4S = realFee4S;
	}
	public BigDecimal getQuantityHedged4S() {
		return QuantityHedged4S;
	}
	public void setQuantityHedged4S(BigDecimal quantityHedged4S) {
		QuantityHedged4S = quantityHedged4S;
	}
	public BigDecimal getPriceHedged4S() {
		return PriceHedged4S;
	}
	public void setPriceHedged4S(BigDecimal priceHedged4S) {
		PriceHedged4S = priceHedged4S;
	}
	public BigDecimal getProfit4S() {
		return Profit4S;
	}
	public void setProfit4S(BigDecimal profit4s) {
		Profit4S = profit4s;
	}
	public BigDecimal getPrice4B() {
		return Price4B;
	}
	public void setPrice4B(BigDecimal price4b) {
		Price4B = price4b;
	}
	public BigDecimal getRealFee4B() {
		return RealFee4B;
	}
	public void setRealFee4B(BigDecimal realFee4B) {
		RealFee4B = realFee4B;
	}
	public BigDecimal getQuantityHedged4B() {
		return QuantityHedged4B;
	}
	public void setQuantityHedged4B(BigDecimal quantityHedged4B) {
		QuantityHedged4B = quantityHedged4B;
	}
	public BigDecimal getPriceHedged4B() {
		return PriceHedged4B;
	}
	public void setPriceHedged4B(BigDecimal priceHedged4B) {
		PriceHedged4B = priceHedged4B;
	}
	public BigDecimal getCost4B() {
		return Cost4B;
	}
	public void setCost4B(BigDecimal cost4b) {
		Cost4B = cost4b;
	}
	public BigDecimal getSumProfit4Goods() {
		return SumProfit4Goods;
	}
	public void setSumProfit4Goods(BigDecimal sumProfit4Goods) {
		SumProfit4Goods = sumProfit4Goods;
	}
	public BigDecimal getSumProfit4Hedged() {
		return SumProfit4Hedged;
	}
	public void setSumProfit4Hedged(BigDecimal sumProfit4Hedged) {
		SumProfit4Hedged = sumProfit4Hedged;
	}
	public BigDecimal getSumProfit4All() {
		return SumProfit4All;
	}
	public void setSumProfit4All(BigDecimal sumProfit4All) {
		SumProfit4All = sumProfit4All;
	}
	public String getCommodityId() {
		return CommodityId;
	}
	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	
	
}

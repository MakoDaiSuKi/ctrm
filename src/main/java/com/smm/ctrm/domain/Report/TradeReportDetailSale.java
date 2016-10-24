

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeReportDetailSale {
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 销售发票号
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 * 销售发票数量（吨数）
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 出厂净重（吨数）
	 */
	@JsonProperty(value = "Quantity4Ware")
	private BigDecimal Quantity4Ware;
	/**
	 * 单价
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 金额
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getInvoiceNo(){
		return InvoiceNo;
	}
	public void setInvoiceNo(String InvoiceNo){
		this.InvoiceNo=InvoiceNo;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantity4Ware(){
		return Quantity4Ware;
	}
	public void setQuantity4Ware(BigDecimal Quantity4Ware){
		this.Quantity4Ware=Quantity4Ware;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getAmount(){
		return Amount;
	}
	public void setAmount(BigDecimal Amount){
		this.Amount=Amount;
	}
}
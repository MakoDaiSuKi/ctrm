

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeReportDetailBuy {
	/**
	 * 采购发票号
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 * 数量（吨数）（入库/出库数量）
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 成本单价
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 成本金额
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 入库/出库
	 */
	@JsonProperty(value = "oType")
	private String oType;
	
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
	
	public String getOType(){
		return oType;
	}
	public void setOType(String oType){
		this.oType=oType;
	}
}
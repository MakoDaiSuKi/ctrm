package com.smm.ctrm.dto.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FundPagerRes {
	public FundPagerRes(Date invoiceTradeDate, String customerName, String fullNo, String invoiceNo,
			BigDecimal invoiceQuantity, BigDecimal invoiceAmount, Date invoiceDueDate, String id, Boolean isExecuted,
			BigDecimal quantity, BigDecimal amount, String currency, String createdBy) {
		super();
		InvoiceTradeDate = invoiceTradeDate;
		CustomerName = customerName;
		FullNo = fullNo;
		InvoiceNo = invoiceNo;
		InvoiceQuantity = invoiceQuantity;
		InvoiceAmount = invoiceAmount;
		InvoiceDueDate = invoiceDueDate;
		Id = id;
		IsExecuted = isExecuted;
		Quantity = quantity;
		Amount = amount;
		Currency = currency;
		CreatedBy = createdBy;
	}
	// 非数据库字段
	@JsonProperty(value = "InvoiceTradeDate")
	private Date InvoiceTradeDate;
	
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	
	@JsonProperty(value = "InvoiceQuantity")
	private BigDecimal InvoiceQuantity;
	
	@JsonProperty(value = "InvoiceAmount")
	private BigDecimal InvoiceAmount;
	
	@JsonProperty(value = "InvoiceDueDate")
	private Date InvoiceDueDate;
	
	// 数据库字段
	@JsonProperty(value = "Id")
	private String Id;
	/**
	 * 是否已经完成收付款
	 */
	@JsonProperty(value = "IsExecuted")
	private Boolean IsExecuted;
	
	/**
	 * 本次付款数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 本次付款金额
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	
	/**
	 * 货币（Dict）
	 */
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 创建人
	 */
	@JsonProperty(value = "CreatedBy")
	private String CreatedBy;
	
	public Date getInvoiceTradeDate() {
		return InvoiceTradeDate;
	}
	public void setInvoiceTradeDate(Date invoiceTradeDate) {
		InvoiceTradeDate = invoiceTradeDate;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getFullNo() {
		return FullNo;
	}
	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}
	public String getInvoiceNo() {
		return InvoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}
	public BigDecimal getInvoiceQuantity() {
		return InvoiceQuantity;
	}
	public void setInvoiceQuantity(BigDecimal invoiceQuantity) {
		InvoiceQuantity = invoiceQuantity;
	}
	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		InvoiceAmount = invoiceAmount;
	}
	public Date getInvoiceDueDate() {
		return InvoiceDueDate;
	}
	public void setInvoiceDueDate(Date invoiceDueDate) {
		InvoiceDueDate = invoiceDueDate;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public Boolean getIsExecuted() {
		return IsExecuted;
	}
	public void setIsExecuted(Boolean isExecuted) {
		IsExecuted = isExecuted;
	}
	public BigDecimal getQuantity() {
		return Quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}
	public BigDecimal getAmount() {
		return Amount;
	}
	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
}

package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * @author zhaoyutao 2016年10月12日
 */
public class InvoiceReport extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3398807985006514385L;
	
	

	public InvoiceReport() {
		super();
	}

	public InvoiceReport(String invoiceNo, String fullNo, String customerName, BigDecimal lotQuantity,
			BigDecimal lotQuantityDelivered, BigDecimal quantity, BigDecimal quantityTotal, BigDecimal price,
			BigDecimal amountNotional, BigDecimal dueAmount, Date tradeDate) {
		super();
		InvoiceNo = invoiceNo;
		FullNo = fullNo;
		CustomerName = customerName;
		LotQuantity = lotQuantity;
		LotQuantityDelivered = lotQuantityDelivered;
		Quantity = quantity;
		QuantityTotal = quantityTotal;
		Price = price;
		AmountNotional = amountNotional;
		DueAmount = dueAmount;
		TradeDate = tradeDate;
	}

	/**
	 * 单据号 InvoiceNo = Prefix + SerialNo + Suffix
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;

	/**
	 * 订单号
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;

	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;

	/**
	 * 批次数量
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;

	/**
	 * 交付数量
	 */
	@JsonProperty(value = "LotQuantityDelivered")
	private BigDecimal LotQuantityDelivered;

	/**
	 * 本次开票数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;

	/**
	 * 已开票数量（含本次）
	 */
	@JsonProperty(value = "QuantityTotal")
	private BigDecimal QuantityTotal;

	/**
	 * 开票价格
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;

	/**
	 * 开票金额
	 */
	@JsonProperty(value = "AmountNotional")
	private BigDecimal AmountNotional;

	/**
	 * 实际金额
	 */
	@JsonProperty(value = "DueAmount")
	private BigDecimal DueAmount;

	/**
	 * 业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public BigDecimal getLotQuantity() {
		return LotQuantity;
	}

	public void setLotQuantity(BigDecimal lotQuantity) {
		LotQuantity = lotQuantity;
	}

	public BigDecimal getLotQuantityDelivered() {
		return LotQuantityDelivered;
	}

	public void setLotQuantityDelivered(BigDecimal lotQuantityDelivered) {
		LotQuantityDelivered = lotQuantityDelivered;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityTotal() {
		return QuantityTotal;
	}

	public void setQuantityTotal(BigDecimal quantityTotal) {
		QuantityTotal = quantityTotal;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public BigDecimal getAmountNotional() {
		return AmountNotional;
	}

	public void setAmountNotional(BigDecimal amountNotional) {
		AmountNotional = amountNotional;
	}

	public BigDecimal getDueAmount() {
		return DueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		DueAmount = dueAmount;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}
}

package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class FundReport extends HibernateEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6410871204415962481L;
	

	public FundReport(String legalName, String headNo, String customerName, BigDecimal amount, String currency,
			BigDecimal lotQuantity, BigDecimal lotQuantityDelivered, BigDecimal final1, Boolean isDelivered,
			BigDecimal lotAmount, BigDecimal amountFunded, Date tradeDate) {
		super();
		LegalName = legalName;
		HeadNo = headNo;
		CustomerName = customerName;
		Amount = amount;
		Currency = currency;
		LotQuantity = lotQuantity;
		LotQuantityDelivered = lotQuantityDelivered;
		Final = final1;
		IsDelivered = isDelivered;
		LotAmount = lotAmount;
		AmountFunded = amountFunded;
		TradeDate = tradeDate;
	}

	/**
	 * 抬头
	 */
	@JsonProperty(value = "LegalName")
	private String LegalName;
	
	/**
	 * 订单号
	 * headNo = Prefix + SerialNo + Suffix
	 */
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
	
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	
	/**
	 * 本次发生金额
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	
	/**
	 * 币种
	 */
	@JsonProperty(value = "Currency")
	private String Currency;
	
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
	 * 价格
	 */
	@JsonProperty(value = "Final")
	private BigDecimal Final;
	
	/**
	 * 收发货的标识
	 */
	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;
	
	/**
	 * 批次金额
	 */
	@JsonProperty(value = "LotAmount")
	private BigDecimal LotAmount;
	
	/**
	 * 已收款金额（含本次）
	 */
	@JsonProperty(value = "AmountFunded")
	private BigDecimal AmountFunded;
	
	/**
	 *业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String legalName) {
		LegalName = legalName;
	}

	public String getHeadNo() {
		return HeadNo;
	}

	public void setHeadNo(String headNo) {
		HeadNo = headNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
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

	public BigDecimal getFinal() {
		return Final;
	}

	public void setFinal(BigDecimal final1) {
		Final = final1;
	}

	public Boolean getIsDelivered() {
		return IsDelivered;
	}

	public void setIsDelivered(Boolean isDelivered) {
		IsDelivered = isDelivered;
	}

	public BigDecimal getLotAmount() {
		return LotAmount;
	}

	public void setLotAmount(BigDecimal lotAmount) {
		LotAmount = lotAmount;
	}

	public BigDecimal getAmountFunded() {
		return AmountFunded;
	}

	public void setAmountFunded(BigDecimal amountFunded) {
		AmountFunded = amountFunded;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}
	
	
}

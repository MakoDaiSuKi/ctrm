package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class PricingReport extends HibernateEntity{
	
	public PricingReport() {
		super();
	}

	public PricingReport(String fullNo, BigDecimal quantity, BigDecimal quantityPriced, BigDecimal major,
			BigDecimal premium, String majorMarketName, String majorType, Date tradeDate) {
		super();
		FullNo = fullNo;
		Quantity = quantity;
		QuantityPriced = quantityPriced;
		Major = major;
		Premium = premium;
		MajorMarketName = majorMarketName;
		MajorType = majorType;
		TradeDate = tradeDate;
	}

	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 点价数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 已经点价的数量之和
	 */
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	
	/**
	 * （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果
	 */
	@JsonProperty(value = "Major")
	private BigDecimal Major;
	
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
	 */
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	
	/**
	 * 点价市场
	 */
	@JsonProperty(value = "MajorMarketName")
	private String MajorMarketName;
	
	/**
	 * { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
	 */
	@JsonProperty(value = "MajorType")
	private String MajorType;
	
	/**
	 * 点价日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal quantityPriced) {
		QuantityPriced = quantityPriced;
	}

	public BigDecimal getMajor() {
		return Major;
	}

	public void setMajor(BigDecimal major) {
		Major = major;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal premium) {
		Premium = premium;
	}

	public String getMajorMarketName() {
		return MajorMarketName;
	}

	public void setMajorMarketName(String majorMarketName) {
		MajorMarketName = majorMarketName;
	}

	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String majorType) {
		MajorType = majorType;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}
}

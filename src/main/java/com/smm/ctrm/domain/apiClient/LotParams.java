
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. 合同明细，class= Lot
 * 
 */
public class LotParams extends ApiGridParams {
	@JsonProperty(value = "Currency")
	private String Currency;

	@JsonProperty(value = "SpotType")
	private String SpotType;

	@JsonProperty(value = "Statuses")
	private String Statuses;

	@JsonProperty(value = "LegalIds")
	private String LegalIds;

	@JsonProperty(value = "MarkColor")
	private int MarkColor;

	@JsonProperty(value = "CustomerId")
	private String CustomerId;

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	@JsonProperty(value = "TraderId")
	private String TraderId;

	@JsonProperty(value = "LegalId")
	private String LegalId;

	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;

	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;

	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;

	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;

	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;

	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	@JsonProperty(value = "StartDate")
	private Date StartDate;

	@JsonProperty(value = "EndDate")
	private Date EndDate;

	@JsonProperty(value = "TradeDateStart")
	private Date TradeDateStart;

	@JsonProperty(value = "TradeDateEnd")
	private Date TradeDateEnd;

	@JsonProperty(value = "IsAverageLotOnly")
	private Boolean IsAverageLotOnly;

	@JsonProperty(value = "AverageAtCurrentDuration")
	private Boolean AverageAtCurrentDuration;

	@JsonProperty(value = "RePricing")
	private Boolean RePricing;

	@JsonProperty(value = "CurrentPageOnly")
	private Boolean CurrentPageOnly;

	@JsonProperty(value = "ReCalculatePL")
	private Boolean ReCalculatePL;

	@JsonProperty(value = "MajorType")
	private String MajorType;

	@JsonProperty(value = "PremiumType")
	private String PremiumType;

	@JsonProperty(value = "CreatedBy")
	private String CreatedBy;

	@JsonProperty(value = "FullNo")
	private String FullNo;// 模糊查询订单号

	@JsonProperty(value = "PriceStartDate")
	private Date PriceStartDate; // 点价期间，开始日期
	
	@JsonProperty(value = "PriceEndDate")
	private Date PriceEndDate; // 点价期间，结束日期
	
	@JsonProperty(value = "PricingType")
	private String PricingType; // 点价类型
	
	@JsonProperty(value = "HadHedged")
	private Boolean HadHedged; // 是否已经开始保值
	
	/**
	 * 是否套利
	 */
	@JsonProperty(value="IsArbitrage")
	private Boolean IsArbitrage;

	public Boolean getAverageAtCurrentDuration() {
		return AverageAtCurrentDuration;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public String getCurrency() {
		return Currency;
	}

	public Boolean getCurrentPageOnly() {
		return CurrentPageOnly;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public Boolean getIsAverageLotOnly() {
		return IsAverageLotOnly != null ? IsAverageLotOnly : false;
	}

	public Boolean getIsDelivered() {
		return IsDelivered;
	}

	public Boolean getIsHedged() {
		return IsHedged;
	}

	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public Boolean getIsPriced() {
		return IsPriced;
	}

	public String getLegalId() {
		return LegalId;
	}

	public String getLegalIds() {
		return LegalIds;
	}

	public String getMajorType() {
		return MajorType;
	}

	public int getMarkColor() {
		return MarkColor;
	}

	public String getPremiumType() {
		return PremiumType;
	}

	public Boolean getReCalculatePL() {
		return ReCalculatePL;
	}

	public Boolean getRePricing() {
		return RePricing;
	}

	public String getSpotDirection() {
		return SpotDirection;
	}

	public String getSpotType() {
		return SpotType;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public String getStatuses() {
		return Statuses;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public Date getTradeDateEnd() {
		return TradeDateEnd;
	}

	public Date getTradeDateStart() {
		return TradeDateStart;
	}

	public String getTraderId() {
		return TraderId;
	}

	public void setAverageAtCurrentDuration(Boolean value) {
		AverageAtCurrentDuration = value;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	public void setCreatedBy(String value) {
		CreatedBy = value;
	}

	public void setCurrency(String value) {
		Currency = value;
	}

	public void setCurrentPageOnly(Boolean value) {
		CurrentPageOnly = value;
	}

	public void setCustomerId(String value) {
		CustomerId = value;
	}

	public void setEndDate(Date value) {
		EndDate = value;
	}

	public void setIsAverageLotOnly(Boolean value) {
		IsAverageLotOnly = value;
	}

	public void setIsDelivered(Boolean value) {
		IsDelivered = value;
	}

	public void setIsHedged(Boolean value) {
		IsHedged = value;
	}

	public void setIsInvoiced(Boolean value) {
		IsInvoiced = value;
	}

	public void setIsPriced(Boolean value) {
		IsPriced = value;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	public void setLegalIds(String value) {
		LegalIds = value;
	}

	public void setMajorType(String value) {
		MajorType = value;
	}

	public void setMarkColor(int value) {
		MarkColor = value;
	}

	public void setPremiumType(String value) {
		PremiumType = value;
	}

	public void setReCalculatePL(Boolean value) {
		ReCalculatePL = value;
	}

	public void setRePricing(Boolean value) {
		RePricing = value;
	}

	public void setSpotDirection(String value) {
		SpotDirection = value;
	}

	public void setSpotType(String value) {
		SpotType = value;
	}

	public void setStartDate(Date value) {
		StartDate = value;
	}

	public void setStatuses(String value) {
		Statuses = value;
	}

	public void setTradeDate(Date value) {
		TradeDate = value;
	}

	public void setTradeDateEnd(Date tradeDateEnd) {
		TradeDateEnd = tradeDateEnd;
	}

	public void setTradeDateStart(Date tradeDateStart) {
		TradeDateStart = tradeDateStart;
	}

	public void setTraderId(String value) {
		TraderId = value;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public Date getPriceStartDate() {
		return PriceStartDate;
	}

	public void setPriceStartDate(Date priceStartDate) {
		PriceStartDate = priceStartDate;
	}

	public Date getPriceEndDate() {
		return PriceEndDate;
	}

	public void setPriceEndDate(Date priceEndDate) {
		PriceEndDate = priceEndDate;
	}

	public String getPricingType() {
		return PricingType;
	}

	public void setPricingType(String pricingType) {
		PricingType = pricingType;
	}

	public Boolean getIsArbitrage() {
		return IsArbitrage;
	}

	public void setIsArbitrage(Boolean isArbitrage) {
		IsArbitrage = isArbitrage;
	}

	public Boolean getHadHedged() {
		return HadHedged;
	}

	public void setHadHedged(Boolean hadHedged) {
		HadHedged = hadHedged;
	}
}
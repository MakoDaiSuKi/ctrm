
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 5. 头寸, class = Position
 * 
 */
public class PositionParams extends ApiGridParams {
	@JsonProperty(value = "AverageAtCurrentDuration")
	private Boolean AverageAtCurrentDuration;

	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	
	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	/**
	 * 创建结束日期
	 */
	@JsonProperty(value = "CreateEndDate")
	private Date CreateEndDate;

	/**
	 * 创建起始日期
	 */
	@JsonProperty(value = "CreateStartDate")
	private Date CreateStartDate;

	@JsonProperty(value = "EndDate")
	private Date EndDate;
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	
	@JsonProperty(value = "InstrumentId")
	private String InstrumentId;

	// 这是较为特殊的用法，返回该发票 --- 无论是销售还是采购的发票，找出其对应的全部的头寸
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;

	@JsonProperty(value = "IsAvailable4Allocate2Lot")
	private Boolean IsAvailable4Allocate2Lot;
	
	@JsonProperty(value = "IsCarry")
	private Boolean IsCarry;

	@JsonProperty(value = "IsCarryAgainst")
	private Boolean IsCarryAgainst;

	@JsonProperty(value = "IsDistributed")
	private Boolean IsDistributed;

	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;

	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;

	@JsonProperty(value = "IsSourceOfCarry")
	private Boolean IsSourceOfCarry;

	@JsonProperty(value = "IsSplitted")
	private Boolean IsSplitted;

	@JsonProperty(value = "IsSquared")
	private Boolean IsSquared;

	@JsonProperty(value = "IsVirtual")
	private Boolean IsVirtual;

	@JsonProperty(value = "MarketCode")
	private String LS;

	@JsonProperty(value = "MarketCode")
	private String MarketCode;

	@JsonProperty(value = "MarketId")
	private String MarketId;

	@JsonProperty(value = "MarketIds")
	private String MarketIds;

	@JsonProperty(value = "OSC")
	private String OSC;

	/**
	 * 交易目的 {保值H, 投机S, 套利A}(Dict)
	 */
	@JsonProperty(value = "Purpose")
	private String Purpose;
	/**
	 * 
	 */
	@JsonProperty(value = "Position4BrokerId")
	private String Position4BrokerId;

	@JsonProperty(value = "StartDate")
	private Date StartDate;

	@JsonProperty(value = "TradeEndDate")
	private Date TradeEndDate;

	@JsonProperty(value = "TradeStartDate")
	private Date TradeStartDate;

	public Boolean getAverageAtCurrentDuration() {
		return AverageAtCurrentDuration;
	}

	public String getBrokerId() {
		return BrokerId;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public Date getCreateEndDate() {
		return CreateEndDate;
	}

	public Date getCreateStartDate() {
		return CreateStartDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public String getForwardType() {
		return ForwardType;
	}

	public String getInstrumentId() {
		return InstrumentId;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public Boolean getIsAvailable4Allocate2Lot() {
		return IsAvailable4Allocate2Lot;
	}

	public Boolean getIsCarry() {
		return IsCarry;
	}

	public Boolean getIsCarryAgainst() {
		return IsCarryAgainst;
	}

	public Boolean getIsDistributed() {
		return IsDistributed;
	}

	public Boolean getIsHedged() {
		return IsHedged;
	}

	public Boolean getIsPriced() {
		return IsPriced;
	}

	public Boolean getIsSourceOfCarry() {
		return IsSourceOfCarry;
	}

	public Boolean getIsSplitted() {
		return IsSplitted;
	}

	public Boolean getIsSquared() {
		return IsSquared;
	}

	public Boolean getIsVirtual() {
		return IsVirtual;
	}

	public String getLS() {
		return LS;
	}

	public String getMarketCode() {
		return MarketCode;
	}

	public String getMarketId() {
		return MarketId;
	}

	public String getMarketIds() {
		return MarketIds;
	}

	public String getOSC() {
		return OSC;
	}

	public String getPurpose() {
		return Purpose;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public Date getTradeEndDate() {
		return TradeEndDate;
	}

	public Date getTradeStartDate() {
		return TradeStartDate;
	}

	public void setAverageAtCurrentDuration(Boolean value) {
		AverageAtCurrentDuration = value;
	}

	public void setBrokerId(String value) {
		BrokerId = value;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	public void setCreateEndDate(Date createEndDate) {
		CreateEndDate = createEndDate;
	}

	public void setCreateStartDate(Date createStartDate) {
		CreateStartDate = createStartDate;
	}

	public void setEndDate(Date value) {
		EndDate = value;
	}

	public void setForwardType(String value) {
		ForwardType = value;
	}

	public void setInstrumentId(String instrumentId) {
		InstrumentId = instrumentId;
	}

	public void setInvoiceId(String value) {
		InvoiceId = value;
	}

	public void setIsAvailable4Allocate2Lot(Boolean value) {
		IsAvailable4Allocate2Lot = value;
	}

	public void setIsCarry(Boolean value) {
		IsCarry = value;
	}

	public void setIsCarryAgainst(Boolean value) {
		IsCarryAgainst = value;
	}
	
	public void setIsDistributed(Boolean isDistributed) {
		IsDistributed = isDistributed;
	}
	
	public void setIsHedged(Boolean value) {
		IsHedged = value;
	}

	public void setIsPriced(Boolean value) {
		IsPriced = value;
	}
	
	public void setIsSourceOfCarry(Boolean value) {
		IsSourceOfCarry = value;
	}
	
	public void setIsSplitted(Boolean value) {
		IsSplitted = value;
	}

	public void setIsSquared(Boolean value) {
		IsSquared = value;
	}
	public void setIsVirtual(Boolean value) {
		IsVirtual = value;
	}

	public void setLS(String lS) {
		LS = lS;
	}

	public void setMarketCode(String value) {
		MarketCode = value;
	}
	
	public void setMarketId(String value) {
		MarketId = value;
	}

	public void setMarketIds(String value) {
		MarketIds = value;
	}

	public void setOSC(String oSC) {
		OSC = oSC;
	}

	public void setPurpose(String value) {
		Purpose = value;
	}

	public void setStartDate(Date value) {
		StartDate = value;
	}

	public void setTradeEndDate(Date value) {
		TradeEndDate = value;
	}

	public void setTradeStartDate(Date value) {
		TradeStartDate = value;
	}

	public String getPosition4BrokerId() {
		return Position4BrokerId;
	}

	public void setPosition4BrokerId(String position4BrokerId) {
		Position4BrokerId = position4BrokerId;
	}
}
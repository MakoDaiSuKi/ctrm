package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionGatherParams extends ApiGridParams {
	
	/**
	 * 
	 */
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	
	/**
	 * 
	 */
	@JsonProperty(value = "InstrumentId")
	private String InstrumentId;

	/**
	 * 起始交易日期
	 */
	@JsonProperty(value = "TradeStartDate")
	private Date TradeStartDate;
	/**
	 * 截止交易日期
	 */
	@JsonProperty(value = "TradeEndDate")
	private Date TradeEndDate;
	/**
	 * 市场Id
	 */
	@JsonProperty(value = "MarketId")
	public String MarketId;

	public Date getTradeStartDate() {
		return TradeStartDate;
	}

	public void setTradeStartDate(Date tradeStartDate) {
		TradeStartDate = tradeStartDate;
	}

	public Date getTradeEndDate() {
		return TradeEndDate;
	}

	public void setTradeEndDate(Date tradeEndDate) {
		TradeEndDate = tradeEndDate;
	}

	public String getMarketId() {
		return MarketId;
	}

	public void setMarketId(String marketId) {
		MarketId = marketId;
	}

	public String getBrokerId() {
		return BrokerId;
	}

	public void setBrokerId(String brokerId) {
		BrokerId = brokerId;
	}

	public String getInstrumentId() {
		return InstrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		InstrumentId = instrumentId;
	}
}

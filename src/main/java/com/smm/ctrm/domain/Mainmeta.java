package com.smm.ctrm.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 主力金属
 * 
 * @author zengshihua
 *
 */
public class Mainmeta {
	/**
	 * 
	 */
	@JsonProperty(value = "InstrumentID")
	private String InstrumentID;
	/**
	 * 
	 */
	@JsonProperty(value = "Turnover")
	private BigDecimal Turnover;
	/**
	 * 
	 */
	@JsonProperty(value = "BestAskPrice")
	private BigDecimal BestAskPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "BestAskPriceVolume")
	private BigDecimal BestAskPriceVolume;
	/**
	 * 
	 */
	@JsonProperty(value = "BestBidPrice")
	private BigDecimal BestBidPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "BestBidPriceVolume")
	private BigDecimal BestBidPriceVolume;
	/**
	 * 
	 */
	@JsonProperty(value = "LastPrice")
	private BigDecimal LastPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "UpDown")
	private BigDecimal UpDown;
	/**
	 * 
	 */
	@JsonProperty(value = "OpenPrice")
	private BigDecimal OpenPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "HighestPrice")
	private BigDecimal HighestPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "LowestPrice")
	private BigDecimal LowestPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "Volume")
	private BigDecimal Volume;
	/**
	 * 
	 */
	@JsonProperty(value = "OpenInterest")
	private BigDecimal OpenInterest;
	/**
	 * 
	 */
	@JsonProperty(value = "PreOpenInterest")
	private BigDecimal PreOpenInterest;
	/**
	 * 
	 */
	@JsonProperty(value = "Average")
	private BigDecimal Average;
	/**
	 * 
	 */
	@JsonProperty(value = "SettlementPrice")
	private BigDecimal SettlementPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "PreSettlementPrice")
	private BigDecimal PreSettlementPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "UpperLimitPrice")
	private BigDecimal UpperLimitPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "LowerLimitPrice")
	private BigDecimal LowerLimitPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "PreClosePrice")
	private BigDecimal PreClosePrice;
	/**
	 * 
	 */
	@JsonProperty(value = "TradingDay")
	private String TradingDay;
	/**
	 * 
	 */
	@JsonProperty(value = "Typename")
	private String Typename;
	
	@JsonProperty(value = "LMEprice")
	private LMEprice LMEprice;
	
	@JsonProperty(value = "PretradingDay")
	private String PretradingDay;
	
	@JsonProperty(value = "Exchange")
	private Exchange Exchange;

	public String getInstrumentID() {
		return InstrumentID;
	}

	public void setInstrumentID(String instrumentID) {
		InstrumentID = instrumentID;
	}

	public BigDecimal getTurnover() {
		return Turnover;
	}

	public void setTurnover(BigDecimal turnover) {
		Turnover = turnover;
	}

	public BigDecimal getBestAskPrice() {
		return BestAskPrice;
	}

	public void setBestAskPrice(BigDecimal bestAskPrice) {
		BestAskPrice = bestAskPrice;
	}

	public BigDecimal getBestAskPriceVolume() {
		return BestAskPriceVolume;
	}

	public void setBestAskPriceVolume(BigDecimal bestAskPriceVolume) {
		BestAskPriceVolume = bestAskPriceVolume;
	}

	public BigDecimal getBestBidPrice() {
		return BestBidPrice;
	}

	public void setBestBidPrice(BigDecimal bestBidPrice) {
		BestBidPrice = bestBidPrice;
	}

	public BigDecimal getBestBidPriceVolume() {
		return BestBidPriceVolume;
	}

	public void setBestBidPriceVolume(BigDecimal bestBidPriceVolume) {
		BestBidPriceVolume = bestBidPriceVolume;
	}

	public BigDecimal getLastPrice() {
		return LastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		LastPrice = lastPrice;
	}

	public BigDecimal getUpDown() {
		return UpDown;
	}

	public void setUpDown(BigDecimal upDown) {
		UpDown = upDown;
	}

	public BigDecimal getOpenPrice() {
		return OpenPrice;
	}

	public void setOpenPrice(BigDecimal openPrice) {
		OpenPrice = openPrice;
	}

	public BigDecimal getHighestPrice() {
		return HighestPrice;
	}

	public void setHighestPrice(BigDecimal highestPrice) {
		HighestPrice = highestPrice;
	}

	public BigDecimal getLowestPrice() {
		return LowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		LowestPrice = lowestPrice;
	}

	public BigDecimal getVolume() {
		return Volume;
	}

	public void setVolume(BigDecimal volume) {
		Volume = volume;
	}

	public BigDecimal getOpenInterest() {
		return OpenInterest;
	}

	public void setOpenInterest(BigDecimal openInterest) {
		OpenInterest = openInterest;
	}

	public BigDecimal getPreOpenInterest() {
		return PreOpenInterest;
	}

	public void setPreOpenInterest(BigDecimal preOpenInterest) {
		PreOpenInterest = preOpenInterest;
	}

	public BigDecimal getAverage() {
		return Average;
	}

	public void setAverage(BigDecimal average) {
		Average = average;
	}

	public BigDecimal getSettlementPrice() {
		return SettlementPrice;
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		SettlementPrice = settlementPrice;
	}

	public BigDecimal getPreSettlementPrice() {
		return PreSettlementPrice;
	}

	public void setPreSettlementPrice(BigDecimal preSettlementPrice) {
		PreSettlementPrice = preSettlementPrice;
	}

	public BigDecimal getUpperLimitPrice() {
		return UpperLimitPrice;
	}

	public void setUpperLimitPrice(BigDecimal upperLimitPrice) {
		UpperLimitPrice = upperLimitPrice;
	}

	public BigDecimal getLowerLimitPrice() {
		return LowerLimitPrice;
	}

	public void setLowerLimitPrice(BigDecimal lowerLimitPrice) {
		LowerLimitPrice = lowerLimitPrice;
	}

	public BigDecimal getPreClosePrice() {
		return PreClosePrice;
	}

	public void setPreClosePrice(BigDecimal preClosePrice) {
		PreClosePrice = preClosePrice;
	}

	public String getTradingDay() {
		return TradingDay;
	}

	public void setTradingDay(String tradingDay) {
		TradingDay = tradingDay;
	}

	public String getTypename() {
		return Typename;
	}

	public void setTypename(String typename) {
		Typename = typename;
	}

	public LMEprice getLMEprice() {
		return LMEprice;
	}

	public void setLMEprice(LMEprice lMEprice) {
		LMEprice = lMEprice;
	}
	
	public Exchange getExchange() {
		return Exchange;
	}

	public void setExchange(Exchange exchange) {
		Exchange = exchange;
	}

	public String getPretradingDay() {
		return PretradingDay;
	}

	public void setPretradingDay(String pretradingDay) {
		PretradingDay = pretradingDay;
	}
	
	
}

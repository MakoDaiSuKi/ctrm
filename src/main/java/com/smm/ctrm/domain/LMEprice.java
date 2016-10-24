package com.smm.ctrm.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LMEprice {

	@JsonProperty(value = "instrument_id")
	private String instrument_id;

	@JsonProperty(value = "trading_day")
	private String trading_day;

	@JsonProperty(value = "open_price")
	private BigDecimal open_price;

	@JsonProperty(value = "close_price")
	private BigDecimal close_price;

	@JsonProperty(value = "highest_price")
	private BigDecimal highest_price;

	@JsonProperty(value = "lowest_price")
	private BigDecimal lowest_price;

	public String getInstrument_id() {
		return instrument_id;
	}

	public void setInstrument_id(String instrument_id) {
		this.instrument_id = instrument_id;
	}

	public String getTrading_day() {
		return trading_day;
	}

	public void setTrading_day(String trading_day) {
		this.trading_day = trading_day;
	}

	public BigDecimal getOpen_price() {
		return open_price;
	}

	public void setOpen_price(BigDecimal open_price) {
		this.open_price = open_price;
	}

	public BigDecimal getClose_price() {
		return close_price;
	}

	public void setClose_price(BigDecimal close_price) {
		this.close_price = close_price;
	}

	public BigDecimal getHighest_price() {
		return highest_price;
	}

	public void setHighest_price(BigDecimal highest_price) {
		this.highest_price = highest_price;
	}

	public BigDecimal getLowest_price() {
		return lowest_price;
	}

	public void setLowest_price(BigDecimal lowest_price) {
		this.lowest_price = lowest_price;
	}

}

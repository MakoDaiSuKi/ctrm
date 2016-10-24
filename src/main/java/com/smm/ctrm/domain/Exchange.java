package com.smm.ctrm.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 主力金属
 * 
 * @author shencl
 *
 */
public class Exchange {
	/**
	 * 
	 */
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 
	 */
	@JsonProperty(value = "Date")
	private Date Date;
	/**
	 * 
	 */
	@JsonProperty(value = "MidPrice")
	private BigDecimal MidPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "XcBuyPrice")
	private BigDecimal XcBuyPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "XcSellPrice")
	private BigDecimal XcSellPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "XhBuyPrice")
	private BigDecimal XhBuyPrice;
	/**
	 * 
	 */
	@JsonProperty(value = "XhSellPrice")
	private BigDecimal XhSellPrice;

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public Date getTurnover() {
		return Date;
	}

	public void setTurnover(Date date) {
		Date = date;
	}

	public BigDecimal getMidPrice() {
		return MidPrice;
	}

	public void setMidPrice(BigDecimal midPrice) {
		MidPrice = midPrice;
	}

	public BigDecimal getXcBuyPrice() {
		return XcBuyPrice;
	}

	public void setXcBuyPrice(BigDecimal xcBuyPrice) {
		XcBuyPrice = xcBuyPrice;
	}

	public BigDecimal getXcSellPrice() {
		return XcSellPrice;
	}

	public void setXcSellPrice(BigDecimal xcSellPrice) {
		XcSellPrice = xcSellPrice;
	}

	public BigDecimal getXhBuyPrice() {
		return XhBuyPrice;
	}

	public void setXhBuyPrice(BigDecimal xhBuyPrice) {
		XhBuyPrice = xhBuyPrice;
	}

	public BigDecimal getXhSellPrice() {
		return XhSellPrice;
	}

	public void setXhSellPrice(BigDecimal xhSellPrice) {
		XhSellPrice = xhSellPrice;
	}
	
}

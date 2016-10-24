
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeParams extends ApiGridParams
{
	@JsonProperty(value = "Currency")
	private String Currency;
	public String getCurrency()
	{
		return Currency;
	}
	public void setCurrency(String value)
	{
		Currency = value;
	}
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	public Date getTradeDate()
	{
		return TradeDate;
	}
	public void setTradeDate(java.util.Date value)
	{
		TradeDate = value;
	}
}
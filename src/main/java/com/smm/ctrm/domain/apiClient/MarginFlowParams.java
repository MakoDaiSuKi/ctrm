
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarginFlowParams extends ApiGridParams {
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date value) {
		TradeDate = value;
	}
}
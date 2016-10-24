
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class M2MParams extends ApiGridParams {
	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	
	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date value) {
		TradeDate = value;
	}

	@JsonProperty(value = "PromptDate")
	private Date PromptDate;

	
	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date value) {
		PromptDate = value;
	}
}
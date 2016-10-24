
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//todo:
public class SettleDailyParams extends ApiGridParams {
	@JsonProperty(value = "MarketId")
	private String MarketId;

	
	public String getMarketId() {
		return MarketId;
	}

	public void setMarketId(String value) {
		MarketId = value;
	}

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	@JsonProperty(value = "StartDate")
	private Date StartDate;

	
	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date value) {
		StartDate = value;
	}

	@JsonProperty(value = "EndDate")
	private Date EndDate;

	
	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date value) {
		EndDate = value;
	}
}
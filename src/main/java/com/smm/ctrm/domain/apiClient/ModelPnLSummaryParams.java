
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelPnLSummaryParams extends ApiGridParams {
	@JsonProperty(value = "LegalId")
	private String LegalId;

	
	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	@JsonProperty(value = "CommodityIds")
	private String CommodityIds;

	
	public String getCommodityIds() {
		return CommodityIds;
	}

	public void setCommodityIds(String value) {
		CommodityIds = value;
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

	String MarketId;

	
	public String getMarketId() {
		return MarketId;
	}

	public void setMarketId(String value) {
		MarketId = value;
	}

}
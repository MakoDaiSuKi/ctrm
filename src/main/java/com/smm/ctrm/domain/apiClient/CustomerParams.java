
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerParams extends ApiGridParams {
	@JsonProperty(value = "Statuses")
	private String Statuses;

	
	public String getStatuses() {
		return Statuses;
	}

	public void setStatuses(String value) {
		Statuses = value;
	}
	
	@JsonProperty(value = "SpotType")
	private String SpotType;

	
	public String getSpotType() {
		return SpotType;
	}

	public void setSpotType(String value) {
		SpotType = value;
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
}
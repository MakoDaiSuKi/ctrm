
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HedgeParams extends ApiGridParams {
	@JsonProperty(value = "LotIds")
	private String LotIds;

	
	public String getLotIds() {
		return LotIds;
	}

	public void setLotIds(String value) {
		LotIds = value;
	}

	@JsonProperty(value = "PositionId")
	private String PositionId;

	
	public String getPositionId() {
		return PositionId;
	}

	public void setPositionId(String value) {
		PositionId = value;
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
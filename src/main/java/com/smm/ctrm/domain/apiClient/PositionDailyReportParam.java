package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyReportParam {
	@JsonProperty(value = "BeginDate")
	private Date BeginDate;
	
	@JsonProperty(value = "EndDate")
	private Date EndDate;

	public Date getBeginDate() {
		return BeginDate;
	}

	public void setBeginDate(Date beginDate) {
		BeginDate = beginDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	
}


package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 8. 信用证, class = LC
 * 
 */
public class LCParams extends ApiGridParams {
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

	@JsonProperty(value = "IssueStartDate")
	private Date IssueStartDate;

	public Date getIssueStartDate() {
		return IssueStartDate;
	}

	public void setIssueStartDate(Date value) {
		IssueStartDate = value;
	}

	@JsonProperty(value = "IssueEndDate")
	private Date IssueEndDate;

	public Date getIssueEndDate() {
		return IssueEndDate;
	}

	public void setIssueEndDate(Date value) {
		IssueEndDate = value;
	}
}
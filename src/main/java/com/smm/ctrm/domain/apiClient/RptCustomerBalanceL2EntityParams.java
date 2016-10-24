
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RptCustomerBalanceL2EntityParams extends ApiGridParams {
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;

	
	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(Boolean value) {
		IsInvoiced = value;
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
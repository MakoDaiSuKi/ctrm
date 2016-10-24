
package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PendingParams extends ApiGridParams {
	@JsonProperty(value = "ApproverId")
	private String ApproverId;

	
	public String getApproverId() {
		return ApproverId;
	}

	public void setApproverId(String value) {
		ApproverId = value;
	}

	@JsonProperty(value = "IsDone")
	private Boolean IsDone;

	
	public Boolean getIsDone() {
		return IsDone;
	}

	public void setIsDone(Boolean value) {
		IsDone = value;
	}

	@JsonProperty(value = "Statuses")
	private String Statuses;

	
	public String getStatuses() {
		return Statuses;
	}

	public void setStatuses(String value) {
		Statuses = value;
	}
	
	/**
	 * 收发货标记 R=收货 S=发货
	 */
	private String Flag = "R";

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}
	
}
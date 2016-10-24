
package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApproveParams extends ApiGridParams
{
	@JsonProperty(value = "ApproverId")
	private String ApproverId;
	public String getApproverId()
	{
		return ApproverId;
	}
	public void setApproverId(String value)
	{
		ApproverId = value;
	}
	@JsonProperty(value = "Statuses")
	private String Statuses;
	public String getStatuses()
	{
		return Statuses;
	}
	public void setStatuses(String value)
	{
		Statuses = value;
	}
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
	public Boolean getIsApproved()
	{
		return IsApproved;
	}
	public void setIsApproved(Boolean value)
	{
		IsApproved = value;
	}
  //  public bool? IsDone { get; set; }
}

package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 7. 资金, class = Fund
 
*/
public class FundParams extends ApiGridParams
{
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	public String getCreatedId()
	{
		return CreatedId;
	}
	public void setCreatedId(String value)
	{
		CreatedId = value;
	}
	@JsonProperty(value = "CommodityIds")
	private String CommodityIds;
	public String getCommodityIds()
	{
		return CommodityIds;
	}
	public void setCommodityIds(String value)
	{
		CommodityIds = value;
	}
	@JsonProperty(value = "LegalIds")
	private String LegalIds;
	public String getLegalIds()
	{
		return LegalIds;
	}
	public void setLegalIds(String value)
	{
		LegalIds = value;
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
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	public String getCommodityId()
	{
		return CommodityId;
	}
	public void setCommodityId(String value)
	{
		CommodityId = value;
	}
	@JsonProperty(value = "LegalId")
	private String LegalId;
	public String getLegalId()
	{
		return LegalId;
	}
	public void setLegalId(String value)
	{
		LegalId = value;
	}
	@JsonProperty(value = "DC")
	private String DC;
	public String getDC()
	{
		return DC;
	}
	public void setDC(String value)
	{
		DC = value;
	}
	@JsonProperty(value = "StartDate")
	private java.util.Date StartDate;
	public java.util.Date getStartDate()
	{
		return StartDate;
	}
	public void setStartDate(java.util.Date value)
	{
		StartDate = value;
	}
	@JsonProperty(value = "EndDate")
	private java.util.Date EndDate;
	public java.util.Date getEndDate()
	{
		return EndDate;
	}
	public void setEndDate(java.util.Date value)
	{
		EndDate = value;
	}
}
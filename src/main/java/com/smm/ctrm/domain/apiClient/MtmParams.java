
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MtmParams extends ApiGridParams
{
	@JsonProperty(value = "Currency")
	private String Currency;
	
	public String getCurrency()
	{
		return Currency;
	}
	public void setCurrency(String value)
	{
		Currency = value;
	}
	@JsonProperty(value = "SpotType")
	private String SpotType;
	
	public String getSpotType()
	{
		return SpotType;
	}
	public void setSpotType(String value)
	{
		SpotType = value;
	}
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	
	public String getSpotDirection()
	{
		return SpotDirection;
	}
	public void setSpotDirection(String value)
	{
		SpotDirection = value;
	}
	@JsonProperty(value = "IsConfirm")
	private Boolean IsConfirm;
	
	public Boolean getIsConfirm()
	{
		return IsConfirm;
	}
	public void setIsConfirm(Boolean value)
	{
		IsConfirm = value;
	}
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
	@JsonProperty(value = "TraderId")
	private String TraderId;
	
	public String getTraderId()
	{
		return TraderId;
	}
	public void setTraderId(String value)
	{
		TraderId = value;
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
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	
	public Date getTradeDate()
	{
		return TradeDate;
	}
	public void setTradeDate(Date value)
	{
		TradeDate = value;
	}
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	
	public Date getStartDate()
	{
		return StartDate;
	}
	public void setStartDate(Date value)
	{
		StartDate = value;
	}
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	
	public Date getEndDate()
	{
		return EndDate;
	}
	public void setEndDate(Date value)
	{
		EndDate = value;
	}
	@JsonProperty(value = "InvoiceStartDate")
	private Date InvoiceStartDate;
	
	public Date getInvoiceStartDate()
	{
		return InvoiceStartDate;
	}
	public void setInvoiceStartDate(Date value)
	{
		InvoiceStartDate = value;
	}
	@JsonProperty(value = "InvoiceEndDate")
	private Date InvoiceEndDate;
	
	public Date getInvoiceEndDate()
	{
		return InvoiceEndDate;
	}

	public void setInvoiceEndDate(Date value)
	{
		InvoiceEndDate = value;
	}
	@JsonProperty(value = "InvoiceStatus")
	private String InvoiceStatus;
	
	public String getInvoiceStatus()
	{
		return InvoiceStatus;
	}
	public void setInvoiceStatus(String value)
	{
		InvoiceStatus = value;
	}
	@JsonProperty(value = "InvoiceCreatedId")
	private String InvoiceCreatedId;
	
	public String getInvoiceCreatedId()
	{
		return InvoiceCreatedId;
	}
	public void setInvoiceCreatedId(String value)
	{
		InvoiceCreatedId = value;
	}
	@JsonProperty(value = "AccountYear")
	private String AccountYear;
	
	public String getAccountYear()
	{
		return AccountYear;
	}
	public void setAccountYear(String value)
	{
		AccountYear = value;
	}
	@JsonProperty(value = "AccountMonth")
	private String AccountMonth;
	
	public String getAccountMonth()
	{
		return AccountMonth;
	}
	public void setAccountMonth(String value)
	{
		AccountMonth = value;
	}
}
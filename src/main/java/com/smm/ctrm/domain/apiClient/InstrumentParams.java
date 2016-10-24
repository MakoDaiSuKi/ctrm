
package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstrumentParams extends ApiGridParams
{
	@JsonProperty(value = "MarketId")
	private String MarketId;
	public String getMarketId()
	{
		return MarketId;
	}
	public void setMarketId(String value)
	{
		MarketId = value;
	}
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	public String getForwardType()
	{
		return ForwardType;
	}
	public void setForwardType(String value)
	{
		ForwardType = value;
	}
}
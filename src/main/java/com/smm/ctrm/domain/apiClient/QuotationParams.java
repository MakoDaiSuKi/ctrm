
package com.smm.ctrm.domain.apiClient;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Basis.Commodity;

/**
 * 市场价格 相关的类包括: (1) LME --- Pager (2) SFE --- Pager (3) Domestic --- Pager
 * 
 */
public class QuotationParams extends ApiGridParams {
	@JsonProperty(value = "MarketId")
	private String MarketId;

	
	public String getMarketId() {
		return MarketId;
	}

	public void setMarketId(String value) {
		MarketId = value;
	}

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
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

	/**
	 * 可用于控制只同步部分商品
	 * 
	 */
	private List<Commodity> LstCommodities;

	public List<Commodity> getLstCommodities() {
		return LstCommodities;
	}

	public void setLstCommodities(List<Commodity> value) {
		LstCommodities = value;
	}
}
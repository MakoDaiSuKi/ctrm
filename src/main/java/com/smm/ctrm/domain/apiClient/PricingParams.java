
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 4. 点价, class = pricing
 * 
 */
public class PricingParams extends ApiGridParams {
	@JsonProperty(value = "MajorType")
	private String MajorType;

	
	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String value) {
		MajorType = value;
	}

	@JsonProperty(value = "ContractId")
	private String ContractId;

	
	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String value) {
		ContractId = value;
	}

	@JsonProperty(value = "LotId")
	private String LotId;

	
	public String getLotId() {
		return LotId;
	}

	public void setLotId(String value) {
		LotId = value;
	}

	@JsonProperty(value = "PriceTiming")
	private String PriceTiming;

	
	public String getPriceTiming() {
		return PriceTiming;
	}

	public void setPriceTiming(String value) {
		PriceTiming = value;
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

	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;

	
	public Date getMajorStartDate() {
		return MajorStartDate;
	}

	public void setMajorStartDate(Date value) {
		MajorStartDate = value;
	}

	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;

	
	public Date getMajorEndDate() {
		return MajorEndDate;
	}

	public void setMajorEndDate(Date value) {
		MajorEndDate = value;
	}

	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;

	
	public Boolean getIsPriced() {
		return IsPriced;
	}

	public void setIsPriced(Boolean value) {
		IsPriced = value;
	}
}
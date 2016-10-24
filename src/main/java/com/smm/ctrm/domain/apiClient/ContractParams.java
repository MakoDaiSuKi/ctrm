
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

///#endregion

///#region 7 + 2类： 主要业务记录的构造参数
/**
 * 0. 合同台账, class = Contract
 * 
 */
public class ContractParams extends ApiGridParams {
	@JsonProperty(value = "Currency")
	private String Currency;

	
	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String value) {
		Currency = value;
	}

	@JsonProperty(value = "SpotType")
	private String SpotType;

	
	public String getSpotType() {
		return SpotType;
	}

	public void setSpotType(String value) {
		SpotType = value;
	}

	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;

	
	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String value) {
		SpotDirection = value;
	}

	@JsonProperty(value = "IsConfirm")
	private Boolean IsConfirm;

	
	public Boolean getIsConfirm() {
		return IsConfirm;
	}

	public void setIsConfirm(Boolean value) {
		IsConfirm = value;
	}

	@JsonProperty(value = "CreatedId")
	private String CreatedId;

	
	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String value) {
		CreatedId = value;
	}

	@JsonProperty(value = "TraderId")
	private String TraderId;

	
	public String getTraderId() {
		return TraderId;
	}

	public void setTraderId(String value) {
		TraderId = value;
	}

	@JsonProperty(value = "Statuses")
	private String Statuses;

	
	public String getStatuses() {
		return Statuses;
	}

	public void setStatuses(String value) {
		Statuses = value;
	}

	@JsonProperty(value = "LegalId")
	private String LegalId;

	
	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	@JsonProperty(value = "LegalIds")
	private String LegalIds;

	
	public String getLegalIds() {
		return LegalIds;
	}

	public void setLegalIds(String value) {
		LegalIds = value;
	}

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	
	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date value) {
		TradeDate = value;
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
	
	@JsonProperty(value = "TransactionType")
	private String TransactionType;


	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	
	
}
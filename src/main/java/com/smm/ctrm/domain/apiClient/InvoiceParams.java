
package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 6. 发票, class = Invoice
 * 
 */
public class InvoiceParams extends ApiGridParams {
	@JsonProperty(value = "CommodityIds")
	private String CommodityIds;

	
	public String getCommodityIds() {
		return CommodityIds;
	}

	public void setCommodityIds(String value) {
		CommodityIds = value;
	}

	@JsonProperty(value = "LotId")
	private String LotId;

	
	public String getLotId() {
		return LotId;
	}

	public void setLotId(String value) {
		LotId = value;
	}

	@JsonProperty(value = "ContractId")
	private String ContractId;

	
	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String value) {
		ContractId = value;
	}

	@JsonProperty(value = "PFA")
	private String PFA;

	
	public String getPFA() {
		return PFA;
	}

	public void setPFA(String value) {
		PFA = value;
	}

	@JsonProperty(value = "FeeCode")
	private String FeeCode;

	
	public String getFeeCode() {
		return FeeCode;
	}

	public void setFeeCode(String value) {
		FeeCode = value;
	}

	@JsonProperty(value = "LegalId")
	private String LegalId;

	
	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	@JsonProperty(value = "CustomerId")
	private String CustomerId;

	
	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String value) {
		CustomerId = value;
	}

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	@JsonProperty(value = "MT")
	private String MT;

	
	public String getMT() {
		return MT;
	}

	public void setMT(String value) {
		MT = value;
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

	@JsonProperty(value = "IsExecuted")
	private Boolean IsExecuted;

	
	public Boolean getIsExecuted() {
		return IsExecuted;
	}

	public void setIsExecuted(Boolean value) {
		IsExecuted = value;
	}

	@JsonProperty(value = "AdjustId")
	private String AdjustId;

	
	public String getAdjustId() {
		return AdjustId;
	}

	public void setAdjustId(String value) {
		AdjustId = value;
	}

	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;

	
	public Boolean getIsSettled() {
		return IsSettled;
	}

	public void setIsSettled(Boolean value) {
		IsSettled = value;
	}

	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;

	
	public Boolean getIsAccounted() {
		return IsAccounted;
	}

	public void setIsAccounted(Boolean value) {
		IsAccounted = value;
	}

	@JsonProperty(value = "IsAdjust")
	private Boolean IsAdjust;

	
	public Boolean getIsAdjust() {
		return IsAdjust;
	}

	public void setIsAdjust(Boolean value) {
		IsAdjust = value;
	}

	@JsonProperty(value = "IsContainSplitLotInvoice")
	private Boolean IsContainSplitLotInvoice;

	
	public Boolean getIsContainSplitLotInvoice() {
		return IsContainSplitLotInvoice;
	}

	public void setIsContainSplitLotInvoice(Boolean value) {
		IsContainSplitLotInvoice = value;
	}
	
	@JsonProperty(value = "IsNeedStorageAndNotice")
	private Boolean IsNeedStorageAndNotice;

	
	public Boolean getIsNeedStorageAndNotice() {
		return IsContainSplitLotInvoice;
	}

	public void setIsNeedStorageAndNotice(Boolean value) {
		IsContainSplitLotInvoice = value;
	}
}
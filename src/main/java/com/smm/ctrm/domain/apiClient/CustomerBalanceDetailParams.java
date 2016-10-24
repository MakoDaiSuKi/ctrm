
package com.smm.ctrm.domain.apiClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

///#endregion

///#region 报表

//客户明细对账表
public class CustomerBalanceDetailParams extends ApiGridParams {
	@JsonProperty(value = "CustomerId")
	private String CustomerId;

	
	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String value) {
		CustomerId = value;
	}

	@JsonProperty(value = "LegalId")
	private String LegalId;

	
	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	@JsonProperty(value = "CommodityIds")
	private String CommodityIds;

	public String getCommodityIds() {
		return CommodityIds;
	}

	public void setCommodityIds(String value) {
		CommodityIds = value;
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

	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;

	
	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(Boolean value) {
		IsInvoiced = value;
	}

	@JsonProperty(value = "MinBalance")
	private BigDecimal MinBalance;

	
	public BigDecimal getMinBalance() {
		return MinBalance;
	}

	public void setMinBalance(BigDecimal value) {
		MinBalance = value;
	}

	@JsonProperty(value = "MaxBalance")
	private BigDecimal MaxBalance;

	
	public BigDecimal getMaxBalance() {
		return MaxBalance;
	}

	public void setMaxBalance(BigDecimal value) {
		MaxBalance = value;
	}

	// 为了支持分页的几个参数，在api层不需要处理，由客户端单独处理
	@JsonProperty(value = "TotalRecords")
	private int TotalRecords;

	
	public int getTotalRecords() {
		return TotalRecords;
	}

	public void setTotalRecords(int value) {
		TotalRecords = value;
	}

	@JsonProperty(value = "TotalPages")
	private int TotalPages;

	
	public int getTotalPages() {
		return TotalPages;
	}

	public void setTotalPages(int value) {
		TotalPages = value;
	}
	
	@JsonProperty(value = "PermissionUserId")
	public List<String> PermissionUserId;
	public List<String> getPermissionUserId() {
		return PermissionUserId;
	}

	public void setPermissionUserId(List<String> permissionUserId) {
		PermissionUserId = permissionUserId;
	}
	
}
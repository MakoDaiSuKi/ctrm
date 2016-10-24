package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankReceiptParams extends ApiGridParams {
	
	@JsonProperty(value = "PageSize")
	 private int PageSize ;
	
	@JsonProperty(value = "PageIndex")
	private int PageIndex ;
	
	@JsonProperty(value = "SortBy")
	private  String SortBy;
	
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	
	@JsonProperty(value = "BankReceiptNo")
	private String BankReceiptNo;

	public int getPageSize() {
		return PageSize;
	}

	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}

	public int getPageIndex() {
		return PageIndex;
	}

	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}

	public String getSortBy() {
		return SortBy;
	}

	public void setSortBy(String sortBy) {
		SortBy = sortBy;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getBankReceiptNo() {
		return BankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		BankReceiptNo = bankReceiptNo;
	}

}

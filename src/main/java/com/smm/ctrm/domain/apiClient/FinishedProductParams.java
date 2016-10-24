package com.smm.ctrm.domain.apiClient;

import java.util.Date;

/**
 * @author zhaoyutao
 *
 */
public class FinishedProductParams extends ApiGridParams {
	/**
	 * 品种标识
	 */
	public String CommodityId;
	/**
	 * 开始日期
	 */
	public Date StartDate;
	/**
	 * 结束日期
	 */
	public Date EndDate;
	/**
	 * 
	 */
	public String LegalId;
	/**
	 * 
	 */
	public String CreatedBys;
	
	/**
	 * 状态
	 */
	public Boolean Status; 

	
	public Boolean getStatus() {
		return Status;
	}

	public void setStatus(Boolean status) {
		Status = status;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
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

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}

	public String getCreatedBys() {
		return CreatedBys;
	}

	public void setCreatedBys(String createdBys) {
		CreatedBys = createdBys;
	}
}

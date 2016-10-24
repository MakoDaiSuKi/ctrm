package com.smm.ctrm.domain.apiClient;

import java.util.Date;

/**
 * @author zhaoyutao
 *
 */
public class ReceiptParams extends ApiGridParams {

	/**
	 * 收发货标记 R=收货 S=发货
	 */
	private String Flag = "R";

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
	public String Status; 

	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
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

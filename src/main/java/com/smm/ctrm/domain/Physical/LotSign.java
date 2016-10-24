package com.smm.ctrm.domain.Physical;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * @author zhaoyutao
 * 2016年10月12日
 */
@Entity
@Table(schema="Physical", name="LotSign")
public class LotSign extends HibernateEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 批次id
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	
	/**
	 * 客户id
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	
	/**
	 * 客户id
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	/**
	 * 批次业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	
	/**
	 * 点价完成日期
	 */
	@JsonProperty(value = "IsPricedDate")
	private Date IsPricedDate;
	
	/**
	 * 交付完成日期
	 */
	@JsonProperty(value = "IsDeliveredDate")
	private Date IsDeliveredDate;
	
	/**
	 * 保值完成日期
	 */
	@JsonProperty(value = "IsHedgedDate")
	private Date IsHedgedDate;
	
	/**
	 * 收付款完成日期
	 */
	@JsonProperty(value = "IsFundedDate")
	private Date IsFundedDate;
	
	/**
	 * 开收票完成日期
	 */
	@JsonProperty(value = "IsInvoicedDate")
	private Date IsInvoicedDate;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	
	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}
	
	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public Date getIsPricedDate() {
		return IsPricedDate;
	}

	public void setIsPricedDate(Date isPricedDate) {
		IsPricedDate = isPricedDate;
	}

	public Date getIsDeliveredDate() {
		return IsDeliveredDate;
	}

	public void setIsDeliveredDate(Date isDeliveredDate) {
		IsDeliveredDate = isDeliveredDate;
	}

	public Date getIsHedgedDate() {
		return IsHedgedDate;
	}

	public void setIsHedgedDate(Date isHedgedDate) {
		IsHedgedDate = isHedgedDate;
	}

	public Date getIsFundedDate() {
		return IsFundedDate;
	}

	public void setIsFundedDate(Date isFundedDate) {
		IsFundedDate = isFundedDate;
	}

	public Date getIsInvoicedDate() {
		return IsInvoicedDate;
	}

	public void setIsInvoicedDate(Date isInvoicedDate) {
		IsInvoicedDate = isInvoicedDate;
	}
	
}

package com.smm.ctrm.domain.apiClient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LotSimpleForStorage {
	
	public LotSimpleForStorage(String id, String fullNo, String discharging, Date estimateSaleDate, String deliveryTerm) {
		super();
		LotId = id;
		FullNo = fullNo;
		Discharging = discharging;
		EstimateSaleDate = estimateSaleDate;
		DeliveryTerm = deliveryTerm;
	}
	
	private String LotId;

	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	/**
	 * 卸货地点
	 */
	@JsonProperty(value = "Discharging")
	private String Discharging;
	
	/**
	 * 预计销售日期（QP）
	 */
	@JsonProperty(value = "EstimateSaleDate")
	private Date EstimateSaleDate;
	
	/**
	 *
	 */
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public String getDischarging() {
		return Discharging;
	}

	public void setDischarging(String discharging) {
		Discharging = discharging;
	}

	public Date getEstimateSaleDate() {
		return EstimateSaleDate;
	}

	public void setEstimateSaleDate(Date estimateSaleDate) {
		EstimateSaleDate = estimateSaleDate;
	}

	public String getDeliveryTerm() {
		return DeliveryTerm;
	}

	public void setDeliveryTerm(String deliveryTerm) {
		DeliveryTerm = deliveryTerm;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}
}

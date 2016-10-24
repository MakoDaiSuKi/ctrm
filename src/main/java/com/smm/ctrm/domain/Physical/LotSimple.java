package com.smm.ctrm.domain.Physical;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Lot", schema = "Physical")
public class LotSimple extends HibernateEntity {
	private static final long serialVersionUID = 1461832991343L;

	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@Column(name = "FullNo")
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	/**
	 * 卸货地点
	 */
	@Column(name = "Discharging", length = 64)
	@JsonProperty(value = "Discharging")
	private String Discharging;
	
	/**
	 * 预计销售日期（QP）
	 */
	@Column(name = "EstimateSaleDate")
	@JsonProperty(value = "EstimateSaleDate")
	private Date EstimateSaleDate;
	/**
	 *
	 */
	@Column(name = "DeliveryTerm")
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
}

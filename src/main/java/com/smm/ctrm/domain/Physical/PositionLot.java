package com.smm.ctrm.domain.Physical;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(schema="Physical", name="PositionLot")
public class PositionLot extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4802465508383940380L;
	
	/**
	 * 头寸Id
	 */
	@JsonProperty("PositionId")
	private String PositionId;
	
	/**
	 * 套利类型
	 */
	@JsonProperty(value = "ArbitrageType")
	public String ArbitrageType;
	
	/**
	 * 套利关联Id,批次Id或头寸Id
	 */
	@JsonProperty(value = "ArbitrageRefId")
	private String ArbitrageRefId;

	public String getPositionId() {
		return PositionId;
	}

	public void setPositionId(String positionId) {
		PositionId = positionId;
	}

	public String getArbitrageType() {
		return ArbitrageType;
	}

	public void setArbitrageType(String arbitrageType) {
		ArbitrageType = arbitrageType;
	}

	public String getArbitrageRefId() {
		return ArbitrageRefId;
	}

	public void setArbitrageRefId(String arbitrageRefId) {
		ArbitrageRefId = arbitrageRefId;
	}
	
}

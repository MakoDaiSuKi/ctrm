package com.smm.ctrm.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiLot4Postion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4408840339806067399L;

	@JsonProperty(value = "LotId")
	public String LotId;
	
	@JsonProperty(value = "QuantityHedge")
	public BigDecimal QuantityHedge;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public BigDecimal getQuantityHedge() {
		return QuantityHedge;
	}

	public void setQuantityHedge(BigDecimal quantityHedge) {
		QuantityHedge = quantityHedge;
	}
}

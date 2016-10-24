package com.smm.ctrm.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Physical.Position;

public class CpPosition4AllocateToMultiLot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4491644948818804709L;

	@JsonProperty(value = "Lots")
	public List<MultiLot4Postion> Lots;

	@JsonProperty(value = "Positions4Allocate")
	public List<Position> Positions4Allocate;

	public List<MultiLot4Postion> getLots() {
		return Lots;
	}

	public void setLots(List<MultiLot4Postion> lots) {
		Lots = lots;
	}

	public List<Position> getPositions4Allocate() {
		return Positions4Allocate;
	}

	public void setPositions4Allocate(List<Position> positions4Allocate) {
		Positions4Allocate = positions4Allocate;
	}

}

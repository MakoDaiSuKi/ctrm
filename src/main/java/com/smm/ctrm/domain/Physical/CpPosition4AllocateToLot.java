
package com.smm.ctrm.domain.Physical;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CpPosition4AllocateToLot {
	@JsonProperty(value = "LotId")
	private String LotId;
	@JsonProperty(value = "Positions4Allocate")
	private List<Position> Positions4Allocate;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public List<Position> getPositions4Allocate() {
		return Positions4Allocate;
	}

	public void setPositions4Allocate(List<Position> positions4Allocate) {
		Positions4Allocate = positions4Allocate;
	}

}
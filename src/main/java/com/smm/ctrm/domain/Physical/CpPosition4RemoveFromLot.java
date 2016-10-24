
package com.smm.ctrm.domain.Physical;

import java.util.List;

public class CpPosition4RemoveFromLot {
	private String LotId;

	private List<Position> Positions4Remove;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public List<Position> getPositions4Remove() {
		return Positions4Remove;
	}

	public void setPositions4Remove(List<Position> positions4Remove) {
		Positions4Remove = positions4Remove;
	}

}
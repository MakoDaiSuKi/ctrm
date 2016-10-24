
package com.smm.ctrm.domain.Physical;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Param4LotPnL {

	/**
	 * 
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	
	/**
	 * 
	 */
	@JsonProperty(value = "Positions")
	private List<Position> Positions;

	/**
	 * 头寸是否需要对齐结算
	 */
	@JsonProperty(value = "IsPositionSquare")
	private Boolean IsPositionSquare;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> positions) {
		Positions = positions;
	}

	public Boolean getIsPositionSquare() {
		return IsPositionSquare;
	}

	public void setIsPositionSquare(Boolean isPositionSquare) {
		IsPositionSquare = isPositionSquare;
	}

}
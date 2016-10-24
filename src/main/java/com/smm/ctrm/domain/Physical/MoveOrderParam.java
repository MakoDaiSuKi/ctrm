package com.smm.ctrm.domain.Physical;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveOrderParam {
	
	@Transient
	@JsonProperty(value = "MoveOrder")
	private MoveOrder MoveOrder;
	
	public MoveOrder getMoveOrder() {
		return MoveOrder;
	}

	public void setMoveOrder(MoveOrder moveOrder) {
		MoveOrder = moveOrder;
	}

	public Storage getStorage() {
		return Storage;
	}

	public void setStorage(Storage storage) {
		Storage = storage;
	}

	@Transient
	@JsonProperty(value = "Storage")
	private Storage Storage;
}

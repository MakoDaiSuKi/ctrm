package com.smm.ctrm.domain.apiClient;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Storage;

public class CpContract implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2651027053870249599L;

	@JsonProperty(value = "CurContract")
	private Contract CurContract;

	@JsonProperty(value = "CurLot")
	private Lot CurLot;

	@JsonProperty(value = "CurStorage")
	private Storage CurStorage;

	public Contract getCurContract() {
		return CurContract;
	}

	public void setCurContract(Contract curContract) {
		CurContract = curContract;
	}

	public Lot getCurLot() {
		return CurLot;
	}

	public void setCurLot(Lot curLot) {
		CurLot = curLot;
	}

	public Storage getCurStorage() {
		return CurStorage;
	}

	public void setCurStorage(Storage curStorage) {
		CurStorage = curStorage;
	}

}

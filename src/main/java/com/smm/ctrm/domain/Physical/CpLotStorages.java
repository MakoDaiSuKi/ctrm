
package com.smm.ctrm.domain.Physical;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yutao.zhao 给指定的批次发货，一次可以发多个
 */
public class CpLotStorages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1087130603285467632L;

	@JsonProperty
	private String LotId;
	@JsonProperty
	private List<Storage> Storages;

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public List<Storage> getStorages() {
		return Storages;
	}

	public void setStorages(List<Storage> storages) {
		Storages = storages;
	}
}

package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NoteParams extends ApiGridParams {
	@JsonProperty(value = "LotId")
	private String LotId;

	
	public String getLotId() {
		return LotId;
	}

	public void setLotId(String value) {
		LotId = value;
	}
}
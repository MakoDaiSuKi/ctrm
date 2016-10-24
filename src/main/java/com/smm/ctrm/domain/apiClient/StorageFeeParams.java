package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 仓储费用
 * 
 */
public class StorageFeeParams extends ApiGridParams {

	@JsonProperty(value = "LegalIds")
	private String LegalIds;

	public String getLegalIds() {
		return LegalIds;
	}

	public void setLegalIds(String legalIds) {
		LegalIds = legalIds;
	}

	public String getWarehouseIds() {
		return WarehouseIds;
	}

	public void setWarehouseIds(String warehouseIds) {
		WarehouseIds = warehouseIds;
	}

	@JsonProperty(value = "WarehouseIds")
	private String WarehouseIds;
}

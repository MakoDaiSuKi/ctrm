
package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HedgeNumberParams extends ApiGridParams {

	@JsonProperty(value = "ContractCategory")
	private String ContractCategory;

	public String getContractCategory() {
		return ContractCategory;
	}

	public void setContractCategory(String contractCategory) {
		ContractCategory = contractCategory;
	}


}

package com.smm.ctrm.domain.Physical;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yutao.zhao 合并bvi的商品，一次可以合并多个
 */
public class CpMergeStorages {
	@JsonProperty
	private Storage Merge;
	@JsonProperty
	private List<Storage> MergedStorages;

	public Storage getMerge() {
		return Merge;
	}

	public void setMerge(Storage merge) {
		Merge = merge;
	}

	public List<Storage> getMergedStorages() {
		return MergedStorages;
	}

	public void setMergedStorages(List<Storage> mergedStorages) {
		MergedStorages = mergedStorages;
	}

}
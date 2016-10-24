package com.smm.ctrm.dto.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RectangleTree {
	
	@JsonProperty("count")
	private String count;
	
	@JsonProperty("chidren")
	private List<RectangleTree> chidren;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<RectangleTree> getChidren() {
		return chidren;
	}

	public void setChidren(List<RectangleTree> chidren) {
		this.chidren = chidren;
	}
}

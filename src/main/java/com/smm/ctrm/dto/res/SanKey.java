package com.smm.ctrm.dto.res;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SanKey {

	@JsonProperty("nodes")
	private List<SanKeyNode> nodes;

	@JsonProperty("links")
	private List<SanKeyLink> links;

	class SanKeyNode {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	class SanKeyLink {
		private String source;
		private String target;
		private BigDecimal value;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

	}

	public List<SanKeyNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<SanKeyNode> nodes) {
		this.nodes = nodes;
	}

	public List<SanKeyLink> getLinks() {
		return links;
	}

	public void setLinks(List<SanKeyLink> links) {
		this.links = links;
	}
}

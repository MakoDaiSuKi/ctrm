package com.smm.ctrm.dto.res;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CalculateDeliveryedQuantityDto {
	/**
	 * 合同数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 实际的交付数量
	 */
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	/**
	 * 约定交付的最低数量
	 */
	@JsonProperty(value = "QuantityLess")
	private BigDecimal QuantityLess;
	
	/**
	 * 约定交付的最多数量
	 */
	@JsonProperty(value = "QuantityMore")
	private BigDecimal QuantityMore;

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}

	public BigDecimal getQuantityLess() {
		return QuantityLess;
	}

	public void setQuantityLess(BigDecimal quantityLess) {
		QuantityLess = quantityLess;
	}

	public BigDecimal getQuantityMore() {
		return QuantityMore;
	}

	public void setQuantityMore(BigDecimal quantityMore) {
		QuantityMore = quantityMore;
	}
	
	
}

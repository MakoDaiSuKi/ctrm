
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

public class CpSplitPosition4Broker {

	private BigDecimal QuantitySplitted;

	private Position4Broker OriginalPosition;

	public BigDecimal getQuantitySplitted() {
		return QuantitySplitted;
	}

	public void setQuantitySplitted(BigDecimal quantitySplitted) {
		QuantitySplitted = quantitySplitted;
	}

	public Position4Broker getOriginalPosition() {
		return OriginalPosition;
	}

	public void setOriginalPosition(Position4Broker originalPosition) {
		OriginalPosition = originalPosition;
	}
}
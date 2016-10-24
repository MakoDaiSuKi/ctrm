
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

public class CpSplitPosition {

	private BigDecimal QuantitySplitted;

	private Position OriginalPosition;

	public BigDecimal getQuantitySplitted() {
		return QuantitySplitted;
	}

	public void setQuantitySplitted(BigDecimal quantitySplitted) {
		QuantitySplitted = quantitySplitted;
	}

	public Position getOriginalPosition() {
		return OriginalPosition;
	}

	public void setOriginalPosition(Position originalPosition) {
		OriginalPosition = originalPosition;
	}
}
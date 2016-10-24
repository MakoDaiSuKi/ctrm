
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

public class CpSplitPricing {

	private BigDecimal QuantitySplitted;

	private Pricing OriginalPricing;

	public BigDecimal getQuantitySplitted() {
		return QuantitySplitted;
	}

	public void setQuantitySplitted(BigDecimal quantitySplitted) {
		QuantitySplitted = quantitySplitted;
	}

	public Pricing getOriginalPricing() {
		return OriginalPricing;
	}

	public void setOriginalPricing(Pricing originalPricing) {
		OriginalPricing = originalPricing;
	}
}
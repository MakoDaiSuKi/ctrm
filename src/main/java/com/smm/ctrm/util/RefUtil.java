package com.smm.ctrm.util;

import java.math.BigDecimal;

public class RefUtil {

	private Integer total = 0;

	private BigDecimal QuantityPriced;

	private BigDecimal QuantityHedged;

	public RefUtil() {
		this.setQuantityHedged(BigDecimal.ZERO);
		this.setQuantityPriced(BigDecimal.ZERO);
	}

	public RefUtil(int total) {
		this.total = total;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {

		if (total < 0)
			total = 0;

		this.total = total;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal quantityPriced) {
		QuantityPriced = quantityPriced;
	}

	public BigDecimal getQuantityHedged() {
		return QuantityHedged;
	}

	public void setQuantityHedged(BigDecimal quantityHedged) {
		QuantityHedged = quantityHedged;
	}

	@Override
	public String toString() {
		return "RefUtil [total=" + total + "]";
	}
}

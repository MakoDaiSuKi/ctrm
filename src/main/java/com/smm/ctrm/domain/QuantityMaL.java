package com.smm.ctrm.domain;

import java.math.BigDecimal;

public class QuantityMaL {

	public BigDecimal quantity;
	public BigDecimal quantityDeliveryed;
	public BigDecimal quantityMore;
	public BigDecimal quantityLess;
	public String moreOrLessBasis;
	public BigDecimal moreOrLess;

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getQuantityDeliveryed() {
		return quantityDeliveryed;
	}

	public void setQuantityDeliveryed(BigDecimal quantityDeliveryed) {
		this.quantityDeliveryed = quantityDeliveryed;
	}

	public BigDecimal getQuantityMore() {
		return quantityMore;
	}

	public void setQuantityMore(BigDecimal quantityMore) {
		this.quantityMore = quantityMore;
	}

	public BigDecimal getQuantityLess() {
		return quantityLess;
	}

	public void setQuantityLess(BigDecimal quantityLess) {
		this.quantityLess = quantityLess;
	}

	public String getMoreOrLessBasis() {
		return moreOrLessBasis;
	}

	public void setMoreOrLessBasis(String moreOrLessBasis) {
		this.moreOrLessBasis = moreOrLessBasis;
	}

	public BigDecimal getMoreOrLess() {
		return moreOrLess;
	}

	public void setMoreOrLess(BigDecimal moreOrLess) {
		this.moreOrLess = moreOrLess;
	}
}

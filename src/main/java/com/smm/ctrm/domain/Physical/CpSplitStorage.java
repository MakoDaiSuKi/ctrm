
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

public class CpSplitStorage {
	private Integer BundlesSplitted;
	private BigDecimal QuantitySplitted;
	private BigDecimal QuantityInvoicedSplitted;
	private BigDecimal GrossSplitted;
	private BigDecimal GrossAtFactorySplitted;
	private BigDecimal QuantityAtWarehouseSplitted;
	private Storage OriginalStorage;

	public Integer getBundlesSplitted() {
		return BundlesSplitted;
	}

	public void setBundlesSplitted(Integer bundlesSplitted) {
		BundlesSplitted = bundlesSplitted;
	}

	public BigDecimal getQuantitySplitted() {
		return QuantitySplitted;
	}

	public void setQuantitySplitted(BigDecimal quantitySplitted) {
		QuantitySplitted = quantitySplitted;
	}

	public BigDecimal getQuantityInvoicedSplitted() {
		return QuantityInvoicedSplitted;
	}

	public void setQuantityInvoicedSplitted(BigDecimal quantityInvoicedSplitted) {
		QuantityInvoicedSplitted = quantityInvoicedSplitted;
	}

	public BigDecimal getGrossSplitted() {
		return GrossSplitted;
	}

	public void setGrossSplitted(BigDecimal grossSplitted) {
		GrossSplitted = grossSplitted;
	}

	public BigDecimal getGrossAtFactorySplitted() {
		return GrossAtFactorySplitted;
	}

	public void setGrossAtFactorySplitted(BigDecimal grossAtFactorySplitted) {
		GrossAtFactorySplitted = grossAtFactorySplitted;
	}

	public Storage getOriginalStorage() {
		return OriginalStorage;
	}

	public void setOriginalStorage(Storage originalStorage) {
		OriginalStorage = originalStorage;
	}

	public BigDecimal getQuantityAtWarehouseSplitted() {
		return QuantityAtWarehouseSplitted;
	}

	public void setQuantityAtWarehouseSplitted(BigDecimal quantityAtWarehouseSplitted) {
		QuantityAtWarehouseSplitted = quantityAtWarehouseSplitted;
	}

}
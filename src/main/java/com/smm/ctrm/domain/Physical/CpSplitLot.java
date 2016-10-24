
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

public class CpSplitLot {
	private BigDecimal QuantitySplitted;
	private Integer LotNo;
	private String DocumentNo;
	private Lot OriginalLot;

	public BigDecimal getQuantitySplitted() {
		return QuantitySplitted;
	}

	public void setQuantitySplitted(BigDecimal quantitySplitted) {
		QuantitySplitted = quantitySplitted;
	}

	public Integer getLotNo() {
		return LotNo;
	}

	public void setLotNo(Integer lotNo) {
		LotNo = lotNo;
	}

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String documentNo) {
		DocumentNo = documentNo;
	}

	public Lot getOriginalLot() {
		return OriginalLot;
	}

	public void setOriginalLot(Lot originalLot) {
		OriginalLot = originalLot;
	}

}

package com.smm.ctrm.domain.Physical;

import java.util.List;

public class Param4InvoicePnL {
	private String InvoiceId;

	private List<Position> Positions;

	private List<Position> NewPostions;

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		InvoiceId = invoiceId;
	}

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> positions) {
		Positions = positions;
	}

	public List<Position> getNewPostions() {
		return NewPostions;
	}

	public void setNewPostions(List<Position> newPostions) {
		NewPostions = newPostions;
	}

}
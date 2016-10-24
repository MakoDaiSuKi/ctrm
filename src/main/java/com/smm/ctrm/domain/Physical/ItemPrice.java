package com.smm.ctrm.domain.Physical;

import java.io.Serializable;

public class ItemPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779262976342404675L;
	private String Item;
	private String Unit;
	private String Price;
	private String Currency;

	public String getItem() {
		return Item;
	}

	public void setItem(String item) {
		Item = item;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}
}

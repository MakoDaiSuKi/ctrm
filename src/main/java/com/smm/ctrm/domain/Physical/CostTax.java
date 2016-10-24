package com.smm.ctrm.domain.Physical;

import java.io.Serializable;

/**
 * 交税成本
 **/
public class CostTax implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7114667008518009926L;
	/**
	 * 交税类型
	 **/
	private String Type;
	/**
	 * 交税单价 元/干吨
	 **/
	private String Price;
	private String Currency;
	/**
	 * 单位
	 **/
	private String Unit;
	/**
	 * 合计
	 **/
	private String Total;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
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

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}
}

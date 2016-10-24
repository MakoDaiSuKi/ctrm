package com.smm.ctrm.domain.Physical;

import java.io.Serializable;

/**
 * @author zhaoyutao 货物成分成本核算
 */
public class CostComponent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9042597190351405118L;
	/**
	 * 成分
	 **/
	private String Item;
	/**
	 * 含量
	 **/
	private String Calories;
	/**
	 * 扣减
	 **/
	private String Deduction;
	private String Currency;
	/**
	 * 单价
	 **/
	private String Price;
	/**
	 * 单位
	 **/
	private String Unit;
	/**
	 * 加工费
	 **/
	private String TC;
	/**
	 * 精炼加工费
	 **/
	private String RC;
	/**
	 * 干重
	 **/
	private String DryWeight;
	/**
	 * 湿重
	 **/
	private String WetWeight;

	public String getItem() {
		return Item;
	}

	public void setItem(String item) {
		Item = item;
	}

	public String getCalories() {
		return Calories;
	}

	public void setCalories(String calories) {
		Calories = calories;
	}

	public String getDeduction() {
		return Deduction;
	}

	public void setDeduction(String deduction) {
		Deduction = deduction;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getTC() {
		return TC;
	}

	public void setTC(String tC) {
		TC = tC;
	}

	public String getRC() {
		return RC;
	}

	public void setRC(String rC) {
		RC = rC;
	}

	public String getDryWeight() {
		return DryWeight;
	}

	public void setDryWeight(String dryWeight) {
		DryWeight = dryWeight;
	}

	public String getWetWeight() {
		return WetWeight;
	}

	public void setWetWeight(String wetWeight) {
		WetWeight = wetWeight;
	}
}

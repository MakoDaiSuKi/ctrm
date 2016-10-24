package com.smm.ctrm.domain.Physical;

import java.io.Serializable;

/**
 * @author zhaoyutao
 *
 */
public class MaterielArrivalPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -772217932784106078L;
	/**
	 * 到货日期
	 */
	private String ArrivalDate;
	/**
	 * 到货数量
	 */
	private String ArrivalNumber;
	/**
	 * 数量单位
	 */
	private String Unit;

	public String getArrivalDate() {
		return ArrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		ArrivalDate = arrivalDate;
	}

	public String getArrivalNumber() {
		return ArrivalNumber;
	}

	public void setArrivalNumber(String arrivalNumber) {
		ArrivalNumber = arrivalNumber;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

}

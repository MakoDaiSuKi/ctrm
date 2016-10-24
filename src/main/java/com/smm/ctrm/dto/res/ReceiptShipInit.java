package com.smm.ctrm.dto.res;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReceiptShipInit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3560182553234702255L;

	/**
	 * 收货单号，格式  yyyyMMxxxx
	 */
	private String no;
	
	/**
	 * 已收货重量
	 */
	private BigDecimal weight;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
}

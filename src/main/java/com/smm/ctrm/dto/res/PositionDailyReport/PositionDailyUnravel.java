package com.smm.ctrm.dto.res.PositionDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyUnravel extends PositionDailyBase {
	
//	public PositionDailyUnravel(){}
	
	public PositionDailyUnravel(String positionBrokerId, String marketName, String forwardType, String purpose, String commodityId, String commodityName, String instrumentName,
			String lS, String oCS, Date promptDate, BigDecimal hands, BigDecimal quantity, BigDecimal ourPrice,
			String currency, BigDecimal unravelQuantity, BigDecimal unravelPrice) {
		super(positionBrokerId, marketName, forwardType, purpose, commodityId, commodityName, instrumentName, lS, oCS, promptDate, hands, quantity,
				ourPrice, currency);
		UnravelQuantity = unravelQuantity;
		UnravelPrice = unravelPrice;
	}

	/**
	 * 平仓数量
	 */
	@JsonProperty(value = "UnravelQuantity")
	private BigDecimal UnravelQuantity;
	
	/**
	 * 平仓头寸价格
	 */
	@JsonProperty(value = "UnravelPrice")
	private BigDecimal UnravelPrice;


	public BigDecimal getUnravelPrice() {
		return UnravelPrice;
	}

	public void setUnravelPrice(BigDecimal unravelPrice) {
		UnravelPrice = unravelPrice;
	}

	public BigDecimal getUnravelQuantity() {
		return UnravelQuantity;
	}

	public void setUnravelQuantity(BigDecimal unravelQuantity) {
		UnravelQuantity = unravelQuantity;
	}

}

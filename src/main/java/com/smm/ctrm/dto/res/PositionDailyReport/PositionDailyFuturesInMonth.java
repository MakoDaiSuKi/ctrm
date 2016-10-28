package com.smm.ctrm.dto.res.PositionDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyFuturesInMonth extends PositionDailyBase {
	
	public PositionDailyFuturesInMonth(){}
	
	public PositionDailyFuturesInMonth(String positionBrokerId, String marketName, String forwardType, String purpose, String commodityId, String commodityName,
			String instrumentName, String lS, String oCS, Date promptDate, BigDecimal hands, BigDecimal quantity,
			BigDecimal ourPrice, String currency, BigDecimal changeMonthQuantity, BigDecimal changeMonthPrice) {
		super(positionBrokerId, marketName, forwardType, purpose, commodityId, commodityName, instrumentName, lS, oCS, promptDate, hands, quantity,
				ourPrice, currency);
		ChangeMonthQuantity = changeMonthQuantity;
		ChangeMonthPrice = changeMonthPrice;
	}

	/**
	 * 换月数量
	 */
	@JsonProperty(value = "ChangeMonthQuantity")
	private BigDecimal ChangeMonthQuantity;

	/**
	 * 换月头寸价格
	 */
	@JsonProperty(value = "ChangeMonthPrice")
	private BigDecimal ChangeMonthPrice;

	public BigDecimal getChangeMonthQuantity() {
		return ChangeMonthQuantity;
	}

	public void setChangeMonthQuantity(BigDecimal changeMonthQuantity) {
		ChangeMonthQuantity = changeMonthQuantity;
	}

	public BigDecimal getChangeMonthPrice() {
		return ChangeMonthPrice;
	}

	public void setChangeMonthPrice(BigDecimal changeMonthPrice) {
		ChangeMonthPrice = changeMonthPrice;
	}


}

package com.smm.ctrm.dto.res.PositionDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyDelivery extends PositionDailyBase{
	
	public PositionDailyDelivery(){}

	public PositionDailyDelivery(String positionBrokerId, String marketName, String forwardType, String purpose, String commodityId, String commodityName, String instrumentName,
			String lS, String oCS, Date promptDate, BigDecimal hands, BigDecimal quantity, BigDecimal ourPrice,
			String currency, BigDecimal deliveryQuantity, BigDecimal deliveryPrice) {
		super(positionBrokerId, marketName, forwardType, purpose, commodityId, commodityName, instrumentName, lS, oCS, promptDate, hands, quantity,
				ourPrice, currency);
		DeliveryQuantity = deliveryQuantity;
		DeliveryPrice = deliveryPrice;
	}

	/**
	 * 交割数量
	 */
	@JsonProperty(value = "DeliveryQuantity")
	private BigDecimal DeliveryQuantity;

	/**
	 * 交割价格
	 */
	@JsonProperty(value = "DeliveryPrice")
	private BigDecimal DeliveryPrice;
	
	public BigDecimal getDeliveryQuantity() {
		return DeliveryQuantity;
	}

	public void setDeliveryQuantity(BigDecimal deliveryQuantity) {
		DeliveryQuantity = deliveryQuantity;
	}

	public BigDecimal getDeliveryPrice() {
		return DeliveryPrice;
	}

	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		DeliveryPrice = deliveryPrice;
	}
}

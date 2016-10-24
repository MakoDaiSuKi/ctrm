package com.smm.ctrm.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LotTag {
	
	public LotTag(Boolean isPriced, Boolean isDelivered, Boolean isHedged, Boolean isFunded, Boolean isInvoiced) {
		super();
		IsPriced = isPriced;
		IsDelivered = isDelivered;
		IsHedged = isHedged;
		IsFunded = isFunded;
		IsInvoiced = isInvoiced;
	}
	/**
	 * 点价的标识
	 */
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	
	/**
	 * 收发货的标识
	 */
	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;
	/**
	 * 保值的标识。如果是FALSE，表示存在敞口。
	 */
	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;
	/**
	 * 收付款的标识
	 */
	@JsonProperty(value = "IsFunded")
	private Boolean IsFunded;
	/**
	 * 收或者开，发票的标识
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	public Boolean getIsPriced() {
		return IsPriced;
	}
	public void setIsPriced(Boolean isPriced) {
		IsPriced = isPriced;
	}
	public Boolean getIsDelivered() {
		return IsDelivered;
	}
	public void setIsDelivered(Boolean isDelivered) {
		IsDelivered = isDelivered;
	}
	public Boolean getIsHedged() {
		return IsHedged;
	}
	public void setIsHedged(Boolean isHedged) {
		IsHedged = isHedged;
	}
	public Boolean getIsFunded() {
		return IsFunded;
	}
	public void setIsFunded(Boolean isFunded) {
		IsFunded = isFunded;
	}
	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean isInvoiced) {
		IsInvoiced = isInvoiced;
	}
	
}


package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yutao.zhao 调期头寸
 */
public class CarryPosition {

	@Transient
	@JsonProperty(value = "CarryCounterPart")
	private String CarryCounterPart;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CarryRef")
	private String CarryRef;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PromptDate4Long")
	private Date PromptDate4Long;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PromptDate4Short")
	private Date PromptDate4Short;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Price4Long")
	private BigDecimal Price4Long;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Price4Short")
	private BigDecimal Price4Short;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Spread")
	private BigDecimal Spread;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Long")
	private Position Long;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Short")
	private Position Short;

	public String getCarryCounterPart() {
		return CarryCounterPart;
	}

	public void setCarryCounterPart(String CarryCounterPart) {
		this.CarryCounterPart = CarryCounterPart;
	}

	public String getCarryRef() {
		return CarryRef;
	}

	public void setCarryRef(String CarryRef) {
		this.CarryRef = CarryRef;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public Date getPromptDate4Long() {
		return PromptDate4Long;
	}

	public void setPromptDate4Long(Date PromptDate4Long) {
		this.PromptDate4Long = PromptDate4Long;
	}

	public Date getPromptDate4Short() {
		return PromptDate4Short;
	}

	public void setPromptDate4Short(Date PromptDate4Short) {
		this.PromptDate4Short = PromptDate4Short;
	}

	public BigDecimal getPrice4Long() {
		return Price4Long;
	}

	public void setPrice4Long(BigDecimal Price4Long) {
		this.Price4Long = Price4Long;
	}

	public BigDecimal getPrice4Short() {
		return Price4Short;
	}

	public void setPrice4Short(BigDecimal Price4Short) {
		this.Price4Short = Price4Short;
	}

	public BigDecimal getSpread() {
		return Spread;
	}

	public void setSpread(BigDecimal Spread) {
		this.Spread = Spread;
	}

	public Position getLong() {
		return Long;
	}

	public void setLong(Position Long) {
		this.Long = Long;
	}

	public Position getShort() {
		return Short;
	}

	public void setShort(Position Short) {
		this.Short = Short;
	}
}
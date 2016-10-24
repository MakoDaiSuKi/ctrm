
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VmContractLot4Combox {
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Id")
	private String Id;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LegalName")
	private String LegalName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
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
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerShortName")
	private String CustomerShortName;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String LegalName) {
		this.LegalName = LegalName;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getHeadNo() {
		return HeadNo;
	}

	public void setHeadNo(String HeadNo) {
		this.HeadNo = HeadNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCustomerShortName() {
		return CustomerShortName;
	}

	public void setCustomerShortName(String CustomerShortName) {
		this.CustomerShortName = CustomerShortName;
	}
}
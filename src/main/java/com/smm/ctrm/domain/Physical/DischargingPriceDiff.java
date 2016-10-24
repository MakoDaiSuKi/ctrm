
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "DischargingPriceDiff", schema = "Physical")

public class DischargingPriceDiff extends HibernateEntity {
	private static final long serialVersionUID = 1461832991341L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ContractHeadNo")
	private String ContractHeadNo;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 到达地点
	 */
	@Column(name = "Discharging", length = 64)
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 * 与上海价差
	 */
	@Column(name = "PriceDiff")
	@JsonProperty(value = "PriceDiff")
	private BigDecimal PriceDiff;
	/**
	 *
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	// @JsonBackReference("DischargingPriceDiff_Contract")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	@JoinColumn(name = "ContractId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Contract")
	private Contract Contract;

	public String getContractHeadNo() {
		return ContractHeadNo;
	}

	public void setContractHeadNo(String ContractHeadNo) {
		this.ContractHeadNo = ContractHeadNo;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getDischarging() {
		return Discharging;
	}

	public void setDischarging(String Discharging) {
		this.Discharging = Discharging;
	}

	public BigDecimal getPriceDiff() {
		return PriceDiff;
	}

	public void setPriceDiff(BigDecimal PriceDiff) {
		this.PriceDiff = PriceDiff;
	}

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String ContractId) {
		this.ContractId = ContractId;
	}

	public Contract getContract() {
		return Contract;
	}

	public void setContract(Contract Contract) {
		this.Contract = Contract;
	}

}
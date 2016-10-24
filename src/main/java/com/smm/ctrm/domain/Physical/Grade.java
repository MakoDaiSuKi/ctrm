
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
import com.smm.ctrm.domain.Basis.Spec;

@Entity
@Table(name = "Grade", schema = "Physical")

public class Grade extends HibernateEntity {
	private static final long serialVersionUID = 1461832991341L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ContractHeadNo")
	private String ContractHeadNo;
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
	@JsonProperty(value = "SpecName")
	private String SpecName;
	/**
	 * 金属含量
	 */
	@Column(name = "Content")
	@JsonProperty(value = "Content")
	private String Content;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 卸货地点
	 */
	@Column(name = "Discharging", length = 64)
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 * Incoterm -1
	 */
	@Column(name = "FCA")
	@JsonProperty(value = "FCA")
	private BigDecimal FCA;
	/**
	 * Incoterm -2
	 */
	@Column(name = "EXW")
	@JsonProperty(value = "EXW")
	private BigDecimal EXW;
	/**
	 * Incoterm -3
	 */
	@Column(name = "FOB")
	@JsonProperty(value = "FOB")
	private BigDecimal FOB;
	/**
	 * Incoterm -4
	 */
	@Column(name = "CIF")
	@JsonProperty(value = "CIF")
	private BigDecimal CIF;
	/**
	 *
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	@JoinColumn(name = "ContractId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Contract")
	private Contract Contract;
	/**
	 * 多对一：规格
	 */
	@Column(name = "SpecId")
	@JsonProperty(value = "SpecId")
	private String SpecId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Spec.class)
	@JoinColumn(name = "SpecId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Spec")
	private Spec Spec;

	public String getContractHeadNo() {
		return ContractHeadNo;
	}

	public void setContractHeadNo(String ContractHeadNo) {
		this.ContractHeadNo = ContractHeadNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String SpecName) {
		this.SpecName = SpecName;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String Content) {
		this.Content = Content;
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

	public BigDecimal getFCA() {
		return FCA;
	}

	public void setFCA(BigDecimal FCA) {
		this.FCA = FCA;
	}

	public BigDecimal getEXW() {
		return EXW;
	}

	public void setEXW(BigDecimal EXW) {
		this.EXW = EXW;
	}

	public BigDecimal getFOB() {
		return FOB;
	}

	public void setFOB(BigDecimal FOB) {
		this.FOB = FOB;
	}

	public BigDecimal getCIF() {
		return CIF;
	}

	public void setCIF(BigDecimal CIF) {
		this.CIF = CIF;
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

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String SpecId) {
		this.SpecId = SpecId;
	}

	public Spec getSpec() {
		return Spec;
	}

	public void setSpec(Spec Spec) {
		this.Spec = Spec;
	}

}
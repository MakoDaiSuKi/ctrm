
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "InvoiceGrade", schema = "Physical")

public class InvoiceGrade extends HibernateEntity {
	private static final long serialVersionUID = 1461832991342L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "SpecName")
	private String SpecName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "SpecId")
	private String SpecId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 *
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * 升贴水差价
	 */
	@Column(name = "DiffPremium")
	@JsonProperty(value = "DiffPremium")
	private BigDecimal DiffPremium;
	/**
	 *
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 *
	 */
	@Column(name = "DeliveryTerm")
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;
	/**
	 * 差额
	 */
	@Column(name = "DiffAmount")
	@JsonProperty(value = "DiffAmount")
	private BigDecimal DiffAmount;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	// @JsonBackReference("InvoiceGrade_Invoice")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	/**
	 *
	 */
	@Column(name = "GradeId")
	@JsonProperty(value = "GradeId")
	private String GradeId;
	// @JsonBackReference("InvoiceGrade_Grade")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Grade.class)
	@JoinColumn(name = "GradeId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Grade")
	private Grade Grade;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String SpecName) {
		this.SpecName = SpecName;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String SpecId) {
		this.SpecId = SpecId;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal Premium) {
		this.Premium = Premium;
	}

	public BigDecimal getDiffPremium() {
		return DiffPremium;
	}

	public void setDiffPremium(BigDecimal DiffPremium) {
		this.DiffPremium = DiffPremium;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public String getDeliveryTerm() {
		return DeliveryTerm;
	}

	public void setDeliveryTerm(String DeliveryTerm) {
		this.DeliveryTerm = DeliveryTerm;
	}

	public BigDecimal getDiffAmount() {
		return DiffAmount;
	}

	public void setDiffAmount(BigDecimal DiffAmount) {
		this.DiffAmount = DiffAmount;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String InvoiceId) {
		this.InvoiceId = InvoiceId;
	}

	public Invoice getInvoice() {
		return Invoice;
	}

	public void setInvoice(Invoice Invoice) {
		this.Invoice = Invoice;
	}

	public String getGradeId() {
		return GradeId;
	}

	public void setGradeId(String GradeId) {
		this.GradeId = GradeId;
	}

	public Grade getGrade() {
		return Grade;
	}

	public void setGrade(Grade Grade) {
		this.Grade = Grade;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

}
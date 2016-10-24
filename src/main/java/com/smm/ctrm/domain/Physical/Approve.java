
package com.smm.ctrm.domain.Physical;

import java.util.Date;

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
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "Approve", schema = "Physical")

public class Approve extends HibernateEntity {
	private static final long serialVersionUID = 1461832991339L;
	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 是否通过
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
	/**
	 * 审批的附言
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *
	 */
	@Column(name = "PendingId")
	@JsonProperty(value = "PendingId")
	private String PendingId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Pending")
	private Pending Pending;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;

	/**
	 *
	 */
	@Column(name = "ApproverId")
	@JsonProperty(value = "ApproverId")
	private String ApproverId;
	// @JsonBackReference("Approve_Approver")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "ApproverId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Approver")
	private User Approver;
	/**
	 *
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	// @JsonBackReference("Approve_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
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
	 *
	 */
	@Column(name = "FundId")
	@JsonProperty(value = "FundId")
	private String FundId;
	// @JsonBackReference("Approve_Fund")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Fund.class)
	@JoinColumn(name = "FundId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Fund")
	private Fund Fund;
	/**
	 * 收发货单ID
	 */
	@Column(name = "ReceiptShipId")
	@JsonProperty(value = "ReceiptShipId")
	private String ReceiptShipId;

	/**
	 * 收发货单
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ReceiptShip.class)
	@JoinColumn(name = "ReceiptShipId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "ReceiptShip")
	private ReceiptShip ReceiptShip;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public Boolean getIsApproved() {
		return IsApproved;
	}

	public void setIsApproved(Boolean IsApproved) {
		this.IsApproved = IsApproved;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getPendingId() {
		return PendingId;
	}

	public void setPendingId(String PendingId) {
		this.PendingId = PendingId;
	}

	public Pending getPending() {
		return Pending;
	}

	public void setPending(Pending Pending) {
		this.Pending = Pending;
	}

	public String getApproverId() {
		return ApproverId;
	}

	public void setApproverId(String ApproverId) {
		this.ApproverId = ApproverId;
	}

	public User getApprover() {
		return Approver;
	}

	public void setApprover(User Approver) {
		this.Approver = Approver;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String CustomerId) {
		this.CustomerId = CustomerId;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer Customer) {
		this.Customer = Customer;
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

	public String getFundId() {
		return FundId;
	}

	public void setFundId(String FundId) {
		this.FundId = FundId;
	}

	public Fund getFund() {
		return Fund;
	}

	public void setFund(Fund Fund) {
		this.Fund = Fund;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public User getCreated() {
		return Created;
	}

	public void setCreated(User Created) {
		this.Created = Created;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

	public User getUpdated() {
		return Updated;
	}

	public void setUpdated(User Updated) {
		this.Updated = Updated;
	}

	public String getReceiptShipId() {
		return ReceiptShipId;
	}

	public void setReceiptShipId(String receiptShipId) {
		ReceiptShipId = receiptShipId;
	}

	public ReceiptShip getReceiptShip() {
		return ReceiptShip;
	}

	public void setReceiptShip(ReceiptShip receiptShip) {
		ReceiptShip = receiptShip;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		InvoiceId = invoiceId;
	}

	public Invoice getInvoice() {
		return Invoice;
	}

	public void setInvoice(Invoice invoice) {
		Invoice = invoice;
	}

}
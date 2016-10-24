
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
@Table(name = "Attachment", schema = "Physical")

public class Attachment extends HibernateEntity {
	private static final long serialVersionUID = 1461832991339L;
	/**
	 * 上传日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 文件名称
	 */
	@Column(name = "FileName")
	@JsonProperty(value = "FileName")
	private String FileName;
	/**
	 * 文件路径
	 */
	@Column(name = "FileUrl")
	@JsonProperty(value = "FileUrl")
	private String FileUrl;
	/**
	 * 是否为外部文档
	 */
	@Column(name = "IsOutDocument")
	@JsonProperty(value = "IsOutDocument")
	private Boolean IsOutDocument;
	/**
	 * 是哪个客户的
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
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
	@Column(name = "StorageId")
	@JsonProperty(value = "StorageId")
	private String StorageId;
/*	@JsonBackReference("Attachment_Storage")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	@JoinColumn(name = "StorageId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Storage")
	private Storage Storage;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
/*	@JsonBackReference("Attachment_Invoice")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	/**
	 *
	 */
	@Column(name = "FundId")
	@JsonProperty(value = "FundId")
	private String FundId;
/*	@JsonBackReference("Attachment_Fund")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Fund.class)
	@JoinColumn(name = "FundId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Fund")
	private Fund Fund;
	/**
	 *
	 */
	@Column(name = "PricingId")
	@JsonProperty(value = "PricingId")
	private String PricingId;
/*	@JsonBackReference("Attachment_Pricing")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Pricing.class)
	@JoinColumn(name = "PricingId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Pricing")
	private Pricing Pricing;
	/**
	 *
	 */
	@Column(name = "PositionId")
	@JsonProperty(value = "PositionId")
	private String PositionId;
/*	@JsonBackReference("Attachment_Position")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position.class)
	@JoinColumn(name = "PositionId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Position")
	private Position Position;
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
	
	@Column(name = "ReceiptShipId")
	@JsonProperty(value = "ReceiptShipId")
    public String ReceiptShipId;
/*	@ManyToOne(fetch=FetchType.EAGER, targetEntity=ReceiptShip.class)
	@JoinColumn(name="ReceiptShipId", insertable=false, updatable=false, foreignKey=@ForeignKey(name="none"))*/
	@Transient
	@JsonProperty(value = "ReceiptShip")
    public ReceiptShip ReceiptShip;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String FileName) {
		this.FileName = FileName;
	}

	public String getFileUrl() {
		return FileUrl;
	}

	public void setFileUrl(String FileUrl) {
		this.FileUrl = FileUrl;
	}

	public Boolean getIsOutDocument() {
		return IsOutDocument;
	}

	public void setIsOutDocument(Boolean IsOutDocument) {
		this.IsOutDocument = IsOutDocument;
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

	public String getStorageId() {
		return StorageId;
	}

	public void setStorageId(String StorageId) {
		this.StorageId = StorageId;
	}

	public Storage getStorage() {
		return Storage;
	}

	public void setStorage(Storage Storage) {
		this.Storage = Storage;
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

	public String getPricingId() {
		return PricingId;
	}

	public void setPricingId(String PricingId) {
		this.PricingId = PricingId;
	}

	public Pricing getPricing() {
		return Pricing;
	}

	public void setPricing(Pricing Pricing) {
		this.Pricing = Pricing;
	}

	public String getPositionId() {
		return PositionId;
	}

	public void setPositionId(String PositionId) {
		this.PositionId = PositionId;
	}

	public Position getPosition() {
		return Position;
	}

	public void setPosition(Position Position) {
		this.Position = Position;
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

}
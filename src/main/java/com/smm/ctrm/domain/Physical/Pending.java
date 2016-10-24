
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
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
@Table(name = "Pending", schema = "Physical")

public class Pending extends HibernateEntity {
	private static final long serialVersionUID = 1461832991343L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Status", defaultValue = "0")
	private Integer Status;
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
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ProductName")
	private String ProductName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "AskerName")
	private String AskerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "AskerDivisionId")
	private String AskerDivisionId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "AskerDivisionName")
	private String AskerDivisionName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproveTradeDate")
	private Date ApproveTradeDate;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "DueDate")
	private Date DueDate;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproverName")
	private String ApproverName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproveComments")
	private String ApproveComments;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CreatedName")
	private String CreatedName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CreatedTime")
	private Date CreatedTime;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "UpdatedName")
	private String UpdatedName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "UpdatedTime")
	private Date UpdatedTime;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Addr")
	private String Addr;
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
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
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
	@JsonProperty(value = "CustomerTitleName")
	private String CustomerTitleName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityOfContract")
	private BigDecimal QuantityOfContract;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityOfLots")
	private BigDecimal QuantityOfLots;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "TraderName")
	private String TraderName;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	/**
	 *
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceTradeDate")
	private Date InvoiceTradeDate;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LotId")
	private String LotId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceAmount")
	private BigDecimal InvoiceAmount;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FundAmount")
	private BigDecimal FundAmount;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FundQuantity")
	private BigDecimal FundQuantity;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproverIds")
	private String ApproverIds;
	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 是否进行审核了
	 */
	@Column(name = "IsDone")
	@JsonProperty(value = "IsDone")
	private Boolean IsDone;
	/**
	 * 审核的结果
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
	/**
	 * 申请的附言
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 申请类型 0 发票付款申请 1 批次付款申请
	 */
	@Column(name = "PendingType")
	@JsonProperty(value = "PendingType")
	private Integer PendingType;
	/**
	 *
	 */
	@Column(name = "AskerId")
	@JsonProperty(value = "AskerId")
	private String AskerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "AskerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Asker")
	private User Asker;
	/**
	 *
	 */
	@Column(name = "ApproverId")
	@JsonProperty(value = "ApproverId")
	private String ApproverId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "ApproverId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Approver")
	private User Approver;

	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproverDivisionId")
	private String ApproverDivisionId;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ApproverDivisionName")
	private String ApproverDivisionName;

	/**
	 *
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
	@Column(name = "FundId")
	@JsonProperty(value = "FundId")
	private String FundId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Fund.class)
	@JoinColumn(name = "FundId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Fund")
	private Fund Fund;

	@Column(name = "ApproveId")
	@JsonProperty(value = "ApproveId")
	private String ApproveId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Approve.class)
	@JoinColumn(name = "ApproveId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Approve")
	private Approve Approve;

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

	public Integer getStatus() {
		return Status == null ? 0 : Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String CommodityName) {
		this.CommodityName = CommodityName;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String ProductName) {
		this.ProductName = ProductName;
	}

	public String getAskerName() {
		return AskerName;
	}

	public void setAskerName(String AskerName) {
		this.AskerName = AskerName;
	}

	public String getAskerDivisionId() {
		return AskerDivisionId;
	}

	public void setAskerDivisionId(String AskerDivisionId) {
		this.AskerDivisionId = AskerDivisionId;
	}

	public String getAskerDivisionName() {
		return AskerDivisionName;
	}

	public void setAskerDivisionName(String AskerDivisionName) {
		this.AskerDivisionName = AskerDivisionName;
	}

	public Date getApproveTradeDate() {
		return ApproveTradeDate;
	}

	public void setApproveTradeDate(Date ApproveTradeDate) {
		this.ApproveTradeDate = ApproveTradeDate;
	}

	public Date getDueDate() {
		return DueDate;
	}

	public void setDueDate(Date DueDate) {
		this.DueDate = DueDate;
	}

	public String getApproverName() {
		return ApproverName;
	}

	public void setApproverName(String ApproverName) {
		this.ApproverName = ApproverName;
	}

	public String getApproveComments() {
		return ApproveComments;
	}

	public void setApproveComments(String ApproveComments) {
		this.ApproveComments = ApproveComments;
	}

	public String getCreatedName() {
		return CreatedName;
	}

	public void setCreatedName(String CreatedName) {
		this.CreatedName = CreatedName;
	}

	public Date getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(Date CreatedTime) {
		this.CreatedTime = CreatedTime;
	}

	public String getUpdatedName() {
		return UpdatedName;
	}

	public void setUpdatedName(String UpdatedName) {
		this.UpdatedName = UpdatedName;
	}

	public Date getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(Date UpdatedTime) {
		this.UpdatedTime = UpdatedTime;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String Addr) {
		this.Addr = Addr;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String LegalName) {
		this.LegalName = LegalName;
	}

	public String getHeadNo() {
		return HeadNo;
	}

	public void setHeadNo(String HeadNo) {
		this.HeadNo = HeadNo;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getCustomerTitleName() {
		return CustomerTitleName;
	}

	public void setCustomerTitleName(String CustomerTitleName) {
		this.CustomerTitleName = CustomerTitleName;
	}

	public BigDecimal getQuantityOfContract() {
		return QuantityOfContract;
	}

	public void setQuantityOfContract(BigDecimal QuantityOfContract) {
		this.QuantityOfContract = QuantityOfContract;
	}

	public BigDecimal getQuantityOfLots() {
		return QuantityOfLots;
	}

	public void setQuantityOfLots(BigDecimal QuantityOfLots) {
		this.QuantityOfLots = QuantityOfLots;
	}

	public String getTraderName() {
		return TraderName;
	}

	public void setTraderName(String TraderName) {
		this.TraderName = TraderName;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String InvoiceId) {
		this.InvoiceId = InvoiceId;
	}

	public Date getInvoiceTradeDate() {
		return InvoiceTradeDate == null ? new Date() : InvoiceTradeDate;
	}

	public void setInvoiceTradeDate(Date InvoiceTradeDate) {
		this.InvoiceTradeDate = InvoiceTradeDate;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount == null ? BigDecimal.ZERO : InvoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal InvoiceAmount) {
		this.InvoiceAmount = InvoiceAmount;
	}

	public BigDecimal getFundAmount() {
		return FundAmount == null ? BigDecimal.ZERO : FundAmount;
	}

	public void setFundAmount(BigDecimal FundAmount) {
		this.FundAmount = FundAmount;
	}

	public BigDecimal getFundQuantity() {
		return FundQuantity == null ? BigDecimal.ZERO : FundQuantity;
	}

	public void setFundQuantity(BigDecimal FundQuantity) {
		this.FundQuantity = FundQuantity;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getApproverIds() {
		return ApproverIds;
	}

	public void setApproverIds(String ApproverIds) {
		this.ApproverIds = ApproverIds;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public Boolean getIsDone() {
		return IsDone;
	}

	public void setIsDone(Boolean IsDone) {
		this.IsDone = IsDone;
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

	public String getAskerId() {
		return AskerId;
	}

	public void setAskerId(String AskerId) {
		this.AskerId = AskerId;
	}

	public User getAsker() {
		return Asker;
	}

	public void setAsker(User Asker) {
		this.Asker = Asker;
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

	public Approve getApprove() {
		return Approve;
	}

	public void setApprove(Approve Approve) {
		this.Approve = Approve;
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

	public Invoice getInvoice() {
		return Invoice;
	}

	public void setInvoice(Invoice invoice) {
		Invoice = invoice;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public Integer getPendingType() {
		return PendingType == null ? 0 : PendingType;
	}

	public void setPendingType(Integer pendingType) {
		PendingType = pendingType;
	}

	public String getApproveId() {
		return ApproveId;
	}

	public void setApproveId(String approveId) {
		ApproveId = approveId;
	}

	public String getApproverDivisionId() {
		return ApproverDivisionId;
	}

	public void setApproverDivisionId(String approverDivisionId) {
		ApproverDivisionId = approverDivisionId;
	}

	public String getApproverDivisionName() {
		return ApproverDivisionName;
	}

	public void setApproverDivisionName(String approverDivisionName) {
		ApproverDivisionName = approverDivisionName;
	}

}
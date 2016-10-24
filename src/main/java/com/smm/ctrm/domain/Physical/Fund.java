
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.LegalBank;
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "Fund", schema = "Physical")
public class Fund extends HibernateEntity {
	private static final long serialVersionUID = 1461832991341L;
	
	/**
	 * 付款
	 */
	public static final String PAYMENT = "D";
	
	/**
	 * 收款
	 */
	public static final String RECEIPT = "C";
	
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Pendings")
	private List<Pending> Pendings;
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
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
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
	@JsonProperty(value = "LegalName")
	private String LegalName;
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
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Unit")
	private String Unit;
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
	@JsonProperty(value = "InvoiceDueDate")
	private Date InvoiceDueDate;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceQuantity")
	private BigDecimal InvoiceQuantity;
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
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;

	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * {D= 付款, C=收款}
	 */
	@Column(name = "DC")
	@JsonProperty(value = "DC")
	private String DC;
	/**
	 * 是否属于初始化业务数据
	 */
	@Column(name = "IsIniatiated")
	@JsonProperty(value = "IsIniatiated")
	private Boolean IsIniatiated;
	/**
	 * 收付款的合同号 + 发票（允许多个，用逗号隔开）
	 */
	@Column(name = "DocumentFor", length = 64)
	@JsonProperty(value = "DocumentFor")
	private String DocumentFor;
	/**
	 * 业务发生日期 ，特别指资金出入的日期
	 */
	@Column(name = "ExecuteDate")
	@JsonProperty(value = "ExecuteDate")
	private Date ExecuteDate;
	/**
	 * 最迟付款时间
	 */
	@Column(name = "LastExecuteDate")
	@JsonProperty(value = "LastExecuteDate")
	private Date LastExecuteDate;
	/**
	 * 终审结论的日期
	 */
	@Column(name = "ApproveDate")
	@JsonProperty(value = "ApproveDate")
	private Date ApproveDate;
	/**
	 * 初次提出申请的日期
	 */
	@Column(name = "AskDate")
	@JsonProperty(value = "AskDate")
	private Date AskDate;
	/**
	 * 初次创建草稿的日期
	 */
	@Column(name = "InitDate")
	@JsonProperty(value = "InitDate")
	private Date InitDate;
	/**
	 * 类别（科目）
	 */
	@Column(name = "Subject", length = 64)
	@JsonProperty(value = "Subject")
	private String Subject;
	/**
	 * 本次付款数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 本次付款单价（发票价格）
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 本次付款金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 货币（Dict）
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 抵押物类型,{C=现金, W=仓单}(Dict)
	 */
	@Column(name = "DepositType", length = 30)
	@JsonProperty(value = "DepositType")
	private String DepositType;
	/**
	 * 货币类型{现金，汇票，信用证……} (Dict)
	 */
	@Column(name = "MonetaryType", length = 30)
	@JsonProperty(value = "MonetaryType")
	private String MonetaryType;
	/**
	 * 抵押物是现金时，是多少金额
	 */
	@Column(name = "DepositAmount")
	@JsonProperty(value = "DepositAmount")
	private BigDecimal DepositAmount;
	/**
	 * 货币（Dict）
	 */
	@Column(name = "DepositCurrency", length = 3)
	@JsonProperty(value = "DepositCurrency")
	private String DepositCurrency;
	/**
	 * 仓单号
	 */
	@Column(name = "WarrantNo", length = 64)
	@JsonProperty(value = "WarrantNo")
	private String WarrantNo;
	/**
	 * 抵押物是仓单时，是多少数量
	 */
	@Column(name = "DepositQuantity")
	@JsonProperty(value = "DepositQuantity")
	private BigDecimal DepositQuantity;
	/**
	 *  
	 */
	@Column(name = "WarrantOwner", length = 64)
	@JsonProperty(value = "WarrantOwner")
	private String WarrantOwner;
	/**
	 *  
	 */
	@Column(name = "WarehouseName", length = 64)
	@JsonProperty(value = "WarehouseName")
	private String WarehouseName;
	/**
	 * 抵押物是仓单时，过期日期
	 */
	@Column(name = "DepositExprityDate")
	@JsonProperty(value = "DepositExprityDate")
	private Date DepositExprityDate;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 是否导入流水
	 */
	@Column(name = "IsImported")
	@JsonProperty(value = "IsImported")
	private Boolean IsImported;
	/**
	 * 是否已经完成收付款
	 */
	@Column(name = "IsExecuted")
	@JsonProperty(value = "IsExecuted")
	private Boolean IsExecuted;
	/**
	 * 付款的类型 0，发票付款 1，批次付款
	 */
	@Column(name = "FundType")
	@JsonProperty(value = "FundType")
	public Integer FundType;

	/*-- 批次拆分 --*/
	/**
	 * 
	 */
	@Column(name = "SplitCount")
	@JsonProperty(value = "SplitCount")
	public BigDecimal SplitCount;
	/**
	 * 
	 */
	@Column(name = "SplitAmount")
	@JsonProperty(value = "SplitAmount")
	public BigDecimal SplitAmount;
	/**
	 * 
	 */
	@Column(name = "IsSelect")
	@JsonProperty(value = "IsSelect")
	public Boolean IsSelect;
	/**
	 * 合同审核的状态(Dict){ 0= 待审，1=pass, -1=deny,..............}
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status")
	private Integer Status;
	/**
	 * 是否通过
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
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
	 * 属于哪个批次
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	/**
	 * 多对一：内部台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 多对一：内部银行
	 */
	@Column(name = "LegalBankId")
	@JsonProperty(value = "LegalBankId")
	private String LegalBankId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = LegalBank.class)
	@JoinColumn(name = "LegalBankId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "LegalBank")
	private LegalBank LegalBank;
	/**
	 * 多对一：客户的收付款标识
	 */
	@Column(name = "CustomerBankId")
	@JsonProperty(value = "CustomerBankId")
	private String CustomerBankId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CustomerBank.class)
	@JoinColumn(name = "CustomerBankId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "CustomerBank")
	private CustomerBank CustomerBank;
	/**
	 * 多对一：客户台头标识
	 */
	@Column(name = "CustomerTitleId")
	@JsonProperty(value = "CustomerTitleId")
	private String CustomerTitleId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CustomerTitle.class)
	@JoinColumn(name = "CustomerTitleId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "CustomerTitle")
	private CustomerTitle CustomerTitle;
	/**
	 * 多对一：客户标识
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
	 * 多对一：品种名称
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 多对一：品种名称
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
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

	/**
	 * 收款水单ID
	 */
	@Column(name = "BankReceiptId")
	@JsonProperty(value = "BankReceiptId")
	private String BankReceiptId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "BankReceiptId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private BankReceipt BankReceipt;

	public List<Pending> getPendings() {
		return Pendings;
	}

	public void setPendings(List<Pending> Pendings) {
		this.Pendings = Pendings;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCustomerTitleName() {
		return CustomerTitleName;
	}

	public void setCustomerTitleName(String CustomerTitleName) {
		this.CustomerTitleName = CustomerTitleName;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String LegalName) {
		this.LegalName = LegalName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String CommodityName) {
		this.CommodityName = CommodityName;
	}

	public String getCommodityCode() {
		return CommodityCode;
	}

	public void setCommodityCode(String CommodityCode) {
		this.CommodityCode = CommodityCode;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public Date getInvoiceTradeDate() {
		return InvoiceTradeDate;
	}

	public void setInvoiceTradeDate(Date InvoiceTradeDate) {
		this.InvoiceTradeDate = InvoiceTradeDate;
	}

	public Date getInvoiceDueDate() {
		return InvoiceDueDate;
	}

	public void setInvoiceDueDate(Date InvoiceDueDate) {
		this.InvoiceDueDate = InvoiceDueDate;
	}

	public BigDecimal getInvoiceQuantity() {
		return InvoiceQuantity;
	}

	public void setInvoiceQuantity(BigDecimal InvoiceQuantity) {
		this.InvoiceQuantity = InvoiceQuantity;
	}

	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal InvoiceAmount) {
		this.InvoiceAmount = InvoiceAmount;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getDC() {
		return DC;
	}

	public void setDC(String DC) {
		this.DC = DC;
	}

	public Boolean getIsIniatiated() {
		return IsIniatiated;
	}

	public void setIsIniatiated(Boolean IsIniatiated) {
		this.IsIniatiated = IsIniatiated;
	}

	public String getDocumentFor() {
		return DocumentFor;
	}

	public void setDocumentFor(String DocumentFor) {
		this.DocumentFor = DocumentFor;
	}

	public Date getExecuteDate() {
		return ExecuteDate;
	}

	public void setExecuteDate(Date ExecuteDate) {
		this.ExecuteDate = ExecuteDate;
	}

	public Date getLastExecuteDate() {
		return LastExecuteDate;
	}

	public void setLastExecuteDate(Date LastExecuteDate) {
		this.LastExecuteDate = LastExecuteDate;
	}

	public Date getApproveDate() {
		return ApproveDate;
	}

	public void setApproveDate(Date ApproveDate) {
		this.ApproveDate = ApproveDate;
	}

	public Date getAskDate() {
		return AskDate;
	}

	public void setAskDate(Date AskDate) {
		this.AskDate = AskDate;
	}

	public Date getInitDate() {
		return InitDate;
	}

	public void setInitDate(Date InitDate) {
		this.InitDate = InitDate;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String Subject) {
		this.Subject = Subject;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getDepositType() {
		return DepositType;
	}

	public void setDepositType(String DepositType) {
		this.DepositType = DepositType;
	}

	public String getMonetaryType() {
		return MonetaryType;
	}

	public void setMonetaryType(String MonetaryType) {
		this.MonetaryType = MonetaryType;
	}

	public BigDecimal getDepositAmount() {
		return DepositAmount;
	}

	public void setDepositAmount(BigDecimal DepositAmount) {
		this.DepositAmount = DepositAmount;
	}

	public String getDepositCurrency() {
		return DepositCurrency;
	}

	public void setDepositCurrency(String DepositCurrency) {
		this.DepositCurrency = DepositCurrency;
	}

	public String getWarrantNo() {
		return WarrantNo;
	}

	public void setWarrantNo(String WarrantNo) {
		this.WarrantNo = WarrantNo;
	}

	public BigDecimal getDepositQuantity() {
		return DepositQuantity;
	}

	public void setDepositQuantity(BigDecimal DepositQuantity) {
		this.DepositQuantity = DepositQuantity;
	}

	public String getWarrantOwner() {
		return WarrantOwner;
	}

	public void setWarrantOwner(String WarrantOwner) {
		this.WarrantOwner = WarrantOwner;
	}

	public String getWarehouseName() {
		return WarehouseName;
	}

	public void setWarehouseName(String WarehouseName) {
		this.WarehouseName = WarehouseName;
	}

	public Date getDepositExprityDate() {
		return DepositExprityDate;
	}

	public void setDepositExprityDate(Date DepositExprityDate) {
		this.DepositExprityDate = DepositExprityDate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public Boolean getIsImported() {
		return IsImported;
	}

	public void setIsImported(Boolean IsImported) {
		this.IsImported = IsImported;
	}

	public Boolean getIsExecuted() {
		return IsExecuted;
	}

	public void setIsExecuted(Boolean IsExecuted) {
		this.IsExecuted = IsExecuted;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Boolean getIsApproved() {
		return IsApproved;
	}

	public void setIsApproved(Boolean IsApproved) {
		this.IsApproved = IsApproved;
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

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String LotId) {
		this.LotId = LotId;
	}

	public Lot getLot() {
		return Lot;
	}

	public void setLot(Lot Lot) {
		this.Lot = Lot;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String LegalId) {
		this.LegalId = LegalId;
	}

	public Legal getLegal() {
		return Legal;
	}

	public void setLegal(Legal Legal) {
		this.Legal = Legal;
	}

	public String getLegalBankId() {
		return LegalBankId;
	}

	public void setLegalBankId(String LegalBankId) {
		this.LegalBankId = LegalBankId;
	}

	public LegalBank getLegalBank() {
		return LegalBank;
	}

	public void setLegalBank(LegalBank LegalBank) {
		this.LegalBank = LegalBank;
	}

	public String getCustomerBankId() {
		return CustomerBankId;
	}

	public void setCustomerBankId(String CustomerBankId) {
		this.CustomerBankId = CustomerBankId;
	}

	public CustomerBank getCustomerBank() {
		return CustomerBank;
	}

	public void setCustomerBank(CustomerBank CustomerBank) {
		this.CustomerBank = CustomerBank;
	}

	public String getCustomerTitleId() {
		return CustomerTitleId;
	}

	public void setCustomerTitleId(String CustomerTitleId) {
		this.CustomerTitleId = CustomerTitleId;
	}

	public CustomerTitle getCustomerTitle() {
		return CustomerTitle;
	}

	public void setCustomerTitle(CustomerTitle CustomerTitle) {
		this.CustomerTitle = CustomerTitle;
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

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String CommodityId) {
		this.CommodityId = CommodityId;
	}

	public Commodity getCommodity() {
		return Commodity;
	}

	public void setCommodity(Commodity Commodity) {
		this.Commodity = Commodity;
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

	public Integer getFundType() {
		return FundType;
	}

	public void setFundType(Integer fundType) {
		FundType = fundType;
	}

	public BigDecimal getSplitCount() {
		return SplitCount;
	}

	public void setSplitCount(BigDecimal splitCount) {
		SplitCount = splitCount;
	}

	public BigDecimal getSplitAmount() {
		return SplitAmount;
	}

	public void setSplitAmount(BigDecimal splitAmount) {
		SplitAmount = splitAmount;
	}

	public Boolean getIsSelect() {
		return IsSelect == null ? Boolean.FALSE : IsSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		IsSelect = isSelect;
	}

	public String getBankReceiptId() {
		return BankReceiptId;
	}

	public void setBankReceiptId(String bankReceiptId) {
		BankReceiptId = bankReceiptId;
	}

	public BankReceipt getBankReceipt() {
		return BankReceipt;
	}

	public void setBankReceipt(BankReceipt bankReceipt) {
		BankReceipt = bankReceipt;
	}

}
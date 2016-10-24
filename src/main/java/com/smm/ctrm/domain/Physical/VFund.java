package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Fund", schema = "Physical")
public class VFund extends HibernateEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = -7970370832940887298L;

	@Transient
	public String FullNo;
	@Transient
	public String CustomerName;
	@Transient
	public String CustomerTitleName;
	@Transient
	public String LegalName;
	@Transient
	public String CommodityName;
	@Transient
	public String CommodityCode;
	@Transient
	public String Unit;
	@Transient
	public Date InvoiceTradeDate;
	@Transient
	public Date InvoiceDueDate;
	@Transient
	public BigDecimal InvoiceQuantity;
	@Transient
	public BigDecimal InvoiceAmount;
	@Transient
	public String InvoiceNo;

	@Column(name = "TradeDate")
	public Date TradeDate;

	/// <summary>
	/// {D= 付款, C=收款}
	/// </summary>
	@Column(name = "DC")
	public String DC;
	/// <summary>
	/// 是否属于初始化业务数据
	/// </summary>
	@Column(name = "IsIniatiated")
	public Boolean IsIniatiated;

	/// <summary>
	/// 收付款的合同号 + 发票（允许多个，用逗号隔开）
	/// </summary>
	@Column(name = "DocumentFor", length = 64)
	public String DocumentFor;

	/// <summary>
	/// 业务发生日期 ，特别指资金出入的日期
	/// </summary>
	@Column(name = "ExecuteDate")
	public Date ExecuteDate;
	/// <summary>
	/// 最迟付款时间
	/// </summary>
	@Column(name = "LastExecuteDate")
	public Date LastExecuteDate;
	/// <summary>
	/// 终审结论的日期
	/// </summary>
	@Column(name = "ApproveDate")
	public Date ApproveDate;

	/// <summary>
	/// 初次提出申请的日期
	/// </summary>
	@Column(name = "AskDate")
	public Date AskDate;

	/// <summary>
	/// 初次创建草稿的日期
	/// </summary>
	@Column(name = "InitDate")
	public Date InitDate;

	/// <summary>
	/// 类别（科目）
	/// </summary>
	@Column(name = "Subject", length = 64)
	public String Subject;
	/// <summary>
	/// 本次付款数量
	/// </summary>
	@Column(name = "Quantity")
	public BigDecimal Quantity;
	/// <summary>
	/// 本次付款单价（发票价格）
	/// </summary>
	@Column(name = "Price")
	public BigDecimal Price;
	/// <summary>
	/// 本次付款金额
	/// </summary>
	@Column(name = "Amount")
	public BigDecimal Amount;

	/// <summary>
	/// 货币（Dict）
	/// </summary>
	@Column(name = "Currency", length = 3)
	public String Currency;

	/// <summary>
	/// 抵押物类型,{C=现金, W=仓单}(Dict)
	/// </summary>
	@Column(name = "DepositType", length = 30)
	public String DepositType;

	/// <summary>
	/// 货币类型{现金，汇票，信用证……} (Dict)
	/// </summary>
	@Column(name = "MonetaryType", length = 30)
	public String MonetaryType;

	/// <summary>
	/// 抵押物是现金时，是多少金额
	/// </summary>
	@Column(name = "DepositAmount")
	public BigDecimal DepositAmount;

	/// <summary>
	/// 货币（Dict）
	/// </summary>
	@Column(name = "DepositCurrency", length = 3)
	public String DepositCurrency;

	/// <summary>
	/// 仓单号
	/// </summary>
	@Column(name = "WarrantNo", length = 64)
	public String WarrantNo;

	/// <summary>
	/// 抵押物是仓单时，是多少数量
	/// </summary>
	@Column(name = "DepositQuantity")
	public BigDecimal DepositQuantity;

	/// <summary>
	///
	/// </summary>
	@Column(name = "WarrantOwner", length = 64)
	public String WarrantOwner;

	/// <summary>
	///
	/// </summary>
	@Column(name = "WarehouseName", length = 64)
	public String WarehouseName;

	/// <summary>
	/// 抵押物是仓单时，过期日期
	/// </summary>
	@Column(name = "DepositExprityDate")
	public Date DepositExprityDate;

	/// <summary>
	/// 备注
	/// </summary>
	@Column(name = "Comments", length = 2000)
	public String Comments;

	/// <summary>
	/// 是否导入流水
	/// </summary>
	@Column(name = "IsImported")
	public Boolean IsImported;

	/// <summary>
	/// 是否已经完成收付款
	/// </summary>
	@Column(name = "IsExecuted")
	public Boolean IsExecuted;

	/// <summary>
	/// 付款的类型 0，发票付款 1，批次付款
	/// </summary>
	@Column(name = "FundType")
	public int FundType;
	/// <summary>
	/// 合同审核的状态(Dict){ 0= 待审，1=pass, -1=deny,..............}
	/// </summary>
	@Column(name = "Status")
	public int Status;

	/// <summary>
	/// 是否通过
	/// </summary>
	@Column(name = "IsApproved")
	public Boolean IsApproved;

	/*-- 批次拆分 --*/
	public BigDecimal SplitCount;
	public BigDecimal SplitAmount;
	public Boolean IsSelect;

	@Column(name = "ContractId")
	public String ContractId;

	/// <summary>
	/// 属于哪个批次
	/// </summary>
	@Column(name = "LotId")
	public String LotId;

	/// <summary>
	/// 多对一：内部台头
	/// </summary>
	@Column(name = "LegalId")
	public String LegalId;

	/// <summary>
	/// 多对一：内部银行
	/// </summary>
	@Column(name = "LegalBankId")
	public String LegalBankId;

	/// <summary>
	/// 多对一：客户的收付款标识
	/// </summary>
	@Column(name = "CustomerBankId")
	public String CustomerBankId;

	/// <summary>
	/// 多对一：客户台头标识
	/// </summary>
	@Column(name = "CustomerTitleId")
	public String CustomerTitleId;

	/// <summary>
	/// 多对一：客户标识
	/// </summary>
	@Column(name = "CustomerId")
	public String CustomerId;

	/// <summary>
	/// 多对一：品种名称
	/// </summary>
	@Column(name = "CommodityId")
	public String CommodityId;

	/// <summary>
	/// 多对一：品种名称
	/// </summary>
	@Column(name = "InvoiceId")
	public String InvoiceId;

	/// <summary>
	/// 创建者Id
	/// </summary>
	@Column(name = "CreatedId")
	public String CreatedId;

	/// <summary>
	/// 更新者Id
	/// </summary>
	@Column(name = "UpdatedId")
	public String UpdatedId;

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String contractId) {
		ContractId = contractId;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}

	public String getLegalBankId() {
		return LegalBankId;
	}

	public void setLegalBankId(String legalBankId) {
		LegalBankId = legalBankId;
	}

	public String getCustomerBankId() {
		return CustomerBankId;
	}

	public void setCustomerBankId(String customerBankId) {
		CustomerBankId = customerBankId;
	}

	public String getCustomerTitleId() {
		return CustomerTitleId;
	}

	public void setCustomerTitleId(String customerTitleId) {
		CustomerTitleId = customerTitleId;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		InvoiceId = invoiceId;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String updatedId) {
		UpdatedId = updatedId;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getCustomerTitleName() {
		return CustomerTitleName;
	}

	public void setCustomerTitleName(String customerTitleName) {
		CustomerTitleName = customerTitleName;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String legalName) {
		LegalName = legalName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}

	public String getCommodityCode() {
		return CommodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		CommodityCode = commodityCode;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public Date getInvoiceTradeDate() {
		return InvoiceTradeDate;
	}

	public void setInvoiceTradeDate(Date invoiceTradeDate) {
		InvoiceTradeDate = invoiceTradeDate;
	}

	public Date getInvoiceDueDate() {
		return InvoiceDueDate;
	}

	public void setInvoiceDueDate(Date invoiceDueDate) {
		InvoiceDueDate = invoiceDueDate;
	}

	public BigDecimal getInvoiceQuantity() {
		return InvoiceQuantity;
	}

	public void setInvoiceQuantity(BigDecimal invoiceQuantity) {
		InvoiceQuantity = invoiceQuantity;
	}

	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		InvoiceAmount = invoiceAmount;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getDC() {
		return DC;
	}

	public void setDC(String dC) {
		DC = dC;
	}

	public Boolean getIsIniatiated() {
		return IsIniatiated;
	}

	public void setIsIniatiated(Boolean isIniatiated) {
		IsIniatiated = isIniatiated;
	}

	public String getDocumentFor() {
		return DocumentFor;
	}

	public void setDocumentFor(String documentFor) {
		DocumentFor = documentFor;
	}

	public Date getExecuteDate() {
		return ExecuteDate;
	}

	public void setExecuteDate(Date executeDate) {
		ExecuteDate = executeDate;
	}

	public Date getLastExecuteDate() {
		return LastExecuteDate;
	}

	public void setLastExecuteDate(Date lastExecuteDate) {
		LastExecuteDate = lastExecuteDate;
	}

	public Date getApproveDate() {
		return ApproveDate;
	}

	public void setApproveDate(Date approveDate) {
		ApproveDate = approveDate;
	}

	public Date getAskDate() {
		return AskDate;
	}

	public void setAskDate(Date askDate) {
		AskDate = askDate;
	}

	public Date getInitDate() {
		return InitDate;
	}

	public void setInitDate(Date initDate) {
		InitDate = initDate;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getDepositType() {
		return DepositType;
	}

	public void setDepositType(String depositType) {
		DepositType = depositType;
	}

	public String getMonetaryType() {
		return MonetaryType;
	}

	public void setMonetaryType(String monetaryType) {
		MonetaryType = monetaryType;
	}

	public BigDecimal getDepositAmount() {
		return DepositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		DepositAmount = depositAmount;
	}

	public String getDepositCurrency() {
		return DepositCurrency;
	}

	public void setDepositCurrency(String depositCurrency) {
		DepositCurrency = depositCurrency;
	}

	public String getWarrantNo() {
		return WarrantNo;
	}

	public void setWarrantNo(String warrantNo) {
		WarrantNo = warrantNo;
	}

	public BigDecimal getDepositQuantity() {
		return DepositQuantity;
	}

	public void setDepositQuantity(BigDecimal depositQuantity) {
		DepositQuantity = depositQuantity;
	}

	public String getWarrantOwner() {
		return WarrantOwner;
	}

	public void setWarrantOwner(String warrantOwner) {
		WarrantOwner = warrantOwner;
	}

	public String getWarehouseName() {
		return WarehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		WarehouseName = warehouseName;
	}

	public Date getDepositExprityDate() {
		return DepositExprityDate;
	}

	public void setDepositExprityDate(Date depositExprityDate) {
		DepositExprityDate = depositExprityDate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public Boolean getIsImported() {
		return IsImported;
	}

	public void setIsImported(Boolean isImported) {
		IsImported = isImported;
	}

	public Boolean getIsExecuted() {
		return IsExecuted;
	}

	public void setIsExecuted(Boolean isExecuted) {
		IsExecuted = isExecuted;
	}

	public int getFundType() {
		return FundType;
	}

	public void setFundType(int fundType) {
		FundType = fundType;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Boolean getIsApproved() {
		return IsApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		IsApproved = isApproved;
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
		return IsSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		IsSelect = isSelect;
	}
	
	

}


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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;

/**
 * 信用证对象
 */
@Entity
@Table(name = "LC", schema = "Physical")
public class LC extends HibernateEntity {
	private static final long serialVersionUID = 1461832991342L;
	/**
	 *
	 */
	@Column(name = "KzRate")
	@JsonProperty(value = "KzRate")
	private BigDecimal KzRate;
	/**
	 *
	 */
	@Column(name = "KzAmount")
	@JsonProperty(value = "KzAmount")
	private BigDecimal KzAmount;
	/**
	 *
	 */
	@Column(name = "CdDate")
	@JsonProperty(value = "CdDate")
	private Date CdDate;
	/**
	 *
	 */
	@Column(name = "CdRate")
	@JsonProperty(value = "CdRate")
	private BigDecimal CdRate;
	/**
	 *
	 */
	@Column(name = "CdAmount")
	@JsonProperty(value = "CdAmount")
	private BigDecimal CdAmount;
	/**
	 *
	 */
	@Column(name = "TxDate")
	@JsonProperty(value = "TxDate")
	private Date TxDate;
	/**
	 *
	 */
	@Column(name = "TxRate")
	@JsonProperty(value = "TxRate")
	private BigDecimal TxRate;
	/**
	 *
	 */
	@Column(name = "TxAmount")
	@JsonProperty(value = "TxAmount")
	private BigDecimal TxAmount;
	/**
	 *
	 */
	@Column(name = "TxAmountActual")
	@JsonProperty(value = "TxAmountActual")
	private BigDecimal TxAmountActual;
	/**
	 *
	 */
	@Column(name = "YfRate")
	@JsonProperty(value = "YfRate")
	private BigDecimal YfRate;
	/**
	 *
	 */
	@Column(name = "YfAmount")
	@JsonProperty(value = "YfAmount")
	private BigDecimal YfAmount;
	/**
	 *
	 */
	@Column(name = "IsInvoiced")
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "BenificiaryName")
	private String BenificiaryName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "IssuerName")
	private String IssuerName;
	/**
	 *
	 */
	@Column(name = "FullNos")
	@JsonProperty(value = "FullNos")
	private String FullNos;
	/**
	 *
	 */
	@Column(name = "InvoiceNos")
	@JsonProperty(value = "InvoiceNos")
	private String InvoiceNos;
	/**
	 * M= 开给对方, T=从对方收取
	 */
	@Column(name = "MT", length = 1)
	@JsonProperty(value = "MT")
	private String MT;
	/**
	 * 信用证号
	 */
	@Column(name = "LcNo", length = 64)
	@JsonProperty(value = "LcNo")
	private String LcNo;
	/**
	 * 信用证类型 (Dict)
	 */
	@Column(name = "LcType", length = 30)
	@JsonProperty(value = "LcType")
	private String LcType;
	/**
	 * 申请日期
	 */
	@Column(name = "ApplyDate")
	@JsonProperty(value = "ApplyDate")
	private Date ApplyDate;
	/**
	 * 审批日期
	 */
	@Column(name = "AuditDate")
	@JsonProperty(value = "AuditDate")
	private Date AuditDate;
	/**
	 * 开证日期
	 */
	@Column(name = "IssueDate")
	@JsonProperty(value = "IssueDate")
	private Date IssueDate;
	/**
	 * 开证价格
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 开证数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 开证行
	 */
	@Column(name = "IssuingBank", length = 64)
	@JsonProperty(value = "IssuingBank")
	private String IssuingBank;
	/**
	 * 到期日
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 * 有效期
	 */
	@Column(name = "MaturityDays")
	@JsonProperty(value = "MaturityDays")
	private Integer MaturityDays;
	/**
	 * 承兑日期
	 */
	@Column(name = "AcceptanceDate")
	@JsonProperty(value = "AcceptanceDate")
	private Date AcceptanceDate;
	/**
	 * 计息天数
	 */
	@Column(name = "InterestDays")
	@JsonProperty(value = "InterestDays")
	private Integer InterestDays;
	/**
	 * 开证费用
	 */
	@Column(name = "Fee4Issue")
	@JsonProperty(value = "Fee4Issue")
	private BigDecimal Fee4Issue;
	/**
	 * 承兑费用
	 */
	@Column(name = "Fee4Execute")
	@JsonProperty(value = "Fee4Execute")
	private BigDecimal Fee4Execute;
	/**
	 * 利息收入 = 存款收入
	 */
	@Column(name = "Interest")
	@JsonProperty(value = "Interest")
	private BigDecimal Interest;
	/**
	 * 是否通过审核
	 */
	@Column(name = "IsAudit")
	@JsonProperty(value = "IsAudit")
	private Boolean IsAudit;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 一对多：发票集合
	 */
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Invoice.class, cascade = {})
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name = "LcId", foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "Invoices")
	private List<Invoice> Invoices;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LotIds")
	private List<String> LotIds;
	/**
	 * 受益方
	 */
	@Column(name = "BenificiaryId")
	@JsonProperty(value = "BenificiaryId")
	private String BenificiaryId;
	/*
	 * @ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	 * 
	 * @JoinColumn(name = "BenificiaryId", insertable = false, updatable =
	 * false, foreignKey = @ForeignKey(name = "none"))
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 */
	@Transient
	@JsonProperty(value = "Benificiary")
	private Customer Benificiary;
	/**
	 * 开证方
	 */
	@Column(name = "IssuerId")
	@JsonProperty(value = "IssuerId")
	private String IssuerId;
	/*
	 * @ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	 * 
	 * @JoinColumn(name = "IssuerId", insertable = false, updatable = false,
	 * foreignKey = @ForeignKey(name = "none"))
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 */
	@Transient
	@JsonProperty(value = "Issuer")
	private Customer Issuer;
	/**
	 * 开证银行
	 */
	@Column(name = "IssueBankId")
	@JsonProperty(value = "IssueBankId")
	private String IssueBankId;
	/*
	 * @ManyToOne(fetch = FetchType.EAGER, targetEntity = Bank.class)
	 * 
	 * @JoinColumn(name = "IssueBankId", insertable = false, updatable = false,
	 * foreignKey = @ForeignKey(name = "none"))
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE)
	 */
	@Transient
	@JsonProperty(value = "IssueBank")
	private Bank IssueBank;
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

	public BigDecimal getKzRate() {
		return KzRate;
	}

	public void setKzRate(BigDecimal KzRate) {
		this.KzRate = KzRate;
	}

	public BigDecimal getKzAmount() {
		return KzAmount;
	}

	public void setKzAmount(BigDecimal KzAmount) {
		this.KzAmount = KzAmount;
	}

	public Date getCdDate() {
		return CdDate;
	}

	public void setCdDate(Date CdDate) {
		this.CdDate = CdDate;
	}

	public BigDecimal getCdRate() {
		return CdRate;
	}

	public void setCdRate(BigDecimal CdRate) {
		this.CdRate = CdRate;
	}

	public BigDecimal getCdAmount() {
		return CdAmount;
	}

	public void setCdAmount(BigDecimal CdAmount) {
		this.CdAmount = CdAmount;
	}

	public Date getTxDate() {
		return TxDate;
	}

	public void setTxDate(Date TxDate) {
		this.TxDate = TxDate;
	}

	public BigDecimal getTxRate() {
		return TxRate;
	}

	public void setTxRate(BigDecimal TxRate) {
		this.TxRate = TxRate;
	}

	public BigDecimal getTxAmount() {
		return TxAmount;
	}

	public void setTxAmount(BigDecimal TxAmount) {
		this.TxAmount = TxAmount;
	}

	public BigDecimal getTxAmountActual() {
		return TxAmountActual;
	}

	public void setTxAmountActual(BigDecimal TxAmountActual) {
		this.TxAmountActual = TxAmountActual;
	}

	public BigDecimal getYfRate() {
		return YfRate;
	}

	public void setYfRate(BigDecimal YfRate) {
		this.YfRate = YfRate;
	}

	public BigDecimal getYfAmount() {
		return YfAmount;
	}

	public void setYfAmount(BigDecimal YfAmount) {
		this.YfAmount = YfAmount;
	}

	public Boolean getIsInvoiced() {
		return IsInvoiced == null ? Boolean.FALSE : IsInvoiced;
	}

	public void setIsInvoiced(Boolean IsInvoiced) {
		this.IsInvoiced = IsInvoiced;
	}

	public String getBenificiaryName() {
		return BenificiaryName;
	}

	public void setBenificiaryName(String BenificiaryName) {
		this.BenificiaryName = BenificiaryName;
	}

	public String getIssuerName() {
		return IssuerName;
	}

	public void setIssuerName(String IssuerName) {
		this.IssuerName = IssuerName;
	}

	public String getFullNos() {
		return FullNos;
	}

	public void setFullNos(String FullNos) {
		this.FullNos = FullNos;
	}

	public String getInvoiceNos() {
		return InvoiceNos;
	}

	public void setInvoiceNos(String InvoiceNos) {
		this.InvoiceNos = InvoiceNos;
	}

	public String getMT() {
		return MT;
	}

	public void setMT(String MT) {
		this.MT = MT;
	}

	public String getLcNo() {
		return LcNo;
	}

	public void setLcNo(String LcNo) {
		this.LcNo = LcNo;
	}

	public String getLcType() {
		return LcType;
	}

	public void setLcType(String LcType) {
		this.LcType = LcType;
	}

	public Date getApplyDate() {
		return ApplyDate;
	}

	public void setApplyDate(Date ApplyDate) {
		this.ApplyDate = ApplyDate;
	}

	public Date getAuditDate() {
		return AuditDate;
	}

	public void setAuditDate(Date AuditDate) {
		this.AuditDate = AuditDate;
	}

	public Date getIssueDate() {
		return IssueDate;
	}

	public void setIssueDate(Date IssueDate) {
		this.IssueDate = IssueDate;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public String getIssuingBank() {
		return IssuingBank;
	}

	public void setIssuingBank(String IssuingBank) {
		this.IssuingBank = IssuingBank;
	}

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date PromptDate) {
		this.PromptDate = PromptDate;
	}

	public Integer getMaturityDays() {
		return MaturityDays;
	}

	public void setMaturityDays(Integer MaturityDays) {
		this.MaturityDays = MaturityDays;
	}

	public Date getAcceptanceDate() {
		return AcceptanceDate;
	}

	public void setAcceptanceDate(Date AcceptanceDate) {
		this.AcceptanceDate = AcceptanceDate;
	}

	public Integer getInterestDays() {
		return InterestDays;
	}

	public void setInterestDays(Integer InterestDays) {
		this.InterestDays = InterestDays;
	}

	public BigDecimal getFee4Issue() {
		return Fee4Issue;
	}

	public void setFee4Issue(BigDecimal Fee4Issue) {
		this.Fee4Issue = Fee4Issue;
	}

	public BigDecimal getFee4Execute() {
		return Fee4Execute;
	}

	public void setFee4Execute(BigDecimal Fee4Execute) {
		this.Fee4Execute = Fee4Execute;
	}

	public BigDecimal getInterest() {
		return Interest;
	}

	public void setInterest(BigDecimal Interest) {
		this.Interest = Interest;
	}

	public Boolean getIsAudit() {
		return IsAudit;
	}

	public void setIsAudit(Boolean IsAudit) {
		this.IsAudit = IsAudit;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public List<Invoice> getInvoices() {
		return Invoices;
	}

	public void setInvoices(List<Invoice> Invoices) {
		this.Invoices = Invoices;
	}

	public List<String> getLotIds() {
		return LotIds;
	}

	public void setLotIds(List<String> LotIds) {
		this.LotIds = LotIds;
	}

	public String getBenificiaryId() {
		return BenificiaryId;
	}

	public void setBenificiaryId(String BenificiaryId) {
		this.BenificiaryId = BenificiaryId;
	}

	public Customer getBenificiary() {
		return Benificiary;
	}

	public void setBenificiary(Customer Benificiary) {
		this.Benificiary = Benificiary;
	}

	public String getIssuerId() {
		return IssuerId;
	}

	public void setIssuerId(String IssuerId) {
		this.IssuerId = IssuerId;
	}

	public Customer getIssuer() {
		return Issuer;
	}

	public void setIssuer(Customer Issuer) {
		this.Issuer = Issuer;
	}

	public String getIssueBankId() {
		return IssueBankId;
	}

	public void setIssueBankId(String IssueBankId) {
		this.IssueBankId = IssueBankId;
	}

	public Bank getIssueBank() {
		return IssueBank;
	}

	public void setIssueBank(Bank IssueBank) {
		this.IssueBank = IssueBank;
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

}
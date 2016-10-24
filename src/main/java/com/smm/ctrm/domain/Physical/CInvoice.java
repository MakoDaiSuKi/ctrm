
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
import com.smm.ctrm.util.DateUtil;


/**
 * 发票对象
 */
@Entity
@Table(name = "Invoice", schema = "Physical")
public class CInvoice extends HibernateEntity {
	private static final long serialVersionUID = 1461832991342L;
	
	@Transient
	@JsonProperty(value = "SplitCount")
	private BigDecimal SplitCount;
	
	@Transient
	@JsonProperty(value = "SplitedAmount")
	private BigDecimal SplitedAmount;
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
	@JsonProperty(value = "Pendings")
	private List<Pending> Pendings;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "GrossProfitAmount")
	private BigDecimal GrossProfitAmount;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "GrossProfitRate")
	private BigDecimal GrossProfitRate;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Exposure")
	private BigDecimal Exposure;
	/**
	 * 是否已经完成收付款
	 */
	@Column(name = "IsExecuted")
	@JsonProperty(value = "IsExecuted")
	private Boolean IsExecuted;
	/**
	 *
	 */
	@Column(name = "UInvoiceCode")
	@JsonProperty(value = "UInvoiceCode")
	private String UInvoiceCode;
	/**
	 *
	 */
	@Column(name = "SplitAmount")
	@JsonProperty(value = "SplitAmount")
	private String SplitAmount;
	/**
	 *
	 */
	@Column(name = "IsSelect")
	@JsonProperty(value = "IsSelect")
	private String IsSelect;
	/**
	 *
	 */
	@Column(name = "FeeCode")
	@JsonProperty(value = "FeeCode")
	private String FeeCode;
	/**
	 *
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 发票类型：{ P=临时, A=调整, F=正式 }按数量或价格是否最终确定划分
	 */
	@Column(name = "PFA", length = 30)
	@JsonProperty(value = "PFA")
	private String PFA;
	/**
	 *
	 */
	@Column(name = "DocumentNo", length = 64)
	@JsonProperty(value = "DocumentNo")
	private String DocumentNo;
	/**
	 * 出入库单编号
	 */
	@Column(name = "DeliveryOrderNo", length = 64)
	@JsonProperty(value = "DeliveryOrderNo")
	private String DeliveryOrderNo;
	/**
	 * 单据号 InvoiceNo = Prefix + SerialNo + Suffix
	 */
	@Column(name = "InvoiceNo", length = 64)
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 *
	 */
	@Column(name = "Prefix", length = 64)
	@JsonProperty(value = "Prefix")
	private String Prefix;
	/**
	 * 流水号,类似“140003” = 2位年份 + 流水号
	 */
	@Column(name = "SerialNo", length = 64)
	@JsonProperty(value = "SerialNo")
	private String SerialNo;
	/**
	 *
	 */
	@Column(name = "Suffix", length = 64)
	@JsonProperty(value = "Suffix")
	private String Suffix;
	/**
	 * 出具日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * M = 开票给对方, T = 从对方收取发票
	 */
	@Column(name = "MT", length = 1)
	@JsonProperty(value = "MT")
	private String MT;
	/**
	 * 数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 数量是否来自交付明细的仓库净重
	 */
	@Column(name = "IsWareHouseQuantity")
	@JsonProperty(value = "IsWareHouseQuantity")
	private Boolean IsWareHouseQuantity;
	/**
	 * 是否使用用户自己输入的数量（如果用户手工修改了开票数量，则该数量不再计算）
	 */
	@Column(name = "IsUserChangedQuantity")
	@JsonProperty(value = "IsUserChangedQuantity")
	private Boolean IsUserChangedQuantity;
	/**
	 * 价格
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 市场点价的价格
	 */
	@Column(name = "Major")
	@JsonProperty(value = "Major")
	private BigDecimal Major;
	/**
	 * 合同约定的升贴水
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * 运输折扣
	 */
	@Column(name = "Discount4Transportation")
	@JsonProperty(value = "Discount4Transportation")
	private BigDecimal Discount4Transportation;
	/**
	 * 其它折扣
	 */
	@Column(name = "DiscountMisc")
	@JsonProperty(value = "DiscountMisc")
	private BigDecimal DiscountMisc;
	/**
	 * 账面金额 = 价格 * 数量
	 */
	@Column(name = "AmountNotional")
	@JsonProperty(value = "AmountNotional")
	private BigDecimal AmountNotional;
	/**
	 * 实际的应收应付的金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 按周经理要求新增: 合同中约定的付款比例
	 */
	@Column(name = "DueRate")
	@JsonProperty(value = "DueRate")
	private BigDecimal DueRate;
	/**
	 * 按比例折算后的金额。也是实际付款的金额。
	 */
	@Column(name = "DueAmount")
	@JsonProperty(value = "DueAmount")
	private BigDecimal DueAmount;
	/**
	 * 按发票进行付款的申请，汇总的申请支付的金额之和
	 */
	@Column(name = "AmountDrafted")
	@JsonProperty(value = "AmountDrafted")
	private BigDecimal AmountDrafted;
	/**
	 * 按发票进行付款的申请，汇总的申请支付的数量之和
	 */
	@Column(name = "QuantityDrafted")
	@JsonProperty(value = "QuantityDrafted")
	private BigDecimal QuantityDrafted;
	/**
	 * 盈亏
	 */
	@Column(name = "PnL")
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 收付款的截止日期
	 */
	@Column(name = "DueDate")
	@JsonProperty(value = "DueDate")
	private Date DueDate;
	/**
	 * 说明
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 已经结算的。之后不允许修改。
	 */
	@Column(name = "IsSettled")
	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;
	/**
	 * 已经过账
	 */
	@Column(name = "IsAccounted")
	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;
	/**
	 * 仅为标识一个发票包含多个批次的记录
	 */
	@Column(name = "Is4MultiLots")
	@JsonProperty(value = "Is4MultiLots")
	private Boolean Is4MultiLots;
	/**
	 * 采购/销售（按批次note使用）
	 */
	@Column(name = "SpotDirection")
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 发票类型 (默认 0：正常单据 ，1：作废单据)
	 */
	@Column(name = "InvoiceType")
	@JsonProperty(value = "InvoiceType")
	private Integer InvoiceType;
	/**
	 * 作废人ID
	 */
	@Column(name = "InvalidId", length = 36)
	@JsonProperty(value = "InvalidId")
	private String InvalidId;
	/**
	 * 作废人
	 */
	@Column(name = "InvalidBy", length = 36)
	@JsonProperty(value = "InvalidBy")
	private String InvalidBy;
	/**
	 * 作废时间
	 */
	@Column(name = "InvalidAt")
	@JsonProperty(value = "InvalidAt")
	private Date InvalidAt;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = CStorage.class)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "InvoiceStorage", schema="Physical", joinColumns= {
	@JoinColumn( name = "InvoiceId") }, inverseJoinColumns={@JoinColumn(name="StorageId")}, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Storages")
	private List<CStorage> Storages;

	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InvoiceGrades")
	private List<InvoiceGrade> InvoiceGrades;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Provisionals")
	private List<CInvoice> Provisionals;
	/**
	 *
	 */
	@Column(name = "LcId")
	@JsonProperty(value = "LcId")
	private String LcId;

	/**
	 *
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;

	@Transient
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 抬头银行信息
	 */
	@Column(name = "LegalBankId")
	@JsonProperty(value = "LegalBankId")
	private String LegalBankId;

	@Transient
	@JsonProperty(value = "LegalBank")
	private LegalBank LegalBank;
	/**
	 *
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;


	@Transient
    @JsonProperty(value = "Contract")
	private Contract Contract;
	/**
	 * 属于哪个批次
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;


	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CLot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private CLot Lot;
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	@Transient
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 客户标识
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;


    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
    @JoinColumn(name = "CustomerId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
    @NotFound(action= NotFoundAction.IGNORE)
    @JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 客户抬头标识
	 */
	@Column(name = "CustomerTitleId")
	@JsonProperty(value = "CustomerTitleId")
	private String CustomerTitleId;


	@Transient
	@JsonProperty(value = "CustomerTitle")
	private CustomerTitle CustomerTitle;
	/**
	 * 客户银行信息
	 */
	@Column(name = "CustomerBankId")
	@JsonProperty(value = "CustomerBankId")
	private String CustomerBankId;
	

	@Transient
	@JsonProperty(value = "CustomerBank")
	private CustomerBank CustomerBank;
	/**
	 * 一对多：头寸集合
	 */
	@Transient
	@JsonProperty(value = "Positions")
	private List<Position> Positions;
	/**
	 * 多对一：父节点
	 */
	@Column(name = "AdjustId")
	@JsonProperty(value = "AdjustId")
	private String AdjustId;
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

	/**
	 * 审核状态 0 待审 1 ...
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status", defaultValue = "0")
	private Integer Status;

	/**
	 * 是否通过
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = CStorage.class)
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "InvoiceNotice", schema="Physical", joinColumns= {
	@JoinColumn( name = "InvoiceId") }, inverseJoinColumns={@JoinColumn(name="StorageId")}, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Notices")
	private List<CStorage> Notices;

	
	public List<CStorage> getNotices() {
		return Notices;
	}

	public void setNotices(List<CStorage> notices) {
		Notices = notices;
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

	public BigDecimal getGrossProfitAmount() {
		return GrossProfitAmount;
	}

	public void setGrossProfitAmount(BigDecimal GrossProfitAmount) {
		this.GrossProfitAmount = GrossProfitAmount;
	}

	public BigDecimal getGrossProfitRate() {
		return GrossProfitRate;
	}

	public void setGrossProfitRate(BigDecimal GrossProfitRate) {
		this.GrossProfitRate = GrossProfitRate;
	}

	public BigDecimal getExposure() {
		return Exposure;
	}

	public void setExposure(BigDecimal Exposure) {
		this.Exposure = Exposure;
	}

	public Boolean getIsExecuted() {
		return IsExecuted != null ? IsExecuted : false;
	}

	public void setIsExecuted(Boolean IsExecuted) {
		this.IsExecuted = IsExecuted;
	}

	public String getUInvoiceCode() {
		return UInvoiceCode;
	}

	public void setUInvoiceCode(String UInvoiceCode) {
		this.UInvoiceCode = UInvoiceCode;
	}

	public String getFeeCode() {
		return FeeCode;
	}

	public void setFeeCode(String FeeCode) {
		this.FeeCode = FeeCode;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getPFA() {
		return PFA;
	}

	public void setPFA(String PFA) {
		this.PFA = PFA;
	}

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String DocumentNo) {
		this.DocumentNo = DocumentNo;
	}

	public String getDeliveryOrderNo() {
		return DeliveryOrderNo;
	}

	public void setDeliveryOrderNo(String DeliveryOrderNo) {
		this.DeliveryOrderNo = DeliveryOrderNo;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public String getPrefix() {
		return Prefix;
	}

	public void setPrefix(String Prefix) {
		this.Prefix = Prefix;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String SerialNo) {
		this.SerialNo = SerialNo;
	}

	public String getSuffix() {
		return Suffix;
	}

	public void setSuffix(String Suffix) {
		this.Suffix = Suffix;
	}

	public Date getTradeDate() {
		return TradeDate!=null?TradeDate:DateUtil.doSFormatDate("1970-01-01", "yyyy-MM-dd");
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getMT() {
		return MT;
	}

	public void setMT(String MT) {
		this.MT = MT;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public Boolean getIsWareHouseQuantity() {
		return IsWareHouseQuantity != null ? IsWareHouseQuantity : false;
	}

	public void setIsWareHouseQuantity(Boolean IsWareHouseQuantity) {
		this.IsWareHouseQuantity = IsWareHouseQuantity;
	}

	public Boolean getIsUserChangedQuantity() {
		return IsUserChangedQuantity != null ? IsUserChangedQuantity : false;
	}

	public void setIsUserChangedQuantity(Boolean IsUserChangedQuantity) {
		this.IsUserChangedQuantity = IsUserChangedQuantity;
	}

	public BigDecimal getPrice() {
		return Price!=null?Price:BigDecimal.ZERO;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public BigDecimal getMajor() {
		return Major;
	}

	public void setMajor(BigDecimal Major) {
		this.Major = Major;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal Premium) {
		this.Premium = Premium;
	}

	public BigDecimal getDiscount4Transportation() {
		return Discount4Transportation;
	}

	public void setDiscount4Transportation(BigDecimal Discount4Transportation) {
		this.Discount4Transportation = Discount4Transportation;
	}

	public BigDecimal getDiscountMisc() {
		return DiscountMisc;
	}

	public void setDiscountMisc(BigDecimal DiscountMisc) {
		this.DiscountMisc = DiscountMisc;
	}

	public BigDecimal getAmountNotional() {
		return AmountNotional;
	}

	public void setAmountNotional(BigDecimal AmountNotional) {
		this.AmountNotional = AmountNotional;
	}

	public BigDecimal getAmount() {
		return Amount!=null?Amount:BigDecimal.ZERO;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public BigDecimal getDueRate() {
		return DueRate!=null?DueRate:BigDecimal.ZERO;
	}

	public void setDueRate(BigDecimal DueRate) {
		this.DueRate = DueRate;
	}

	public BigDecimal getDueAmount() {
		return DueAmount!=null?DueAmount:BigDecimal.ZERO;
	}

	public void setDueAmount(BigDecimal DueAmount) {
		this.DueAmount = DueAmount;
	}

	public BigDecimal getAmountDrafted() {
		return AmountDrafted!=null?AmountDrafted:BigDecimal.ZERO;
	}

	public void setAmountDrafted(BigDecimal AmountDrafted) {
		this.AmountDrafted = AmountDrafted;
	}

	public BigDecimal getQuantityDrafted() {
		return QuantityDrafted!=null?QuantityDrafted:BigDecimal.ZERO;
	}

	public void setQuantityDrafted(BigDecimal QuantityDrafted) {
		this.QuantityDrafted = QuantityDrafted;
	}

	public BigDecimal getPnL() {
		return PnL;
	}

	public void setPnL(BigDecimal PnL) {
		this.PnL = PnL;
	}

	public Date getDueDate() {
		return DueDate;
	}

	public void setDueDate(Date DueDate) {
		this.DueDate = DueDate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public Boolean getIsSettled() {
		return IsSettled != null ? IsSettled : false;
	}

	public void setIsSettled(Boolean IsSettled) {
		this.IsSettled = IsSettled;
	}

	public Boolean getIsAccounted() {
		return IsAccounted != null ? IsAccounted : false;
	}

	public void setIsAccounted(Boolean IsAccounted) {
		this.IsAccounted = IsAccounted;
	}

	public Boolean getIs4MultiLots() {
		return Is4MultiLots != null ? Is4MultiLots : false;
	}

	public void setIs4MultiLots(Boolean Is4MultiLots) {
		this.Is4MultiLots = Is4MultiLots;
	}

	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String SpotDirection) {
		this.SpotDirection = SpotDirection;
	}

	public Integer getInvoiceType() {
		return InvoiceType == null ? 0 : InvoiceType;
	}

	public void setInvoiceType(Integer InvoiceType) {
		this.InvoiceType = InvoiceType;
	}

	public String getInvalidId() {
		return InvalidId;
	}

	public void setInvalidId(String InvalidId) {
		this.InvalidId = InvalidId;
	}

	public String getInvalidBy() {
		return InvalidBy;
	}

	public void setInvalidBy(String InvalidBy) {
		this.InvalidBy = InvalidBy;
	}

	public Date getInvalidAt() {
		return InvalidAt;
	}

	public void setInvalidAt(Date InvalidAt) {
		this.InvalidAt = InvalidAt;
	}

	public List<InvoiceGrade> getInvoiceGrades() {
		return InvoiceGrades;
	}

	public void setInvoiceGrades(List<InvoiceGrade> InvoiceGrades) {
		this.InvoiceGrades = InvoiceGrades;
	}

	public List<CInvoice> getProvisionals() {
		return Provisionals;
	}

	public void setProvisionals(List<CInvoice> Provisionals) {
		this.Provisionals = Provisionals;
	}

	public String getLcId() {
		return LcId;
	}

	public void setLcId(String LcId) {
		this.LcId = LcId;
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

	public CLot getLot() {
		return Lot;
	}

	public void setLot(CLot Lot) {
		this.Lot = Lot;
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

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> Positions) {
		this.Positions = Positions;
	}

	public String getAdjustId() {
		return AdjustId;
	}

	public void setAdjustId(String AdjustId) {
		this.AdjustId = AdjustId;
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

	public Integer getStatus() {
		return Status == null ? 0 : Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public Boolean getIsApproved() {
		return IsApproved == null ? Boolean.FALSE : IsApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		IsApproved = isApproved;
	}

	public List<Pending> getPendings() {
		return Pendings;
	}

	public void setPendings(List<Pending> pendings) {
		Pendings = pendings;
	}

	public String getSplitAmount() {
		return SplitAmount;
	}

	public void setSplitAmount(String splitAmount) {
		SplitAmount = splitAmount;
	}

	public String getIsSelect() {
		return IsSelect;
	}

	public void setIsSelect(String isSelect) {
		IsSelect = isSelect;
	}

	public BigDecimal getSplitCount() {
		return SplitCount;
	}

	public void setSplitCount(BigDecimal splitCount) {
		SplitCount = splitCount;
	}

	public BigDecimal getSplitedAmount() {
		return SplitedAmount;
	}

	public void setSplitedAmount(BigDecimal splitedAmount) {
		SplitedAmount = splitedAmount;
	}

	public List<CStorage> getStorages() {
		return Storages;
	}

	public void setStorages(List<CStorage> storages) {
		Storages = storages;
	}

}
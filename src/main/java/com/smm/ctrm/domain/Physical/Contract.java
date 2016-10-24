
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.GradeSet;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.Warehouse;

@Entity
@Table(name = "Contract", schema = "Physical")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract extends HibernateEntity {
	private static final long serialVersionUID = 1461832991340L;
	/**
	 * 商品价格 Price = Major + Premium
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
    public BigDecimal Price;
	/**
	 *
	 */
	@Column(name = "PricingType", length = 30)
	@JsonProperty(value = "PricingType")
	private String PricingType;
    /// <summary>
    /// （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果 
    /// </summary>
	@Column(name = "Major")
	@JsonProperty(value = "Major")
    public BigDecimal Major;
	/**
	 * { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
	 */
	@Column(name = "MajorType", length = 30)
	@JsonProperty(value = "MajorType")
	private String MajorType;
	/**
	 * 多对一：主价市场的标识
	 */
	@Column(name = "MajorMarketId")
	@JsonProperty(value = "MajorMarketId")
	private String MajorMarketId;
	/**
	 * 是否按照ETA船期点价
	 */
	@Column(name = "IsEtaPricing")
	@JsonProperty(value = "IsEtaPricing")
	private Boolean IsEtaPricing;
	/**
	 *｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
	 */
	@Column(name = "MajorBasis", length = 30)
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;
	/**
	 * ｛M-3, M-2, M-1, M, M+1, M+2, M+3｝M=到货月份
	 */
	@Column(name = "EtaDuaration")
	@JsonProperty(value = "EtaDuaration")
	private String EtaDuaration;
	/**
	 * {0=正常的月均价, 1=月连续5天均价中的最低价}
	 */
	@Column(name = "EtaPrice")
	@JsonProperty(value = "EtaPrice")
	private String EtaPrice;
	/**
	 *
	 */
	@Column(name = "MajorStartDate")
	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;
	/**
	 *
	 */
	@Column(name = "MajorEndDate")
	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;
	/**
	 * { 固定升贴水 = F(FIX)，均价升贴水 = A(AVERAGE)}(dict)
	 */
	@Column(name = "PremiumType", length = 30)
	@JsonProperty(value = "PremiumType")
	private String PremiumType;
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * { 最低价的均价 = LOW,  最高价的均价 = HIGH, 均价 = AVERAGE}(dict)
	 */
	@Column(name = "PremiumBasis", length = 30)
	@JsonProperty(value = "PremiumBasis")
	private String PremiumBasis;
	/**
	 * 多对一：升贴水市场的标识
	 */
	@Column(name = "PremiumMarketId")
	@JsonProperty(value = "PremiumMarketId")
	private String PremiumMarketId;
	/**
	 *
	 */
	@Column(name = "PremiumStartDate")
	@JsonProperty(value = "PremiumStartDate")
	private Date PremiumStartDate;
	/**
	 *
	 */
	@Column(name = "PremiumEndDate")
	@JsonProperty(value = "PremiumEndDate")
	private Date PremiumEndDate;
	/**
	 * 品级Id
	 */
	@Column(name = "GradeSetIds")
	@JsonProperty(value = "GradeSetIds")
	private String GradeSetIds;
	/**
	 * 品级Id
	 */
	@Column(name = "GradeSetNames")
	@JsonProperty(value = "GradeSetNames")
	private String GradeSetNames;
	public String getGradeSetNames() {
		return GradeSetNames;
	}

	public void setGradeSetNames(String gradeSetNames) {
		GradeSetNames = gradeSetNames;
	}

	/**
	 * 品牌Id 多个以逗号分隔
	 */
	@Column(name = "BrandIds")
	@JsonProperty(value = "BrandIds")
	private String BrandIds;
	/**
	 * 品牌名 多个以逗号分隔
	 */
	@Column(name = "BrandNames")
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 * 计算溢短装率的基准{  OnQuantity, OnPercentage    }
	 */
	@Column(name = "MoreOrLessBasis")
	@JsonProperty(value = "MoreOrLessBasis")
	private String MoreOrLessBasis;
	/**
	 * 溢短装率的值，默认 = 0M
	 */
	@Column(name = "MoreOrLess")
	@JsonProperty(value = "MoreOrLess")
	private BigDecimal MoreOrLess;
	/**
	 * 品级
	 */
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = GradeSet.class)
//	@JoinColumn(name = "GradeSetId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@Fetch(FetchMode.SELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "GradeSets")
	private List<GradeSet> GradeSets;
	/**
	 * 约定仓库Id
	 */
	@Column(name = "RuleWareHouseIds")
	@JsonProperty(value = "RuleWareHouseIds")
	private String RuleWareHouseIds;
	/**
	 * 约定仓库Name
	 */
	@Column(name = "RuleWareHouseNames")
	@JsonProperty(value = "RuleWareHouseNames")
	private String RuleWareHouseNames;
	public String getRuleWareHouseNames() {
		return RuleWareHouseNames;
	}

	public void setRuleWareHouseNames(String ruleWareHouseNames) {
		RuleWareHouseNames = ruleWareHouseNames;
	}

	/**
	 * 约定仓库
	 */
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
//	@JoinColumn(name = "RuleWareHouseId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@Fetch(FetchMode.SELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "RuleWareHouses")
	private List<Warehouse> RuleWareHouses;

	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "FileInfoUploads")
	List<FileInfoUpload> FileInfoUploads;
	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "Ingredients")
	List<Ingredients> Ingredients;
	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "Type")
	List<Ingredients> Type;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Grades")
	private List<Grade> Grades;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PriceDiffs")
	private List<DischargingPriceDiff> PriceDiffs;
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
	@JsonProperty(value = "LegalCode")
	private String LegalCode;
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
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
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
	@JsonProperty(value = "TraderName")
	private String TraderName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Invoices")
	private List<Invoice> Invoices;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Funds")
	private List<Fund> Funds;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Storages")
	private List<Storage> Storages;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Positions")
	private List<Position> Positions;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PricingRecords")
	private List<PricingRecord> PricingRecords;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Unit")
	private String Unit;
	/**
	 * 是否已经完成点价
	 */
	@Column(name = "IsPriced")
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 * 是否内部交易
	 */
	@Column(name = "IsInternal")
	@JsonProperty(value = "IsInternal")
	private Boolean IsInternal;
	/**
	 * 是否回购订单
	 */
	@Column(name = "IsReBuy")
	@JsonProperty(value = "IsReBuy")
	private Boolean IsReBuy;
	/**
	 * 是否属于初始化业务数据
	 */
	@Column(name = "IsIniatiated")
	@JsonProperty(value = "IsIniatiated")
	private Boolean IsIniatiated;
	
	/**
	 * 交易类型
	 */
	@Column(name = "TransactionType")
	@JsonProperty(value = "TransactionType")
	private String TransactionType;
	/**
	 * bvi - sm之间的天然的特殊关系，bvi销售合同 + sm采购合同必须同步生成和更新。指对应的ContractId
	 */
	@Column(name = "CounterpartId")
	@JsonProperty(value = "CounterpartId")
	private String CounterpartId;
	/**
	 * 获批后的合同可以被“修正”。记录被“修正”的ContractId
	 */
	@Column(name = "ContractAmendId")
	@JsonProperty(value = "ContractAmendId")
	private String ContractAmendId;
	/**
	 * 2修正其他信息的合同在生效后覆盖原合同新合同隐藏 withHold =0(false)
	 */
	@Column(name = "WithHold")
	@JsonProperty(value = "WithHold")
	private Boolean WithHold;
	/**
	 * 已点数量。最初时为CRRC增加此成员，因为点价是针对Contract而非Lot进行，所以才需要这个成员
	 */
	@Column(name = "QuantityPriced")
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * headNo = Prefix + SerialNo + Suffix
	 */
	@Column(name = "HeadNo", length = 64)
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
	/**
	 *
	 */
	@Column(name = "Prefix", length = 64)
	@JsonProperty(value = "Prefix")
	private String Prefix;
	/**
	 * 流水号,类似“140702001”
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
	 * 商品名称
	 */
	@Column(name = "Product", length = 64)
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 单据编号。在此处，指的是合同的原始编号。
	 */
	@Column(name = "DocumentNo", length = 128)
	@JsonProperty(value = "DocumentNo")
	private String DocumentNo;
	/**
	 * 合同数量。
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 为了保持统一，如果是临单，也给这个成员赋值，即 = QuantityContractual
	 */
	@Column(name = "QuantityOfLots")
	@JsonProperty(value = "QuantityOfLots")
	private BigDecimal QuantityOfLots;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 现货交易方向 {B, S} (Dict)
	 */
	@Column(name = "SpotDirection", length = 2)
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 现货交易类型，eg {内贸， 外贸, 转口, 生产, ...} (Dict)
	 */
	@Column(name = "SpotType")
	@JsonProperty(value = "SpotType")
	private String SpotType;
	/**
	 * 交付方式，eg {海运，铁路，...} (Dict)
	 */
	@Column(name = "DeliveryTerm")
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;
	/**
	 * 支付条款，{信用证，DC，...} (Dict)
	 */
	@Column(name = "PaymentTerm")
	@JsonProperty(value = "PaymentTerm")
	private String PaymentTerm;
	/**
	 *
	 */
	@Column(name = "DueDays")
	@JsonProperty(value = "DueDays")
	private Integer DueDays;
	/**
	 *
	 */
	@Column(name = "DueDate")
	@JsonProperty(value = "DueDate")
	private Date DueDate;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 付款所需文件
	 */
	@Column(name = "NeedFiles", length = 2000)
	@JsonProperty(value = "NeedFiles")
	private String NeedFiles;
	/**
	 * 称重
	 */
	@Column(name = "NaQuantity")
	@JsonProperty(value = "NaQuantity")
	private String NaQuantity;
	/**
	 * 检验机构
	 */
	@Column(name = "TestOrg", length = 64)
	@JsonProperty(value = "TestOrg")
	private String TestOrg;
	/**
	 * 点价方
	 */
	@Column(name = "Pricer", length = 64)
	@JsonProperty(value = "Pricer")
	private String Pricer;
	/**
	 * 贸易术语
	 */
	@Column(name = "Term")
	@JsonProperty(value = "Term")
	private String Term;
	/**
	 * 交付期的开始日期
	 */
	@Column(name = "DeliveryStartDate")
	@JsonProperty(value = "DeliveryStartDate")
	private Date DeliveryStartDate;
	/**
	 * 交付期的结束日期
	 */
	@Column(name = "DeliveryEndDate")
	@JsonProperty(value = "DeliveryEndDate")
	private Date DeliveryEndDate;
	/**
	 * true = 临单, false = 长单
	 */
	@Column(name = "IsProvisional")
	@JsonProperty(value = "IsProvisional")
	private Boolean IsProvisional;
	/**
	 * 业务状态
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
	 * 交易账号（=业务经理）
	 */
	@Column(name = "TraderId")
	@JsonProperty(value = "TraderId")
	private String TraderId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "TraderId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Trader")
	private User Trader;
	/**
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
    @JoinColumn(name = "LegalId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
    @Fetch(FetchMode.SELECT)
    @NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 交易对手 = 客户或者供应商
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
    @JoinColumn(name = "CustomerId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
    @Fetch(FetchMode.SELECT)
    @NotFound(action=NotFoundAction.IGNORE)
    @JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 交易对手的、具体的使用哪个台头
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
	 * 品种
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
	 * 一对多：批次集合
	 */
	@Transient
	@JsonProperty(value = "Lots")
	private List<Lot> Lots;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	
	@Transient
	@JsonProperty(value = "MaterielArrivalPlans")
	private List<MaterielArrivalPlan> MaterielArrivalPlans;

	@Column(name="HedgeRatio")
	@JsonProperty(value="HedgeRatio")
	private BigDecimal HedgeRatio;


	@Transient
    @JsonProperty(value="MaterieCostAccount")
    private MaterieCostAccount MaterieCostAccount;


    @Column(name = "RivalOrderID")
    @JsonProperty(value = "RivalOrderID")
    private String RivalOrderID;
    
    @Transient
    @JsonProperty(value = "SpotDirectName")
    private String SpotDirectName;

    public String getRivalOrderID() {
        return RivalOrderID;
    }

    public void setRivalOrderID(String rivalOrderID) {
        RivalOrderID = rivalOrderID;
    }

    public com.smm.ctrm.domain.Physical.MaterieCostAccount getMaterieCostAccount() {
        return MaterieCostAccount;
    }

    public void setMaterieCostAccount(com.smm.ctrm.domain.Physical.MaterieCostAccount materieCostAccount) {
        MaterieCostAccount = materieCostAccount;
    }

    public List<Grade> getGrades() {
		return Grades;
	}

	public void setGrades(List<Grade> Grades) {
		this.Grades = Grades;
	}

	public List<DischargingPriceDiff> getPriceDiffs() {
		return PriceDiffs;
	}

	public void setPriceDiffs(List<DischargingPriceDiff> PriceDiffs) {
		this.PriceDiffs = PriceDiffs;
	}

	public List<Pending> getPendings() {
		return Pendings;
	}

	public void setPendings(List<Pending> Pendings) {
		this.Pendings = Pendings;
	}

	public String getLegalCode() {
		return LegalCode;
	}

	public void setLegalCode(String LegalCode) {
		this.LegalCode = LegalCode;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String LegalName) {
		this.LegalName = LegalName;
	}

	public String getCommodityCode() {
		return CommodityCode;
	}

	public void setCommodityCode(String CommodityCode) {
		this.CommodityCode = CommodityCode;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String CommodityName) {
		this.CommodityName = CommodityName;
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

	public String getTraderName() {
		return TraderName;
	}

	public void setTraderName(String TraderName) {
		this.TraderName = TraderName;
	}

	public List<Invoice> getInvoices() {
		return Invoices;
	}

	public void setInvoices(List<Invoice> Invoices) {
		this.Invoices = Invoices;
	}

	public List<Fund> getFunds() {
		return Funds;
	}

	public void setFunds(List<Fund> Funds) {
		this.Funds = Funds;
	}

	public List<Storage> getStorages() {
		return Storages;
	}

	public void setStorages(List<Storage> Storages) {
		this.Storages = Storages;
	}

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> Positions) {
		this.Positions = Positions;
	}

	public List<PricingRecord> getPricingRecords() {
		return PricingRecords;
	}

	public void setPricingRecords(List<PricingRecord> PricingRecords) {
		this.PricingRecords = PricingRecords;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public Boolean getIsPriced() {
		return IsPriced;
	}

	public void setIsPriced(Boolean IsPriced) {
		this.IsPriced = IsPriced;
	}

	public Boolean getIsInternal() {
		return IsInternal;
	}

	public void setIsInternal(Boolean IsInternal) {
		this.IsInternal = IsInternal;
	}

	public Boolean getIsReBuy() {
		return IsReBuy!=null?IsReBuy:false;
	}

	public void setIsReBuy(Boolean IsReBuy) {
		this.IsReBuy = IsReBuy;
	}

	public Boolean getIsIniatiated() {
		return IsIniatiated;
	}

	public void setIsIniatiated(Boolean IsIniatiated) {
		this.IsIniatiated = IsIniatiated;
	}

	public String getCounterpartId() {
		return CounterpartId;
	}

	public void setCounterpartId(String CounterpartId) {
		this.CounterpartId = CounterpartId;
	}

	public String getContractAmendId() {
		return ContractAmendId;
	}

	public void setContractAmendId(String ContractAmendId) {
		this.ContractAmendId = ContractAmendId;
	}

	public Boolean getWithHold() {
		return WithHold;
	}

	public void setWithHold(Boolean WithHold) {
		this.WithHold = WithHold;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal QuantityPriced) {
		this.QuantityPriced = QuantityPriced;
	}

	public String getHeadNo() {
		return HeadNo;
	}

	public void setHeadNo(String HeadNo) {
		this.HeadNo = HeadNo;
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

	public String getProduct() {
		return Product;
	}

	public void setProduct(String Product) {
		this.Product = Product;
	}

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String DocumentNo) {
		this.DocumentNo = DocumentNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getQuantityOfLots() {
		return QuantityOfLots;
	}

	public void setQuantityOfLots(BigDecimal QuantityOfLots) {
		this.QuantityOfLots = QuantityOfLots;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String SpotDirection) {
		this.SpotDirection = SpotDirection;
	}

	public String getSpotType() {
		return SpotType;
	}

	public void setSpotType(String SpotType) {
		this.SpotType = SpotType;
	}

	public String getDeliveryTerm() {
		return DeliveryTerm;
	}

	public void setDeliveryTerm(String DeliveryTerm) {
		this.DeliveryTerm = DeliveryTerm;
	}

	public String getPaymentTerm() {
		return PaymentTerm;
	}

	public void setPaymentTerm(String PaymentTerm) {
		this.PaymentTerm = PaymentTerm;
	}

	public Integer getDueDays() {
		return DueDays;
	}

	public void setDueDays(Integer DueDays) {
		this.DueDays = DueDays;
	}

	public Date getDueDate() {
		return DueDate;
	}

	public void setDueDate(Date DueDate) {
		this.DueDate = DueDate;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getNeedFiles() {
		return NeedFiles;
	}

	public void setNeedFiles(String NeedFiles) {
		this.NeedFiles = NeedFiles;
	}

	public String getNaQuantity() {
		return NaQuantity;
	}

	public void setNaQuantity(String NaQuantity) {
		this.NaQuantity = NaQuantity;
	}

	public String getTestOrg() {
		return TestOrg;
	}

	public void setTestOrg(String TestOrg) {
		this.TestOrg = TestOrg;
	}

	public String getPricer() {
		return Pricer;
	}

	public void setPricer(String Pricer) {
		this.Pricer = Pricer;
	}

	public String getTerm() {
		return Term;
	}

	public void setTerm(String Term) {
		this.Term = Term;
	}

	public Date getDeliveryStartDate() {
		return DeliveryStartDate;
	}

	public void setDeliveryStartDate(Date DeliveryStartDate) {
		this.DeliveryStartDate = DeliveryStartDate;
	}

	public Date getDeliveryEndDate() {
		return DeliveryEndDate;
	}

	public void setDeliveryEndDate(Date DeliveryEndDate) {
		this.DeliveryEndDate = DeliveryEndDate;
	}

	public Boolean getIsProvisional() {
		return IsProvisional;
	}

	public void setIsProvisional(Boolean IsProvisional) {
		this.IsProvisional = IsProvisional;
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

	public String getTraderId() {
		return TraderId;
	}

	public void setTraderId(String TraderId) {
		this.TraderId = TraderId;
	}

	public User getTrader() {
		return Trader;
	}

	public void setTrader(User Trader) {
		this.Trader = Trader;
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

	public List<Lot> getLots() {
		return Lots;
	}

	public void setLots(List<Lot> Lots) {
		this.Lots = Lots;
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

	public List<FileInfoUpload> getFileInfoUploads() {
		return FileInfoUploads;
	}

	public void setFileInfoUploads(List<FileInfoUpload> fileInfoUploads) {
		FileInfoUploads = fileInfoUploads;
	}

	public List<Ingredients> getIngredients() {
		return Ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		Ingredients = ingredients;
	}

	public List<Ingredients> getType() {
		return Type;
	}

	public void setType(List<Ingredients> type) {
		Type = type;
	}

	public List<MaterielArrivalPlan> getMaterielArrivalPlans() {
		return MaterielArrivalPlans;
	}

	public void setMaterielArrivalPlans(List<MaterielArrivalPlan> materielArrivalPlans) {
		MaterielArrivalPlans = materielArrivalPlans;
	}

	public BigDecimal getHedgeRatio() {
		return HedgeRatio!=null?HedgeRatio:BigDecimal.ZERO;
	}

	public void setHedgeRatio(BigDecimal hedgeRatio) {
		HedgeRatio = hedgeRatio;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public String getSpotDirectName() {
		return SpotDirectName;
	}

	public void setSpotDirectName(String spotDirectName) {
		SpotDirectName = spotDirectName;
	}

	@Override
	public String toString() {
		return String.format(
				"Contract [FileInfoUploads=%s, Ingredients=%s, Type=%s, Grades=%s, PriceDiffs=%s, Pendings=%s, LegalCode=%s, LegalName=%s, CommodityCode=%s, CommodityName=%s, CustomerName=%s, CustomerTitleName=%s, TraderName=%s, Invoices=%s, Funds=%s, Storages=%s, Positions=%s, PricingRecords=%s, Unit=%s, IsPriced=%s, IsInternal=%s, IsReBuy=%s, IsIniatiated=%s, TransactionType=%s, CounterpartId=%s, ContractAmendId=%s, WithHold=%s, QuantityPriced=%s, HeadNo=%s, Prefix=%s, SerialNo=%s, Suffix=%s, Product=%s, DocumentNo=%s, Quantity=%s, QuantityOfLots=%s, Currency=%s, SpotDirection=%s, SpotType=%s, DeliveryTerm=%s, PaymentTerm=%s, DueDays=%s, DueDate=%s, TradeDate=%s, Comments=%s, NeedFiles=%s, NaQuantity=%s, TestOrg=%s, Pricer=%s, Term=%s, DeliveryStartDate=%s, DeliveryEndDate=%s, IsProvisional=%s, Status=%s, IsApproved=%s, TraderId=%s, Trader=%s, LegalId=%s, Legal=%s, CustomerId=%s, Customer=%s, CustomerTitleId=%s, CustomerTitle=%s, CommodityId=%s, Commodity=%s, Lots=%s, CreatedId=%s, Created=%s, UpdatedId=%s, MaterielArrivalPlans=%s, HedgeRatio=%s, MaterieCostAccount=%s, RivalOrderID=%s]",
				FileInfoUploads, Ingredients, Type, Grades, PriceDiffs, Pendings, LegalCode, LegalName, CommodityCode,
				CommodityName, CustomerName, CustomerTitleName, TraderName, Invoices, Funds, Storages, Positions,
				PricingRecords, Unit, IsPriced, IsInternal, IsReBuy, IsIniatiated, TransactionType, CounterpartId,
				ContractAmendId, WithHold, QuantityPriced, HeadNo, Prefix, SerialNo, Suffix, Product, DocumentNo,
				Quantity, QuantityOfLots, Currency, SpotDirection, SpotType, DeliveryTerm, PaymentTerm, DueDays,
				DueDate, TradeDate, Comments, NeedFiles, NaQuantity, TestOrg, Pricer, Term, DeliveryStartDate,
				DeliveryEndDate, IsProvisional, Status, IsApproved, TraderId, Trader, LegalId, Legal, CustomerId,
				Customer, CustomerTitleId, CustomerTitle, CommodityId, Commodity, Lots, CreatedId, Created, UpdatedId,
				MaterielArrivalPlans, HedgeRatio, MaterieCostAccount, RivalOrderID);
	}

	public String getPricingType() {
		return PricingType;
	}

	public void setPricingType(String pricingType) {
		PricingType = pricingType;
	}

	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String majorType) {
		MajorType = majorType;
	}

	public String getMajorMarketId() {
		return MajorMarketId;
	}

	public void setMajorMarketId(String majorMarketId) {
		MajorMarketId = majorMarketId;
	}

	public Boolean getIsEtaPricing() {
		return IsEtaPricing;
	}

	public void setIsEtaPricing(Boolean isEtaPricing) {
		IsEtaPricing = isEtaPricing;
	}

	public String getMajorBasis() {
		return MajorBasis;
	}

	public void setMajorBasis(String majorBasis) {
		MajorBasis = majorBasis;
	}

	public String getEtaDuaration() {
		return EtaDuaration;
	}

	public void setEtaDuaration(String etaDuaration) {
		EtaDuaration = etaDuaration;
	}

	public String getEtaPrice() {
		return EtaPrice;
	}

	public void setEtaPrice(String etaPrice) {
		EtaPrice = etaPrice;
	}

	public Date getMajorStartDate() {
		return MajorStartDate;
	}

	public void setMajorStartDate(Date majorStartDate) {
		MajorStartDate = majorStartDate;
	}

	public Date getMajorEndDate() {
		return MajorEndDate;
	}

	public void setMajorEndDate(Date majorEndDate) {
		MajorEndDate = majorEndDate;
	}

	public String getPremiumType() {
		return PremiumType;
	}

	public void setPremiumType(String premiumType) {
		PremiumType = premiumType;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal premium) {
		Premium = premium;
	}

	public String getPremiumBasis() {
		return PremiumBasis;
	}

	public void setPremiumBasis(String premiumBasis) {
		PremiumBasis = premiumBasis;
	}

	public String getPremiumMarketId() {
		return PremiumMarketId;
	}

	public void setPremiumMarketId(String premiumMarketId) {
		PremiumMarketId = premiumMarketId;
	}

	public Date getPremiumStartDate() {
		return PremiumStartDate;
	}

	public void setPremiumStartDate(Date premiumStartDate) {
		PremiumStartDate = premiumStartDate;
	}

	public Date getPremiumEndDate() {
		return PremiumEndDate;
	}

	public void setPremiumEndDate(Date premiumEndDate) {
		PremiumEndDate = premiumEndDate;
	}

	public String getGradeSetIds() {
		return GradeSetIds;
	}

	public void setGradeSetIds(String gradeSetIds) {
		GradeSetIds = gradeSetIds;
	}

	public List<GradeSet> getGradeSets() {
		return GradeSets;
	}

	public void setGradeSets(List<GradeSet> gradeSets) {
		GradeSets = gradeSets;
	}
	
	public String getBrandIds() {
		return BrandIds;
	}

	public void setBrandIds(String brandIds) {
		BrandIds = brandIds;
	}
	
	public String getBrandNames() {
		return BrandNames;
	}

	public void setBrandNames(String brandNames) {
		BrandNames = brandNames;
	}

	public String getMoreOrLessBasis() {
		return MoreOrLessBasis;
	}

	public void setMoreOrLessBasis(String moreOrLessBasis) {
		MoreOrLessBasis = moreOrLessBasis;
	}
	
	public BigDecimal getMoreOrLess() {
		return MoreOrLess;
	}

	public void setMoreOrLess(BigDecimal moreOrLess) {
		MoreOrLess = moreOrLess ;
	}

	public String getRuleWareHouseIds() {
		return RuleWareHouseIds;
	}

	public void setRuleWareHouseIds(String ruleWareHouseIds) {
		RuleWareHouseIds = ruleWareHouseIds;
	}

	public List<Warehouse> getRuleWareHouses() {
		return RuleWareHouses;
	}

	public void setRuleWareHouses(List<Warehouse> ruleWareHouses) {
		RuleWareHouses = ruleWareHouses;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public BigDecimal getMajor() {
		return Major;
	}

	public void setMajor(BigDecimal major) {
		Major = major;
	}
	
}
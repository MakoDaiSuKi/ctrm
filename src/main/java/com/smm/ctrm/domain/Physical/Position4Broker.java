
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
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "Position4Broker", schema = "Physical")

public class Position4Broker extends HibernateEntity {
	private static final long serialVersionUID = 1461832991344L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "M2MPrice")
	private BigDecimal M2MPrice;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
     * 头寸类型
     */
    @JsonProperty(value = "PositionType")
    public String  PositionType;
	/**
	 * 每次加载自动与Quantity相同，用于后台判断是否数量发生变更
	 */
	@Transient
	@JsonProperty(value = "QuantityBeforeChanged")
	private BigDecimal QuantityBeforeChanged;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "M2MAmount")
	private BigDecimal M2MAmount;
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
	@JsonProperty(value = "MarketName")
	private String MarketName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "MarketCode")
	private String MarketCode;
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
	@JsonProperty(value = "TraderName")
	private String TraderName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "InstrumentName")
	private String InstrumentName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "BrokerName")
	private String BrokerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "IsDel")
	private Boolean IsDel;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PositionList")
	private List<Position> PositionList;
	/**
	 *
	 */
	@Column(name = "QuantityPerLot")
	@JsonProperty(value = "QuantityPerLot")
	private Integer QuantityPerLot;
	/**
	 * 据此生成相应的一对虚拟头寸
	 */
	@Column(name = "IsSourceOfVirtual")
	@JsonProperty(value = "IsSourceOfVirtual")
	private Boolean IsSourceOfVirtual;
	/**
	 * 是否虚拟头寸
	 */
	@Column(name = "IsVirtual")
	@JsonProperty(value = "IsVirtual")
	private Boolean IsVirtual;
	/**
	 * 是否拆分出来的
	 */
	@Column(name = "IsSplitted")
	@JsonProperty(value = "IsSplitted")
	private Boolean IsSplitted;
	/**
	 * 业务日期。指头寸交易委托的日期。
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * Broker交易号
	 */
	@Column(name = "OurRef", length = 64)
	@JsonProperty(value = "OurRef")
	private String OurRef;
	/**
	 * 给客户的交易号
	 */
	@Column(name = "TheirRef", length = 64)
	@JsonProperty(value = "TheirRef")
	private String TheirRef;
	/**
	 * {L=买, S=卖}
	 */
	@Column(name = "LS")
	@JsonProperty(value = "LS")
	private String LS;
	/**
	 * {F = 期货，A = 均价, O = 期权}
	 */
	@Column(name = "ForwardType")
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	/**
	 * 头寸重量。已经从“手数 * 每手数量”计算得出
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 头寸初始重量（头寸录入时的重量，拆分后此数量始终保持并不变化）
	 */
	@Column(name = "QuantityOriginal")
	@JsonProperty(value = "QuantityOriginal")
	private BigDecimal QuantityOriginal;
	/**
	 * 查询头寸净持仓时，只需要通过这个QuantityUnSquared .> 0，即可获得
	 */
	@Column(name = "QuantityUnSquared")
	@JsonProperty(value = "QuantityUnSquared")
	private BigDecimal QuantityUnSquared;
	/**
	 * 头寸手数。
	 */
	@Column(name = "Hands")
	@JsonProperty(value = "Hands")
	private BigDecimal Hands;
	/**
	 * 已保数量
	 */
	@Column(name = "QuantityHedged")
	@JsonProperty(value = "QuantityHedged")
	private BigDecimal QuantityHedged;
	/**
	 * 真正的价格
	 */
	@Column(name = "OurPrice")
	@JsonProperty(value = "OurPrice")
	private BigDecimal OurPrice;
	/**
	 * 成交均价
	 */
	@Column(name = "TradePrice")
	@JsonProperty(value = "TradePrice")
	private BigDecimal TradePrice;
	/**
	 * 调期价差
	 */
	@Column(name = "CarryDiffPrice")
	@JsonProperty(value = "CarryDiffPrice")
	private BigDecimal CarryDiffPrice;
	/**
	 * 交易费用
	 */
	@Column(name = "TradeFee")
	@JsonProperty(value = "TradeFee")
	private BigDecimal TradeFee;
	/**
	 * 客户委托的价格
	 */
	@Column(name = "TheirPrice")
	@JsonProperty(value = "TheirPrice")
	private BigDecimal TheirPrice;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 到期日
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 * 是否调期头寸
	 */
	@Column(name = "IsCarry")
	@JsonProperty(value = "IsCarry")
	private Boolean IsCarry;
	/**
	 * 据此生成相应的一对调期头寸（此头寸被调期）
	 */
	@Column(name = "IsSourceOfCarry")
	@JsonProperty(value = "IsSourceOfCarry")
	private Boolean IsSourceOfCarry;
	/**
	 * 调期头寸中的冲销原头寸的头寸（与原头寸数量品种到期日相同，方向相反）
	 */
	@Column(name = "IsCarryAgainst")
	@JsonProperty(value = "IsCarryAgainst")
	private Boolean IsCarryAgainst;
	/**
	 * 调期编号，用户自定义
	 */
	@Column(name = "CarryRef")
	@JsonProperty(value = "CarryRef")
	private String CarryRef;
	/**
	 * 调期方向{ 调成更早的到期日 = B(ack)， 调成更以后的日期 = F(orward)}
	 */
	@Column(name = "CarryDirection")
	@JsonProperty(value = "CarryDirection")
	private String CarryDirection;
	/**
	 * 被调期的头寸id
	 */
	@Column(name = "CarryPositionId")
	@JsonProperty(value = "CarryPositionId")
	private String CarryPositionId;
	/**
	 * 调期头寸的对手（用于标识一对调期头寸）
	 */
	@Column(name = "CarryCounterpart")
	@JsonProperty(value = "CarryCounterpart")
	private String CarryCounterpart;
	/**
	 * 调期的价差
	 */
	@Column(name = "CarryPriceDiff")
	@JsonProperty(value = "CarryPriceDiff")
	private BigDecimal CarryPriceDiff;
	/**
	 * 是否套利
	 */
	@Column(name = "IsArbitrage")
	@JsonProperty(value = "IsArbitrage")
	private Boolean IsArbitrage;
	/**
	 * 套利编号，用户自定义。--- 这只是一个预留的字段，尚不清楚套利交易的具体需要。
	 */
	@Column(name = "ArbitrageRef")
	@JsonProperty(value = "ArbitrageRef")
	private String ArbitrageRef;
	/**
	 * 交易目的 {保值, 投机, 套利}(Dict)
	 */
	@Column(name = "Purpose")
	@JsonProperty(value = "Purpose")
	private String Purpose;
	/**
	 * 是否已经被用于保值
	 */
	@Column(name = "IsHedged")
	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;
	/**
	 * 是否属于导入的头寸
	 */
	@Column(name = "IsImported")
	@JsonProperty(value = "IsImported")
	private Boolean IsImported;
	/**
	 * 是否已过账
	 */
	@Column(name = "IsAccounted")
	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;
	/**
	 * 是否免佣金
	 */
	@Column(name = "IsCommissionFree")
	@JsonProperty(value = "IsCommissionFree")
	private Boolean IsCommissionFree;
	/**
	 * 是否电子盘交易
	 */
	@Column(name = "IsElectronicTrade")
	@JsonProperty(value = "IsElectronicTrade")
	private Boolean IsElectronicTrade;
	/**
	 * 系统计算得到的佣金。手工调整时，仅修改这个字段即可。
	 */
	@Column(name = "Commission")
	@JsonProperty(value = "Commission")
	private BigDecimal Commission;
	/**
	 * 系统自动计算佣金时，从字段 = Commission、复制一个值给CommissionOriginal
	 */
	@Column(name = "CommissionOriginal")
	@JsonProperty(value = "CommissionOriginal")
	private BigDecimal CommissionOriginal;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 合约代号。只有上海头寸，才有这个代号。类似CU1409, ...
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 开平类型。{开Open = O，平Square = S，平今Close = C}
	 */
	@Column(name = "OCS")
	@JsonProperty(value = "OCS")
	private String OCS;
	/**
	 * 调期费用。只有LME的均价头寸，才有这个值
	 */
	@Column(name = "Spread")
	@JsonProperty(value = "Spread")
	private BigDecimal Spread;
	/**
	 * 均价头寸的开始日期
	 */
	@Column(name = "AverageStartDate")
	@JsonProperty(value = "AverageStartDate")
	private Date AverageStartDate;
	/**
	 * 均价头寸的结束日期
	 */
	@Column(name = "AverageEndDate")
	@JsonProperty(value = "AverageEndDate")
	private Date AverageEndDate;
	/**
	 * 是否确定价格。如果是均价头寸，则初始值 = false，点价期结束且由系统后台自动计算更新，变为true；如果不是均价头寸，则初始值= true
	 */
	@Column(name = "IsPriced")
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 * 已经点价的数量。截止到某个指定日期，这个日期可能是位于头寸的开始和结束日期之间。
	 */
	@Column(name = "QuantityPriced")
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 对于SFE，｛结算价，日加权平均价格｝
	 */
	@Column(name = "PromptBasis")
	@JsonProperty(value = "PromptBasis")
	private String PromptBasis;
	/**
	 * 所需天数
	 */
	@Column(name = "PricingDays")
	@JsonProperty(value = "PricingDays")
	private Integer PricingDays;
	/**
	 * 点价的每天数量
	 */
	@Column(name = "QtyPerPricingDay")
	@JsonProperty(value = "QtyPerPricingDay")
	private BigDecimal QtyPerPricingDay;
	/**
	 * 期权类型, C=Call, Put=P(Dict)
	 */
	@Column(name = "CallPut")
	@JsonProperty(value = "CallPut")
	private String CallPut;
	/**
	 * 期权：权利金
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * 期权：行权价格
	 */
	@Column(name = "StrikePrice")
	@JsonProperty(value = "StrikePrice")
	private BigDecimal StrikePrice;
	/**
	 * 期权：宣布日期
	 */
	@Column(name = "DeclareDate")
	@JsonProperty(value = "DeclareDate")
	private Date DeclareDate;
	/**
	 * 是否被执行，默认 = false
	 */
	@Column(name = "IsExecuted")
	@JsonProperty(value = "IsExecuted")
	private Boolean IsExecuted;
	/**
	 * 头寸对齐（结算标记）
	 */
	@Column(name = "IsSquared")
	@JsonProperty(value = "IsSquared")
	private Boolean IsSquared;
	/**
	 * 行权数量
	 */
	@Column(name = "QuantityExecuted")
	@JsonProperty(value = "QuantityExecuted")
	private BigDecimal QuantityExecuted;
	/**
	 * 交易员
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
	 * 市场标识
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
	// @JsonBackReference("Position4Broker_Market")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
	/**
	 * 商品标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	// @JsonBackReference("Position4Broker_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 经纪商标识
	 */
	@Column(name = "BrokerId")
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	// @JsonBackReference("Position4Broker_Broker")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Broker.class)
	@JoinColumn(name = "BrokerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Broker")
	private Broker Broker;
	/**
	 * 顾客标识
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
	@Column(name = "SourceId")
	@JsonProperty(value = "SourceId")
	private String SourceId;
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position.class)
//	@JoinColumn(name = "SourceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Source")
	private Position Source;
	/**
	 *
	 */
	@Column(name = "VirtualId")
	@JsonProperty(value = "VirtualId")
	private String VirtualId;
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position.class)
//	@JoinColumn(name = "VirtualId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Virtual")
	private Position Virtual;
	/**
	 * 内部实体
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
	 * 属于哪个合同
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	// @JsonBackReference("Position4Broker_Contract")
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
	// @JsonBackReference("Position4Broker_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	/**
	 * 属于哪个发票
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	// @JsonBackReference("Position4Broker_Invoice")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	/**
	 * 合约
	 */
	@Column(name = "InstrumentId")
	@JsonProperty(value = "InstrumentId")
	private String InstrumentId;
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
	 * 对应Position中的原始头寸
	 */
	@Column(name = "OriginalId")
	@JsonProperty(value = "OriginalId")
	private String OriginalId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position.class)
	@JoinColumn(name = "OriginalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Original")
	private Position Original;

	
	//十二期增加字段
    /**
	 * 交割数量
	 */
	@Column(name = "DeliveryQuantity")
	@JsonProperty(value = "DeliveryQuantity")
	private BigDecimal DeliveryQuantity;
	
	/**
	 * 交割价格(加权平均价)
	 */
	@Column(name = "DeliveryPrice")
	@JsonProperty(value = "DeliveryPrice")
	private BigDecimal DeliveryPrice;
    
	/**
	 * 最后交割时间
	 */
	@Column(name = "LastDeliveryDate")
	@JsonProperty(value = "LastDeliveryDate")
	private Date LastDeliveryDate;
	
	/**
	 * 平仓数量(已平仓数量汇总)
	 */
	@Column(name = "UnravelQuantity")
	@JsonProperty(value = "UnravelQuantity")
	private BigDecimal UnravelQuantity;
	
	
	/**
	 * 平仓价格(加权平均)
	 */
	@Column(name = "UnravelPrice")
	@JsonProperty(value = "UnravelPrice")
	private BigDecimal UnravelPrice;
	
	/**
	 * 最后平仓时间
	 */
	@Column(name = "LastUnravelDate")
	@JsonProperty(value = "LastUnravelDate")
	private Date LastUnravelDate;
	
	
	public BigDecimal getM2MPrice() {
		return M2MPrice;
	}

	public void setM2MPrice(BigDecimal M2MPrice) {
		this.M2MPrice = M2MPrice;
	}

	public BigDecimal getM2MAmount() {
		return M2MAmount;
	}

	public void setM2MAmount(BigDecimal M2MAmount) {
		this.M2MAmount = M2MAmount;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String MarketName) {
		this.MarketName = MarketName;
	}

	public String getMarketCode() {
		return MarketCode;
	}

	public void setMarketCode(String MarketCode) {
		this.MarketCode = MarketCode;
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

	public String getBrokerName() {
		return BrokerName;
	}

	public void setBrokerName(String BrokerName) {
		this.BrokerName = BrokerName;
	}

	public String getBrandNames() {
		return BrandNames;
	}

	public void setBrandNames(String BrandNames) {
		this.BrandNames = BrandNames;
	}

	public Boolean getIsDel() {
		return IsDel == null ? Boolean.FALSE : IsDel;
	}

	public void setIsDel(Boolean IsDel) {
		this.IsDel = IsDel;
	}

	public Integer getQuantityPerLot() {
		return QuantityPerLot;
	}

	public void setQuantityPerLot(Integer QuantityPerLot) {
		this.QuantityPerLot = QuantityPerLot;
	}

	public Boolean getIsSourceOfVirtual() {
		return IsSourceOfVirtual;
	}

	public void setIsSourceOfVirtual(Boolean IsSourceOfVirtual) {
		this.IsSourceOfVirtual = IsSourceOfVirtual;
	}

	public Boolean getIsVirtual() {
		return IsVirtual;
	}

	public void setIsVirtual(Boolean IsVirtual) {
		this.IsVirtual = IsVirtual;
	}

	public Boolean getIsSplitted() {
		return IsSplitted;
	}

	public void setIsSplitted(Boolean IsSplitted) {
		this.IsSplitted = IsSplitted;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getOurRef() {
		return OurRef;
	}

	public void setOurRef(String OurRef) {
		this.OurRef = OurRef;
	}

	public String getTheirRef() {
		return TheirRef;
	}

	public void setTheirRef(String TheirRef) {
		this.TheirRef = TheirRef;
	}

	public String getLS() {
		return LS;
	}

	public void setLS(String LS) {
		this.LS = LS;
	}

	public String getForwardType() {
		return ForwardType;
	}

	public void setForwardType(String ForwardType) {
		this.ForwardType = ForwardType;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getQuantityOriginal() {
		return QuantityOriginal;
	}

	public void setQuantityOriginal(BigDecimal QuantityOriginal) {
		this.QuantityOriginal = QuantityOriginal;
	}

	public BigDecimal getQuantityUnSquared() {
		return QuantityUnSquared;
	}

	public void setQuantityUnSquared(BigDecimal QuantityUnSquared) {
		this.QuantityUnSquared = QuantityUnSquared;
	}

	public BigDecimal getHands() {
		return Hands;
	}

	public void setHands(BigDecimal Hands) {
		this.Hands = Hands;
	}

	public BigDecimal getQuantityHedged() {
		return QuantityHedged;
	}

	public void setQuantityHedged(BigDecimal QuantityHedged) {
		this.QuantityHedged = QuantityHedged;
	}

	public BigDecimal getOurPrice() {
		return OurPrice;
	}

	public void setOurPrice(BigDecimal OurPrice) {
		this.OurPrice = OurPrice;
	}

	public BigDecimal getTradePrice() {
		return TradePrice;
	}

	public void setTradePrice(BigDecimal TradePrice) {
		this.TradePrice = TradePrice;
	}

	public BigDecimal getCarryDiffPrice() {
		return CarryDiffPrice;
	}

	public void setCarryDiffPrice(BigDecimal CarryDiffPrice) {
		this.CarryDiffPrice = CarryDiffPrice;
	}

	public BigDecimal getTradeFee() {
		return TradeFee;
	}

	public void setTradeFee(BigDecimal TradeFee) {
		this.TradeFee = TradeFee;
	}

	public BigDecimal getTheirPrice() {
		return TheirPrice;
	}

	public void setTheirPrice(BigDecimal TheirPrice) {
		this.TheirPrice = TheirPrice;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date PromptDate) {
		this.PromptDate = PromptDate;
	}

	public Boolean getIsCarry() {
		return IsCarry;
	}

	public void setIsCarry(Boolean IsCarry) {
		this.IsCarry = IsCarry;
	}

	public Boolean getIsSourceOfCarry() {
		return IsSourceOfCarry;
	}

	public void setIsSourceOfCarry(Boolean IsSourceOfCarry) {
		this.IsSourceOfCarry = IsSourceOfCarry;
	}

	public Boolean getIsCarryAgainst() {
		return IsCarryAgainst;
	}

	public void setIsCarryAgainst(Boolean IsCarryAgainst) {
		this.IsCarryAgainst = IsCarryAgainst;
	}

	public String getCarryRef() {
		return CarryRef;
	}

	public void setCarryRef(String CarryRef) {
		this.CarryRef = CarryRef;
	}

	public String getCarryDirection() {
		return CarryDirection;
	}

	public void setCarryDirection(String CarryDirection) {
		this.CarryDirection = CarryDirection;
	}

	public String getCarryPositionId() {
		return CarryPositionId;
	}

	public void setCarryPositionId(String CarryPositionId) {
		this.CarryPositionId = CarryPositionId;
	}

	public String getCarryCounterpart() {
		return CarryCounterpart;
	}

	public void setCarryCounterpart(String CarryCounterpart) {
		this.CarryCounterpart = CarryCounterpart;
	}

	public BigDecimal getCarryPriceDiff() {
		return CarryPriceDiff;
	}

	public void setCarryPriceDiff(BigDecimal CarryPriceDiff) {
		this.CarryPriceDiff = CarryPriceDiff;
	}

	public Boolean getIsArbitrage() {
		return IsArbitrage;
	}

	public void setIsArbitrage(Boolean IsArbitrage) {
		this.IsArbitrage = IsArbitrage;
	}

	public String getArbitrageRef() {
		return ArbitrageRef;
	}

	public void setArbitrageRef(String ArbitrageRef) {
		this.ArbitrageRef = ArbitrageRef;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String Purpose) {
		this.Purpose = Purpose;
	}

	public Boolean getIsHedged() {
		return IsHedged;
	}

	public void setIsHedged(Boolean IsHedged) {
		this.IsHedged = IsHedged;
	}

	public Boolean getIsImported() {
		return IsImported;
	}

	public void setIsImported(Boolean IsImported) {
		this.IsImported = IsImported;
	}

	public Boolean getIsAccounted() {
		return IsAccounted;
	}

	public void setIsAccounted(Boolean IsAccounted) {
		this.IsAccounted = IsAccounted;
	}

	public Boolean getIsCommissionFree() {
		return IsCommissionFree;
	}

	public void setIsCommissionFree(Boolean IsCommissionFree) {
		this.IsCommissionFree = IsCommissionFree;
	}

	public Boolean getIsElectronicTrade() {
		return IsElectronicTrade;
	}

	public void setIsElectronicTrade(Boolean IsElectronicTrade) {
		this.IsElectronicTrade = IsElectronicTrade;
	}

	public BigDecimal getCommission() {
		return Commission;
	}

	public void setCommission(BigDecimal Commission) {
		this.Commission = Commission;
	}

	public BigDecimal getCommissionOriginal() {
		return CommissionOriginal;
	}

	public void setCommissionOriginal(BigDecimal CommissionOriginal) {
		this.CommissionOriginal = CommissionOriginal;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String Code) {
		this.Code = Code;
	}

	public String getOCS() {
		return OCS;
	}

	public void setOCS(String OCS) {
		this.OCS = OCS;
	}

	public BigDecimal getSpread() {
		return Spread;
	}

	public void setSpread(BigDecimal Spread) {
		this.Spread = Spread;
	}

	public Date getAverageStartDate() {
		return AverageStartDate;
	}

	public void setAverageStartDate(Date AverageStartDate) {
		this.AverageStartDate = AverageStartDate;
	}

	public Date getAverageEndDate() {
		return AverageEndDate;
	}

	public void setAverageEndDate(Date AverageEndDate) {
		this.AverageEndDate = AverageEndDate;
	}

	public Boolean getIsPriced() {
		return IsPriced;
	}

	public void setIsPriced(Boolean IsPriced) {
		this.IsPriced = IsPriced;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal QuantityPriced) {
		this.QuantityPriced = QuantityPriced;
	}

	public String getPromptBasis() {
		return PromptBasis;
	}

	public void setPromptBasis(String PromptBasis) {
		this.PromptBasis = PromptBasis;
	}

	public Integer getPricingDays() {
		return PricingDays;
	}

	public void setPricingDays(Integer PricingDays) {
		this.PricingDays = PricingDays;
	}

	public BigDecimal getQtyPerPricingDay() {
		return QtyPerPricingDay;
	}

	public void setQtyPerPricingDay(BigDecimal QtyPerPricingDay) {
		this.QtyPerPricingDay = QtyPerPricingDay;
	}

	public String getCallPut() {
		return CallPut;
	}

	public void setCallPut(String CallPut) {
		this.CallPut = CallPut;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal Premium) {
		this.Premium = Premium;
	}

	public BigDecimal getStrikePrice() {
		return StrikePrice;
	}

	public void setStrikePrice(BigDecimal StrikePrice) {
		this.StrikePrice = StrikePrice;
	}

	public Date getDeclareDate() {
		return DeclareDate;
	}

	public void setDeclareDate(Date DeclareDate) {
		this.DeclareDate = DeclareDate;
	}

	public Boolean getIsExecuted() {
		return IsExecuted;
	}

	public void setIsExecuted(Boolean IsExecuted) {
		this.IsExecuted = IsExecuted;
	}

	public Boolean getIsSquared() {
		return IsSquared;
	}

	public void setIsSquared(Boolean IsSquared) {
		this.IsSquared = IsSquared;
	}

	public BigDecimal getQuantityExecuted() {
		return QuantityExecuted;
	}

	public void setQuantityExecuted(BigDecimal QuantityExecuted) {
		this.QuantityExecuted = QuantityExecuted;
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

	public String getMarketId() {
		return MarketId;
	}

	public void setMarketId(String MarketId) {
		this.MarketId = MarketId;
	}

	public Market getMarket() {
		return Market;
	}

	public void setMarket(Market Market) {
		this.Market = Market;
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

	public String getBrokerId() {
		return BrokerId;
	}

	public void setBrokerId(String BrokerId) {
		this.BrokerId = BrokerId;
	}

	public Broker getBroker() {
		return Broker;
	}

	public void setBroker(Broker Broker) {
		this.Broker = Broker;
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

	public String getSourceId() {
		return SourceId;
	}

	public void setSourceId(String SourceId) {
		this.SourceId = SourceId;
	}

	public Position getSource() {
		return Source;
	}

	public void setSource(Position Source) {
		this.Source = Source;
	}

	public String getVirtualId() {
		return VirtualId;
	}

	public void setVirtualId(String VirtualId) {
		this.VirtualId = VirtualId;
	}

	public Position getVirtual() {
		return Virtual;
	}

	public void setVirtual(Position Virtual) {
		this.Virtual = Virtual;
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

	public String getInstrumentId() {
		return InstrumentId;
	}

	public void setInstrumentId(String InstrumentId) {
		this.InstrumentId = InstrumentId;
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

	public String getOriginalId() {
		return OriginalId;
	}

	public void setOriginalId(String OriginalId) {
		this.OriginalId = OriginalId;
	}

	public Position getOriginal() {
		return Original;
	}

	public void setOriginal(Position Original) {
		this.Original = Original;
	}

	public List<Position> getPositionList() {
		return PositionList;
	}

	public void setPositionList(List<Position> positionList) {
		PositionList = positionList;
	}

	public String getTraderName() {
		return TraderName;
	}

	public void setTraderName(String traderName) {
		TraderName = traderName;
	}

	public String getInstrumentName() {
		return InstrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		InstrumentName = instrumentName;
	}

	public BigDecimal getQuantityBeforeChanged() {
		return QuantityBeforeChanged;
	}

	public void setQuantityBeforeChanged(BigDecimal quantityBeforeChanged) {
		QuantityBeforeChanged = quantityBeforeChanged;
	}

	public String getPositionType() {
		return PositionType;
	}

	public void setPositionType(String positionType) {
		PositionType = positionType;
	}

	public BigDecimal getDeliveryQuantity() {
		return DeliveryQuantity;
	}

	public void setDeliveryQuantity(BigDecimal deliveryQuantity) {
		DeliveryQuantity = deliveryQuantity;
	}

	public BigDecimal getDeliveryPrice() {
		return DeliveryPrice;
	}

	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		DeliveryPrice = deliveryPrice;
	}

	public Date getLastDeliveryDate() {
		return LastDeliveryDate;
	}

	public void setLastDeliveryDate(Date lastDeliveryDate) {
		LastDeliveryDate = lastDeliveryDate;
	}

	public BigDecimal getUnravelQuantity() {
		return UnravelQuantity;
	}

	public void setUnravelQuantity(BigDecimal unravelQuantity) {
		UnravelQuantity = unravelQuantity;
	}

	public BigDecimal getUnravelPrice() {
		return UnravelPrice;
	}

	public void setUnravelPrice(BigDecimal unravelPrice) {
		UnravelPrice = unravelPrice;
	}

	public Date getLastUnravelDate() {
		return LastUnravelDate;
	}

	public void setLastUnravelDate(Date lastUnravelDate) {
		LastUnravelDate = lastUnravelDate;
	}

}
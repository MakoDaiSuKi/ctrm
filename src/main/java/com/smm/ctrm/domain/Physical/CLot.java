
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
/**
 * 为发票管理列表而生
 * @author zengshihua
 *
 */
@Entity
@Table(name = "Lot", schema = "Physical")
public class CLot extends HibernateEntity {
	private static final long serialVersionUID = 1461832991343L;
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
	@JsonProperty(value = "PriceDiff")
	private BigDecimal PriceDiff;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Exposure")
	private BigDecimal Exposure;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityUnPriced")
	private BigDecimal QuantityUnPriced;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityUnInvoiced")
	private BigDecimal QuantityUnInvoiced;
	/**
	 * 记录传递到前台的数量（用于后台判断数量是否发生变更）
	 */
	@Transient
	@JsonProperty(value = "QuantityBeforeChanged")
	private BigDecimal QuantityBeforeChanged;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Digits")
	private Integer Digits = 0;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;

	/**
	 * 直接从批次付款的申请付款的净重之和
	 */
	@Column(name = "QuantityFunded")
	@JsonProperty("QuantityFunded")
	public BigDecimal QuantityFunded;

	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerShortName")
	private String CustomerShortName;
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
	@JsonProperty(value = "SpecName")
	private String SpecName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "OriginName")
	private String OriginName;
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
	@JsonProperty(value = "MajorMarketName")
	private String MajorMarketName;

	/**
	 * 被批次拆分拆出的新批次
	 */
	@Column(name = "IsSplitLot")
	public Boolean IsSplitLot;

	/**
	 * 批次拆分的原始批次
	 */
	@Column(name = "IsOriginalLot")
	public Boolean IsOriginalLot;

	/**
	 * 原始批次ID
	 */
	@Column(name = "OriginalLotId")
	public String OriginalLotId;

	/**
	 * 是否属于初始化业务数据
	 */
	@Column(name = "IsIniatiated")
	@JsonProperty(value = "IsIniatiated")
	private Boolean IsIniatiated;
	/**
	 * 品位，百分比表示，e.g.0.66 = 66%
	 */
	@Column(name = "Grade")
	@JsonProperty(value = "Grade")
	private BigDecimal Grade;
	/**
	 * 计价折扣率，百分比表示，同上
	 */
	@Column(name = "Discount")
	@JsonProperty(value = "Discount")
	private BigDecimal Discount;
	/**
	 * 是否拆分出来的
	 */
	@Column(name = "IsSplitted")
	@JsonProperty(value = "IsSplitted")
	private Boolean IsSplitted;
	/**
	 * 被拆分的来源批次
	 */
	@Column(name = "IsSourceOfSplitted")
	@JsonProperty(value = "IsSourceOfSplitted")
	private Boolean IsSourceOfSplitted;
	/**
	 * 是否回购订单
	 */
	@Column(name = "IsReBuy")
	@JsonProperty(value = "IsReBuy")
	private Boolean IsReBuy;
	/**
	 * 合同原始数量
	 */
	@Column(name = "QuantityOriginal")
	@JsonProperty(value = "QuantityOriginal")
	private BigDecimal QuantityOriginal;
	/**
	 * 分拆而来的批次Id
	 */
	@Column(name = "SplitFromId")
	@JsonProperty(value = "SplitFromId")
	private String SplitFromId;
	/**
	 * 计算溢短装率的基准{ OnQuantity, OnPercentage }
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
	 * 只应用于简单的业务场景
	 */
	@Column(name = "IsQuantityConfirmed")
	@JsonProperty(value = "IsQuantityConfirmed")
	private Boolean IsQuantityConfirmed;
	/**
	 * 背景色
	 */
	@Column(name = "MarkColor")
	@JsonProperty(value = "MarkColor")
	private Integer MarkColor;
	/**
	 * 是否允许覆盖合同编号
	 */
	@Transient
	@JsonProperty(value = "IsAllowOverrideContractNo")
	private Boolean IsAllowOverrideContractNo;
	/**
	 * 该批次的实际的开（收）票的日期
	 */
	@Column(name = "DateInvoiced")
	@JsonProperty(value = "DateInvoiced")
	private Date DateInvoiced;
	/**
	 * 该批次的开（收）票的截止日期
	 */
	@Column(name = "DateDueInvoice")
	@JsonProperty(value = "DateDueInvoice")
	private Date DateDueInvoice;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@Column(name = "FullNo")
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 合同头编号。headNo = commodity + P/S + serialNo, 类似“CUP140702001”
	 */
	@Column(name = "HeadNo", length = 64)
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
	/**
	 *
	 */
	@Column(name = "PrefixNo", length = 64)
	@JsonProperty(value = "PrefixNo")
	private String PrefixNo;
	/**
	 * 流水号,类似“140702001”
	 */
	@Column(name = "SerialNo", length = 64)
	@JsonProperty(value = "SerialNo")
	private String SerialNo;
	/**
	 * 批次编号{0, 10,20,.....}
	 */
	@Column(name = "LotNo")
	@JsonProperty(value = "LotNo")
	private Integer LotNo;
	/**
	 * 批次文档号
	 */
	@Column(name = "DocumentNo", length = 128)
	@JsonProperty(value = "DocumentNo")
	private String DocumentNo;
	/**
	 * 盈亏
	 */
	@Column(name = "PnL")
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 合同数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 约定交付的最低数量
	 */
	@Column(name = "QuantityLess")
	@JsonProperty(value = "QuantityLess")
	private BigDecimal QuantityLess;
	/**
	 * 约定交付的最多数量
	 */
	@Column(name = "QuantityMore")
	@JsonProperty(value = "QuantityMore")
	private BigDecimal QuantityMore;
	/**
	 * 实际的交付数量
	 */
	@Column(name = "QuantityDelivered")
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	/**
	 * 已经开票的净重之和
	 */
	@Column(name = "QuantityInvoiced")
	@JsonProperty(value = "QuantityInvoiced")
	private BigDecimal QuantityInvoiced;
	/**
	 * 已经开票的金额之和
	 */
	@Column(name = "AmountInvoiced")
	@JsonProperty(value = "AmountInvoiced")
	private BigDecimal AmountInvoiced;

	/**
	 * 直接从批次付款的已经付款的金额之和
	 */
	@Column(name = "AmountFunded")
	@JsonProperty(value = "AmountFunded")
	private BigDecimal AmountFunded;

	/**
	 * 直接从批次付款的已经付款的金额之和
	 */
	@Column(name = "AmountFundedDraft")
	@JsonProperty(value = "AmountFundedDraft")
	private BigDecimal AmountFundedDraft;

	/**
	 * 已经点价的数量之和
	 */
	@Column(name = "QuantityPriced")
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 已经保值的数量之和
	 */
	@Column(name = "QuantityHedged")
	@JsonProperty(value = "QuantityHedged")
	private BigDecimal QuantityHedged;
	
	
	
	 /// <summary>
    /// 批次数量-交付数量
    /// </summary>
	//@Transient
	//@JsonProperty(value = "QuantityDiff")
    //public BigDecimal QuantityDiff;
    
	/**
	 * 头寸价格（保值价格），均价
	 */
	@Column(name = "HedgedPrice")
	@JsonProperty(value = "HedgedPrice")
	private BigDecimal HedgedPrice;
	/**
	 * 可多选品牌，逗号隔开
	 */
	@Column(name = "BrandIds")
	@JsonProperty(value = "BrandIds")
	private String BrandIds;
	/**
	 *
	 */
	@Column(name = "BrandNames", length = 128)
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 * 现货交易类型，eg {采购:B，销售:S，转口:I，生产，...} (Dict)
	 */
	@Column(name = "SpotDirection", length = 2)
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 *
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status")
	private Integer Status;
	/**
	 * IsStoraged = false时，DueBalance才 = Quantity * Price
	 */
	@Column(name = "DueBalance")
	@JsonProperty(value = "DueBalance")
	private BigDecimal DueBalance;
	/**
	 * 已经发生的余额
	 */
	@Column(name = "PaidBalance")
	@JsonProperty(value = "PaidBalance")
	private BigDecimal PaidBalance;
	/**
	 * 最新余额 = 应收应付 - 已经发生
	 */
	@Column(name = "LastBalance")
	@JsonProperty(value = "LastBalance")
	private BigDecimal LastBalance;
	/**
	 * 收发货的标识
	 */
	@Column(name = "IsDelivered")
	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;
	/**
	 * 收或者开，发票的标识
	 */
	@Column(name = "IsInvoiced")
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 * 保值的标识。如果是FALSE，表示存在敞口。
	 */
	@Column(name = "IsHedged")
	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;
	/**
	 * 收付款的标识
	 */
	@Column(name = "IsFunded")
	@JsonProperty(value = "IsFunded")
	private Boolean IsFunded;
	/**
	 * 盈亏结算的标识。完成盈亏结算 不等于 会计记账
	 */
	@Column(name = "IsSettled")
	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;
	/**
	 * 会计记账的标识
	 */
	@Column(name = "IsAccounted")
	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;
	/**
	 * 点价的标识
	 */
	@Column(name = "IsPriced")
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 * 各项杂费是否已完成冲销
	 */
	@Column(name = "IsFeeEliminated")
	@JsonProperty(value = "IsFeeEliminated")
	private Boolean IsFeeEliminated;
	/**
	 * 点价方式 {PAF + FA的组合模式}
	 */
	@Column(name = "PricingType", length = 30)
	@JsonProperty(value = "PricingType")
	private String PricingType;
	/**
	 * 最终价格 Final = Price + Fee，其中：Price = Major + Premium
	 */
	@Column(name = "Final")
	@JsonProperty(value = "Final")
	private BigDecimal Final;
	/**
	 * 商品价格 Price = Major + Premium
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果
	 */
	@Column(name = "Major")
	@JsonProperty(value = "Major")
	private BigDecimal Major;
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * （3）各种杂费(未发生取预估，已发生取实际）：不论原始状态如何，都是折算成 ?/MT 的值
	 */
	@Column(name = "Fee")
	@JsonProperty(value = "Fee")
	private BigDecimal Fee;
	/**
	 * （3.1）各种杂费(实际发生)：都是折算成 ?/MT 的值
	 */
	@Column(name = "RealFee")
	@JsonProperty(value = "RealFee")
	private BigDecimal RealFee;
	/**
	 * （3.2）各种杂费(预估)：都是折算成 ?/MT 的值
	 */
	@Column(name = "EstimateFee")
	@JsonProperty(value = "EstimateFee")
	private BigDecimal EstimateFee;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 商品名称
	 */
	@Column(name = "Product", length = 64)
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 预计销售日期（QP）
	 */
	@Column(name = "EstimateSaleDate")
	@JsonProperty(value = "EstimateSaleDate")
	private Date EstimateSaleDate;
	/**
	 * 是否按照ETA船期点价
	 */
	@Column(name = "IsEtaPricing")
	@JsonProperty(value = "IsEtaPricing")
	private Boolean IsEtaPricing;
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
	 * { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
	 */
	@Column(name = "MajorType", length = 30)
	@JsonProperty(value = "MajorType")
	private String MajorType;
	/**
	 * ｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
	 */
	@Column(name = "MajorBasis", length = 30)
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;
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
	 * 主价点价时的所需天数
	 */
	@Column(name = "MajorDays")
	@JsonProperty(value = "MajorDays")
	private Integer MajorDays;
	/**
	 * 主价点价的每天数量
	 */
	@Column(name = "QtyPerMainDay")
	@JsonProperty(value = "QtyPerMainDay")
	private BigDecimal QtyPerMainDay;
	/**
	 * 升贴水为均价点价时的所需天数
	 */
	@Column(name = "PremiumDays")
	@JsonProperty(value = "PremiumDays")
	private Integer PremiumDays;
	/**
	 * 均价点价的每天数量
	 */
	@Column(name = "QtyPerPremiumDay")
	@JsonProperty(value = "QtyPerPremiumDay")
	private BigDecimal QtyPerPremiumDay;
	/**
	 * { 固定升贴水 = F(FIX)，均价升贴水 = A(AVERAGE)}(dict)
	 */
	@Column(name = "PremiumType", length = 30)
	@JsonProperty(value = "PremiumType")
	private String PremiumType;
	/**
	 * { 最低价的均价 = LOW, 最高价的均价 = HIGH, 均价 = AVERAGE}(dict)
	 */
	@Column(name = "PremiumBasis", length = 30)
	@JsonProperty(value = "PremiumBasis")
	private String PremiumBasis;
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
	 *
	 */
	@Column(name = "DeliveryTerm")
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;
	/**
	 * 装运地点
	 */
	@Column(name = "Loading", length = 64)
	@JsonProperty(value = "Loading")
	private String Loading;
	/**
	 * 卸货地点
	 */
	@Column(name = "Discharging", length = 64)
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 * 预计销售地点
	 */
	@Column(name = "EstDischarging", length = 64)
	@JsonProperty(value = "EstDischarging")
	private String EstDischarging;
	/**
	 * 估计出发日期
	 */
	@Column(name = "ETD")
	@JsonProperty(value = "ETD")
	private Date ETD;
	/**
	 * 估计到达日期
	 */
	@Column(name = "ETA")
	@JsonProperty(value = "ETA")
	private Date ETA;
	/**
	 * 实际出发日期
	 */
	@Column(name = "ATD")
	@JsonProperty(value = "ATD")
	private Date ATD;
	/**
	 * 实际到达日期
	 */
	@Column(name = "ATA")
	@JsonProperty(value = "ATA")
	private Date ATA;
	/**
	 *  
	 */
	@Column(name = "CommentsLot", length = 2000)
	@JsonProperty(value = "CommentsLot")
	private String CommentsLot;
	/**
	 * 结算日期
	 */
	@Column(name = "QP")
	@JsonProperty(value = "QP")
	private Date QP;
	/**
	 * 结算日期 --- 原始的日期。在新增记录时，COPY一个值给这个字段，供将来可能需要的查询
	 */
	@Column(name = "OriginalQP")
	@JsonProperty(value = "OriginalQP")
	private Date OriginalQP;
	/**
	 * bvi - sm之间的天然的特殊关系，bvi销售合同 + sm采购合同必须同步生成和更新
	 */
	@Column(name = "CounterpartId")
	@JsonProperty(value = "CounterpartId")
	private String CounterpartId;
	
	/**
	 * 多对一：合同
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;

	/**
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;

	/**
	 * 交易对手 = 客户或者供应商
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	
	/**
	 * 多对一：规格
	 */
	@Column(name = "SpecId")
	@JsonProperty(value = "SpecId")
	private String SpecId;
	
	/**
	 * 多对一：原产地
	 */
	@Column(name = "OriginId")
	@JsonProperty(value = "OriginId")
	private String OriginId;
	
	/**
	 * 多对一：仓库 = add by zhu yixin on 2014/8/5，为了迎合v1的需求
	 */
	@Column(name = "WarehouseId")
	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;
	
	/**
	 * 多对一：主价市场的标识
	 */
	@Column(name = "MajorMarketId")
	@JsonProperty(value = "MajorMarketId")
	private String MajorMarketId;

	
	/**
	 * 多对一：升贴水市场的标识
	 */
	@Column(name = "PremiumMarketId")
	@JsonProperty(value = "PremiumMarketId")
	private String PremiumMarketId;
	
	
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
	 * 是否退货
	 */
	@Column(name = "IsReturn")
	@JsonProperty(value = "IsReturn")
	private Boolean IsReturn;
	/**
	 * 是否退结
	 */
	@Column(name = "IsReturnEnd")
	@JsonProperty(value = "IsReturnEnd")
	private Boolean IsReturnEnd;
	/**
	 * 入帐年
	 */
	@Column(name = "AccountYear")
	@JsonProperty(value = "AccountYear")
	private Integer AccountYear;
	/**
	 * 入帐月
	 */
	@Column(name = "AccountMonth")
	@JsonProperty(value = "AccountMonth", defaultValue = "0")
	private Integer AccountMonth;

	public BigDecimal getM2MPrice() {
		return M2MPrice;
	}

	public void setM2MPrice(BigDecimal M2MPrice) {
		this.M2MPrice = M2MPrice;
	}

	public BigDecimal getPriceDiff() {
		return PriceDiff;
	}

	public void setPriceDiff(BigDecimal PriceDiff) {
		this.PriceDiff = PriceDiff;
	}

	public BigDecimal getExposure() {
		return Exposure;
	}

	public void setExposure(BigDecimal Exposure) {
		this.Exposure = Exposure;
	}

	public BigDecimal getQuantityUnPriced() {
		return QuantityUnPriced;
	}

	public void setQuantityUnPriced(BigDecimal QuantityUnPriced) {
		this.QuantityUnPriced = QuantityUnPriced;
	}

	public BigDecimal getQuantityUnInvoiced() {
		return QuantityUnInvoiced;
	}

	public void setQuantityUnInvoiced(BigDecimal QuantityUnInvoiced) {
		this.QuantityUnInvoiced = QuantityUnInvoiced;
	}

	public BigDecimal getQuantityBeforeChanged() {
		return QuantityBeforeChanged;
	}

	public void setQuantityBeforeChanged(BigDecimal QuantityBeforeChanged) {
		this.QuantityBeforeChanged = QuantityBeforeChanged;
	}

	public Integer getDigits() {
		return Digits;
	}

	public void setDigits(Integer Digits) {
		this.Digits = Digits;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getCustomerShortName() {
		return CustomerShortName;
	}

	public void setCustomerShortName(String CustomerShortName) {
		this.CustomerShortName = CustomerShortName;
	}

	public String getTraderName() {
		return TraderName;
	}

	public void setTraderName(String TraderName) {
		this.TraderName = TraderName;
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

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String SpecName) {
		this.SpecName = SpecName;
	}

	public String getOriginName() {
		return OriginName;
	}

	public void setOriginName(String OriginName) {
		this.OriginName = OriginName;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public String getMajorMarketName() {
		return MajorMarketName;
	}

	public void setMajorMarketName(String MajorMarketName) {
		this.MajorMarketName = MajorMarketName;
	}

	public Boolean getIsIniatiated() {
		return IsIniatiated;
	}

	public void setIsIniatiated(Boolean IsIniatiated) {
		this.IsIniatiated = IsIniatiated;
	}

	public BigDecimal getGrade() {
		return Grade;
	}

	public void setGrade(BigDecimal Grade) {
		this.Grade = Grade;
	}

	public BigDecimal getDiscount() {
		return Discount;
	}

	public void setDiscount(BigDecimal Discount) {
		this.Discount = Discount;
	}

	public Boolean getIsSplitted() {
		return IsSplitted;
	}

	public void setIsSplitted(Boolean IsSplitted) {
		this.IsSplitted = IsSplitted;
	}

	public Boolean getIsSourceOfSplitted() {
		return IsSourceOfSplitted;
	}

	public void setIsSourceOfSplitted(Boolean IsSourceOfSplitted) {
		this.IsSourceOfSplitted = IsSourceOfSplitted;
	}

	public Boolean getIsReBuy() {
		return IsReBuy;
	}

	public void setIsReBuy(Boolean IsReBuy) {
		this.IsReBuy = IsReBuy;
	}

	public BigDecimal getQuantityOriginal() {
		return QuantityOriginal;
	}

	public void setQuantityOriginal(BigDecimal QuantityOriginal) {
		this.QuantityOriginal = QuantityOriginal;
	}

	public String getSplitFromId() {
		return SplitFromId;
	}

	public void setSplitFromId(String SplitFromId) {
		this.SplitFromId = SplitFromId;
	}

	public String getMoreOrLessBasis() {
		return MoreOrLessBasis;
	}

	public void setMoreOrLessBasis(String MoreOrLessBasis) {
		this.MoreOrLessBasis = MoreOrLessBasis;
	}

	public BigDecimal getMoreOrLess() {
		return MoreOrLess;
	}

	public void setMoreOrLess(BigDecimal MoreOrLess) {
		this.MoreOrLess = MoreOrLess;
	}

	public Boolean getIsQuantityConfirmed() {
		return IsQuantityConfirmed != null ? IsQuantityConfirmed : false;
	}

	public void setIsQuantityConfirmed(Boolean IsQuantityConfirmed) {
		this.IsQuantityConfirmed = IsQuantityConfirmed;
	}

	public Integer getMarkColor() {
		return MarkColor;
	}

	public void setMarkColor(Integer MarkColor) {
		this.MarkColor = MarkColor;
	}

	public Boolean getIsAllowOverrideContractNo() {
		return IsAllowOverrideContractNo = Boolean.FALSE;
	}

	public void setIsAllowOverrideContractNo(Boolean IsAllowOverrideContractNo) {
		this.IsAllowOverrideContractNo = IsAllowOverrideContractNo;
	}

	public Date getDateInvoiced() {
		return DateInvoiced;
	}

	public void setDateInvoiced(Date DateInvoiced) {
		this.DateInvoiced = DateInvoiced;
	}

	public Date getDateDueInvoice() {
		return DateDueInvoice;
	}

	public void setDateDueInvoice(Date DateDueInvoice) {
		this.DateDueInvoice = DateDueInvoice;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getHeadNo() {
		return HeadNo;
	}

	public void setHeadNo(String HeadNo) {
		this.HeadNo = HeadNo;
	}

	public String getPrefixNo() {
		return PrefixNo;
	}

	public void setPrefixNo(String PrefixNo) {
		this.PrefixNo = PrefixNo;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String SerialNo) {
		this.SerialNo = SerialNo;
	}

	public Integer getLotNo() {
		return LotNo;
	}

	public void setLotNo(Integer LotNo) {
		this.LotNo = LotNo;
	}

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String DocumentNo) {
		this.DocumentNo = DocumentNo;
	}

	public BigDecimal getPnL() {
		return PnL;
	}

	public void setPnL(BigDecimal PnL) {
		this.PnL = PnL;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getQuantityLess() {
		return QuantityLess;
	}

	public void setQuantityLess(BigDecimal QuantityLess) {
		this.QuantityLess = QuantityLess;
	}

	public BigDecimal getQuantityMore() {
		return QuantityMore;
	}

	public void setQuantityMore(BigDecimal QuantityMore) {
		this.QuantityMore = QuantityMore;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal QuantityDelivered) {
		this.QuantityDelivered = QuantityDelivered;
	}

	public BigDecimal getQuantityInvoiced() {
		return QuantityInvoiced;
	}

	public void setQuantityInvoiced(BigDecimal QuantityInvoiced) {
		this.QuantityInvoiced = QuantityInvoiced;
	}

	public BigDecimal getAmountInvoiced() {
		return AmountInvoiced;
	}

	public void setAmountInvoiced(BigDecimal AmountInvoiced) {
		this.AmountInvoiced = AmountInvoiced;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal QuantityPriced) {
		this.QuantityPriced = QuantityPriced;
	}

	public BigDecimal getQuantityHedged() {
		return QuantityHedged;
	}

	public void setQuantityHedged(BigDecimal QuantityHedged) {
		this.QuantityHedged = QuantityHedged;
	}

	public BigDecimal getHedgedPrice() {
		return HedgedPrice;
	}

	public void setHedgedPrice(BigDecimal HedgedPrice) {
		this.HedgedPrice = HedgedPrice;
	}

	public String getBrandIds() {
		return BrandIds;
	}

	public void setBrandIds(String BrandIds) {
		this.BrandIds = BrandIds;
	}

	public String getBrandNames() {
		return BrandNames;
	}

	public void setBrandNames(String BrandNames) {
		this.BrandNames = BrandNames;
	}

	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String SpotDirection) {
		this.SpotDirection = SpotDirection;
	}

	public Integer getStatus() {
		return Status != null ? Status : 0;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public BigDecimal getDueBalance() {
		return DueBalance;
	}

	public void setDueBalance(BigDecimal DueBalance) {
		this.DueBalance = DueBalance;
	}

	public BigDecimal getPaidBalance() {
		return PaidBalance;
	}

	public void setPaidBalance(BigDecimal PaidBalance) {
		this.PaidBalance = PaidBalance;
	}

	public BigDecimal getLastBalance() {
		return LastBalance;
	}

	public void setLastBalance(BigDecimal LastBalance) {
		this.LastBalance = LastBalance;
	}

	public Boolean getIsDelivered() {
		return IsDelivered;
	}

	public void setIsDelivered(Boolean IsDelivered) {
		this.IsDelivered = IsDelivered;
	}

	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(Boolean IsInvoiced) {
		this.IsInvoiced = IsInvoiced;
	}

	public Boolean getIsHedged() {
		return IsHedged;
	}

	public void setIsHedged(Boolean IsHedged) {
		this.IsHedged = IsHedged;
	}

	public Boolean getIsFunded() {
		return IsFunded;
	}

	public void setIsFunded(Boolean IsFunded) {
		this.IsFunded = IsFunded;
	}

	public Boolean getIsSettled() {
		return IsSettled;
	}

	public void setIsSettled(Boolean IsSettled) {
		this.IsSettled = IsSettled;
	}

	public Boolean getIsAccounted() {
		return IsAccounted;
	}

	public void setIsAccounted(Boolean IsAccounted) {
		this.IsAccounted = IsAccounted;
	}

	public Boolean getIsPriced() {
		return IsPriced;
	}

	public void setIsPriced(Boolean IsPriced) {
		this.IsPriced = IsPriced;
	}

	public Boolean getIsFeeEliminated() {
		return IsFeeEliminated;
	}

	public void setIsFeeEliminated(Boolean IsFeeEliminated) {
		this.IsFeeEliminated = IsFeeEliminated;
	}

	public String getPricingType() {
		return PricingType;
	}

	public void setPricingType(String PricingType) {
		this.PricingType = PricingType;
	}

	public BigDecimal getFinal() {
		return Final;
	}

	public void setFinal(BigDecimal Final) {
		this.Final = Final;
	}

	public BigDecimal getPrice() {
		return Price;
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

	public BigDecimal getFee() {
		return Fee;
	}

	public void setFee(BigDecimal Fee) {
		this.Fee = Fee;
	}

	public BigDecimal getRealFee() {
		return RealFee;
	}

	public void setRealFee(BigDecimal RealFee) {
		this.RealFee = RealFee;
	}

	public BigDecimal getEstimateFee() {
		return EstimateFee;
	}

	public void setEstimateFee(BigDecimal EstimateFee) {
		this.EstimateFee = EstimateFee;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String Product) {
		this.Product = Product;
	}

	public Date getEstimateSaleDate() {
		return EstimateSaleDate;
	}

	public void setEstimateSaleDate(Date EstimateSaleDate) {
		this.EstimateSaleDate = EstimateSaleDate;
	}

	public Boolean getIsEtaPricing() {
		return IsEtaPricing;
	}

	public void setIsEtaPricing(Boolean IsEtaPricing) {
		this.IsEtaPricing = IsEtaPricing;
	}

	public String getEtaDuaration() {
		return EtaDuaration;
	}

	public void setEtaDuaration(String EtaDuaration) {
		this.EtaDuaration = EtaDuaration;
	}

	public String getEtaPrice() {
		return EtaPrice;
	}

	public void setEtaPrice(String EtaPrice) {
		this.EtaPrice = EtaPrice;
	}

	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String MajorType) {
		this.MajorType = MajorType;
	}

	public String getMajorBasis() {
		return MajorBasis;
	}

	public void setMajorBasis(String MajorBasis) {
		this.MajorBasis = MajorBasis;
	}

	public Date getMajorStartDate() {
		return MajorStartDate;
	}

	public void setMajorStartDate(Date MajorStartDate) {
		this.MajorStartDate = MajorStartDate;
	}

	public Date getMajorEndDate() {
		return MajorEndDate;
	}

	public void setMajorEndDate(Date MajorEndDate) {
		this.MajorEndDate = MajorEndDate;
	}

	public Integer getMajorDays() {
		return MajorDays;
	}

	public void setMajorDays(Integer MajorDays) {
		this.MajorDays = MajorDays;
	}

	public BigDecimal getQtyPerMainDay() {
		return QtyPerMainDay;
	}

	public void setQtyPerMainDay(BigDecimal QtyPerMainDay) {
		this.QtyPerMainDay = QtyPerMainDay;
	}

	public Integer getPremiumDays() {
		return PremiumDays;
	}

	public void setPremiumDays(Integer PremiumDays) {
		this.PremiumDays = PremiumDays;
	}

	public BigDecimal getQtyPerPremiumDay() {
		return QtyPerPremiumDay;
	}

	public void setQtyPerPremiumDay(BigDecimal QtyPerPremiumDay) {
		this.QtyPerPremiumDay = QtyPerPremiumDay;
	}

	public String getPremiumType() {
		return PremiumType;
	}

	public void setPremiumType(String PremiumType) {
		this.PremiumType = PremiumType;
	}

	public String getPremiumBasis() {
		return PremiumBasis;
	}

	public void setPremiumBasis(String PremiumBasis) {
		this.PremiumBasis = PremiumBasis;
	}

	public Date getPremiumStartDate() {
		return PremiumStartDate;
	}

	public void setPremiumStartDate(Date PremiumStartDate) {
		this.PremiumStartDate = PremiumStartDate;
	}

	public Date getPremiumEndDate() {
		return PremiumEndDate;
	}

	public void setPremiumEndDate(Date PremiumEndDate) {
		this.PremiumEndDate = PremiumEndDate;
	}

	public String getDeliveryTerm() {
		return DeliveryTerm;
	}

	public void setDeliveryTerm(String DeliveryTerm) {
		this.DeliveryTerm = DeliveryTerm;
	}

	public String getLoading() {
		return Loading;
	}

	public void setLoading(String Loading) {
		this.Loading = Loading;
	}

	public String getDischarging() {
		return Discharging;
	}

	public void setDischarging(String Discharging) {
		this.Discharging = Discharging;
	}

	public String getEstDischarging() {
		return EstDischarging;
	}

	public void setEstDischarging(String EstDischarging) {
		this.EstDischarging = EstDischarging;
	}

	public Date getETD() {
		return ETD;
	}

	public void setETD(Date ETD) {
		this.ETD = ETD;
	}

	public Date getETA() {
		return ETA;
	}

	public void setETA(Date ETA) {
		this.ETA = ETA;
	}

	public Date getATD() {
		return ATD;
	}

	public void setATD(Date ATD) {
		this.ATD = ATD;
	}

	public Date getATA() {
		return ATA;
	}

	public void setATA(Date ATA) {
		this.ATA = ATA;
	}

	public String getCommentsLot() {
		return CommentsLot;
	}

	public void setCommentsLot(String CommentsLot) {
		this.CommentsLot = CommentsLot;
	}

	public Date getQP() {
		return QP;
	}

	public void setQP(Date QP) {
		this.QP = QP;
	}

	public Date getOriginalQP() {
		return OriginalQP;
	}

	public void setOriginalQP(Date OriginalQP) {
		this.OriginalQP = OriginalQP;
	}

	public String getCounterpartId() {
		return CounterpartId;
	}

	public void setCounterpartId(String CounterpartId) {
		this.CounterpartId = CounterpartId;
	}


	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String ContractId) {
		this.ContractId = ContractId;
	}


	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String LegalId) {
		this.LegalId = LegalId;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String CustomerId) {
		this.CustomerId = CustomerId;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String SpecId) {
		this.SpecId = SpecId;
	}

	public String getOriginId() {
		return OriginId;
	}

	public void setOriginId(String OriginId) {
		this.OriginId = OriginId;
	}

	public String getWarehouseId() {
		return WarehouseId;
	}

	public void setWarehouseId(String WarehouseId) {
		this.WarehouseId = WarehouseId;
	}

	public String getMajorMarketId() {
		return MajorMarketId;
	}

	public void setMajorMarketId(String MajorMarketId) {
		this.MajorMarketId = MajorMarketId;
	}

	public String getPremiumMarketId() {
		return PremiumMarketId;
	}

	public void setPremiumMarketId(String PremiumMarketId) {
		this.PremiumMarketId = PremiumMarketId;
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

	public Boolean getIsReturn() {
		return IsReturn;
	}

	public void setIsReturn(Boolean IsReturn) {
		this.IsReturn = IsReturn;
	}

	public Boolean getIsReturnEnd() {
		return IsReturnEnd;
	}

	public void setIsReturnEnd(Boolean IsReturnEnd) {
		this.IsReturnEnd = IsReturnEnd;
	}

	public Integer getAccountYear() {
		return AccountYear;
	}

	public void setAccountYear(Integer AccountYear) {
		this.AccountYear = AccountYear;
	}

	public Integer getAccountMonth() {
		return AccountMonth;
	}

	public void setAccountMonth(Integer AccountMonth) {
		this.AccountMonth = AccountMonth;
	}

	public BigDecimal getQuantityFunded() {
		return QuantityFunded;
	}

	public void setQuantityFunded(BigDecimal quantityFunded) {
		QuantityFunded = quantityFunded;
	}

	public BigDecimal getAmountFunded() {
		return AmountFunded;
	}

	public void setAmountFunded(BigDecimal amountFunded) {
		AmountFunded = amountFunded;
	}

	public BigDecimal getAmountFundedDraft() {
		return AmountFundedDraft;
	}

	public void setAmountFundedDraft(BigDecimal amountFundedDraft) {
		AmountFundedDraft = amountFundedDraft;
	}

	public Boolean getIsSplitLot() {
		return IsSplitLot == null ? Boolean.FALSE : IsSplitLot;
	}

	public void setIsSplitLot(Boolean isSplitLot) {
		IsSplitLot = isSplitLot;
	}

	public Boolean getIsOriginalLot() {
		return IsOriginalLot == null ? Boolean.FALSE : IsOriginalLot;
	}

	public void setIsOriginalLot(Boolean isOriginalLot) {
		IsOriginalLot = isOriginalLot;
	}

	public String getOriginalLotId() {
		return OriginalLotId;
	}

	public void setOriginalLotId(String originalLotId) {
		OriginalLotId = originalLotId;
	}

	/*public BigDecimal getQuantityDiff() {
		return QuantityDiff;
	}

	public void setQuantityDiff(BigDecimal quantityDiff) {
		QuantityDiff = quantityDiff;
	}*/

}
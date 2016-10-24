
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
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;

@Entity
@Table(name = "PricingRecord", schema = "Physical")

public class PricingRecord extends HibernateEntity {
	private static final long serialVersionUID = 1461832991345L;
	
	@Transient
	@JsonProperty(value = "IsSelect")
	private boolean IsSelect;
	
	@Transient
	@JsonProperty(value = "SplitCount")
	public BigDecimal SplitCount;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "MajorMarketName")
	private String MajorMarketName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PremiumMarketName")
	private String PremiumMarketName;
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
	@JsonProperty(value = "HeadNo")
	private String HeadNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LotNo")
	private Integer LotNo;
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
	 * {O=按时, E=改期}
	 */
	@Column(name = "PriceTiming", length = 64)
	@JsonProperty(value = "PriceTiming")
	private String PriceTiming;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 点价日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 点价方。这个是由合同记载清楚的。{ 我方点价 = US, 均价点价 = MARKET, 对方点价 = COUNTERPARTY }
	 */
	@Column(name = "Pricer", length = 64)
	@JsonProperty(value = "Pricer")
	private String Pricer;
	/**
	 * 点价数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 点价类型 --- 早先放在这里的成员，没有明确具体的取值
	 */
	@Column(name = "PricingType", length = 64)
	@JsonProperty(value = "PricingType")
	private String PricingType;
	/**
	 * 不保存到数据库，每次取数据处理
	 */
	@Transient
	@JsonProperty(value = "PriceContainFee")
	private BigDecimal PriceContainFee;
	/**
	 * Price = Major + Premium + Fee
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
	 * 提前或延后的改期费用，按每重量单位计费，例如，每MT
	 */
	@Column(name = "Fee")
	@JsonProperty(value = "Fee")
	private BigDecimal Fee;
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
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	// @JsonBackReference("PricingRecord_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 主价市场的标识
	 */
	@Column(name = "MajorMarketId")
	@JsonProperty(value = "MajorMarketId")
	private String MajorMarketId;
	// @JsonBackReference("PricingRecord_MajorMarket")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MajorMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "MajorMarket")
	private Market MajorMarket;
	/**
	 * 升贴水市场的标识
	 */
	@Column(name = "PremiumMarketId")
	@JsonProperty(value = "PremiumMarketId")
	private String PremiumMarketId;
	// @JsonBackReference("PricingRecord_PremiumMarket")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "PremiumMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "PremiumMarket")
	private Market PremiumMarket;
	/**
	 * 多对一：批次
	 */
	@Column(name = "PricingId")
	@JsonProperty(value = "PricingId")
	private String PricingId;
	// @JsonBackReference("PricingRecord_Pricing")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Pricing.class)
	@JoinColumn(name = "PricingId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Pricing")
	private Pricing Pricing;
	/**
	 * 合同的标识
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	// @JsonBackReference("PricingRecord_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	/**
	 * 合同的标识
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	// @JsonBackReference("PricingRecord_Contract")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	@JoinColumn(name = "ContractId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Contract")
	private Contract Contract;
	/**
	 * 交易对手 = 客户或者供应商
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	// @JsonBackReference("PricingRecord_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	// @JsonBackReference("PricingRecord_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;

	public String getMajorMarketName() {
		return MajorMarketName;
	}

	public void setMajorMarketName(String MajorMarketName) {
		this.MajorMarketName = MajorMarketName;
	}

	public String getPremiumMarketName() {
		return PremiumMarketName;
	}

	public void setPremiumMarketName(String PremiumMarketName) {
		this.PremiumMarketName = PremiumMarketName;
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

	public Integer getLotNo() {
		return LotNo;
	}

	public void setLotNo(Integer LotNo) {
		this.LotNo = LotNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
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

	public String getPriceTiming() {
		return PriceTiming;
	}

	public void setPriceTiming(String PriceTiming) {
		this.PriceTiming = PriceTiming;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getPricer() {
		return Pricer;
	}

	public void setPricer(String Pricer) {
		this.Pricer = Pricer;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public String getPricingType() {
		return PricingType;
	}

	public void setPricingType(String PricingType) {
		this.PricingType = PricingType;
	}

	public BigDecimal getPriceContainFee() {
		return PriceContainFee;
	}

	public void setPriceContainFee(BigDecimal PriceContainFee) {
		this.PriceContainFee = PriceContainFee;
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

	public String getMajorMarketId() {
		return MajorMarketId;
	}

	public void setMajorMarketId(String MajorMarketId) {
		this.MajorMarketId = MajorMarketId;
	}

	public Market getMajorMarket() {
		return MajorMarket;
	}

	public void setMajorMarket(Market MajorMarket) {
		this.MajorMarket = MajorMarket;
	}

	public String getPremiumMarketId() {
		return PremiumMarketId;
	}

	public void setPremiumMarketId(String PremiumMarketId) {
		this.PremiumMarketId = PremiumMarketId;
	}

	public Market getPremiumMarket() {
		return PremiumMarket;
	}

	public void setPremiumMarket(Market PremiumMarket) {
		this.PremiumMarket = PremiumMarket;
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

	public BigDecimal getSplitCount() {
		return SplitCount;
	}

	public void setSplitCount(BigDecimal splitCount) {
		SplitCount = splitCount;
	}

	public boolean isIsSelect() {
		return IsSelect;
	}

	public void setIsSelect(boolean isSelect) {
		IsSelect = isSelect;
	}

}
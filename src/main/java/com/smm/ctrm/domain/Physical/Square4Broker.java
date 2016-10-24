
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
import com.smm.ctrm.domain.Basis.Market;

@Entity
@Table(name = "Square4Broker", schema = "Physical")

public class Square4Broker extends HibernateEntity {
	private static final long serialVersionUID = 1461832991345L;
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
	@JsonProperty(value = "BrokerName")
	private String BrokerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PromptDateLong")
	private Date PromptDateLong;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "PromptDateShort")
	private Date PromptDateShort;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityLong")
	private BigDecimal QuantityLong;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityShort")
	private BigDecimal QuantityShort;
	/**
	 * （需要在保存结算时同步保存到头寸记录表）
	 */
	@Transient
	@JsonProperty(value = "SplitSquarePosition")
	private Position4Broker SplitSquarePosition;
	/**
	 * 经纪商ID
	 */
	@Transient
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	/**
	 *
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@Column(name = "LS")
	@JsonProperty(value = "LS")
	private String LS;
	/**
	 *
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 *
	 */
	@Column(name = "TradeDateLong")
	@JsonProperty(value = "TradeDateLong")
	private Date TradeDateLong;
	/**
	 *
	 */
	@Column(name = "RefLong")
	@JsonProperty(value = "RefLong")
	private String RefLong;
	/**
	 *
	 */
	@Column(name = "PriceLong")
	@JsonProperty(value = "PriceLong")
	private BigDecimal PriceLong;
	/**
	 *
	 */
	@Column(name = "TradeDateShort")
	@JsonProperty(value = "TradeDateShort")
	private Date TradeDateShort;
	/**
	 *
	 */
	@Column(name = "RefShort")
	@JsonProperty(value = "RefShort")
	private String RefShort;
	/**
	 *
	 */
	@Column(name = "PriceShort")
	@JsonProperty(value = "PriceShort")
	private BigDecimal PriceShort;
	/**
	 * 结算日期。指的是后一条头寸的交易日期，是冗余
	 */
	@Column(name = "SquareDate")
	@JsonProperty(value = "SquareDate")
	private Date SquareDate;
	/**
	 * 也就是说，会牵涉到头寸的分拆的问题
	 */
	@Column(name = "PnL")
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 *
	 */
	@Column(name = "Currency")
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 
	 */
	@Column(name = "InvoicePnLId")
	@JsonProperty(value = "InvoicePnLId")
	private String InvoicePnLId;
	// @JsonBackReference("Square4Broker_InvoicePnL")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = InvoicePnL.class)
	@JoinColumn(name = "InvoicePnLId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "InvoicePnL")
	private InvoicePnL InvoicePnL;
	/**
	 * 多头头寸
	 */
	@Column(name = "LongId")
	@JsonProperty(value = "LongId")
	private String LongId;
	// @JsonBackReference("Square4Broker_Long")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position4Broker.class)
	@JoinColumn(name = "LongId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Long")
	private Position4Broker Long;
	/**
	 * 空头头寸
	 */
	@Column(name = "ShortId")
	@JsonProperty(value = "ShortId")
	private String ShortId;
	// @JsonBackReference("Square4Broker_Short")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Position4Broker.class)
	@JoinColumn(name = "ShortId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Short")
	private Position4Broker Short;
	/**
	 * 市场标识
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
	// @JsonBackReference("Square4Broker_Market")
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
	// @JsonBackReference("Square4Broker_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;

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

	public String getBrokerName() {
		return BrokerName;
	}

	public void setBrokerName(String BrokerName) {
		this.BrokerName = BrokerName;
	}

	public Date getPromptDateLong() {
		return PromptDateLong;
	}

	public void setPromptDateLong(Date PromptDateLong) {
		this.PromptDateLong = PromptDateLong;
	}

	public Date getPromptDateShort() {
		return PromptDateShort;
	}

	public void setPromptDateShort(Date PromptDateShort) {
		this.PromptDateShort = PromptDateShort;
	}

	public BigDecimal getQuantityLong() {
		return QuantityLong;
	}

	public void setQuantityLong(BigDecimal QuantityLong) {
		this.QuantityLong = QuantityLong;
	}

	public BigDecimal getQuantityShort() {
		return QuantityShort;
	}

	public void setQuantityShort(BigDecimal QuantityShort) {
		this.QuantityShort = QuantityShort;
	}

	public Position4Broker getSplitSquarePosition() {
		return SplitSquarePosition;
	}

	public void setSplitSquarePosition(Position4Broker SplitSquarePosition) {
		this.SplitSquarePosition = SplitSquarePosition;
	}

	public String getBrokerId() {
		return BrokerId;
	}

	public void setBrokerId(String BrokerId) {
		this.BrokerId = BrokerId;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public String getLS() {
		return LS;
	}

	public void setLS(String LS) {
		this.LS = LS;
	}

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date PromptDate) {
		this.PromptDate = PromptDate;
	}

	public Date getTradeDateLong() {
		return TradeDateLong;
	}

	public void setTradeDateLong(Date TradeDateLong) {
		this.TradeDateLong = TradeDateLong;
	}

	public String getRefLong() {
		return RefLong;
	}

	public void setRefLong(String RefLong) {
		this.RefLong = RefLong;
	}

	public BigDecimal getPriceLong() {
		return PriceLong;
	}

	public void setPriceLong(BigDecimal PriceLong) {
		this.PriceLong = PriceLong;
	}

	public Date getTradeDateShort() {
		return TradeDateShort;
	}

	public void setTradeDateShort(Date TradeDateShort) {
		this.TradeDateShort = TradeDateShort;
	}

	public String getRefShort() {
		return RefShort;
	}

	public void setRefShort(String RefShort) {
		this.RefShort = RefShort;
	}

	public BigDecimal getPriceShort() {
		return PriceShort;
	}

	public void setPriceShort(BigDecimal PriceShort) {
		this.PriceShort = PriceShort;
	}

	public Date getSquareDate() {
		return SquareDate;
	}

	public void setSquareDate(Date SquareDate) {
		this.SquareDate = SquareDate;
	}

	public BigDecimal getPnL() {
		return PnL;
	}

	public void setPnL(BigDecimal PnL) {
		this.PnL = PnL;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getInvoicePnLId() {
		return InvoicePnLId;
	}

	public void setInvoicePnLId(String InvoicePnLId) {
		this.InvoicePnLId = InvoicePnLId;
	}

	public InvoicePnL getInvoicePnL() {
		return InvoicePnL;
	}

	public void setInvoicePnL(InvoicePnL InvoicePnL) {
		this.InvoicePnL = InvoicePnL;
	}

	public String getLongId() {
		return LongId;
	}

	public void setLongId(String LongId) {
		this.LongId = LongId;
	}

	public Position4Broker getLong() {
		return Long;
	}

	public void setLong(Position4Broker Long) {
		this.Long = Long;
	}

	public String getShortId() {
		return ShortId;
	}

	public void setShortId(String ShortId) {
		this.ShortId = ShortId;
	}

	public Position4Broker getShort() {
		return Short;
	}

	public void setShort(Position4Broker Short) {
		this.Short = Short;
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

}

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Market;

@Entity
@Table(name = "DailyPosition", schema = "Physical")

public class DailyPosition extends HibernateEntity {
	private static final long serialVersionUID = 1461574933804L;
	/**
	 * 到期日
	 */
	@JsonProperty(value = "sPromptDate")
	private String sPromptDate;
	/**
	 * 截止日期
	 */
	@JsonProperty(value = "sEndLineDate")
	private String sEndLineDate;
	/**
	 * 经纪商
	 */
	@JsonProperty(value = "MarketName")
	private String MarketName;
	/**
	 * 经纪商
	 */
	@JsonProperty(value = "BrokerNames")
	private String BrokerNames;
	/**
	 * 品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 * 净持仓数量
	 */
	@Column(name = "ConsultQuantity")
	@JsonProperty(value = "ConsultQuantity")
	private BigDecimal ConsultQuantity;
	/**
	 * 持仓均价
	 */
	@Column(name = "PositionPrice")
	@JsonProperty(value = "PositionPrice")
	private BigDecimal PositionPrice;
	/**
	 * 结算价
	 */
	@Column(name = "BalancePrice")
	@JsonProperty(value = "BalancePrice")
	private BigDecimal BalancePrice;
	/**
	 * 浮动盈亏
	 */
	@Column(name = "FloatPnl")
	@JsonProperty(value = "FloatPnl")
	private BigDecimal FloatPnl;
	/**
	 * 浮亏比例
	 */
	@Column(name = "FloatPnlRatio")
	@JsonProperty(value = "FloatPnlRatio")
	private BigDecimal FloatPnlRatio;
	/**
	 * 经纪商标识
	 */
	@Column(name = "BrokerId")
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	// @JsonBackReference("DailyPosition_Broker")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Broker.class)
	@JoinColumn(foreignKey = @ForeignKey(name = "none"), insertable = false, updatable = false, name = "BrokerId")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Broker")
	private Broker Broker;
	/**
	 * 市场标识
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
	// @JsonBackReference("DailyPosition_Market")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(foreignKey = @ForeignKey(name = "none"), insertable = false, updatable = false, name = "MarketId")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
	/**
	 * 商品标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	// @JsonBackReference("DailyPosition_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(foreignKey = @ForeignKey(name = "none"), insertable = false, updatable = false, name = "CommodityId")
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 到期日
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 * 截止日期
	 */
	@Column(name = "EndLineDate")
	@JsonProperty(value = "EndLineDate")
	private Date EndLineDate;

	public String getSPromptDate() {
		return sPromptDate;
	}

	public void setSPromptDate(String sPromptDate) {
		this.sPromptDate = sPromptDate;
	}

	public String getSEndLineDate() {
		return sEndLineDate;
	}

	public void setSEndLineDate(String sEndLineDate) {
		this.sEndLineDate = sEndLineDate;
	}

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String MarketName) {
		this.MarketName = MarketName;
	}

	public String getBrokerNames() {
		return BrokerNames;
	}

	public void setBrokerNames(String BrokerNames) {
		this.BrokerNames = BrokerNames;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String CommodityName) {
		this.CommodityName = CommodityName;
	}

	public BigDecimal getConsultQuantity() {
		return ConsultQuantity;
	}

	public void setConsultQuantity(BigDecimal ConsultQuantity) {
		this.ConsultQuantity = ConsultQuantity;
	}

	public BigDecimal getPositionPrice() {
		return PositionPrice;
	}

	public void setPositionPrice(BigDecimal PositionPrice) {
		this.PositionPrice = PositionPrice;
	}

	public BigDecimal getBalancePrice() {
		return BalancePrice;
	}

	public void setBalancePrice(BigDecimal BalancePrice) {
		this.BalancePrice = BalancePrice;
	}

	public BigDecimal getFloatPnl() {
		return FloatPnl;
	}

	public void setFloatPnl(BigDecimal FloatPnl) {
		this.FloatPnl = FloatPnl;
	}

	public BigDecimal getFloatPnlRatio() {
		return FloatPnlRatio;
	}

	public void setFloatPnlRatio(BigDecimal FloatPnlRatio) {
		this.FloatPnlRatio = FloatPnlRatio;
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

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date PromptDate) {
		this.PromptDate = PromptDate;
	}

	public Date getEndLineDate() {
		return EndLineDate;
	}

	public void setEndLineDate(Date EndLineDate) {
		this.EndLineDate = EndLineDate;
	}

}
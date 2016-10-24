package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "PositionGather", schema = "Physical")
public class PositionGather extends HibernateEntity {
	private static final long serialVersionUID = 1473819704712L;
	
	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "InstrumentName")
	public  String InstrumentName;

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
	@JsonProperty(value = "InstrumentCode")
	private String InstrumentCode;
	/**
	 * 交易日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 经计商
	 */
	@Column(name = "BrokerId")
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	/**
	 * 合约
	 */
	@Column(name = "InstrumentId")
	@JsonProperty(value = "InstrumentId")
	private String InstrumentId;
	/**
	 * 买持仓（手数）
	 */
	@Column(name = "Lot4PositionBuy")
	@JsonProperty(value = "Lot4PositionBuy")
	private BigDecimal Lot4PositionBuy;
	/**
	 * 买持仓（数量）
	 */
	@Column(name = "Quantity4PositionBuy")
	@JsonProperty(value = "Quantity4PositionBuy")
	private BigDecimal Quantity4PositionBuy;
	/**
	 * 买均价
	 */
	@Column(name = "AvgPrice4Buy")
	@JsonProperty(value = "AvgPrice4Buy")
	private BigDecimal AvgPrice4Buy;
	/**
	 * 卖持仓（手数）
	 */
	@Column(name = "Lot4PositionSell")
	@JsonProperty(value = "Lot4PositionSell")
	private BigDecimal Lot4PositionSell;
	/**
	 * 卖持仓（数量）
	 */
	@Column(name = "Quantity4PositionSell")
	@JsonProperty(value = "Quantity4PositionSell")
	private BigDecimal Quantity4PositionSell;
	/**
	 * 卖均价
	 */
	@Column(name = "AvgPrice4Sell")
	@JsonProperty(value = "AvgPrice4Sell")
	private BigDecimal AvgPrice4Sell;
	/**
	 * 昨结算价
	 */
	@Column(name = "SettlePrice4Yesterday")
	@JsonProperty(value = "SettlePrice4Yesterday")
	private BigDecimal SettlePrice4Yesterday;
	/**
	 * 今结算价
	 */
	@Column(name = "SettlePrice4Today")
	@JsonProperty(value = "SettlePrice4Today")
	private BigDecimal SettlePrice4Today;
	/**
	 * 浮动盈亏
	 */
	@Column(name = "FloatePnl")
	@JsonProperty(value = "FloatePnl")
	private BigDecimal FloatePnl;
	/**
	 * 交易保证金
	 */
	@Column(name = "Deposit")
	@JsonProperty(value = "Deposit")
	private BigDecimal Deposit;
	/**
	 * 持仓类型 {保值, 投机, 套利}(Dict)
	 */
	@Column(name = "Purpose")
	@JsonProperty(value = "Purpose")
	private String Purpose;

	public String getBrokerName() {
		return BrokerName;
	}

	public void setBrokerName(String BrokerName) {
		this.BrokerName = BrokerName;
	}

	public String getInstrumentCode() {
		return InstrumentCode;
	}

	public void setInstrumentCode(String InstrumentCode) {
		this.InstrumentCode = InstrumentCode;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getBrokerId() {
		return BrokerId;
	}

	public void setBrokerId(String BrokerId) {
		this.BrokerId = BrokerId;
	}

	public String getInstrumentId() {
		return InstrumentId;
	}

	public void setInstrumentId(String InstrumentId) {
		this.InstrumentId = InstrumentId;
	}

	public BigDecimal getLot4PositionBuy() {
		return Lot4PositionBuy;
	}

	public void setLot4PositionBuy(BigDecimal Lot4PositionBuy) {
		this.Lot4PositionBuy = Lot4PositionBuy;
	}

	public BigDecimal getQuantity4PositionBuy() {
		return Quantity4PositionBuy;
	}

	public void setQuantity4PositionBuy(BigDecimal Quantity4PositionBuy) {
		this.Quantity4PositionBuy = Quantity4PositionBuy;
	}

	public BigDecimal getAvgPrice4Buy() {
		return AvgPrice4Buy;
	}

	public void setAvgPrice4Buy(BigDecimal AvgPrice4Buy) {
		this.AvgPrice4Buy = AvgPrice4Buy;
	}

	public BigDecimal getLot4PositionSell() {
		return Lot4PositionSell;
	}

	public void setLot4PositionSell(BigDecimal Lot4PositionSell) {
		this.Lot4PositionSell = Lot4PositionSell;
	}

	public BigDecimal getQuantity4PositionSell() {
		return Quantity4PositionSell;
	}

	public void setQuantity4PositionSell(BigDecimal Quantity4PositionSell) {
		this.Quantity4PositionSell = Quantity4PositionSell;
	}

	public BigDecimal getAvgPrice4Sell() {
		return AvgPrice4Sell;
	}

	public void setAvgPrice4Sell(BigDecimal AvgPrice4Sell) {
		this.AvgPrice4Sell = AvgPrice4Sell;
	}

	public BigDecimal getSettlePrice4Yesterday() {
		return SettlePrice4Yesterday;
	}

	public void setSettlePrice4Yesterday(BigDecimal SettlePrice4Yesterday) {
		this.SettlePrice4Yesterday = SettlePrice4Yesterday;
	}

	public BigDecimal getSettlePrice4Today() {
		return SettlePrice4Today;
	}

	public void setSettlePrice4Today(BigDecimal SettlePrice4Today) {
		this.SettlePrice4Today = SettlePrice4Today;
	}

	public BigDecimal getFloatePnl() {
		return FloatePnl;
	}

	public void setFloatePnl(BigDecimal FloatePnl) {
		this.FloatePnl = FloatePnl;
	}

	public BigDecimal getDeposit() {
		return Deposit;
	}

	public void setDeposit(BigDecimal Deposit) {
		this.Deposit = Deposit;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String Purpose) {
		this.Purpose = Purpose;
	}

	public String getInstrumentName() {
		return InstrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		InstrumentName = instrumentName;
	}

}
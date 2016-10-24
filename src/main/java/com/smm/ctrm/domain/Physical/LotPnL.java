
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "LotPnL", schema = "Physical")

public class LotPnL extends HibernateEntity {
	private static final long serialVersionUID = 1461832991343L;
	/**
	 *
	 */
	@Column(name = "ContractFullNo")
	@JsonProperty(value = "ContractFullNo")
	private String ContractFullNo;
	/**
	 *
	 */
	@Column(name = "CustomerName")
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@Column(name = "QuantitySell")
	@JsonProperty(value = "QuantitySell")
	private BigDecimal QuantitySell;
	/**
	 *
	 */
	@Column(name = "QuantityPurchase")
	@JsonProperty(value = "QuantityPurchase")
	private BigDecimal QuantityPurchase;
	/**
	 *
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 *
	 */
	@Column(name = "PriceSell")
	@JsonProperty(value = "PriceSell")
	private BigDecimal PriceSell;
	/**
	 *
	 */
	@Column(name = "PricePurchase")
	@JsonProperty(value = "PricePurchase")
	private BigDecimal PricePurchase;
	/**
	 *
	 */
	@Column(name = "AmountSell")
	@JsonProperty(value = "AmountSell")
	private BigDecimal AmountSell;
	/**
	 * 采购成本 - 仅指商品采购成本
	 */
	@Column(name = "AmountPurchase")
	@JsonProperty(value = "AmountPurchase")
	private BigDecimal AmountPurchase;
	/**
	 * 发生在（或者分摊到）该发票的综合成本，不包括商品采购的成本
	 */
	@Column(name = "Fee")
	@JsonProperty(value = "Fee")
	private BigDecimal Fee;
	/**
	 * 现货盈亏
	 */
	@Column(name = "PnL4Spot")
	@JsonProperty(value = "PnL4Spot")
	private BigDecimal PnL4Spot;
	/**
	 * 调期盈亏
	 */
	@Column(name = "PnL4Carry")
	@JsonProperty(value = "PnL4Carry")
	private BigDecimal PnL4Carry;
	/**
	 * 保值盈亏
	 */
	@Column(name = "PnL4Hedge")
	@JsonProperty(value = "PnL4Hedge")
	private BigDecimal PnL4Hedge;
	/**
	 * 合计盈亏
	 */
	@Column(name = "PnLTotal")
	@JsonProperty(value = "PnLTotal")
	private BigDecimal PnLTotal;
	/**
	 * 币别
	 */
	@Column(name = "Currency")
	@JsonProperty(value = "Currency")
	private String Currency;
	// @JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Square.class, cascade = {})
	@JoinColumn(name = "LotPnLId", foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SUBSELECT)
	@JsonProperty(value = "Squares")
	private List<Square> Squares;
	/**
	 * 属于哪个批次(销售批次）
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;

	// @JsonBackReference("LotPnL_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "Lot")
	private Lot Lot;

	/**
	 * 1-n：采购商品明细的集合
	 */
	// @JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Storage.class, cascade = {})
	@JoinColumn(name = "LotPnLId", foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SUBSELECT)
	@JsonProperty(value = "StorageIns")
	private List<Storage> StorageIns;
	/**
	 * 1-n：销售商品明细的集合
	 */
	// @JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Storage.class, cascade = {})
	@JoinColumn(name = "LotPnLId", foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SUBSELECT)
	@JsonProperty(value = "StorageOuts")
	private List<Storage> StorageOuts;
	/**
	 * 1-n：参与结算的头寸(销售批次对应的头寸和采购批次对应的头寸)
	 */
	// @JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Position.class, cascade = {})
	@JoinColumn(name = "LotPnLId", foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SUBSELECT)
	@JsonProperty(value = "Positions")
	private List<Position> Positions;

	public String getContractFullNo() {
		return ContractFullNo;
	}

	public void setContractFullNo(String ContractFullNo) {
		this.ContractFullNo = ContractFullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public BigDecimal getQuantitySell() {
		return QuantitySell;
	}

	public void setQuantitySell(BigDecimal QuantitySell) {
		this.QuantitySell = QuantitySell;
	}

	public BigDecimal getQuantityPurchase() {
		return QuantityPurchase;
	}

	public void setQuantityPurchase(BigDecimal QuantityPurchase) {
		this.QuantityPurchase = QuantityPurchase;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal Premium) {
		this.Premium = Premium;
	}

	public BigDecimal getPriceSell() {
		return PriceSell;
	}

	public void setPriceSell(BigDecimal PriceSell) {
		this.PriceSell = PriceSell;
	}

	public BigDecimal getPricePurchase() {
		return PricePurchase;
	}

	public void setPricePurchase(BigDecimal PricePurchase) {
		this.PricePurchase = PricePurchase;
	}

	public BigDecimal getAmountSell() {
		return AmountSell;
	}

	public void setAmountSell(BigDecimal AmountSell) {
		this.AmountSell = AmountSell;
	}

	public BigDecimal getAmountPurchase() {
		return AmountPurchase;
	}

	public void setAmountPurchase(BigDecimal AmountPurchase) {
		this.AmountPurchase = AmountPurchase;
	}

	public BigDecimal getFee() {
		return Fee;
	}

	public void setFee(BigDecimal Fee) {
		this.Fee = Fee;
	}

	public BigDecimal getPnL4Spot() {
		return PnL4Spot;
	}

	public void setPnL4Spot(BigDecimal PnL4Spot) {
		this.PnL4Spot = PnL4Spot;
	}

	public BigDecimal getPnL4Carry() {
		return PnL4Carry!=null?PnL4Carry:BigDecimal.ZERO;
	}

	public void setPnL4Carry(BigDecimal PnL4Carry) {
		this.PnL4Carry = PnL4Carry;
	}

	public BigDecimal getPnL4Hedge() {
		return PnL4Hedge!=null?PnL4Hedge:BigDecimal.ZERO;
	}

	public void setPnL4Hedge(BigDecimal PnL4Hedge) {
		this.PnL4Hedge = PnL4Hedge;
	}

	public BigDecimal getPnLTotal() {
		return PnLTotal;
	}

	public void setPnLTotal(BigDecimal PnLTotal) {
		this.PnLTotal = PnLTotal;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public List<Square> getSquares() {
		return Squares;
	}

	public void setSquares(List<Square> Squares) {
		this.Squares = Squares;
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

	public List<Storage> getStorageIns() {
		return StorageIns;
	}

	public void setStorageIns(List<Storage> StorageIns) {
		this.StorageIns = StorageIns;
	}

	public List<Storage> getStorageOuts() {
		return StorageOuts;
	}

	public void setStorageOuts(List<Storage> StorageOuts) {
		this.StorageOuts = StorageOuts;
	}

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> Positions) {
		this.Positions = Positions;
	}

}
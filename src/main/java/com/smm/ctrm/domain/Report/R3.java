

package com.smm.ctrm.domain.Report;

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
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Lot;
@Entity
@Table(name = "Position", schema = "Physical")
public class R3 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	/**
	 * 市场
	 */
	@Transient
	@JsonProperty(value = "MarketName")
	private String MarketName;
	/**
	 * 品种
	 */
	@Transient
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 * 单位
	 */
	@Transient
	@JsonProperty(value = "CommodityUnit")
	private String CommodityUnit;
	/**
	 * 到期日
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 * 经纪商
	 */
	@Transient
	@JsonProperty(value = "BrokerName")
	private String BrokerName;
	/**
	 * 交易日
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 合同号
	 */
	@Transient
	@JsonProperty(value = "ContractHeadNo")
	private String ContractHeadNo;
	/**
	 * 交易号
	 */
	@Column(name = "OurRef", length = 64)
	@JsonProperty(value = "OurRef")
	private String OurRef;
	/**
	 *  给客户的交易号
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
	 * 头寸重量。已经从“手数 * 每手数量”计算得出 
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 真正的价格 
	 */
	@Column(name = "OurPrice")
	@JsonProperty(value = "OurPrice")
	private BigDecimal OurPrice;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 金额
	 */
	@Transient
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 调期费用。只有LME的均价头寸，才有这个值
	 */
	@Column(name = "Spread")
	@JsonProperty(value = "Spread")
	private BigDecimal Spread;
	
	/**
	 * 经纪商标识
	 */
	@Column(name = "BrokerId")
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
	// @JsonBackReference("Position_Broker")
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Broker.class)
	@JoinColumn(name = "BrokerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	//@Transient
	@JsonProperty(value = "Broker")
	private Broker Broker;
	
	/**
	 * 商品标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;


	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
	// @JsonBackReference("Position_Market")
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Market.class)
	@JoinColumn(name = "MarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
	
	/**
	 * 属于哪个合同
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;


    // @JsonBackReference("Position_Contract")
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Contract.class)
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
	//@JsonBackReference("Position_Lot")
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	
	public String getMarketName(){
		return MarketName;
	}
	public void setMarketName(String MarketName){
		this.MarketName=MarketName;
	}
	
	public String getCommodityName(){
		return CommodityName;
	}
	public void setCommodityName(String CommodityName){
		this.CommodityName=CommodityName;
	}
	
	public String getCommodityUnit(){
		return CommodityUnit;
	}
	public void setCommodityUnit(String CommodityUnit){
		this.CommodityUnit=CommodityUnit;
	}
	
	public Date getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(Date PromptDate){
		this.PromptDate=PromptDate;
	}
	
	public String getBrokerName(){
		return BrokerName;
	}
	public void setBrokerName(String BrokerName){
		this.BrokerName=BrokerName;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getContractHeadNo(){
		return ContractHeadNo;
	}
	public void setContractHeadNo(String ContractHeadNo){
		this.ContractHeadNo=ContractHeadNo;
	}
	
	public String getOurRef(){
		return OurRef;
	}
	public void setOurRef(String OurRef){
		this.OurRef=OurRef;
	}
	
	public String getTheirRef(){
		return TheirRef;
	}
	public void setTheirRef(String TheirRef){
		this.TheirRef=TheirRef;
	}
	
	public String getLS(){
		return LS;
	}
	public void setLS(String LS){
		this.LS=LS;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getOurPrice(){
		return OurPrice;
	}
	public void setOurPrice(BigDecimal OurPrice){
		this.OurPrice=OurPrice;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
	}
	
	public BigDecimal getAmount(){
		return Amount;
	}
	public void setAmount(BigDecimal Amount){
		this.Amount=Amount;
	}
	
	public BigDecimal getSpread(){
		return Spread;
	}
	public void setSpread(BigDecimal Spread){
		this.Spread=Spread;
	}
	public String getBrokerId() {
		return BrokerId;
	}
	public void setBrokerId(String brokerId) {
		BrokerId = brokerId;
	}
	public Broker getBroker() {
		return Broker;
	}
	public void setBroker(Broker broker) {
		Broker = broker;
	}
	public String getCommodityId() {
		return CommodityId;
	}
	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}
	public Commodity getCommodity() {
		return Commodity;
	}
	public void setCommodity(Commodity commodity) {
		Commodity = commodity;
	}
	public String getMarketId() {
		return MarketId;
	}
	public void setMarketId(String marketId) {
		MarketId = marketId;
	}
	public Market getMarket() {
		return Market;
	}
	public void setMarket(Market market) {
		Market = market;
	}
	public String getContractId() {
		return ContractId;
	}
	public void setContractId(String contractId) {
		ContractId = contractId;
	}
	public Contract getContract() {
		return Contract;
	}
	public void setContract(Contract contract) {
		Contract = contract;
	}
	public String getLotId() {
		return LotId;
	}
	public void setLotId(String lotId) {
		LotId = lotId;
	}
	public Lot getLot() {
		return Lot;
	}
	public void setLot(Lot lot) {
		Lot = lot;
	}

}


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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Warehouse;
@Entity
@Table(name = "Inventory", schema = "Report")

public class Inventory extends HibernateEntity {
	private static final long serialVersionUID = 1461719402482L;
	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 计划（名义）的库存数量余额
	 */
	@Column(name = "QtyScheduledBalance")
	@JsonProperty(value = "QtyScheduledBalance")
	private BigDecimal QtyScheduledBalance;
	/**
	 * 计划入 = 名义入的数量余额
	 */
	@Column(name = "QtyScheduledIn")
	@JsonProperty(value = "QtyScheduledIn")
	private BigDecimal QtyScheduledIn;
	/**
	 * 计划出 = 名义出的数量余额
	 */
	@Column(name = "QtyScheduledOut")
	@JsonProperty(value = "QtyScheduledOut")
	private BigDecimal QtyScheduledOut;
	/**
	 * 在途的库存数量的余额
	 */
	@Column(name = "QtyInTransitBalance")
	@JsonProperty(value = "QtyInTransitBalance")
	private BigDecimal QtyInTransitBalance;
	/**
	 * 在途入
	 */
	@Column(name = "QtyInTransitIn")
	@JsonProperty(value = "QtyInTransitIn")
	private BigDecimal QtyInTransitIn;
	/**
	 * 在途出
	 */
	@Column(name = "QtyInTransitOut")
	@JsonProperty(value = "QtyInTransitOut")
	private BigDecimal QtyInTransitOut;
	/**
	 * 实际发生的库存数量的余额
	 */
	@Column(name = "QtyOccuredBalance")
	@JsonProperty(value = "QtyOccuredBalance")
	private BigDecimal QtyOccuredBalance;
	/**
	 * 实际进
	 */
	@Column(name = "QtyOccuredIn")
	@JsonProperty(value = "QtyOccuredIn")
	private BigDecimal QtyOccuredIn;
	/**
	 * 实际出
	 */
	@Column(name = "QtyOccuredOut")
	@JsonProperty(value = "QtyOccuredOut")
	private BigDecimal QtyOccuredOut;
	/**
	 * 全部的库存数量的余额 QtyTotalBalance = QtyScheduledBalance + QtyInTransitBalance + QtyOccuredBalance
	 */
	@Column(name = "QtyTotalBalance")
	@JsonProperty(value = "QtyTotalBalance")
	private BigDecimal QtyTotalBalance;
	/**
	 * 全部入
	 */
	@Column(name = "QtyTotalIn")
	@JsonProperty(value = "QtyTotalIn")
	private BigDecimal QtyTotalIn;
	/**
	 * 全部出
	 */
	@Column(name = "QtyTotalOut")
	@JsonProperty(value = "QtyTotalOut")
	private BigDecimal QtyTotalOut;
	/**
	 * 计划出入的库存金额的余额
	 */
	@Column(name = "AmtScheduledBalance")
	@JsonProperty(value = "AmtScheduledBalance")
	private BigDecimal AmtScheduledBalance;
	/**
	 * 在途的库存金额的余额
	 */
	@Column(name = "AmtInTransitBalance")
	@JsonProperty(value = "AmtInTransitBalance")
	private BigDecimal AmtInTransitBalance;
	/**
	 * 实际发生的库存金额的余额
	 */
	@Column(name = "AmtOccuredBalance")
	@JsonProperty(value = "AmtOccuredBalance")
	private BigDecimal AmtOccuredBalance;
	/**
	 * 全部的库存数量的余额 AmtTotalBalance = AmtScheduledBalance + AmtInTransitBalance + AmtOccuredBalance
	 */
	@Column(name = "AmtTotalBalance")
	@JsonProperty(value = "AmtTotalBalance")
	private BigDecimal AmtTotalBalance;
	/**
	 *  内部台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("Inventory_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("Inventory_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 品牌
	 */
	@Column(name = "BrandId")
	@JsonProperty(value = "BrandId")
	private String BrandId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Brand.class)
	@JoinColumn(name = "BrandId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Brand")
	private Brand Brand;
	/**
	 * 仓库
	 */
	@Column(name = "WarehouseId")
	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;
//	@JsonBackReference("Inventory_Warehouse")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@JoinColumn(name = "WarehouseId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Warehouse")
	private Warehouse Warehouse;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getQtyScheduledBalance(){
		return QtyScheduledBalance;
	}
	public void setQtyScheduledBalance(BigDecimal QtyScheduledBalance){
		this.QtyScheduledBalance=QtyScheduledBalance;
	}
	
	public BigDecimal getQtyScheduledIn(){
		return QtyScheduledIn;
	}
	public void setQtyScheduledIn(BigDecimal QtyScheduledIn){
		this.QtyScheduledIn=QtyScheduledIn;
	}
	
	public BigDecimal getQtyScheduledOut(){
		return QtyScheduledOut;
	}
	public void setQtyScheduledOut(BigDecimal QtyScheduledOut){
		this.QtyScheduledOut=QtyScheduledOut;
	}
	
	public BigDecimal getQtyInTransitBalance(){
		return QtyInTransitBalance;
	}
	public void setQtyInTransitBalance(BigDecimal QtyInTransitBalance){
		this.QtyInTransitBalance=QtyInTransitBalance;
	}
	
	public BigDecimal getQtyInTransitIn(){
		return QtyInTransitIn;
	}
	public void setQtyInTransitIn(BigDecimal QtyInTransitIn){
		this.QtyInTransitIn=QtyInTransitIn;
	}
	
	public BigDecimal getQtyInTransitOut(){
		return QtyInTransitOut;
	}
	public void setQtyInTransitOut(BigDecimal QtyInTransitOut){
		this.QtyInTransitOut=QtyInTransitOut;
	}
	
	public BigDecimal getQtyOccuredBalance(){
		return QtyOccuredBalance;
	}
	public void setQtyOccuredBalance(BigDecimal QtyOccuredBalance){
		this.QtyOccuredBalance=QtyOccuredBalance;
	}
	
	public BigDecimal getQtyOccuredIn(){
		return QtyOccuredIn;
	}
	public void setQtyOccuredIn(BigDecimal QtyOccuredIn){
		this.QtyOccuredIn=QtyOccuredIn;
	}
	
	public BigDecimal getQtyOccuredOut(){
		return QtyOccuredOut;
	}
	public void setQtyOccuredOut(BigDecimal QtyOccuredOut){
		this.QtyOccuredOut=QtyOccuredOut;
	}
	
	public BigDecimal getQtyTotalBalance(){
		return QtyTotalBalance;
	}
	public void setQtyTotalBalance(BigDecimal QtyTotalBalance){
		this.QtyTotalBalance=QtyTotalBalance;
	}
	
	public BigDecimal getQtyTotalIn(){
		return QtyTotalIn;
	}
	public void setQtyTotalIn(BigDecimal QtyTotalIn){
		this.QtyTotalIn=QtyTotalIn;
	}
	
	public BigDecimal getQtyTotalOut(){
		return QtyTotalOut;
	}
	public void setQtyTotalOut(BigDecimal QtyTotalOut){
		this.QtyTotalOut=QtyTotalOut;
	}
	
	public BigDecimal getAmtScheduledBalance(){
		return AmtScheduledBalance;
	}
	public void setAmtScheduledBalance(BigDecimal AmtScheduledBalance){
		this.AmtScheduledBalance=AmtScheduledBalance;
	}
	
	public BigDecimal getAmtInTransitBalance(){
		return AmtInTransitBalance;
	}
	public void setAmtInTransitBalance(BigDecimal AmtInTransitBalance){
		this.AmtInTransitBalance=AmtInTransitBalance;
	}
	
	public BigDecimal getAmtOccuredBalance(){
		return AmtOccuredBalance;
	}
	public void setAmtOccuredBalance(BigDecimal AmtOccuredBalance){
		this.AmtOccuredBalance=AmtOccuredBalance;
	}
	
	public BigDecimal getAmtTotalBalance(){
		return AmtTotalBalance;
	}
	public void setAmtTotalBalance(BigDecimal AmtTotalBalance){
		this.AmtTotalBalance=AmtTotalBalance;
	}
	
	public String getLegalId(){
		return LegalId;
	}
	public void setLegalId(String LegalId){
		this.LegalId=LegalId;
	}
	
	public Legal getLegal(){
		return Legal;
	}
	public void setLegal(Legal Legal){
		this.Legal=Legal;
	}
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public Commodity getCommodity(){
		return Commodity;
	}
	public void setCommodity(Commodity Commodity){
		this.Commodity=Commodity;
	}
	
	public String getBrandId(){
		return BrandId;
	}
	public void setBrandId(String BrandId){
		this.BrandId=BrandId;
	}
	
	public Brand getBrand(){
		return Brand;
	}
	public void setBrand(Brand Brand){
		this.Brand=Brand;
	}
	
	public String getWarehouseId(){
		return WarehouseId;
	}
	public void setWarehouseId(String WarehouseId){
		this.WarehouseId=WarehouseId;
	}
	
	public Warehouse getWarehouse(){
		return Warehouse;
	}
	public void setWarehouse(Warehouse Warehouse){
		this.Warehouse=Warehouse;
	}

}
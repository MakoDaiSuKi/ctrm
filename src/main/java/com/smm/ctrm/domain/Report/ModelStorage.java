

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

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
@Table(name = "ModelStorage", schema = "Report")

public class ModelStorage extends HibernateEntity {
	private static final long serialVersionUID = 1461719402485L;
	/**
	 *
	 */
	@JsonProperty(value = "Card")
	private String Card;
	/**
	 *
	 */
	@JsonProperty(value = "QtyNotionalIn")
	private BigDecimal QtyNotionalIn;
	/**
	 * 名义空头
	 */
	@JsonProperty(value = "QtyNotionalOut")
	private BigDecimal QtyNotionalOut;
	/**
	 * 名义余额
	 */
	@JsonProperty(value = "QtyNotionalBalance")
	private BigDecimal QtyNotionalBalance;
	/**
	 * 在途多头
	 */
	@JsonProperty(value = "QtyTransitIn")
	private BigDecimal QtyTransitIn;
	/**
	 * 在途空头
	 */
	@JsonProperty(value = "QtyTransitOut")
	private BigDecimal QtyTransitOut;
	/**
	 * 在途余额
	 */
	@JsonProperty(value = "QtyTransitBalance")
	private BigDecimal QtyTransitBalance;
	/**
	 * 实际入库数量
	 */
	@JsonProperty(value = "QtyStorageIn")
	private BigDecimal QtyStorageIn;
	/**
	 * 实际出库数量
	 */
	@JsonProperty(value = "QtyStorageOut")
	private BigDecimal QtyStorageOut;
	/**
	 * （实际入库数量） - （实际出库数量）
	 */
	@JsonProperty(value = "QtyStorageBalance")
	private BigDecimal QtyStorageBalance;
	/**
	 *
	 */
	@JsonProperty(value = "QtyInTotal")
	private BigDecimal QtyInTotal;
	/**
	 *
	 */
	@JsonProperty(value = "QtyOutTotal")
	private BigDecimal QtyOutTotal;
	/**
	 *
	 */
	@JsonProperty(value = "QtyTotal")
	private BigDecimal QtyTotal;
	/**
	 *
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 *
	 */
	@JsonProperty(value = "AmountNotional")
	private BigDecimal AmountNotional;
	/**
	 *
	 */
	@JsonProperty(value = "AmountTransit")
	private BigDecimal AmountTransit;
	/**
	 *
	 */
	@JsonProperty(value = "AmountStorage")
	private BigDecimal AmountStorage;
	/**
	 *
	 */
	@JsonProperty(value = "AmountTotal")
	private BigDecimal AmountTotal;
	/**
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("ModelStorage_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 *  品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("ModelStorage_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 *
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
	 *
	 */
	@Column(name = "WarehouseId")
	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;
//	@JsonBackReference("ModelStorage_Warehouse")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@JoinColumn(name = "WarehouseId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Warehouse")
	private Warehouse Warehouse;
	
	public String getCard(){
		return Card;
	}
	public void setCard(String Card){
		this.Card=Card;
	}
	
	public BigDecimal getQtyNotionalIn(){
		return QtyNotionalIn;
	}
	public void setQtyNotionalIn(BigDecimal QtyNotionalIn){
		this.QtyNotionalIn=QtyNotionalIn;
	}
	
	public BigDecimal getQtyNotionalOut(){
		return QtyNotionalOut;
	}
	public void setQtyNotionalOut(BigDecimal QtyNotionalOut){
		this.QtyNotionalOut=QtyNotionalOut;
	}
	
	public BigDecimal getQtyNotionalBalance(){
		return QtyNotionalBalance;
	}
	public void setQtyNotionalBalance(BigDecimal QtyNotionalBalance){
		this.QtyNotionalBalance=QtyNotionalBalance;
	}
	
	public BigDecimal getQtyTransitIn(){
		return QtyTransitIn;
	}
	public void setQtyTransitIn(BigDecimal QtyTransitIn){
		this.QtyTransitIn=QtyTransitIn;
	}
	
	public BigDecimal getQtyTransitOut(){
		return QtyTransitOut;
	}
	public void setQtyTransitOut(BigDecimal QtyTransitOut){
		this.QtyTransitOut=QtyTransitOut;
	}
	
	public BigDecimal getQtyTransitBalance(){
		return QtyTransitBalance;
	}
	public void setQtyTransitBalance(BigDecimal QtyTransitBalance){
		this.QtyTransitBalance=QtyTransitBalance;
	}
	
	public BigDecimal getQtyStorageIn(){
		return QtyStorageIn;
	}
	public void setQtyStorageIn(BigDecimal QtyStorageIn){
		this.QtyStorageIn=QtyStorageIn;
	}
	
	public BigDecimal getQtyStorageOut(){
		return QtyStorageOut;
	}
	public void setQtyStorageOut(BigDecimal QtyStorageOut){
		this.QtyStorageOut=QtyStorageOut;
	}
	
	public BigDecimal getQtyStorageBalance(){
		return QtyStorageBalance;
	}
	public void setQtyStorageBalance(BigDecimal QtyStorageBalance){
		this.QtyStorageBalance=QtyStorageBalance;
	}
	
	public BigDecimal getQtyInTotal(){
		return QtyInTotal;
	}
	public void setQtyInTotal(BigDecimal QtyInTotal){
		this.QtyInTotal=QtyInTotal;
	}
	
	public BigDecimal getQtyOutTotal(){
		return QtyOutTotal;
	}
	public void setQtyOutTotal(BigDecimal QtyOutTotal){
		this.QtyOutTotal=QtyOutTotal;
	}
	
	public BigDecimal getQtyTotal(){
		return QtyTotal;
	}
	public void setQtyTotal(BigDecimal QtyTotal){
		this.QtyTotal=QtyTotal;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getAmountNotional(){
		return AmountNotional;
	}
	public void setAmountNotional(BigDecimal AmountNotional){
		this.AmountNotional=AmountNotional;
	}
	
	public BigDecimal getAmountTransit(){
		return AmountTransit;
	}
	public void setAmountTransit(BigDecimal AmountTransit){
		this.AmountTransit=AmountTransit;
	}
	
	public BigDecimal getAmountStorage(){
		return AmountStorage;
	}
	public void setAmountStorage(BigDecimal AmountStorage){
		this.AmountStorage=AmountStorage;
	}
	
	public BigDecimal getAmountTotal(){
		return AmountTotal;
	}
	public void setAmountTotal(BigDecimal AmountTotal){
		this.AmountTotal=AmountTotal;
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
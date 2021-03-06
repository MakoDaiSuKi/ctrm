

package com.smm.ctrm.domain.Maintain;

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
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.User;
@Entity
@Table(name = "LMB", schema="Maintain")

public class LMB extends HibernateEntity {
	private static final long serialVersionUID = 1461832991338L;
	/**
	 * 交易日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 
	 */
	@Column(name = "PriceLow")
	@JsonProperty(value = "PriceLow")
	private BigDecimal PriceLow;
	/**
	 * 
	 */
	@Column(name = "PriceAverage")
	@JsonProperty(value = "PriceAverage")
	private BigDecimal PriceAverage;
	/**
	 * 
	 */
	@Column(name = "PriceHigh")
	@JsonProperty(value = "PriceHigh")
	private BigDecimal PriceHigh;
	/**
	 * 市场标识
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
//	@JsonBackReference("LMB_Market")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
	/**
	 * 品种标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("LMB_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 规格
	 */
	@Column(name = "SpecId")
	@JsonProperty(value = "SpecId")
	private String SpecId;
//	@JsonBackReference("LMB_Spec")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Spec.class)
	@JoinColumn(name = "SpecId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Spec")
	private Spec Spec;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getPriceLow(){
		return PriceLow;
	}
	public void setPriceLow(BigDecimal PriceLow){
		this.PriceLow=PriceLow;
	}
	
	public BigDecimal getPriceAverage(){
		return PriceAverage;
	}
	public void setPriceAverage(BigDecimal PriceAverage){
		this.PriceAverage=PriceAverage;
	}
	
	public BigDecimal getPriceHigh(){
		return PriceHigh;
	}
	public void setPriceHigh(BigDecimal PriceHigh){
		this.PriceHigh=PriceHigh;
	}
	
	public String getMarketId(){
		return MarketId;
	}
	public void setMarketId(String MarketId){
		this.MarketId=MarketId;
	}
	
	public Market getMarket(){
		return Market;
	}
	public void setMarket(Market Market){
		this.Market=Market;
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
	
	public String getSpecId(){
		return SpecId;
	}
	public void setSpecId(String SpecId){
		this.SpecId=SpecId;
	}
	
	public Spec getSpec(){
		return Spec;
	}
	public void setSpec(Spec Spec){
		this.Spec=Spec;
	}
	
	public String getCreatedId(){
		return CreatedId;
	}
	public void setCreatedId(String CreatedId){
		this.CreatedId=CreatedId;
	}
	
	public User getCreated(){
		return Created;
	}
	public void setCreated(User Created){
		this.Created=Created;
	}
	
	public String getUpdatedId(){
		return UpdatedId;
	}
	public void setUpdatedId(String UpdatedId){
		this.UpdatedId=UpdatedId;
	}
	
	public User getUpdated(){
		return Updated;
	}
	public void setUpdated(User Updated){
		this.Updated=Updated;
	}

}


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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.User;
@Entity
@Table(name = "FetchSFE", schema="Maintain")

public class FetchSFE extends HibernateEntity {
	private static final long serialVersionUID = 1461832991338L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityIndex")
	private Integer CommodityIndex;
	/**
	 * 结算日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 上期所每天的价格_1：日结算价格
	 */
	@Column(name = "PriceSettle")
	@JsonProperty(value = "PriceSettle")
	private BigDecimal PriceSettle;
	/**
	 * 上期所每天的价格_2：日加权平均价格
	 */
	@Column(name = "PriceWeighted")
	@JsonProperty(value = "PriceWeighted")
	private BigDecimal PriceWeighted;
	/**
	 * 当天最低价
	 */
	@Column(name = "Low")
	@JsonProperty(value = "Low")
	private BigDecimal Low;
	/**
	 * 当天均价
	 */
	@Column(name = "Average")
	@JsonProperty(value = "Average")
	private BigDecimal Average;
	/**
	 * 当天最高价
	 */
	@Column(name = "High")
	@JsonProperty(value = "High")
	private BigDecimal High;
	/**
	 * 品种标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("FetchSFE_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
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
	
	public Integer getCommodityIndex(){
		return CommodityIndex;
	}
	public void setCommodityIndex(Integer CommodityIndex){
		this.CommodityIndex=CommodityIndex;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getPriceSettle(){
		return PriceSettle;
	}
	public void setPriceSettle(BigDecimal PriceSettle){
		this.PriceSettle=PriceSettle;
	}
	
	public BigDecimal getPriceWeighted(){
		return PriceWeighted;
	}
	public void setPriceWeighted(BigDecimal PriceWeighted){
		this.PriceWeighted=PriceWeighted;
	}
	
	public BigDecimal getLow(){
		return Low;
	}
	public void setLow(BigDecimal Low){
		this.Low=Low;
	}
	
	public BigDecimal getAverage(){
		return Average;
	}
	public void setAverage(BigDecimal Average){
		this.Average=Average;
	}
	
	public BigDecimal getHigh(){
		return High;
	}
	public void setHigh(BigDecimal High){
		this.High=High;
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
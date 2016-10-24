

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
@Table(name = "FetchLME", schema="Maintain")

public class FetchLME extends HibernateEntity {
	private static final long serialVersionUID = 1461832991338L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityIndex")
	private Integer CommodityIndex;
	/**
	 * 交易日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * LME每天的现货价格
	 */
	@Column(name = "CashSell")
	@JsonProperty(value = "CashSell")
	private BigDecimal CashSell;
	/**
	 * LME每天的现货价格
	 */
	@Column(name = "CashBuy")
	@JsonProperty(value = "CashBuy")
	private BigDecimal CashBuy;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M3Sell")
	@JsonProperty(value = "M3Sell")
	private BigDecimal M3Sell;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M3Buy")
	@JsonProperty(value = "M3Buy")
	private BigDecimal M3Buy;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M15Sell")
	@JsonProperty(value = "M15Sell")
	private BigDecimal M15Sell;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M15Buy")
	@JsonProperty(value = "M15Buy")
	private BigDecimal M15Buy;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M27Sell")
	@JsonProperty(value = "M27Sell")
	private BigDecimal M27Sell;
	/**
	 * LME每天的3月价格
	 */
	@Column(name = "M27Buy")
	@JsonProperty(value = "M27Buy")
	private BigDecimal M27Buy;
	/**
	 * 品种类别
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("FetchLME_Commodity")
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
	
	public BigDecimal getCashSell(){
		return CashSell;
	}
	public void setCashSell(BigDecimal CashSell){
		this.CashSell=CashSell;
	}
	
	public BigDecimal getCashBuy(){
		return CashBuy;
	}
	public void setCashBuy(BigDecimal CashBuy){
		this.CashBuy=CashBuy;
	}
	
	public BigDecimal getM3Sell(){
		return M3Sell;
	}
	public void setM3Sell(BigDecimal M3Sell){
		this.M3Sell=M3Sell;
	}
	
	public BigDecimal getM3Buy(){
		return M3Buy;
	}
	public void setM3Buy(BigDecimal M3Buy){
		this.M3Buy=M3Buy;
	}
	
	public BigDecimal getM15Sell(){
		return M15Sell;
	}
	public void setM15Sell(BigDecimal M15Sell){
		this.M15Sell=M15Sell;
	}
	
	public BigDecimal getM15Buy(){
		return M15Buy;
	}
	public void setM15Buy(BigDecimal M15Buy){
		this.M15Buy=M15Buy;
	}
	
	public BigDecimal getM27Sell(){
		return M27Sell;
	}
	public void setM27Sell(BigDecimal M27Sell){
		this.M27Sell=M27Sell;
	}
	
	public BigDecimal getM27Buy(){
		return M27Buy;
	}
	public void setM27Buy(BigDecimal M27Buy){
		this.M27Buy=M27Buy;
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
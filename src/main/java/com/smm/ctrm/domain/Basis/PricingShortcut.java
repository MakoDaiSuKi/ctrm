

package com.smm.ctrm.domain.Basis;

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
@Entity
@Table(name = "PricingShortcut", schema="Basis")

public class PricingShortcut extends HibernateEntity {
	private static final long serialVersionUID = 1461832991334L;
	/**
	 * {上期所2013/12结算均价 + 同期长江最高均价, 上期所2013/12结算均价 + 同期长江最低均价, 上期所2013/12结算均价 + 同期长江均价}
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * { 点价 = PRICING，均价 = AVERAGE，固定价 = FIX} (dict)
	 */
	@Column(name = "MajorType", length = 30)
	@JsonProperty(value = "MajorType")
	private String MajorType;
	/**
	 *｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
	 */
	@Column(name = "MajorBasis", length = 30)
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;
	/**
	 * 上期所开始日期
	 */
	@Column(name = "MajorStartDate")
	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;
	/**
	 * 上期所结束日期
	 */
	@Column(name = "MajorEndDate")
	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;
	/**
	 * { 固定升贴水 = FIX，均价升贴水 = AVERAGE}(dict)
	 */
	@Column(name = "PremiumType", length = 30)
	@JsonProperty(value = "PremiumType")
	private String PremiumType;
	/**
	 * { 最低价的均价 = LOW,  最高价的均价 = HIGH, 均价 = AVERAGE}(dict)
	 */
	@Column(name = "PremiumBasis", length = 30)
	@JsonProperty(value = "PremiumBasis")
	private String PremiumBasis;
	/**
	 * 开始日期
	 */
	@Column(name = "PremiumStartDate")
	@JsonProperty(value = "PremiumStartDate")
	private Date PremiumStartDate;
	/**
	 * 结束日期
	 */
	@Column(name = "PremiumEndDate")
	@JsonProperty(value = "PremiumEndDate")
	private Date PremiumEndDate;
	/**
	 *  备份
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *  排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex" ,defaultValue="0")
	private Integer OrderIndex;
	/**
	 * 主要价格的市场的标识
	 */
	@Column(name = "MajorMarketId")
	@JsonProperty(value = "MajorMarketId")
	private String MajorMarketId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MajorMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "MajorMarke")
	private Market MajorMarke;
	/**
	 * 升贴水的市场的标识
	 */
	@Column(name = "PremiumMarketId")
	@JsonProperty(value = "PremiumMarketId")
	private String PremiumMarketId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "PremiumMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "PremiumMarket")
	private Market PremiumMarket;
	/**
	 *  品种标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getMajorType(){
		return MajorType;
	}
	public void setMajorType(String MajorType){
		this.MajorType=MajorType;
	}
	
	public String getMajorBasis(){
		return MajorBasis;
	}
	public void setMajorBasis(String MajorBasis){
		this.MajorBasis=MajorBasis;
	}
	
	public Date getMajorStartDate(){
		return MajorStartDate;
	}
	public void setMajorStartDate(Date MajorStartDate){
		this.MajorStartDate=MajorStartDate;
	}
	
	public Date getMajorEndDate(){
		return MajorEndDate;
	}
	public void setMajorEndDate(Date MajorEndDate){
		this.MajorEndDate=MajorEndDate;
	}
	
	public String getPremiumType(){
		return PremiumType;
	}
	public void setPremiumType(String PremiumType){
		this.PremiumType=PremiumType;
	}
	
	public String getPremiumBasis(){
		return PremiumBasis;
	}
	public void setPremiumBasis(String PremiumBasis){
		this.PremiumBasis=PremiumBasis;
	}
	
	public Date getPremiumStartDate(){
		return PremiumStartDate;
	}
	public void setPremiumStartDate(Date PremiumStartDate){
		this.PremiumStartDate=PremiumStartDate;
	}
	
	public Date getPremiumEndDate(){
		return PremiumEndDate;
	}
	public void setPremiumEndDate(Date PremiumEndDate){
		this.PremiumEndDate=PremiumEndDate;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}
	
	public String getMajorMarketId(){
		return MajorMarketId;
	}
	public void setMajorMarketId(String MajorMarketId){
		this.MajorMarketId=MajorMarketId;
	}
	
	public Market getMajorMarke(){
		return MajorMarke;
	}
	public void setMajorMarke(Market MajorMarke){
		this.MajorMarke=MajorMarke;
	}
	
	public String getPremiumMarketId(){
		return PremiumMarketId;
	}
	public void setPremiumMarketId(String PremiumMarketId){
		this.PremiumMarketId=PremiumMarketId;
	}
	
	public Market getPremiumMarket(){
		return PremiumMarket;
	}
	public void setPremiumMarket(Market PremiumMarket){
		this.PremiumMarket=PremiumMarket;
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

}
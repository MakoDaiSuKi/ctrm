

package com.smm.ctrm.domain.Maintain;

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
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.User;


/**
 * 交易日历
 */
@Entity
@Table(name = "Calendar", schema="Maintain")
public class Calendar extends HibernateEntity {
	private static final long serialVersionUID = 1461832991337L;
	/**
	 * 交易日历
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 是否交易日
	 */
	@Column(name = "IsTrade")
	@JsonProperty(value = "IsTrade")
	private Boolean IsTrade;
	/**
	 * 市场标识
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
//	@JsonBackReference("Calendar_Market")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
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
	
	public Boolean getIsTrade(){
		return IsTrade;
	}
	public void setIsTrade(Boolean IsTrade){
		this.IsTrade=IsTrade;
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
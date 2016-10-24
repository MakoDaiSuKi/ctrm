
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "Portfolio", schema = "Physical")

public class Portfolio extends HibernateEntity {
	private static final long serialVersionUID = 1461832991344L;
	/**
	 * 分配日期，即保值日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 保值（组合）的名称
	 */
	@Column(name = "Name", length = 64)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 策略选择 (Dict)
	 */
	@Column(name = "Strategy", length = 64)
	@JsonProperty(value = "Strategy")
	private String Strategy;
	/**
	 * 保值比例
	 */
	@Column(name = "HedgeRate")
	@JsonProperty(value = "HedgeRate")
	private BigDecimal HedgeRate;
	/**
	 * 美元-人民币的比价，在6左右
	 */
	@Column(name = "CurrencyRate")
	@JsonProperty(value = "CurrencyRate")
	private BigDecimal CurrencyRate;
	/**
	 * 现货升贴水
	 */
	@Column(name = "SpotPremium")
	@JsonProperty(value = "SpotPremium")
	private BigDecimal SpotPremium;
	/**
	 * 资金成本，记账本位币的年化利率，%为单位。
	 */
	@Column(name = "InterestAnnual")
	@JsonProperty(value = "InterestAnnual")
	private BigDecimal InterestAnnual;
	/**
	 * 是否模拟组合
	 */
	@Column(name = "IsSimulate")
	@JsonProperty(value = "IsSimulate")
	private Boolean IsSimulate;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Lots")
	private List<Lot> Lots;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getStrategy() {
		return Strategy;
	}

	public void setStrategy(String Strategy) {
		this.Strategy = Strategy;
	}

	public BigDecimal getHedgeRate() {
		return HedgeRate;
	}

	public void setHedgeRate(BigDecimal HedgeRate) {
		this.HedgeRate = HedgeRate;
	}

	public BigDecimal getCurrencyRate() {
		return CurrencyRate;
	}

	public void setCurrencyRate(BigDecimal CurrencyRate) {
		this.CurrencyRate = CurrencyRate;
	}

	public BigDecimal getSpotPremium() {
		return SpotPremium;
	}

	public void setSpotPremium(BigDecimal SpotPremium) {
		this.SpotPremium = SpotPremium;
	}

	public BigDecimal getInterestAnnual() {
		return InterestAnnual;
	}

	public void setInterestAnnual(BigDecimal InterestAnnual) {
		this.InterestAnnual = InterestAnnual;
	}

	public Boolean getIsSimulate() {
		return IsSimulate;
	}

	public void setIsSimulate(Boolean IsSimulate) {
		this.IsSimulate = IsSimulate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public List<Lot> getLots() {
		return Lots;
	}

	public void setLots(List<Lot> Lots) {
		this.Lots = Lots;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public User getCreated() {
		return Created;
	}

	public void setCreated(User Created) {
		this.Created = Created;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

	public User getUpdated() {
		return Updated;
	}

	public void setUpdated(User Updated) {
		this.Updated = Updated;
	}

}

package com.smm.ctrm.domain.Physical;

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
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "MarginFlow", schema = "Physical")

public class MarginFlow extends HibernateEntity {
	private static final long serialVersionUID = 1461832991343L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "BrokerName")
	private String BrokerName;
	/**
	 * 交易日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * {D= 付款, C=收款}
	 */
	@Column(name = "DC")
	@JsonProperty(value = "DC")
	private String DC;
	/**
	 * 数量
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 货币（Dict）
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 内部台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
//	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 经纪商标识
	 */
	@Column(name = "BrokerId")
	@JsonProperty(value = "BrokerId")
	private String BrokerId;
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Broker.class)
//	@JoinColumn(name = "BrokerId", insertable = false, updatable = false)
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Broker")
	private Broker Broker;
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

	public String getBrokerName() {
		return BrokerName;
	}

	public void setBrokerName(String BrokerName) {
		this.BrokerName = BrokerName;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getDC() {
		return DC;
	}

	public void setDC(String DC) {
		this.DC = DC;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String LegalId) {
		this.LegalId = LegalId;
	}

	public Legal getLegal() {
		return Legal;
	}

	public void setLegal(Legal Legal) {
		this.Legal = Legal;
	}

	public String getBrokerId() {
		return BrokerId;
	}

	public void setBrokerId(String BrokerId) {
		this.BrokerId = BrokerId;
	}

	public Broker getBroker() {
		return Broker;
	}

	public void setBroker(Broker Broker) {
		this.Broker = Broker;
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


package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.Period;
@Entity
@Table(name = "FundBalanceMonthly", schema="Report")

public class FundBalanceMonthly extends HibernateEntity {
	private static final long serialVersionUID = 1461719402482L;
	/**
	 * 金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 账期标识
	 */
	@Column(name = "PeriodId")
	@JsonProperty(value = "PeriodId")
	private String PeriodId;
//	@JsonBackReference("FundBalanceMonthly_Period")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Period.class)
	@JoinColumn(name = "PeriodId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Period")
	private Period Period;
	/**
	 * 科目标识
	 */
	@Column(name = "AccountId")
	@JsonProperty(value = "AccountId")
	private String AccountId;
//	@JsonBackReference("FundBalanceMonthly_Account")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
	@JoinColumn(name = "AccountId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Account")
	private Account Account;
	
	public BigDecimal getAmount(){
		return Amount;
	}
	public void setAmount(BigDecimal Amount){
		this.Amount=Amount;
	}
	
	public String getPeriodId(){
		return PeriodId;
	}
	public void setPeriodId(String PeriodId){
		this.PeriodId=PeriodId;
	}
	
	public Period getPeriod(){
		return Period;
	}
	public void setPeriod(Period Period){
		this.Period=Period;
	}
	
	public String getAccountId(){
		return AccountId;
	}
	public void setAccountId(String AccountId){
		this.AccountId=AccountId;
	}
	
	public Account getAccount(){
		return Account;
	}
	public void setAccount(Account Account){
		this.Account=Account;
	}

}
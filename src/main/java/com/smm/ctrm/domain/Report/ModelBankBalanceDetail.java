

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Legal;
public class ModelBankBalanceDetail extends HibernateEntity {
	private static final long serialVersionUID = 1461719402483L;
	/**
	 * 账户ID
	 */
	@JsonProperty(value = "AccountID")
	private String AccountID;
	/**
	 * 银行名称
	 */
	@JsonProperty(value = "BankName")
	private String BankName;
	/**
	 * 银行账号
	 */
	@JsonProperty(value = "BankAccountNo")
	private String BankAccountNo;
	/**
	 * 记录时间
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 银行余额
	 */
	@JsonProperty(value = "AccountBalance")
	private BigDecimal AccountBalance;
	/**
	 *
	 */
	@JsonProperty(value = "CorporationName")
	private String CorporationName;
	/**
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("ModelBankBalanceDetail_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	
	public String getAccountID(){
		return AccountID;
	}
	public void setAccountID(String AccountID){
		this.AccountID=AccountID;
	}
	
	public String getBankName(){
		return BankName;
	}
	public void setBankName(String BankName){
		this.BankName=BankName;
	}
	
	public String getBankAccountNo(){
		return BankAccountNo;
	}
	public void setBankAccountNo(String BankAccountNo){
		this.BankAccountNo=BankAccountNo;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getAccountBalance(){
		return AccountBalance;
	}
	public void setAccountBalance(BigDecimal AccountBalance){
		this.AccountBalance=AccountBalance;
	}
	
	public String getCorporationName(){
		return CorporationName;
	}
	public void setCorporationName(String CorporationName){
		this.CorporationName=CorporationName;
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

}


package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class ModelBankBalanceGroup extends HibernateEntity {
	private static final long serialVersionUID = 1461719402484L;
	/**
	 * 日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 日期字符串
	 */
	@JsonProperty(value = "TradeDateString")
	private String TradeDateString;
	/**
	 * 余额总计
	 */
	@JsonProperty(value = "BalanceSum")
	private BigDecimal BalanceSum;
	/**
	 * 余额明细
	 */
	@JsonProperty(value = "BalanceDetails")
	private List<ModelBankBalanceDetail> BalanceDetails;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getTradeDateString(){
		return TradeDateString;
	}
	public void setTradeDateString(String TradeDateString){
		this.TradeDateString=TradeDateString;
	}
	
	public BigDecimal getBalanceSum(){
		return BalanceSum;
	}
	public void setBalanceSum(BigDecimal BalanceSum){
		this.BalanceSum=BalanceSum;
	}
	
	public List<ModelBankBalanceDetail> getBalanceDetails(){
		return BalanceDetails;
	}
	public void setBalanceDetails(List<ModelBankBalanceDetail> BalanceDetails){
		this.BalanceDetails=BalanceDetails;
	}

}
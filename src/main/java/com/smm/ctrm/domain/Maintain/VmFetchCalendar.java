

package com.smm.ctrm.domain.Maintain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Basis.Market;

public class VmFetchCalendar {
	/**
	 *
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 *
	 */
	@JsonProperty(value = "Markets")
	private List<Market> Markets;
	
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public List<Market> getMarkets(){
		return Markets;
	}
	public void setMarkets(List<Market> Markets){
		this.Markets=Markets;
	}
}
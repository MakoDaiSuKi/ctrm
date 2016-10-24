

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Physical.Lot;
public class R1 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@JsonProperty(value = "Exposure")
	private BigDecimal Exposure;
	/**
	 *
	 */
	@JsonProperty(value = "Lots")
	private List<Lot> Lots;
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public BigDecimal getExposure(){
		return Exposure;
	}
	public void setExposure(BigDecimal Exposure){
		this.Exposure=Exposure;
	}
	
	public List<Lot> getLots(){
		return Lots;
	}
	public void setLots(List<Lot> Lots){
		this.Lots=Lots;
	}

}
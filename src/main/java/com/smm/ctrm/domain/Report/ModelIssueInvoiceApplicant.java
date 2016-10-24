

package com.smm.ctrm.domain.Report;

import java.util.Date;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;

public class ModelIssueInvoiceApplicant {
	/**
	 *
	 */
	@JsonProperty(value = "PID")
	private String PID;
	/**
	 *
	 */
	@JsonProperty(value = "ApplyDate")
	private Date ApplyDate;
	/**
	 *
	 */
	@JsonProperty(value = "AmountCapital")
	private String AmountCapital;
	/**
	 *
	 */
	@JsonProperty(value = "Details")
	private List<RptApplyInvoiceDetail> Details;
	/**
	 *
	 */
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("ModelIssueInvoiceApplicant_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
//	@JsonBackReference("ModelIssueInvoiceApplicant_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 *
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("ModelIssueInvoiceApplicant_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	public String getPID(){
		return PID;
	}
	public void setPID(String PID){
		this.PID=PID;
	}
	
	public Date getApplyDate(){
		return ApplyDate;
	}
	public void setApplyDate(Date ApplyDate){
		this.ApplyDate=ApplyDate;
	}
	
	public String getAmountCapital(){
		return AmountCapital;
	}
	public void setAmountCapital(String AmountCapital){
		this.AmountCapital=AmountCapital;
	}
	
	public List<RptApplyInvoiceDetail> getDetails(){
		return Details;
	}
	public void setDetails(List<RptApplyInvoiceDetail> Details){
		this.Details=Details;
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
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public Customer getCustomer(){
		return Customer;
	}
	public void setCustomer(Customer Customer){
		this.Customer=Customer;
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
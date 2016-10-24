

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class Lot4FeesOverview extends HibernateEntity {
	private static final long serialVersionUID = 1461719402483L;
	/**
	 *
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	/**
	 *
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity4Lot")
	private BigDecimal Quantity4Lot;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity4Delivery")
	private BigDecimal Quantity4Delivery;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Trans")
	private String EstimateBasis4Trans;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Trans")
	private BigDecimal EstimateRate4Trans;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Trans")
	private BigDecimal EstimateAmount4Trans;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Trans")
	private BigDecimal RealAmount4Trans;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Test")
	private String EstimateBasis4Test;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Test")
	private BigDecimal EstimateRate4Test;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Test")
	private BigDecimal EstimateAmount4Test;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Test")
	private BigDecimal RealAmount4Test;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Insu")
	private String EstimateBasis4Insu;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Insu")
	private BigDecimal EstimateRate4Insu;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Insu")
	private BigDecimal EstimateAmount4Insu;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Insu")
	private BigDecimal RealAmount4Insu;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Cost")
	private String EstimateBasis4Cost;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Cost")
	private BigDecimal EstimateRate4Cost;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Cost")
	private BigDecimal EstimateAmount4Cost;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Cost")
	private BigDecimal RealAmount4Cost;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Bank")
	private String EstimateBasis4Bank;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Bank")
	private BigDecimal EstimateRate4Bank;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Bank")
	private BigDecimal EstimateAmount4Bank;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Bank")
	private BigDecimal RealAmount4Bank;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Buy")
	private String EstimateBasis4Buy;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Buy")
	private BigDecimal EstimateRate4Buy;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Buy")
	private BigDecimal EstimateAmount4Buy;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Buy")
	private BigDecimal RealAmount4Buy;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Fine")
	private String EstimateBasis4Fine;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Fine")
	private BigDecimal EstimateRate4Fine;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Fine")
	private BigDecimal EstimateAmount4Fine;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Fine")
	private BigDecimal RealAmount4Fine;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Hedge")
	private String EstimateBasis4Hedge;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Hedge")
	private BigDecimal EstimateRate4Hedge;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Hedge")
	private BigDecimal EstimateAmount4Hedge;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Hedge")
	private BigDecimal RealAmount4Hedge;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateBasis4Other")
	private String EstimateBasis4Other;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateRate4Other")
	private BigDecimal EstimateRate4Other;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateAmount4Other")
	private BigDecimal EstimateAmount4Other;
	/**
	 *
	 */
	@JsonProperty(value = "RealAmount4Other")
	private BigDecimal RealAmount4Other;
	/**
	 *
	 */
	@JsonProperty(value = "SumEstimatePrice")
	private BigDecimal SumEstimatePrice;
	/**
	 *
	 */
	@JsonProperty(value = "SumEstimateAmount")
	private BigDecimal SumEstimateAmount;
	/**
	 *
	 */
	@JsonProperty(value = "SumRealPrice")
	private BigDecimal SumRealPrice;
	/**
	 *
	 */
	@JsonProperty(value = "SumRealAmount")
	private BigDecimal SumRealAmount;
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public String getFullNo(){
		return FullNo;
	}
	public void setFullNo(String FullNo){
		this.FullNo=FullNo;
	}
	
	public BigDecimal getQuantity4Lot(){
		return Quantity4Lot;
	}
	public void setQuantity4Lot(BigDecimal Quantity4Lot){
		this.Quantity4Lot=Quantity4Lot;
	}
	
	public BigDecimal getQuantity4Delivery(){
		return Quantity4Delivery;
	}
	public void setQuantity4Delivery(BigDecimal Quantity4Delivery){
		this.Quantity4Delivery=Quantity4Delivery;
	}
	
	public String getEstimateBasis4Trans(){
		return EstimateBasis4Trans;
	}
	public void setEstimateBasis4Trans(String EstimateBasis4Trans){
		this.EstimateBasis4Trans=EstimateBasis4Trans;
	}
	
	public BigDecimal getEstimateRate4Trans(){
		return EstimateRate4Trans;
	}
	public void setEstimateRate4Trans(BigDecimal EstimateRate4Trans){
		this.EstimateRate4Trans=EstimateRate4Trans;
	}
	
	public BigDecimal getEstimateAmount4Trans(){
		return EstimateAmount4Trans;
	}
	public void setEstimateAmount4Trans(BigDecimal EstimateAmount4Trans){
		this.EstimateAmount4Trans=EstimateAmount4Trans;
	}
	
	public BigDecimal getRealAmount4Trans(){
		return RealAmount4Trans;
	}
	public void setRealAmount4Trans(BigDecimal RealAmount4Trans){
		this.RealAmount4Trans=RealAmount4Trans;
	}
	
	public String getEstimateBasis4Test(){
		return EstimateBasis4Test;
	}
	public void setEstimateBasis4Test(String EstimateBasis4Test){
		this.EstimateBasis4Test=EstimateBasis4Test;
	}
	
	public BigDecimal getEstimateRate4Test(){
		return EstimateRate4Test;
	}
	public void setEstimateRate4Test(BigDecimal EstimateRate4Test){
		this.EstimateRate4Test=EstimateRate4Test;
	}
	
	public BigDecimal getEstimateAmount4Test(){
		return EstimateAmount4Test;
	}
	public void setEstimateAmount4Test(BigDecimal EstimateAmount4Test){
		this.EstimateAmount4Test=EstimateAmount4Test;
	}
	
	public BigDecimal getRealAmount4Test(){
		return RealAmount4Test;
	}
	public void setRealAmount4Test(BigDecimal RealAmount4Test){
		this.RealAmount4Test=RealAmount4Test;
	}
	
	public String getEstimateBasis4Insu(){
		return EstimateBasis4Insu;
	}
	public void setEstimateBasis4Insu(String EstimateBasis4Insu){
		this.EstimateBasis4Insu=EstimateBasis4Insu;
	}
	
	public BigDecimal getEstimateRate4Insu(){
		return EstimateRate4Insu;
	}
	public void setEstimateRate4Insu(BigDecimal EstimateRate4Insu){
		this.EstimateRate4Insu=EstimateRate4Insu;
	}
	
	public BigDecimal getEstimateAmount4Insu(){
		return EstimateAmount4Insu;
	}
	public void setEstimateAmount4Insu(BigDecimal EstimateAmount4Insu){
		this.EstimateAmount4Insu=EstimateAmount4Insu;
	}
	
	public BigDecimal getRealAmount4Insu(){
		return RealAmount4Insu;
	}
	public void setRealAmount4Insu(BigDecimal RealAmount4Insu){
		this.RealAmount4Insu=RealAmount4Insu;
	}
	
	public String getEstimateBasis4Cost(){
		return EstimateBasis4Cost;
	}
	public void setEstimateBasis4Cost(String EstimateBasis4Cost){
		this.EstimateBasis4Cost=EstimateBasis4Cost;
	}
	
	public BigDecimal getEstimateRate4Cost(){
		return EstimateRate4Cost;
	}
	public void setEstimateRate4Cost(BigDecimal EstimateRate4Cost){
		this.EstimateRate4Cost=EstimateRate4Cost;
	}
	
	public BigDecimal getEstimateAmount4Cost(){
		return EstimateAmount4Cost;
	}
	public void setEstimateAmount4Cost(BigDecimal EstimateAmount4Cost){
		this.EstimateAmount4Cost=EstimateAmount4Cost;
	}
	
	public BigDecimal getRealAmount4Cost(){
		return RealAmount4Cost;
	}
	public void setRealAmount4Cost(BigDecimal RealAmount4Cost){
		this.RealAmount4Cost=RealAmount4Cost;
	}
	
	public String getEstimateBasis4Bank(){
		return EstimateBasis4Bank;
	}
	public void setEstimateBasis4Bank(String EstimateBasis4Bank){
		this.EstimateBasis4Bank=EstimateBasis4Bank;
	}
	
	public BigDecimal getEstimateRate4Bank(){
		return EstimateRate4Bank;
	}
	public void setEstimateRate4Bank(BigDecimal EstimateRate4Bank){
		this.EstimateRate4Bank=EstimateRate4Bank;
	}
	
	public BigDecimal getEstimateAmount4Bank(){
		return EstimateAmount4Bank;
	}
	public void setEstimateAmount4Bank(BigDecimal EstimateAmount4Bank){
		this.EstimateAmount4Bank=EstimateAmount4Bank;
	}
	
	public BigDecimal getRealAmount4Bank(){
		return RealAmount4Bank;
	}
	public void setRealAmount4Bank(BigDecimal RealAmount4Bank){
		this.RealAmount4Bank=RealAmount4Bank;
	}
	
	public String getEstimateBasis4Buy(){
		return EstimateBasis4Buy;
	}
	public void setEstimateBasis4Buy(String EstimateBasis4Buy){
		this.EstimateBasis4Buy=EstimateBasis4Buy;
	}
	
	public BigDecimal getEstimateRate4Buy(){
		return EstimateRate4Buy;
	}
	public void setEstimateRate4Buy(BigDecimal EstimateRate4Buy){
		this.EstimateRate4Buy=EstimateRate4Buy;
	}
	
	public BigDecimal getEstimateAmount4Buy(){
		return EstimateAmount4Buy;
	}
	public void setEstimateAmount4Buy(BigDecimal EstimateAmount4Buy){
		this.EstimateAmount4Buy=EstimateAmount4Buy;
	}
	
	public BigDecimal getRealAmount4Buy(){
		return RealAmount4Buy;
	}
	public void setRealAmount4Buy(BigDecimal RealAmount4Buy){
		this.RealAmount4Buy=RealAmount4Buy;
	}
	
	public String getEstimateBasis4Fine(){
		return EstimateBasis4Fine;
	}
	public void setEstimateBasis4Fine(String EstimateBasis4Fine){
		this.EstimateBasis4Fine=EstimateBasis4Fine;
	}
	
	public BigDecimal getEstimateRate4Fine(){
		return EstimateRate4Fine;
	}
	public void setEstimateRate4Fine(BigDecimal EstimateRate4Fine){
		this.EstimateRate4Fine=EstimateRate4Fine;
	}
	
	public BigDecimal getEstimateAmount4Fine(){
		return EstimateAmount4Fine;
	}
	public void setEstimateAmount4Fine(BigDecimal EstimateAmount4Fine){
		this.EstimateAmount4Fine=EstimateAmount4Fine;
	}
	
	public BigDecimal getRealAmount4Fine(){
		return RealAmount4Fine;
	}
	public void setRealAmount4Fine(BigDecimal RealAmount4Fine){
		this.RealAmount4Fine=RealAmount4Fine;
	}
	
	public String getEstimateBasis4Hedge(){
		return EstimateBasis4Hedge;
	}
	public void setEstimateBasis4Hedge(String EstimateBasis4Hedge){
		this.EstimateBasis4Hedge=EstimateBasis4Hedge;
	}
	
	public BigDecimal getEstimateRate4Hedge(){
		return EstimateRate4Hedge;
	}
	public void setEstimateRate4Hedge(BigDecimal EstimateRate4Hedge){
		this.EstimateRate4Hedge=EstimateRate4Hedge;
	}
	
	public BigDecimal getEstimateAmount4Hedge(){
		return EstimateAmount4Hedge;
	}
	public void setEstimateAmount4Hedge(BigDecimal EstimateAmount4Hedge){
		this.EstimateAmount4Hedge=EstimateAmount4Hedge;
	}
	
	public BigDecimal getRealAmount4Hedge(){
		return RealAmount4Hedge;
	}
	public void setRealAmount4Hedge(BigDecimal RealAmount4Hedge){
		this.RealAmount4Hedge=RealAmount4Hedge;
	}
	
	public String getEstimateBasis4Other(){
		return EstimateBasis4Other;
	}
	public void setEstimateBasis4Other(String EstimateBasis4Other){
		this.EstimateBasis4Other=EstimateBasis4Other;
	}
	
	public BigDecimal getEstimateRate4Other(){
		return EstimateRate4Other;
	}
	public void setEstimateRate4Other(BigDecimal EstimateRate4Other){
		this.EstimateRate4Other=EstimateRate4Other;
	}
	
	public BigDecimal getEstimateAmount4Other(){
		return EstimateAmount4Other;
	}
	public void setEstimateAmount4Other(BigDecimal EstimateAmount4Other){
		this.EstimateAmount4Other=EstimateAmount4Other;
	}
	
	public BigDecimal getRealAmount4Other(){
		return RealAmount4Other;
	}
	public void setRealAmount4Other(BigDecimal RealAmount4Other){
		this.RealAmount4Other=RealAmount4Other;
	}
	
	public BigDecimal getSumEstimatePrice(){
		return SumEstimatePrice;
	}
	public void setSumEstimatePrice(BigDecimal SumEstimatePrice){
		this.SumEstimatePrice=SumEstimatePrice;
	}
	
	public BigDecimal getSumEstimateAmount(){
		return SumEstimateAmount;
	}
	public void setSumEstimateAmount(BigDecimal SumEstimateAmount){
		this.SumEstimateAmount=SumEstimateAmount;
	}
	
	public BigDecimal getSumRealPrice(){
		return SumRealPrice;
	}
	public void setSumRealPrice(BigDecimal SumRealPrice){
		this.SumRealPrice=SumRealPrice;
	}
	
	public BigDecimal getSumRealAmount(){
		return SumRealAmount;
	}
	public void setSumRealAmount(BigDecimal SumRealAmount){
		this.SumRealAmount=SumRealAmount;
	}

}
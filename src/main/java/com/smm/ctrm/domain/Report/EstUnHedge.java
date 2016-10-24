

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class EstUnHedge extends HibernateEntity {
	private static final long serialVersionUID = 1461719402482L;
	/**
	 *
	 */
	@JsonProperty(value = "BuyFullNo")
	private String BuyFullNo;
	/**
	 * 销售批次号
	 */
	@JsonProperty(value = "SellFullNo")
	private String SellFullNo;
	/**
	 * 预计销售日期
	 */
	@JsonProperty(value = "EstSellDate")
	private String EstSellDate;
	/**
	 * 实际销售日期
	 */
	@JsonProperty(value = "RealSellDate")
	private String RealSellDate;
	/**
	 * 销售数量
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 * 头寸数量
	 */
	@JsonProperty(value = "PositionQuantity")
	private BigDecimal PositionQuantity;
	/**
	 * 交付明细数量
	 */
	@JsonProperty(value = "StorageQuantity")
	private BigDecimal StorageQuantity;
	/**
	 * 交付明细保值标记
	 */
	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;
	
	public String getBuyFullNo(){
		return BuyFullNo;
	}
	public void setBuyFullNo(String BuyFullNo){
		this.BuyFullNo=BuyFullNo;
	}
	
	public String getSellFullNo(){
		return SellFullNo;
	}
	public void setSellFullNo(String SellFullNo){
		this.SellFullNo=SellFullNo;
	}
	
	public String getEstSellDate(){
		return EstSellDate;
	}
	public void setEstSellDate(String EstSellDate){
		this.EstSellDate=EstSellDate;
	}
	
	public String getRealSellDate(){
		return RealSellDate;
	}
	public void setRealSellDate(String RealSellDate){
		this.RealSellDate=RealSellDate;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getPositionQuantity(){
		return PositionQuantity;
	}
	public void setPositionQuantity(BigDecimal PositionQuantity){
		this.PositionQuantity=PositionQuantity;
	}
	
	public BigDecimal getStorageQuantity(){
		return StorageQuantity;
	}
	public void setStorageQuantity(BigDecimal StorageQuantity){
		this.StorageQuantity=StorageQuantity;
	}
	
	public Boolean getIsHedged(){
		return IsHedged;
	}
	public void setIsHedged(Boolean IsHedged){
		this.IsHedged=IsHedged;
	}

}
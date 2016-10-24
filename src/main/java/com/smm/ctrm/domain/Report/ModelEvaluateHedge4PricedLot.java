

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Square;
public class ModelEvaluateHedge4PricedLot {
	private static final long serialVersionUID = 1461719402485L;
	/**
	 *
	 */
	@JsonProperty(value = "ValuableofHedge")
	private BigDecimal ValuableofHedge;
	/**
	 *
	 */
	@JsonProperty(value = "ValuationOfSpot")
	private BigDecimal ValuationOfSpot;
	/**
	 *
	 */
	@JsonProperty(value = "ValuableofTotal")
	private BigDecimal ValuableofTotal;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@JsonProperty(value = "QuantitySold")
	private BigDecimal QuantitySold;
	/**
	 *
	 */
	@JsonProperty(value = "QuantityUnSold")
	private BigDecimal QuantityUnSold;
	/**
	 *
	 */
	@JsonProperty(value = "M2MPrice")
	private BigDecimal M2MPrice;
	/**
	 *
	 */
	@JsonProperty(value = "Coef")
	private BigDecimal Coef;
	/**
	 *
	 */
	@JsonProperty(value = "InOut")
	private String InOut;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@JsonProperty(value = "QuantitySell")
	private BigDecimal QuantitySell;
	/**
	 *
	 */
	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;
	/**
	 *
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "IsStoraged")
	private Boolean IsStoraged;
	/**
	 *
	 */
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 *
	 */
	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;
	/**
	 *
	 */
	@JsonProperty(value = "PnL4Spot")
	private BigDecimal PnL4Spot;
	/**
	 *
	 */
	@JsonProperty(value = "PnL4Hedge")
	private BigDecimal PnL4Hedge;
	/**
	 *
	 */
	@JsonProperty(value = "PnLTotal")
	private BigDecimal PnLTotal;
	/**
	 *
	 */
	@JsonProperty(value = "Positions")
	private List<Position> Positions;
	/**
	 *
	 */
	@JsonProperty(value = "LstSpotBuy")
	private List<VmPnL4Spot> LstSpotBuy;
	/**
	 *
	 */
	@JsonProperty(value = "LstSpotSell")
	private List<VmPnL4Spot> LstSpotSell;
	/**
	 *
	 */
	@JsonProperty(value = "Squares")
	private List<Square> Squares;
	/**
	 *
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
//	@JsonBackReference("ModelEvaluateHedge4PricedLot_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	
	public BigDecimal getValuableofHedge(){
		return ValuableofHedge;
	}
	public void setValuableofHedge(BigDecimal ValuableofHedge){
		this.ValuableofHedge=ValuableofHedge;
	}
	
	public BigDecimal getValuationOfSpot(){
		return ValuationOfSpot;
	}
	public void setValuationOfSpot(BigDecimal ValuationOfSpot){
		this.ValuationOfSpot=ValuationOfSpot;
	}
	
	public BigDecimal getValuableofTotal(){
		return ValuableofTotal;
	}
	public void setValuableofTotal(BigDecimal ValuableofTotal){
		this.ValuableofTotal=ValuableofTotal;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantitySold(){
		return QuantitySold;
	}
	public void setQuantitySold(BigDecimal QuantitySold){
		this.QuantitySold=QuantitySold;
	}
	
	public BigDecimal getQuantityUnSold(){
		return QuantityUnSold;
	}
	public void setQuantityUnSold(BigDecimal QuantityUnSold){
		this.QuantityUnSold=QuantityUnSold;
	}
	
	public BigDecimal getM2MPrice(){
		return M2MPrice;
	}
	public void setM2MPrice(BigDecimal M2MPrice){
		this.M2MPrice=M2MPrice;
	}
	
	public BigDecimal getCoef(){
		return Coef;
	}
	public void setCoef(BigDecimal Coef){
		this.Coef=Coef;
	}
	
	public String getInOut(){
		return InOut;
	}
	public void setInOut(String InOut){
		this.InOut=InOut;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public BigDecimal getQuantitySell(){
		return QuantitySell;
	}
	public void setQuantitySell(BigDecimal QuantitySell){
		this.QuantitySell=QuantitySell;
	}
	
	public Boolean getIsDelivered(){
		return IsDelivered;
	}
	public void setIsDelivered(Boolean IsDelivered){
		this.IsDelivered=IsDelivered;
	}
	
	public Boolean getIsInvoiced(){
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean IsInvoiced){
		this.IsInvoiced=IsInvoiced;
	}
	
	public Boolean getIsStoraged(){
		return IsStoraged;
	}
	public void setIsStoraged(Boolean IsStoraged){
		this.IsStoraged=IsStoraged;
	}
	
	public Boolean getIsPriced(){
		return IsPriced;
	}
	public void setIsPriced(Boolean IsPriced){
		this.IsPriced=IsPriced;
	}
	
	public Boolean getIsSettled(){
		return IsSettled;
	}
	public void setIsSettled(Boolean IsSettled){
		this.IsSettled=IsSettled;
	}
	
	public BigDecimal getPnL4Spot(){
		return PnL4Spot;
	}
	public void setPnL4Spot(BigDecimal PnL4Spot){
		this.PnL4Spot=PnL4Spot;
	}
	
	public BigDecimal getPnL4Hedge(){
		return PnL4Hedge;
	}
	public void setPnL4Hedge(BigDecimal PnL4Hedge){
		this.PnL4Hedge=PnL4Hedge;
	}
	
	public BigDecimal getPnLTotal(){
		return PnLTotal;
	}
	public void setPnLTotal(BigDecimal PnLTotal){
		this.PnLTotal=PnLTotal;
	}
	
	public List<Position> getPositions(){
		return Positions;
	}
	public void setPositions(List<Position> Positions){
		this.Positions=Positions;
	}
	
	public List<VmPnL4Spot> getLstSpotBuy(){
		return LstSpotBuy;
	}
	public void setLstSpotBuy(List<VmPnL4Spot> LstSpotBuy){
		this.LstSpotBuy=LstSpotBuy;
	}
	
	public List<VmPnL4Spot> getLstSpotSell(){
		return LstSpotSell;
	}
	public void setLstSpotSell(List<VmPnL4Spot> LstSpotSell){
		this.LstSpotSell=LstSpotSell;
	}
	
	public List<Square> getSquares(){
		return Squares;
	}
	public void setSquares(List<Square> Squares){
		this.Squares=Squares;
	}
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public Lot getLot(){
		return Lot;
	}
	public void setLot(Lot Lot){
		this.Lot=Lot;
	}

}
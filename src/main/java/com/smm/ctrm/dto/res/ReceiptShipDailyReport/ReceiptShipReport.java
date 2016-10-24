package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class ReceiptShipReport extends HibernateEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1912481971189670256L;

	public ReceiptShipReport() {
		super();
	}

	public ReceiptShipReport(String receiptShipNo, String fullNo, BigDecimal weight, BigDecimal quantityDelivered,
			String whName, String truckNo, String deliveryUnit, String deliveryMan, String deliveryManIDCard,
			String deliveryTruckNo, Date receiptShipDate) {
		super();
		ReceiptShipNo = receiptShipNo;
		FullNo = fullNo;
		Weight = weight;
		QuantityDelivered = quantityDelivered;
		WhName = whName;
		TruckNo = truckNo;
		DeliveryUnit = deliveryUnit;
		DeliveryMan = deliveryMan;
		DeliveryManIDCard = deliveryManIDCard;
		DeliveryTruckNo = deliveryTruckNo;
		ReceiptShipDate = receiptShipDate;
	}

	/**
	 * 收发货单号
	 */
	@JsonProperty(value = "ReceiptShipNo")
	private String ReceiptShipNo;
	
	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	/**
	 * 重量
	 */
	@JsonProperty(value = "Weight")
	private BigDecimal Weight;
	
	/**
	 * 实际的交付数量
	 */
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	
	/**
	 * 仓储地
	 */
	@JsonProperty(value = "WhName")
	private String WhName;
	
	/**
	 * 车号
	 */
	@JsonProperty(value = "TruckNo")
	private String TruckNo;
	
	/**
	 * 提货单位
	 */
	@JsonProperty(value = "DeliveryUnit")
	private String DeliveryUnit;

	/**
	 * 提货人
	 */
	@JsonProperty(value = "DeliveryMan")
	private String DeliveryMan;

	/**
	 * 提货人身份证
	 */
	@JsonProperty(value = "DeliveryManIDCard")
	private String DeliveryManIDCard;

	/**
	 * 提货车号
	 */
	@JsonProperty(value = "DeliveryTruckNo")
	private String DeliveryTruckNo;

	/**
	 * 收发货日期
	 */
	@JsonProperty(value = "ReceiptShipDate")
	private Date ReceiptShipDate;

	public String getReceiptShipNo() {
		return ReceiptShipNo;
	}

	public void setReceiptShipNo(String receiptShipNo) {
		ReceiptShipNo = receiptShipNo;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public BigDecimal getWeight() {
		return Weight;
	}

	public void setWeight(BigDecimal weight) {
		Weight = weight;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}

	public String getWhName() {
		return WhName;
	}

	public void setWhName(String whName) {
		WhName = whName;
	}

	public String getTruckNo() {
		return TruckNo;
	}

	public void setTruckNo(String truckNo) {
		TruckNo = truckNo;
	}

	public String getDeliveryUnit() {
		return DeliveryUnit;
	}

	public void setDeliveryUnit(String deliveryUnit) {
		DeliveryUnit = deliveryUnit;
	}

	public String getDeliveryMan() {
		return DeliveryMan;
	}

	public void setDeliveryMan(String deliveryMan) {
		DeliveryMan = deliveryMan;
	}

	public String getDeliveryManIDCard() {
		return DeliveryManIDCard;
	}

	public void setDeliveryManIDCard(String deliveryManIDCard) {
		DeliveryManIDCard = deliveryManIDCard;
	}

	public String getDeliveryTruckNo() {
		return DeliveryTruckNo;
	}

	public void setDeliveryTruckNo(String deliveryTruckNo) {
		DeliveryTruckNo = deliveryTruckNo;
	}

	public Date getReceiptShipDate() {
		return ReceiptShipDate;
	}

	public void setReceiptShipDate(Date receiptShipDate) {
		ReceiptShipDate = receiptShipDate;
	}
}

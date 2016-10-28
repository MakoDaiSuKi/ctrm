package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * 头寸交割表
 * 
 * @author zengshihua
 *
 */
@Entity
@Table(name = "PositionDelivery", schema = "Physical")
public class PositionDelivery extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7842230534782667641L;
	
	/**
	 * 头寸ID
	 */
	@Column(name = "Position4BrokerId")
	@JsonProperty(value = "Position4BrokerId")
	private String Position4BrokerId;
	
	/**
	 * 交割平仓头寸Id
	 */
	@Column(name = "PositionId")
	@JsonProperty(value = "PositionId")
	private String PositionId;

	/**
	 * 批次ID
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	
	/**
	 * 头寸交易编号
	 */
	@Column(name = "Position4BrokerNo")
	@JsonProperty(value = "Position4BrokerNo")
	private String Position4BrokerNo;

	/**
	 * 批次编号
	 */
	@Column(name = "LotNo")
	@JsonProperty(value = "LotNo")
	private String LotNo;

	/**
	 * 交割数量
	 */
	@Column(name = "DeliveryQuantity")
	@JsonProperty(value = "DeliveryQuantity")
	private BigDecimal DeliveryQuantity;

	/**
	 * 交割价格
	 */
	@Column(name = "DeliveryPrice")
	@JsonProperty(value = "DeliveryPrice")
	private BigDecimal DeliveryPrice;

	/**
	 * 升贴水
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;

	/**
	 * 交割时间
	 */
	@Column(name = "DeliveryDate")
	@JsonProperty(value = "DeliveryDate")
	private Date DeliveryDate;

	/**
	 * 创建人Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;

	/**
	 * 仓库ID
	 */
	@Column(name = "WareHouseId")
	@JsonProperty(value = "WareHouseId")
	private String WareHouseId;
	
	
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	

	@Transient
	@JsonProperty(value = "Product")
	private String Product;
	
	
	@Transient
	@JsonProperty(value = "SpecId")
	private String SpecId;
	
	
	/**
	 * 品级Id
	 */
	@Transient
	@JsonProperty(value = "GradeSetIds")
	private String GradeSetIds;
	
	/**
	 * 可多选品牌，逗号隔开
	 */
	@Transient
	@JsonProperty(value = "BrandIds")
	private String BrandIds;
	
	/**
	 * 多对一：原产地
	 */
	@Transient
	@JsonProperty(value = "OriginId")
	private String OriginId;
	
	
	@Transient
	@JsonProperty(value = "BrandName")
	private String BrandName;
	
	
	@Transient
	@JsonProperty(value = "ProjectName")
	private String ProjectName;
	

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

	public String getPosition4BrokerNo() {
		return Position4BrokerNo;
	}

	public void setPosition4BrokerNo(String position4BrokerNo) {
		Position4BrokerNo = position4BrokerNo;
	}

	public String getLotNo() {
		return LotNo;
	}

	public void setLotNo(String lotNo) {
		LotNo = lotNo;
	}

	public BigDecimal getDeliveryQuantity() {
		return DeliveryQuantity;
	}

	public void setDeliveryQuantity(BigDecimal deliveryQuantity) {
		DeliveryQuantity = deliveryQuantity;
	}

	public BigDecimal getDeliveryPrice() {
		return DeliveryPrice;
	}

	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		DeliveryPrice = deliveryPrice;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal premium) {
		Premium = premium;
	}

	public Date getDeliveryDate() {
		return DeliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		DeliveryDate = deliveryDate;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	public String getPositionId() {
		return PositionId;
	}

	public void setPositionId(String positionId) {
		PositionId = positionId;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public String getWareHouseId() {
		return WareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		WareHouseId = wareHouseId;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getPosition4BrokerId() {
		return Position4BrokerId;
	}

	public void setPosition4BrokerId(String position4BrokerId) {
		Position4BrokerId = position4BrokerId;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String specId) {
		SpecId = specId;
	}

	public String getGradeSetIds() {
		return GradeSetIds;
	}

	public void setGradeSetIds(String gradeSetIds) {
		GradeSetIds = gradeSetIds;
	}

	public String getBrandIds() {
		return BrandIds;
	}

	public void setBrandIds(String brandIds) {
		BrandIds = brandIds;
	}

	public String getOriginId() {
		return OriginId;
	}

	public void setOriginId(String originId) {
		OriginId = originId;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
}

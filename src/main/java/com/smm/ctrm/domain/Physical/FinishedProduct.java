package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.Warehouse;

/**
 * @author zhaoyutao
 *
 */
/**
 * @author zhaoyutao
 *
 */
@Entity
@Table(name = "FinishedProduct", schema = "Physical")
public class FinishedProduct extends HibernateEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -509327119515298402L;
	/**
	 *
	 */
	@JsonProperty(value = "No")
	private String No;
	/**
	 * 文档号
	 */
	@JsonProperty(value = "DocNo")
	private String DocNo;
	/**
	 * 抬头Id
	 */
	@JsonProperty(value = "LegalId")
	private String LegalId;
	/**
	 * 抬头信息，根据抬头Id 查询时需要
	 */
	@JsonProperty(value = "Legal")
	@Transient
	private Legal Legal;
	/**
	 * 品牌Id
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	/**
	 * 品牌信息，根据品牌Id 查询时需要
	 */
	@JsonProperty(value = "Commodity")
	@Transient
	private Commodity Commodity;
	/**
	 * 产品Id
	 */
	@JsonProperty(value = "ProductName")
	private String ProductName;
	/**
	 * 品牌Id
	 */
	@JsonProperty(value = "BrandId")
	private String BrandId;
	/**
	 * 品牌信息
	 */
	@JsonProperty(value = "Brand")
	@Transient
	private Brand Brand;
	/**
	 * 规格Id
	 */
	@JsonProperty(value = "SpecId")
	private String SpecId;
	/**
	 * 规格信息
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Spec.class)
    @JoinColumn(name = "SpecId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
    @Fetch(FetchMode.SELECT)
    @NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Spec")
	private Spec Spec;
	/**
	 * 数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 溢短装
	 */
	@JsonProperty(value = "MoreOrLess")
	private BigDecimal MoreOrLess;
	/**
	 * 装运地点名称
	 */
	@JsonProperty(value = "LodingName")
	private String LodingName;
	/**
	 * 到达地点Id
	 */
	@JsonProperty(value = "DischargingName")
	private String DischargingName;
	/**
	 * 交货期始
	 */
	@JsonProperty(value = "DeliveryStartDate")
	private Date DeliveryStartDate;
	/**
	 * 交货期止
	 */
	@JsonProperty(value = "DeliveryEndDate")
	private Date DeliveryEndDate;
	/**
	 * 业务经理
	 */
	@JsonProperty(value = "UserId")
	private String UserId;
	/**
	 * 业务经理信息
	 */
	@JsonProperty(value = "UserInfo")
	@Transient
	private User UserInfo;
	/**
	 * 提货单位ID
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 * 提货单位
	 */
	@JsonProperty(value = "Customer")
	@Transient
	private Customer Customer;
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
	 * 收货商品明细
	 */
	@JsonProperty(value = "Storages")
	@Transient
	private List<Storage> Storages;
	/**
	 * 是否收货完成
	 */
	@JsonProperty(value = "IsEnd")
	private Boolean IsEnd;
	
	/**
	 * 备注
	 */
	@JsonProperty(value = "Remark")
	private String Remark;
	
	/**
	 * 审核状态 0 待审 1 ...
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status", defaultValue = "0")
	private Integer Status;

	/**
	 * 是否通过
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;

	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
    /**
     * 仓储地Id
     */
	@Column(name = "WareHouseId")
	@JsonProperty(value = "WareHouseId")
    public String WareHouseId;
    /**
     * 仓储地名
     */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
    @JoinColumn(name = "WareHouseId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
    @Fetch(FetchMode.SELECT)
    @NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "WareHouse")
    public Warehouse WareHouse;
	
	/**
     * 已收数量
     */
    public BigDecimal QuantityDelivered;
	public String getNo() {
		return No;
	}

	public void setNo(String No) {
		this.No = No;
	}

	public String getDocNo() {
		return DocNo;
	}

	public void setDocNo(String DocNo) {
		this.DocNo = DocNo;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String LegalId) {
		this.LegalId = LegalId;
	}

	public Legal getLegal() {
		return Legal;
	}

	public void setLegal(Legal Legal) {
		this.Legal = Legal;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String CommodityId) {
		this.CommodityId = CommodityId;
	}

	public Commodity getCommodity() {
		return Commodity;
	}

	public void setCommodity(Commodity Commodity) {
		this.Commodity = Commodity;
	}

	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String BrandId) {
		this.BrandId = BrandId;
	}

	public Brand getBrand() {
		return Brand;
	}

	public void setBrand(Brand Brand) {
		this.Brand = Brand;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String SpecId) {
		this.SpecId = SpecId;
	}

	public Spec getSpec() {
		return Spec;
	}

	public void setSpec(Spec Spec) {
		this.Spec = Spec;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getMoreOrLess() {
		return MoreOrLess;
	}

	public void setMoreOrLess(BigDecimal MoreOrLess) {
		this.MoreOrLess = MoreOrLess;
	}

	public Date getDeliveryStartDate() {
		return DeliveryStartDate;
	}

	public void setDeliveryStartDate(Date DeliveryStartDate) {
		this.DeliveryStartDate = DeliveryStartDate;
	}

	public Date getDeliveryEndDate() {
		return DeliveryEndDate;
	}

	public void setDeliveryEndDate(Date DeliveryEndDate) {
		this.DeliveryEndDate = DeliveryEndDate;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String UserId) {
		this.UserId = UserId;
	}

	public User getUserInfo() {
		return UserInfo;
	}

	public void setUserInfo(User UserInfo) {
		this.UserInfo = UserInfo;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String CustomerId) {
		this.CustomerId = CustomerId;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer Customer) {
		this.Customer = Customer;
	}

	public String getDeliveryMan() {
		return DeliveryMan;
	}

	public void setDeliveryMan(String DeliveryMan) {
		this.DeliveryMan = DeliveryMan;
	}

	public String getDeliveryManIDCard() {
		return DeliveryManIDCard;
	}

	public void setDeliveryManIDCard(String DeliveryManIDCard) {
		this.DeliveryManIDCard = DeliveryManIDCard;
	}

	public String getDeliveryTruckNo() {
		return DeliveryTruckNo;
	}

	public void setDeliveryTruckNo(String DeliveryTruckNo) {
		this.DeliveryTruckNo = DeliveryTruckNo;
	}

	public List<Storage> getStorages() {
		return Storages;
	}

	public void setStorages(List<Storage> Storages) {
		this.Storages = Storages;
	}

	public Boolean getIsEnd() {
		return IsEnd;
	}

	public void setIsEnd(Boolean IsEnd) {
		this.IsEnd = IsEnd;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public Integer getStatus() {
		return Status == null ? 0 : Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public Boolean getIsApproved() {
		return IsApproved == null ? Boolean.FALSE : IsApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		IsApproved = isApproved;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String updatedId) {
		UpdatedId = updatedId;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getLodingName() {
		return LodingName;
	}

	public void setLodingName(String lodingName) {
		LodingName = lodingName;
	}

	public String getDischargingName() {
		return DischargingName;
	}

	public void setDischargingName(String dischargingName) {
		DischargingName = dischargingName;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getWareHouseId() {
		return WareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		WareHouseId = wareHouseId;
	}

	public Warehouse getWareHouse() {
		return WareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		WareHouse = wareHouse;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}
}
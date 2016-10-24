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
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.Warehouse;

/**
 * @author zhaoyutao
 *
 */
@Entity
@Table(name = "ReceiptShip", schema = "Physical")
public class ReceiptShip extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059243660755439083L;

	/**
	 * 收货
	 */
	public static final String RECEIPT = "R";

	/**
	 * 发货
	 */
	public static final String SHIP = "S";

	/**
	 * 出库
	 */
	public static final String OUT = "C";

	/**
	 * 收发货标记 R=收货 S=发货 C=出库
	 */
	@Column(name = "Flag")
	@JsonProperty(value = "Flag")
	private String Flag;

	/**
	 * 收发货单号
	 */
	@Column(name = "ReceiptShipNo")
	@JsonProperty(value = "ReceiptShipNo")
	private String ReceiptShipNo;

	/**
	 * 合同Id
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;

	/**
	 * 批次Id
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "Lot")
	@NotFound(action=NotFoundAction.IGNORE)
	private Lot Lot;

	/**
	 * 卡号
	 */
	private String CardNo;

	/**
	 * 品种ID
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;

	/**
	 * 品牌ID
	 */
	@Column(name = "BrandId")
	@JsonProperty(value = "BrandId")
	private String BrandId;

	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Brand.class)
	@JoinColumn(name = "BrandId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Brand")
	private Brand Brand;

	/**
	 * 规格ID
	 */
	@Column(name = "SpecId")
	@JsonProperty(value = "SpecId")
	private String SpecId;

	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Spec.class)
	@JoinColumn(name = "SpecId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Spec")
	private Spec Spec;

	/**
	 * 车号
	 */
	@Column(name = "TruckNo")
	@JsonProperty(value = "TruckNo")
	private String TruckNo;

	/**
	 * 重量
	 */
	@Column(name = "Weight")
	@JsonProperty(value = "Weight")
	private BigDecimal Weight;

	/**
	 * 提货单位
	 */
	@Column(name = "DeliveryUnit")
	@JsonProperty(value = "DeliveryUnit")
	private String DeliveryUnit;

	/**
	 * 提货人
	 */
	@Column(name = "DeliveryMan")
	@JsonProperty(value = "DeliveryMan")
	private String DeliveryMan;

	/**
	 * 提货人身份证
	 */
	@Column(name = "DeliveryManIDCard")
	@JsonProperty(value = "DeliveryManIDCard")
	private String DeliveryManIDCard;

	/**
	 * 提货车号
	 */
	@Column(name = "DeliveryTruckNo")
	@JsonProperty(value = "DeliveryTruckNo")
	private String DeliveryTruckNo;

	/**
	 * 收发货日期
	 */
	@Column(name = "ReceiptShipDate")
	@JsonProperty(value = "ReceiptShipDate")
	private Date ReceiptShipDate;

	/**
	 * 仓库id
	 */
	@Column(name = "WhId")
	@JsonProperty(value = "WhId")
	private String WhId;

	/**
	 * 出入库日期
	 */
	@Column(name = "WhOutEntryDate")
	@JsonProperty(value = "WhOutEntryDate")
	private Date WhOutEntryDate;

	/**
	 * 仓储地
	 */
	@Column(name = "WhName")
	@JsonProperty(value = "WhName")
	private String WhName;

	/**
	 * 供应商Id
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;

	/**
	 * 供应商名称
	 */
	@Column(name = "CustomerName")
	@JsonProperty(value = "CustomerName")
	private String CustomerName;

	/**
	 * 备注
	 */
	@Column(name = "Remark")
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
	 * 
	 */
	@Transient
	@JsonProperty(value = "Storages")
	private List<Storage> Storages;

	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "Pendings")
	private List<Pending> Pendings;

	/**
	 * 签发日期
	 **/
	@Column(name = "IsSureDate")
	@JsonProperty(value = "IsSureDate")
	private Date IsSureDate;
	/**
	 * 出库类别
	 **/
	@Column(name = "ShipTypeCode")
	@JsonProperty(value = "ShipTypeCode")
	private String ShipTypeCode;
	/**
	 * 库存类别
	 **/
	@Column(name = "StockTypeCode")
	@JsonProperty(value = "StockTypeCode")
	private String StockTypeCode;
	/**
	 * 销售部名称
	 **/
	@Column(name = "SaleDeptId")
	@JsonProperty(value = "SaleDeptId")
	private String SaleDeptId;
	/**
	 * 销售点名称
	 **/
	@Column(name = "SalePointId")
	@JsonProperty(value = "SalePointId")
	private String SalePointId;

	@Column(name = "SupplierName")
	@JsonProperty(value = "SupplierName")
	private String SupplierName;

	@Column(name = "IsSureData")
	@JsonProperty(value = "IsSureData")
	private Date IsSureData;

	/**
	 * 联系电话
	 */
	@Column(name = "DeliveryTelePhone")
	@JsonProperty(value = "DeliveryTelePhone")
	public String DeliveryTelePhone;
	
	/**
	 * 是否为初始化
	 */
	@Column(name = "IsIniatiated")
	@JsonProperty(value = "IsIniatiated")
    public Boolean IsIniatiated;

	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	public BigDecimal Quantity;
	
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "WhId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Warehouse")
	private Warehouse Warehouse;
	
	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getReceiptShipNo() {
		return ReceiptShipNo;
	}

	public void setReceiptShipNo(String receiptShipNo) {
		ReceiptShipNo = receiptShipNo;
	}

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String contractId) {
		ContractId = contractId;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String lotId) {
		LotId = lotId;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}

	public Commodity getCommodity() {
		return Commodity;
	}

	public void setCommodity(Commodity commodity) {
		Commodity = commodity;
	}

	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String brandId) {
		BrandId = brandId;
	}

	public Brand getBrand() {
		return Brand;
	}

	public void setBrand(Brand brand) {
		Brand = brand;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String specId) {
		SpecId = specId;
	}

	public Spec getSpec() {
		return Spec;
	}

	public void setSpec(Spec spec) {
		Spec = spec;
	}

	public String getTruckNo() {
		return TruckNo;
	}

	public void setTruckNo(String truckNo) {
		TruckNo = truckNo;
	}

	public BigDecimal getWeight() {
		return Weight;
	}

	public void setWeight(BigDecimal weight) {
		Weight = weight;
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

	public Date getWhOutEntryDate() {
		return WhOutEntryDate;
	}

	public void setWhOutEntryDate(Date whOutEntryDate) {
		WhOutEntryDate = whOutEntryDate;
	}

	public String getWhName() {
		return WhName;
	}

	public void setWhName(String whName) {
		WhName = whName;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public List<Storage> getStorages() {
		return Storages;
	}

	public void setStorages(List<Storage> storages) {
		Storages = storages;
	}

	public String getWhId() {
		return WhId;
	}

	public void setWhId(String whId) {
		WhId = whId;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public Boolean getIsApproved() {
		return IsApproved == null ? Boolean.FALSE : IsApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		IsApproved = isApproved;
	}

	public Integer getStatus() {
		return Status == null ? 0 : Status;
	}

	public void setStatus(Integer status) {
		Status = status;
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

	public String getSaleDeptId() {
		return SaleDeptId;
	}

	public void setSaleDeptId(String saleDeptId) {
		SaleDeptId = saleDeptId;
	}

	public String getSalePointId() {
		return SalePointId;
	}

	public void setSalePointId(String salePointId) {
		SalePointId = salePointId;
	}

	public String getShipTypeCode() {
		return ShipTypeCode;
	}

	public void setShipTypeCode(String shipTypeCode) {
		ShipTypeCode = shipTypeCode;
	}

	public String getStockTypeCode() {
		return StockTypeCode;
	}

	public void setStockTypeCode(String stockTypeCode) {
		StockTypeCode = stockTypeCode;
	}

	public Date getIsSureDate() {
		return IsSureDate;
	}

	public void setIsSureDate(Date isSureDate) {
		IsSureDate = isSureDate;
	}

	public List<Pending> getPendings() {
		return Pendings;
	}

	public void setPendings(List<Pending> pendings) {
		Pendings = pendings;
	}

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}

	public Date getIsSureData() {
		return IsSureData;
	}

	public void setIsSureData(Date isSureData) {
		IsSureData = isSureData;
	}

	public String getDeliveryTelePhone() {
		return DeliveryTelePhone;
	}

	public void setDeliveryTelePhone(String deliveryTelePhone) {
		DeliveryTelePhone = deliveryTelePhone;
	}

	public Lot getLot() {
		return Lot;
	}

	public void setLot(Lot lot) {
		Lot = lot;
	}

	public Boolean getIsIniatiated() {
		return IsIniatiated!=null?IsIniatiated:false;
	}

	public void setIsIniatiated(Boolean isIniatiated) {
		IsIniatiated = isIniatiated;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer customer) {
		Customer = customer;
	}

	public Warehouse getWarehouse() {
		return Warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		Warehouse = warehouse;
	}

	@Override
	public String toString() {
		return "ReceiptShip [Flag=" + Flag + ", ReceiptShipNo=" + ReceiptShipNo + ", ContractId=" + ContractId
				+ ", LotId=" + LotId + ", Lot=" + Lot + ", CardNo=" + CardNo + ", CommodityId=" + CommodityId
				+ ", Commodity=" + Commodity + ", BrandId=" + BrandId + ", Brand=" + Brand + ", SpecId=" + SpecId
				+ ", Spec=" + Spec + ", TruckNo=" + TruckNo + ", Weight=" + Weight + ", DeliveryUnit=" + DeliveryUnit
				+ ", DeliveryMan=" + DeliveryMan + ", DeliveryManIDCard=" + DeliveryManIDCard + ", DeliveryTruckNo="
				+ DeliveryTruckNo + ", ReceiptShipDate=" + ReceiptShipDate + ", WhId=" + WhId + ", WhOutEntryDate="
				+ WhOutEntryDate + ", WhName=" + WhName + ", CustomerId=" + CustomerId + ", CustomerName="
				+ CustomerName + ", Remark=" + Remark + ", Status=" + Status + ", IsApproved=" + IsApproved
				+ ", CreatedId=" + CreatedId + ", UpdatedId=" + UpdatedId + ", Storages=" + Storages + ", Pendings="
				+ Pendings + ", IsSureDate=" + IsSureDate + ", ShipTypeCode=" + ShipTypeCode + ", StockTypeCode="
				+ StockTypeCode + ", SaleDeptId=" + SaleDeptId + ", SalePointId=" + SalePointId + ", SupplierName="
				+ SupplierName + ", IsSureData=" + IsSureData + ", DeliveryTelePhone=" + DeliveryTelePhone + "]";
	}

}

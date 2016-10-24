
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Area;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;

@Entity
@Table(name = "BakCustomer", schema = "Basis")

public class BakCustomer extends HibernateEntity {
	private static final long serialVersionUID = 1461832991339L;
	/**
	 * 是否是内部客户{true = 是内部客户, false = 不是内部客户}
	 */
	@Column(name = "IsInternalCustomer")
	@JsonProperty(value = "IsInternalCustomer")
	private Boolean IsInternalCustomer;
	/**
	 * 客户名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 客户英文名称
	 */
	@Column(name = "EnName")
	@JsonProperty(value = "EnName")
	private String EnName;
	/**
	 * 客户简称
	 */
	@Column(name = "ShortName")
	@JsonProperty(value = "ShortName")
	private String ShortName;
	/**
	 * 注册资本
	 */
	@Column(name = "RegisteredCapital")
	@JsonProperty(value = "RegisteredCapital")
	private BigDecimal RegisteredCapital;
	/**
	 * 营业执照
	 */
	@Column(name = "TradingLicenseNo")
	@JsonProperty(value = "TradingLicenseNo")
	private String TradingLicenseNo;
	/**
	 * 组织机构代码证
	 */
	@Column(name = "OrgLicenseNo")
	@JsonProperty(value = "OrgLicenseNo")
	private String OrgLicenseNo;
	/**
	 * 一般纳税人号
	 */
	@Column(name = "TaxVerifyNo")
	@JsonProperty(value = "TaxVerifyNo")
	private String TaxVerifyNo;
	/**
	 * 税务登记证
	 */
	@Column(name = "TaxLicenseNo")
	@JsonProperty(value = "TaxLicenseNo")
	private String TaxLicenseNo;
	/**
	 * 客户代码
	 */
	@Column(name = "Code", length = 64)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 伙伴类型（Dict）
	 */
	@Column(name = "Type", length = 30)
	@JsonProperty(value = "Type")
	private String Type;
	/**
	 * 企业性质（类型）
	 */
	@Column(name = "OwnerType")
	@JsonProperty(value = "OwnerType")
	private String OwnerType;
	/**
	 * 合作类型
	 */
	@Column(name = "CooperationType")
	@JsonProperty(value = "CooperationType")
	private String CooperationType;
	/**
	 * 终端类型
	 */
	@Column(name = "TerminalType")
	@JsonProperty(value = "TerminalType")
	private String TerminalType;
	/**
	 * 准入类型
	 */
	@Column(name = "EntryType")
	@JsonProperty(value = "EntryType")
	private String EntryType;
	/**
	 * 联系人
	 */
	@Column(name = "Liaison", length = 64)
	@JsonProperty(value = "Liaison")
	private String Liaison;
	/**
	 * 邮箱
	 */
	@Column(name = "Email", length = 64)
	@JsonProperty(value = "Email")
	private String Email;
	/**
	 * 电话
	 */
	@Column(name = "Tel", length = 64)
	@JsonProperty(value = "Tel")
	private String Tel;
	/**
	 * 传真
	 */
	@Column(name = "Fax", length = 64)
	@JsonProperty(value = "Fax")
	private String Fax;
	/**
	 * 地址
	 */
	@Column(name = "Address", length = 100)
	@JsonProperty(value = "Address")
	private String Address;
	/**
	 * 邮编
	 */
	@Column(name = "Zip", length = 6)
	@JsonProperty(value = "Zip")
	private String Zip;
	/**
	 * Logo存放的绝对路径
	 */
	@Column(name = "LogoUrl")
	@JsonProperty(value = "LogoUrl")
	private String LogoUrl;
	/**
	 * 合同审核的状态(Dict){ 0= 待审，1=pass, -1=deny,..............}
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status")
	private Integer Status;
	/**
	 * 是否通过
	 */
	@Column(name = "IsApproved")
	@JsonProperty(value = "IsApproved")
	private Boolean IsApproved;
	/**
	 *
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 属于哪个地区
	 */
	@Column(name = "AreaId")
	@JsonProperty(value = "AreaId")
	private String AreaId;
	// @JsonBackReference("BakCustomer_Area")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Area.class)
	@JoinColumn(name = "AreaId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Area")
	private Area Area;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;

	public Boolean getIsInternalCustomer() {
		return IsInternalCustomer;
	}

	public void setIsInternalCustomer(Boolean IsInternalCustomer) {
		this.IsInternalCustomer = IsInternalCustomer;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getEnName() {
		return EnName;
	}

	public void setEnName(String EnName) {
		this.EnName = EnName;
	}

	public String getShortName() {
		return ShortName;
	}

	public void setShortName(String ShortName) {
		this.ShortName = ShortName;
	}

	public BigDecimal getRegisteredCapital() {
		return RegisteredCapital;
	}

	public void setRegisteredCapital(BigDecimal RegisteredCapital) {
		this.RegisteredCapital = RegisteredCapital;
	}

	public String getTradingLicenseNo() {
		return TradingLicenseNo;
	}

	public void setTradingLicenseNo(String TradingLicenseNo) {
		this.TradingLicenseNo = TradingLicenseNo;
	}

	public String getOrgLicenseNo() {
		return OrgLicenseNo;
	}

	public void setOrgLicenseNo(String OrgLicenseNo) {
		this.OrgLicenseNo = OrgLicenseNo;
	}

	public String getTaxVerifyNo() {
		return TaxVerifyNo;
	}

	public void setTaxVerifyNo(String TaxVerifyNo) {
		this.TaxVerifyNo = TaxVerifyNo;
	}

	public String getTaxLicenseNo() {
		return TaxLicenseNo;
	}

	public void setTaxLicenseNo(String TaxLicenseNo) {
		this.TaxLicenseNo = TaxLicenseNo;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String Code) {
		this.Code = Code;
	}

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public String getOwnerType() {
		return OwnerType;
	}

	public void setOwnerType(String OwnerType) {
		this.OwnerType = OwnerType;
	}

	public String getCooperationType() {
		return CooperationType;
	}

	public void setCooperationType(String CooperationType) {
		this.CooperationType = CooperationType;
	}

	public String getTerminalType() {
		return TerminalType;
	}

	public void setTerminalType(String TerminalType) {
		this.TerminalType = TerminalType;
	}

	public String getEntryType() {
		return EntryType;
	}

	public void setEntryType(String EntryType) {
		this.EntryType = EntryType;
	}

	public String getLiaison() {
		return Liaison;
	}

	public void setLiaison(String Liaison) {
		this.Liaison = Liaison;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String Tel) {
		this.Tel = Tel;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String Fax) {
		this.Fax = Fax;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getZip() {
		return Zip;
	}

	public void setZip(String Zip) {
		this.Zip = Zip;
	}

	public String getLogoUrl() {
		return LogoUrl;
	}

	public void setLogoUrl(String LogoUrl) {
		this.LogoUrl = LogoUrl;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Boolean getIsApproved() {
		return IsApproved;
	}

	public void setIsApproved(Boolean IsApproved) {
		this.IsApproved = IsApproved;
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

	public String getAreaId() {
		return AreaId;
	}

	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
	}

	public Area getArea() {
		return Area;
	}

	public void setArea(Area Area) {
		this.Area = Area;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public User getCreated() {
		return Created;
	}

	public void setCreated(User Created) {
		this.Created = Created;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

	public User getUpdated() {
		return Updated;
	}

	public void setUpdated(User Updated) {
		this.Updated = Updated;
	}

}
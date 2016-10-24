
package com.smm.ctrm.domain.Basis;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Users", schema = "Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 * 用户名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 用户密码
	 */
	@Column(name = "Password", length = 128)
	@JsonProperty(value = "Password")
	private String Password;
	/**
	 * 用户账户
	 */
	@Column(name = "Account", length = 32, nullable = false)
	@JsonProperty(value = "Account")
	private String Account;
	/**
	 * 员工编号
	 */
	@Column(name = "StaffNo", length = 32)
	@JsonProperty(value = "StaffNo")
	private String StaffNo;
	/**
	 * 别名
	 */
	@Column(name = "Alias", length = 32)
	@JsonProperty(value = "Alias")
	private String Alias;
	/**
	 * 邮件
	 */
	@Column(name = "Email", length = 100)
	@JsonProperty(value = "Email")
	private String Email;
	/**
	 * 电话
	 */
	@Column(name = "Mobile", length = 20)
	@JsonProperty(value = "Mobile")
	private String Mobile;
	/**
	 * 是否锁定
	 */
	@Column(name = "IsLocked")
	@JsonProperty(value = "IsLocked")
	private Boolean IsLocked;
	/**
	 * 最近登陆
	 */
	@Column(name = "LastLogin")
	@JsonProperty(value = "LastLogin")
	private Date LastLogin;
	/**
	 * 是否拥有交易的权限，如果是，则属于“交易账号”
	 */
	@Column(name = "IsTrader")
	@JsonProperty(value = "IsTrader", defaultValue = "false")
	private Boolean IsTrader;
	/**
	 * 是否客户的终审者
	 */
	@Column(name = "IsCustomerTerminator")
	@JsonProperty(value = "IsCustomerTerminator", defaultValue = "false")
	private Boolean IsCustomerTerminator;
	/**
	 * 是否合同的终审者
	 */
	@Column(name = "IsContractTerminator")
	@JsonProperty(value = "IsContractTerminator", defaultValue = "false")
	private Boolean IsContractTerminator;
	/**
	 * 是否付款的终审者
	 */
	@Column(name = "IsPaymentTerminator")
	@JsonProperty(value = "IsPaymentTerminator", defaultValue = "false")
	private Boolean IsPaymentTerminator;
	/**
	 * 是否通过审核,系统自由设置是否需要审核、才可以启用账号
	 */
	@Column(name = "IsAudited")
	@JsonProperty(value = "IsAudited", defaultValue = "false")
	private Boolean IsAudited;
	/**
	 * 是否系统管理员（承担系统的账号，权限信息维护）
	 */
	@Column(name = "IsSysAdmin")
	@JsonProperty(value = "IsSysAdmin", defaultValue = "false")
	private Boolean IsSysAdmin;
	/**
	 * 机构名称：多对一
	 */
	@Column(name = "OrgId")
	@JsonProperty(value = "OrgId")
	private String OrgId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Org.class)
	@JoinColumn(name = "OrgId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Org")
	private Org Org;
	/**
	 * 部门名称：多对一
	 */
	@Column(name = "DivisionId")
	@JsonProperty(value = "DivisionId")
	private String DivisionId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Division.class)
	@JoinColumn(name = "DivisionId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Division")
	private Division Division;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Roles")
	private List<Role> Roles;

	/**
	 * 是否发票的终审者
	 */
	@Column(name = "IsInvoiceTerminator")
	@JsonProperty(value = "IsInvoiceTerminator", defaultValue = "false")
	private Boolean IsInvoiceTerminator;

	/**
	 * 是否发货单的终审者
	 */
	@Column(name = "IsReceiptShipTerminator")
	@JsonProperty(value = "IsReceiptShipTerminator", defaultValue = "false")
	private Boolean IsReceiptShipTerminator;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String Password) {
		this.Password = Password;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String Account) {
		this.Account = Account;
	}

	public String getStaffNo() {
		return StaffNo;
	}

	public void setStaffNo(String StaffNo) {
		this.StaffNo = StaffNo;
	}

	public String getAlias() {
		return Alias;
	}

	public void setAlias(String Alias) {
		this.Alias = Alias;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String Mobile) {
		this.Mobile = Mobile;
	}

	public Boolean getIsLocked() {
		return IsLocked != null ? IsLocked : false;
	}

	public void setIsLocked(Boolean IsLocked) {
		this.IsLocked = IsLocked;
	}

	public Date getLastLogin() {
		return LastLogin;
	}

	public void setLastLogin(Date LastLogin) {
		this.LastLogin = LastLogin;
	}

	public Boolean getIsTrader() {
		return IsTrader != null ? IsTrader : false;
	}

	public void setIsTrader(Boolean IsTrader) {
		this.IsTrader = IsTrader;
	}

	public Boolean getIsCustomerTerminator() {
		return IsCustomerTerminator != null ? IsCustomerTerminator : false;
	}

	public void setIsCustomerTerminator(Boolean IsCustomerTerminator) {
		this.IsCustomerTerminator = IsCustomerTerminator;
	}

	public Boolean getIsContractTerminator() {
		return IsContractTerminator != null ? IsContractTerminator : false;
	}

	public void setIsContractTerminator(Boolean IsContractTerminator) {
		this.IsContractTerminator = IsContractTerminator;
	}

	public Boolean getIsPaymentTerminator() {
		return IsPaymentTerminator != null ? IsPaymentTerminator : false;
	}

	public void setIsPaymentTerminator(Boolean IsPaymentTerminator) {
		this.IsPaymentTerminator = IsPaymentTerminator;
	}

	public Boolean getIsAudited() {
		return IsAudited != null ? IsAudited : false;
	}

	public void setIsAudited(Boolean IsAudited) {
		this.IsAudited = IsAudited;
	}

	public Boolean getIsSysAdmin() {
		return IsSysAdmin != null ? IsSysAdmin : false;
	}

	public void setIsSysAdmin(Boolean IsSysAdmin) {
		this.IsSysAdmin = IsSysAdmin;
	}

	public String getOrgId() {
		return OrgId;
	}

	public void setOrgId(String OrgId) {
		this.OrgId = OrgId;
	}

	public Org getOrg() {
		return Org;
	}

	public void setOrg(Org Org) {
		this.Org = Org;
	}

	public String getDivisionId() {
		return DivisionId;
	}

	public void setDivisionId(String DivisionId) {
		this.DivisionId = DivisionId;
	}

	public Division getDivision() {
		return Division;
	}

	public void setDivision(Division Division) {
		this.Division = Division;
	}

	public List<Role> getRoles() {
		return Roles;
	}

	public void setRoles(List<Role> Roles) {
		this.Roles = Roles;
	}

	public Boolean getIsInvoiceTerminator() {
		return IsInvoiceTerminator != null ? IsInvoiceTerminator : false;
	}

	public void setIsInvoiceTerminator(Boolean isInvoiceTerminator) {
		IsInvoiceTerminator = isInvoiceTerminator;
	}

	public Boolean getIsReceiptShipTerminator() {
		return IsReceiptShipTerminator != null ? IsReceiptShipTerminator : false;
	}

	public void setIsReceiptShipTerminator(Boolean isReceiptShipTerminator) {
		IsReceiptShipTerminator = isReceiptShipTerminator;
	}

}

package com.smm.ctrm.domain.Basis;

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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Division", schema = "Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Division extends HibernateEntity {
	private static final long serialVersionUID = 1461832991322L;
	/**
	 * 部门名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 部门代码
	 */
	@Column(name = "Code")
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 电话
	 */
	@Column(name = "Tel")
	@JsonProperty(value = "Tel")
	private String Tel;
	/**
	 * 传真
	 */
	@Column(name = "Fax")
	@JsonProperty(value = "Fax")
	private String Fax;
	/**
	 * 备注说明
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;

	/**
	 * 上一级部门的标识
	 */
	@Column(name = "ParentId")
	@JsonProperty(value = "ParentId")
	private String ParentId;
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Division.class)
	// @JoinColumn(name = "ParentId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name="none"))
	@Transient
	@JsonProperty(value = "Parent")
	private Division Parent;

	// @OneToMany(fetch = FetchType.EAGER, targetEntity = Division.class,
	// cascade=CascadeType.ALL, orphanRemoval=true/*, mappedBy="Parent"*/)
	// @JoinColumn(name="ParentId", foreignKey=@ForeignKey(name="none"))
	// @Fetch(FetchMode.SUBSELECT)
	@Transient
	@JsonProperty(value = "children")
	private List<Division> children;
	/**
	 * 机构名称：多对一
	 */
	@Column(name = "OrgId")
	@JsonProperty(value = "OrgId")
	private String OrgId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Org.class)
	@JoinColumn(name = "OrgId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Org")
	private Org Org;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String Code) {
		this.Code = Code;
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

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String ParentId) {
		this.ParentId = ParentId;
	}

	public Division getParent() {
		return Parent;
	}

	public void setParent(Division Parent) {
		this.Parent = Parent;
	}

	public List<Division> getChildren() {
		return children;
	}

	public void setChildren(List<Division> Children) {
		this.children = Children;
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

}
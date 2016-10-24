
package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Dictionary", schema = "Basis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dictionary extends HibernateEntity {
	
	private static final long serialVersionUID = 1461832991322L;
	public static final String REPLACE_OLD_DOC = "ReplaceOldDoc";
	/**
	 * 名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 字典项
	 */
	@Column(name = "Code")
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 字典值
	 */
	@Column(name = "Value", length = 30)
	@JsonProperty(value = "Value")
	private String Value;
	/**
	 * 语言
	 */
	@Column(name = "Lang", length = 10)
	@JsonProperty(value = "Lang")
	private String Lang;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex", defaultValue = "0")
	private Integer OrderIndex;
	/**
	 * 父节点
	 */
	@Column(name = "ParentId")
	@JsonProperty(value = "ParentId")
	private String ParentId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Dictionary.class)
	@JoinColumn(name = "ParentId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Parent")
	private Dictionary Parent;

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

	public String getValue() {
		return Value;
	}

	public void setValue(String Value) {
		this.Value = Value;
	}

	public String getLang() {
		return Lang;
	}

	public void setLang(String Lang) {
		this.Lang = Lang;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public Integer getOrderIndex() {
		return OrderIndex;
	}

	public void setOrderIndex(Integer OrderIndex) {
		this.OrderIndex = OrderIndex;
	}

	public String getParentId() {
		return ParentId;
	}

	public void setParentId(String ParentId) {
		this.ParentId = ParentId;
	}

	public Dictionary getParent() {
		return Parent;
	}

	public void setParent(Dictionary Parent) {
		this.Parent = Parent;
	}

}
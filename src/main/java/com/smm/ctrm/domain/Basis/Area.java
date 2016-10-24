
package com.smm.ctrm.domain.Basis;

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
@Table(name = "Area", schema = "Basis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Area extends HibernateEntity {
	private static final long serialVersionUID = 1461832991321L;
	/**
	 * 地区名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex", defaultValue = "0")
	private Integer OrderIndex;
	/**
	 * 父结点
	 */
	@Column(name = "ParentId")
	@JsonProperty(value = "ParentId")
	private String ParentId;
	// @JsonBackReference(value="Area-Parent")
	/*
	 * @ManyToOne(fetch = FetchType.EAGER, targetEntity = Area.class)
	 * 
	 * @JoinColumn(name = "ParentId", insertable = false, updatable = false,
	 * foreignKey = @ForeignKey(name="none"))
	 */
	@Transient
	@JsonProperty(value = "Parent")
	private Area Parent;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
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
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
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

	public Area getParent() {
		return Parent;
	}

	public void setParent(Area Parent) {
		this.Parent = Parent;
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
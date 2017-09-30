
package com.smm.ctrm.domain;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.util.DateUtil;

@MappedSuperclass
// @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,
// property="@Id")
public abstract class HibernateEntity implements Serializable {
	private static final long serialVersionUID = 1461575705448L;
	/**
	 * 主键
	 */
//	@Id
//	@GenericGenerator(name = "generator", strategy = "guid", parameters = {})
//	@GeneratedValue(generator = "generator")
//	@Column(name = "Id", columnDefinition = "uniqueidentifier")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(value = "Id")
	private String Id;
	/**
	 * 并发版本控制
	 */
	@javax.persistence.Version
	@JsonProperty(value = "Version")
	private Integer Version = 1;
	/**
	 * 实例编码
	 */
	@Column(name = "Instance", length = 36)
	private String Instance;
	/**
	 * 数据状态（新增，编辑，废弃）
	 */
	@Column(name = "IsDeleted")
    @JsonProperty(value = "IsDeleted")
	private Boolean IsDeleted = false;
	/**
	 * 是否隐藏
	 */
	@Column(name = "IsHidden")
	@JsonProperty(value = "IsHidden")
	private Boolean IsHidden = false;
	/**
	 * 创建人
	 */
	@Column(name = "CreatedBy", length = 36)
	@JsonProperty(value = "CreatedBy")
	private String CreatedBy;
	/**
	 * 创建时间
	 */
	@Column(name = "CreatedAt")
	@JsonProperty(value = "CreatedAt")
	private Date CreatedAt;
	/**
	 * 更新人
	 */
	@Column(name = "UpdatedBy", length = 36)
	@JsonProperty(value = "UpdatedBy")
	private String UpdatedBy;
	/**
	 * 更新时间
	 */
	@Column(name = "UpdatedAt")
	@JsonProperty(value = "UpdatedAt")
	private Date UpdatedAt;
	/**
	 * 删除人
	 */
	@Column(name = "DeletedBy", length = 36)
	@JsonProperty(value = "DeletedBy")
	private String DeletedBy;
	/**
	 * 删除时间
	 */
	@Column(name = "DeletedAt")
	@JsonProperty(value = "DeletedAt")
	private Date DeletedAt;

	public String getId() {
		return Id!=null?Id.toUpperCase():Id;
	}

	public void setId(String Id) {
		this.Id = Id!=null?Id.toUpperCase():Id;
	}

	public Integer getVersion() {
		return Version != null ?Version:1;
	}

	// 版本号从1开始
	public void setVersion(Integer Version) {
		this.Version = Version != 0 ? Version : 1;
	}

	@JsonIgnore
	public String getInstance() {
		return Instance;
	}

	public void setInstance(String Instance) {
		this.Instance = Instance;
	}

	@JsonIgnore
	public Boolean getIsDeleted() {
		return IsDeleted;
	}

	public void setIsDeleted(Boolean IsDeleted) {

		this.IsDeleted = IsDeleted;
	}

	public Boolean getIsHidden() {
		return IsHidden;
	}

	public void setIsHidden(Boolean IsHidden) {
		this.IsHidden = IsHidden;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String CreatedBy) {
		this.CreatedBy = CreatedBy;
	}

	public Date getCreatedAt() {
		return CreatedAt!=null?CreatedAt:DateUtil.doSFormatDate("1970-01-01", "yyyy-MM-dd");
	}

	public void setCreatedAt(Date CreatedAt) {
		this.CreatedAt = CreatedAt;
	}

	public String getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(String UpdatedBy) {
		this.UpdatedBy = UpdatedBy;
	}

	public Date getUpdatedAt() {
		return UpdatedAt;
	}

	public void setUpdatedAt(Date UpdatedAt) {
		this.UpdatedAt = UpdatedAt;
	}

	public String getDeletedBy() {
		return DeletedBy;
	}

	public void setDeletedBy(String DeletedBy) {
		this.DeletedBy = DeletedBy;
	}

	public Date getDeletedAt() {
		return DeletedAt;
	}

	public void setDeletedAt(Date DeletedAt) {
		this.DeletedAt = DeletedAt;
	}

	@Override
	public String toString(){
		return super.toString() + "@" + this.getId();
	}
}

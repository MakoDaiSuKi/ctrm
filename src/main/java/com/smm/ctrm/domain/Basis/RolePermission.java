package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * 数据权限
 * 
 * @author zengshihua
 *
 */
@Entity
@Table(name = "RolePermission", schema = "Basis")
public class RolePermission extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5561787850852002455L;

	/**
	 * 角色ID
	 */
	@Column(name = "RoleId")
	@JsonProperty(value = "RoleId")
	public String RoleId;

	/**
	 * 分配的角色Id
	 */
	@Column(name = "SubRoleId")
	@JsonProperty(value = "SubRoleId")
	public String SubRoleId;

	/**
	 * 权限类型 Q=查询 D=删除 U=更新 A=添加
	 */
	@Column(name = "IsQuery")
	@JsonProperty(value = "IsQuery")
	public boolean IsQuery;

	/**
	 * 
	 */
	@Column(name = "IsDelete")
	@JsonProperty(value = "IsDelete")
	public boolean IsDelete;

	/**
	 * 
	 */
	@Column(name = "IsUpdate")
	@JsonProperty(value = "IsUpdate")
	public boolean IsUpdate;

	/**
	 * 
	 */
	@Column(name = "IsAdd")
	@JsonProperty(value = "IsAdd")
	public boolean IsAdd;

	/**
	 * 备注
	 */
	@Column(name = "Remark")
	@JsonProperty(value = "Remark")
	public String Remark;

	public String getRoleId() {
		return RoleId;
	}

	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

	public String getSubRoleId() {
		return SubRoleId;
	}

	public void setSubRoleId(String subRoleId) {
		SubRoleId = subRoleId;
	}

	public boolean getIsQuery() {
		return IsQuery;
	}

	public void setIsQuery(boolean isQuery) {
		IsQuery = isQuery;
	}

	public boolean isIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(boolean isDelete) {
		IsDelete = isDelete;
	}

	public boolean getIsUpdate() {
		return IsUpdate;
	}

	public void setIsUpdate(boolean isUpdate) {
		IsUpdate = isUpdate;
	}

	public boolean isIsAdd() {
		return IsAdd;
	}

	public void setIsAdd(boolean isAdd) {
		IsAdd = isAdd;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

}

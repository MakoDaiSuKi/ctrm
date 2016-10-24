package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.RolePermission;
import com.smm.ctrm.dto.res.ActionResult;

public interface RolePermissionService {
	ActionResult<String> Save(List<RolePermission> rolePermission);

	ActionResult<List<RolePermission>> GetByRoleId(String id);
}

package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface RoleService {
	ActionResult<List<Role>> Roles();
	ActionResult<Role> Save(Role role);
	ActionResult<Role> AllocateUsers2Role(Role role);
	ActionResult<Role> AllocateMenus2Role(Role role);
	ActionResult<String> Delete(String roleId);
	ActionResult<Role> GetById(String roleId);
	ActionResult<Role> GetByIdDetail(String roleId);
	ActionResult<Role> GetByIdIncUsers(String roleId);
	ActionResult<Role> GetByIdIncMenus(String roleId);

}
package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.RolePermissionService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.RolePermission;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Auth/RolePermission/")
public class RolePermissionApiController {

	@Autowired
	private RolePermissionService rolePermissionService;

	@Autowired
	private CommonService commonService;

	/**
	 * 保存
	 * 
	 * @param rolePermission
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody List<RolePermission> rolePermission) {
		try {

			for (RolePermission item : rolePermission) {
				if (item.getId() != null) {
					item.setUpdatedAt(new Date());
					item.setUpdatedBy(LoginHelper.GetLoginInfo().getName());
				} else {
					item.setCreatedAt(new Date());
					item.setCreatedBy(LoginHelper.GetLoginInfo().getName());
				}
			}

			return rolePermissionService.Save(rolePermission);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ActionResult<String>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
	}

	/**
	 * 根据RoleId获取列表
	 * 
	 * @return
	 */
	@RequestMapping("GetByRoleId")
	@ResponseBody
	public ActionResult<List<RolePermission>> GetByRoleId(@RequestBody String id) {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			commonService.GetUserPermissionByUser(loginInfo.UserId);
			return rolePermissionService.GetByRoleId(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ActionResult<List<RolePermission>>(Boolean.FALSE, ex.getMessage());
		}
	}
}

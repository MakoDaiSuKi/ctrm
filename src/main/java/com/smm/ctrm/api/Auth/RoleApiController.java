



package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.RoleService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.Basis.Role;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Auth/Role/")
public class RoleApiController {
	
	@Resource
	private RoleService roleService;

	@Resource
	private CommonService commonService;


	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Role> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return roleService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetByIdDetail")
	@ResponseBody
	public ActionResult<Role> GetByIdDetail(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return roleService.GetByIdDetail(id);
		} catch (Exception ex) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetByIdIncUsers")
	@ResponseBody
	public ActionResult<Role> GetByIdIncUsers(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return roleService.GetByIdIncUsers(id);
		} catch (Exception ex) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetByIdIncMenus")
	@ResponseBody
	public ActionResult<Role> GetByIdIncMenus(HttpServletRequest request, @RequestBody String id) {
		try {
			return roleService.GetByIdIncMenus(id);
		} catch (Exception ex) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("Roles")
	@ResponseBody
	public ActionResult<List<Role>> Roles(HttpServletRequest request) {
		return roleService.Roles();
	}

	/**
	 * 保存
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Role> Save(HttpServletRequest request, @RequestBody Role role) {
		try {
			if (role.getId()!=null) {
				role.setUpdatedAt(new Date());
				role.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				role.setCreatedAt(new Date());
				role.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return roleService.Save(role);
		} catch (Exception e) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setData(null);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(MessageCtrm.SaveFaile);
			return tempVar;
		}
	}

	@RequestMapping("AllocateUsers2Role")
	@ResponseBody
	public ActionResult<Role> AllocateUsers2Role(HttpServletRequest request, @RequestBody Role role) {
		try {
			return roleService.AllocateUsers2Role(role);
		} catch (Exception e) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(MessageCtrm.Faile);
			return tempVar;
		}
	}

	@RequestMapping("AllocateMenus2Role")
	@ResponseBody
	public ActionResult<Role> AllocateMenus2Role(HttpServletRequest request, @RequestBody Role role) {
		try {
			return roleService.AllocateMenus2Role(role);
		} catch (Exception e) {
			ActionResult<Role> tempVar = new ActionResult<Role>();
			tempVar.setSuccess(false);
			tempVar.setMessage(MessageCtrm.Faile);
			return tempVar;
		}
	}

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return roleService.Delete(id);
		} catch (Exception e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("delete fail");
			return tempVar;
		}
	}
}
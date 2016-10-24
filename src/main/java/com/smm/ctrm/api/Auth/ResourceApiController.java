



package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.ResourceService;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Auth/Resource/")
public class ResourceApiController {

	@Autowired
	private ResourceService resourceService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Resource> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return resourceService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Resource> tempVar = new ActionResult<Resource>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	///// <summary>
	///// 根据角色获取资源
	///// </summary>
	///// <param name="id"></param>
	///// <returns></returns>
	// [System.Web.Http.HttpPost]
	// public dynamic GetResourcesByRoleId([FromBody] string id)
	// {
	// try
	// {
	// id? id = id;
	// return ResourceService.GetResourcesByRoleId(id);
	// }
	// catch (Exception ex)
	// {
	// return new ActionResult<string>
	// {
	// Success = false,
	// Message = ex.Message
	// };
	// }
	// }

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("Resources")
	@ResponseBody
	public ActionResult<List<Resource>> Resources(HttpServletRequest request) {
		return resourceService.Resources();
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
			return resourceService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Resource> Save(HttpServletRequest request, @RequestBody Resource resource) {
		try {
			if (resource.getId()!=null) {
				resource.setUpdatedAt(new Date());
				resource.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				resource.setCreatedAt(new Date());
				resource.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return resourceService.Save(resource);
		} catch (Exception e) {
			ActionResult<Resource> tempVar = new ActionResult<Resource>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("save fail");
			return tempVar;
		}
	}

	/**
	 * 指定控件向上移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			resourceService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 指定控件向下移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			resourceService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
}
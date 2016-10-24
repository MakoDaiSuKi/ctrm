



package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.DivisionService;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Auth/Division/")
public class DivisionApiController {
	
	@Autowired
	private DivisionService divisionService;


	/** 
	 根据Id获得单个实体
	 @param id
	 @return 
	*/
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Division> GetById(HttpServletRequest request, @RequestBody String id)
	{
		try
		{
			return divisionService.GetById(id);
		}
		catch (Exception ex)
		{
			ActionResult<Division> tempVar = new ActionResult<Division>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	/** 
	 不含分页的列表
	 @return 
	*/
	@RequestMapping("Divisions")
	@ResponseBody
	public ActionResult<List<Division>> Divisions(HttpServletRequest request)
	{
		try
		{
			return divisionService.Divisions();
		}
		catch(RuntimeException ex)
		{
			ActionResult<List<Division>> tempVar = new ActionResult<List<Division>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	
	/** 
	 获取Org下的部门
	 @param id
	 @return 
	*/
	@RequestMapping("GetDivisionsByOrgId")
	@ResponseBody
	public ActionResult<List<Division>> GetDivisionsByOrgId(HttpServletRequest request, @RequestBody String orgId){
		try
		{
			return divisionService.GetDivisionsByOrgId(orgId);
		}
		catch (RuntimeException ex)
		{
			ActionResult<List<Division>> tempVar = new ActionResult<List<Division>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	
	/** 
	 根据Id删除单个实体
	 @param id
	 @return 
	*/
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String guid)
	{
		try
		{
			return divisionService.Delete(guid);
		}
		catch (RuntimeException ex)
		{
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	
	/** 
	 保存
	 @param division
	 @return 
	*/
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Division> Save(HttpServletRequest request, @RequestBody Division division)
	{
		if (division.getId()!=null)
		{
			division.setUpdatedAt(new Date());
			division.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		else
		{
			division.setCreatedAt(new Date());
			division.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return divisionService.Save(division);
	}

	/** 
	 指定控件向上移动
	 @param id
	 @return 
	*/
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id)
	{
		if (StringUtils.isBlank(id))
		{
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try
		{
			divisionService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		}
		catch (RuntimeException ex)
		{
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
	/** 
	 指定控件向下移动
	 
	 @param id
	 @return 
	*/
	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id)
	{
		if (StringUtils.isBlank(id))
		{
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try
		{
			divisionService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		}
		catch (RuntimeException ex)
		{
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
	
}
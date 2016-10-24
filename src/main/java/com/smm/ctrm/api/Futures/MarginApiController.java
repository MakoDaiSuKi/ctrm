



package com.smm.ctrm.api.Futures;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Futures.MarginService;
import com.smm.ctrm.domain.Physical.Margin;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Futures/Margin/")
public class MarginApiController {
	
	@Resource
	private MarginService marginService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Margins")
	@ResponseBody
	public ActionResult<List<Margin>> Margins(HttpServletRequest request) {
		return marginService.Margins();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Margin> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return marginService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Margin> tempVar = new ActionResult<Margin>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult Save(HttpServletRequest request, @RequestBody Margin margin) {
		try {
			if (margin.getId()!=null) {
				margin.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				margin.setUpdatedAt(new Date());
			} else {
				margin.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				margin.setCreatedAt(new Date());
			}
			return marginService.Save(margin);
		} catch (Exception ex) {
			ActionResult<Margin> tempVar = new ActionResult<Margin>();
			tempVar.setSuccess(false);
			tempVar.setData(margin);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return marginService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
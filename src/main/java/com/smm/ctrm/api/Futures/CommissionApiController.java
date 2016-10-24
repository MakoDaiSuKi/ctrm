



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

import com.smm.ctrm.bo.Futures.CommissionService;
import com.smm.ctrm.domain.Physical.Commission;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Futures/Commission/")
public class CommissionApiController {

	@Resource
	private CommissionService commissionService;


	@RequestMapping("Commissions")
	@ResponseBody
	public ActionResult<List<Commission>> Commissions(HttpServletRequest request) {
		return commissionService.Commissions();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Commission> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return commissionService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Commission> tempVar = new ActionResult<Commission>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param commission
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Commission> Save(HttpServletRequest request, @RequestBody Commission commission) {
		try {
			if (commission.getId()!=null) {
				commission.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				commission.setUpdatedAt(new Date());
			} else {
				commission.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				commission.setCreatedAt(new Date());
			}
			return commissionService.Save(commission);
		} catch (RuntimeException ex) {
			ActionResult<Commission> tempVar = new ActionResult<Commission>();
			tempVar.setSuccess(false);
			tempVar.setData(commission);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据Id删除单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return commissionService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
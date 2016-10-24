



package com.smm.ctrm.api.Physical;

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

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.DischargingPriceDiffService;
import com.smm.ctrm.domain.Physical.DischargingPriceDiff;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by zhenghao on 2016/4/21.
 */

@Controller
@RequestMapping("api/Physical/DischargingPriceDiff/")
public class DischargingPriceDiffApiController {
	
	@Resource
	private DischargingPriceDiffService dischargingPriceDiffService;

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
	public ActionResult<DischargingPriceDiff> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return dischargingPriceDiffService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<DischargingPriceDiff> tempVar = new ActionResult<DischargingPriceDiff>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	@RequestMapping("Grades")
	@ResponseBody
	public  ActionResult<List<DischargingPriceDiff>> Grades(HttpServletRequest request) {
		
		List<DischargingPriceDiff> pricediffs = dischargingPriceDiffService.PriceDiffs();
		ActionResult<List<DischargingPriceDiff>> tempVar = new ActionResult<List<DischargingPriceDiff>>();
		tempVar.setSuccess(true);
		tempVar.setData(pricediffs);
		return tempVar;
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String gradeId) {
		try {
			return dischargingPriceDiffService.Delete(gradeId);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("DeleteFail");
			return tempVar;
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<DischargingPriceDiff> Save(HttpServletRequest request, @RequestBody DischargingPriceDiff pricediff) {
		try {
			if (pricediff.getId()!=null) {
				pricediff.setUpdatedAt(new Date());
				pricediff.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				pricediff.setCreatedAt(new Date());
				pricediff.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return dischargingPriceDiffService.Save(pricediff);
		} catch (RuntimeException e) {
			ActionResult<DischargingPriceDiff> tempVar = new ActionResult<DischargingPriceDiff>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(MessageCtrm.SaveFaile);
			return tempVar;
		}
	}

	@RequestMapping("PriceDiffsByContractId")
	@ResponseBody
	public ActionResult<List<DischargingPriceDiff>> PriceDiffsByContractId(HttpServletRequest request, @RequestBody String contractId) {
		return dischargingPriceDiffService.PriceDiffsByContractId(contractId);
	}
}
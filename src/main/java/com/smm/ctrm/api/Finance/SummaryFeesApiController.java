



package com.smm.ctrm.api.Finance;

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

import com.smm.ctrm.bo.Finance.SummaryFeesService;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Finance/SummaryFees/")
public class SummaryFeesApiController {
	
	@Resource
	private SummaryFeesService sumFeeService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<SummaryFees> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return sumFeeService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<SummaryFees> tempVar = new ActionResult<SummaryFees>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
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
			return sumFeeService.Delete(id);
		} catch (RuntimeException ex) {
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
	 * @param fee
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<SummaryFees> Save(HttpServletRequest request, @RequestBody SummaryFees fee) {
		try {
			if (fee.getId()!=null) {
				fee.setUpdatedAt(new Date());
				fee.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				fee.setCreatedAt(new Date());
				fee.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return sumFeeService.Save(fee);
		} catch (RuntimeException ex) {
			ActionResult<SummaryFees> tempVar = new ActionResult<SummaryFees>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("SummaryFeesByInvoiceId")
	@ResponseBody
	public ActionResult<List<SummaryFees>> SummaryFeesByInvoiceId(HttpServletRequest request, @RequestBody String lotId) {
		try {
			
			return sumFeeService.SummaryFeesByInvoiceId(lotId);
		} catch (RuntimeException ex) {
			ActionResult<List<SummaryFees>> tempVar = new ActionResult<List<SummaryFees>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
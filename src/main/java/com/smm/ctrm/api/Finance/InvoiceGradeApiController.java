



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

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.InvoiceGradeService;
import com.smm.ctrm.domain.Physical.InvoiceGrade;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Finance/InvoiceGrade/")
public class InvoiceGradeApiController {
	
	
	@Resource
	private InvoiceGradeService invoiceGradeService;

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
	public ActionResult<InvoiceGrade> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return invoiceGradeService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<InvoiceGrade> tempVar = new ActionResult<InvoiceGrade>();
			tempVar.setSuccess(false);
			tempVar.setSuccess(true);
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
			return invoiceGradeService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setSuccess(true);
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param invoiceGrade
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody InvoiceGrade invoiceGrade) {
		if (invoiceGrade.getId()!=null) {
			invoiceGrade.setUpdatedAt(new Date());
			invoiceGrade.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			invoiceGrade.setCreatedAt(new Date());
			invoiceGrade.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return invoiceGradeService.Save(invoiceGrade);
	}

	/**
	 * 获取指定发票的附属Grades
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetByInvoiceId")
	@ResponseBody
	public ActionResult<List<InvoiceGrade>> GetByInvoiceId(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return invoiceGradeService.GetByInvoiceId(id);
		} catch (RuntimeException ex) {
			ActionResult<List<InvoiceGrade>> tempVar = new ActionResult<List<InvoiceGrade>>();
			tempVar.setSuccess(false);
			tempVar.setSuccess(true);
			return tempVar;
		}
	}
}
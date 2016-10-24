
package com.smm.ctrm.api.Physical;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.GradeService;
import com.smm.ctrm.domain.Physical.Grade;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by zhenghao on 2016/4/21.
 */
@Controller
@RequestMapping("api/Physical/Grade/")
public class GradeApiController {

	@Resource
	private GradeService gradeService;

	@Resource
	private CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Grade> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return gradeService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Grade> tempVar = new ActionResult<Grade>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	@RequestMapping("Grades")
	@ResponseBody
	public ActionResult<List<Grade>> Grades(HttpServletRequest request) {
		List<Grade> grades = gradeService.Grades();
		ActionResult<List<Grade>> tempVar = new ActionResult<List<Grade>>();
		tempVar.setSuccess(true);
		tempVar.setData(grades);
		return tempVar;
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String gradeId) {
		try {
			return gradeService.Delete(gradeId);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Delete Fail");
			return tempVar;
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Grade> Save(HttpServletRequest request, @RequestBody Grade grade) {
		try {
			return gradeService.Save(grade);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
	}

	@RequestMapping(value = "GradesByContractId")
	@ResponseBody
	public ActionResult<List<Grade>> GradesByContractId(HttpServletRequest request, @RequestBody String contractId) {
		return gradeService.GradesByContractId(contractId);
	}

	@RequestMapping("GradesByInvoiceId")
	@ResponseBody
	public ActionResult<List<Grade>> GradesByInvoiceId(HttpServletRequest request, @RequestBody String invoiceId) {

		return gradeService.GradesByInvoiceId(invoiceId);
	}
}
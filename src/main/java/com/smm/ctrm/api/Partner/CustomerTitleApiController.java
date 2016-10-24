
package com.smm.ctrm.api.Partner;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.CustomerTitleService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Partner/CustomerTitle/")
public class CustomerTitleApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private CustomerTitleService customerTitleService;

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
	public ActionResult<CustomerTitle> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return customerTitleService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 获取客户下的抬头
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetTitlesByCustomerId")
	@ResponseBody
	public ActionResult<List<CustomerTitle>> GetTitlesByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		try {
			return customerTitleService.GetTitlesByCustomerId(customerId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("CustomerTitles")
	@ResponseBody
	public ActionResult<List<CustomerTitle>> CustomerTitles(HttpServletRequest request) {
		try {
			return customerTitleService.CustomerTitles();
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return customerTitleService.Delete(id);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(MessageCtrm.DeleteFaile);
			return tempVar2;
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<CustomerTitle> Save(HttpServletRequest request, @RequestBody CustomerTitle customerTitle) {
		try {
			return customerTitleService.Save(customerTitle);
		} catch (RuntimeException e) {
			ActionResult<CustomerTitle> tempVar = new ActionResult<CustomerTitle>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("SaveFail");
			return tempVar;
		}
	}
}
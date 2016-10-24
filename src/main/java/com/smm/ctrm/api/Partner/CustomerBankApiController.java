



package com.smm.ctrm.api.Partner;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.CustomerBankService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Partner/CustomerBank/")
public class CustomerBankApiController {
	
	@Resource
	private CustomerBankService customerBankService;

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
	public ActionResult<CustomerBank> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return customerBankService.GetById(id);
		} catch (Exception ex) {
			ActionResult<CustomerBank> tempVar = new ActionResult<CustomerBank>();
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
	@RequestMapping("CustomerBanks")
	@ResponseBody
	public ActionResult<List<CustomerBank>> CustomerBanks(HttpServletRequest request) {
		return customerBankService.CustomerBanks();
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return customerBankService.Delete(id);
		} catch (Exception e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Delete Fail");
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param customerBank
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<CustomerBank> Save(HttpServletRequest request, @RequestBody CustomerBank customerBank) {
		try {
			if (customerBank.getId()!=null) {
				customerBank.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				customerBank.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return customerBankService.Save(customerBank);
		} catch (Exception e) {
			ActionResult<CustomerBank> tempVar = new ActionResult<CustomerBank>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Save Fail");
			return tempVar;
		}
	}

	/**
	 * 返回客户的银行开户信息的列表
	 * 
	 * @param id
	 *            CustomerId
	 * @return
	 */
	@RequestMapping("GetCustomerBanksByCustomerId")
	@ResponseBody
	public  ActionResult<List<CustomerBank>> GetCustomerBanksByCustomerId(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return customerBankService.GetCustomerBanksByCustomerId(id);
		} catch (Exception ex) {
			ActionResult<List<CustomerBank>> tempVar = new ActionResult<List<CustomerBank>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}
	}

	/**
	 * 返回客户的银行开户信息的列表
	 * 
	 * @param id
	 *            CustomerTitleId
	 * @return
	 */
	@RequestMapping("GetCustomerBanksByCustomerTitleId")
	@ResponseBody
	public ActionResult<List<CustomerBank>> GetCustomerBanksByCustomerTitleId(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return customerBankService.GetCustomerBanksByCustomerTitleId(id);
		} catch (Exception ex) {
			ActionResult<List<CustomerBank>> tempVar = new ActionResult<List<CustomerBank>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

}
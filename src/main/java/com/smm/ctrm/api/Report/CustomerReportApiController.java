
package com.smm.ctrm.api.Report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Report.CustomerReportService;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Report.ModelCustomerBalanceL1;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Report/CustomerReport/")
public class CustomerReportApiController {

	@Resource
	private CustomerReportService customerReportService;

	/**
	 * 客户业务和资金明细对账表
	 * 
	 * @param customers
	 * @return
	 * @return
	 */
	@RequestMapping("PagerCustomerBalance4MultiCustomers")
	@ResponseBody
	public ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4MultiCustomers(List<Customer> customers) {
		try {
			return customerReportService.PagerCustomerBalance4MultiCustomers(customers);
		} catch (RuntimeException ex) {
			ActionResult<List<ModelCustomerBalanceL1>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("PagerCustomerBalance4OneCustomer")
	@ResponseBody
	public ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4OneCustomer(String id) {
		try {
			return customerReportService.PagerCustomerBalance4OneCustomer(id);
		} catch (RuntimeException ex) {
			ActionResult<List<ModelCustomerBalanceL1>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

}
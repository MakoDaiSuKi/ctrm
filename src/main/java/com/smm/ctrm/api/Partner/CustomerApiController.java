
package com.smm.ctrm.api.Partner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.bo.Basis.UserService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.apiClient.CustomerParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

@Controller
@RequestMapping("api/Partner/Customer/")
public class CustomerApiController {

	private Logger logger = Logger.getLogger(CustomerApiController.class);
	@Resource
	private CustomerService customerService;
	
	@Resource
	private CheckService checkService;
	
	@Resource
	private UserService userService;

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Customer>> Pager(HttpServletRequest request, @RequestBody CustomerParams param) {
		try {
			if (param == null) {
				param = new CustomerParams();
			}
			Criteria criteria = customerService.GetCriteria();
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.add(Restrictions.or(Restrictions.like("Name", "%" + param.getKeyword() + "%"),
						Restrictions.like("ShortName", "%" + param.getKeyword() + "%")));
			}

			// 业务状态
			if (!StringUtils.isBlank(param.getStatuses())) {

				String[] strings = param.getStatuses().split(",");

				List<Integer> arrs = Arrays.asList(strings).stream().map(s -> Integer.valueOf(s))
						.collect(Collectors.toList());

				criteria.add(Restrictions.in("Status", arrs));
			}

			RefUtil total = new RefUtil();
			List<Customer> customers = customerService.Customers(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());

			ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
			tempVar.setData(customers);
			tempVar.setTotal(total.getTotal());
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false,e.getMessage());
		}
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Customer> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return customerService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetByCustomerName")
	@ResponseBody
	public ActionResult<List<Customer>> GetByCustomerName(HttpServletRequest request,
			@RequestBody String CustomerName) {
		try {
			if (StringUtils.isBlank(CustomerName)) {
				ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
				tempVar.setSuccess(false);
				tempVar.setMessage("客户名称为空");
				return tempVar;
			}
			ActionResult<List<Customer>> tempVar2 = new ActionResult<List<Customer>>();
			tempVar2.setData(customerService.GetByCustomerName(CustomerName));
			tempVar2.setSuccess(true);
			return tempVar2;
		} catch (RuntimeException ex) {
			ActionResult<List<Customer>> tempVar3 = new ActionResult<List<Customer>>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 不带分页，包括内部客户
	 * 
	 * @return
	 */
	@RequestMapping("CustomersIncInternals")
	@ResponseBody
	public ActionResult<List<Customer>> CustomersIncInternals(HttpServletRequest request) {
		List<Customer> customers = customerService.CustomersCustomersIncInternals();
		ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
		tempVar.setData(customers);
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 不带分页，不包括内部客户
	 * 
	 * @return
	 */
	@RequestMapping("CustomersExcInternals")
	@ResponseBody
	public ActionResult<List<Customer>> CustomersExcInternals(HttpServletRequest request) {
		List<Customer> customers = customerService.CustomersCustomersExcInternals();
		ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
		tempVar.setData(customers);
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 不带分页，仅仅包括内部客户
	 * 
	 * @return
	 */
	@RequestMapping("InternalCustomersOnly")
	@ResponseBody
	public ActionResult<List<Customer>> InternalCustomersOnly(HttpServletRequest request) {
		List<Customer> customers = customerService.InternalCustomersOnly();
		return new ActionResult<>(true, "", customers);
	}

	/**
	 * 批量导入客户，期初时使用
	 * 
	 * @return
	 */
	@RequestMapping("ImportCustomers")
	@ResponseBody
	public ActionResult<List<Customer>> ImportCustomers(HttpServletRequest request) {
		List<Customer> customers = customerService.ImportCustomers();
		ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
		tempVar.setData(customers);
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("Top10CustomerByTurnover")
	@ResponseBody
	public ActionResult<List<Customer>> Top10CustomerByTurnover(HttpServletRequest request) {
		List<Customer> customers = customerService.Top10CustomerByTurnover();
		ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
		tempVar.setData(customers);
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("Top10CustomerByProfit")
	@ResponseBody
	public ActionResult<List<Customer>> Top10CustomerByProfit(HttpServletRequest request) {
		List<Customer> customers = customerService.Top10CustomerByProfit();
		ActionResult<List<Customer>> tempVar = new ActionResult<List<Customer>>();
		tempVar.setData(customers);
		tempVar.setSuccess(true);
		return tempVar;
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
			
			
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Contract);
			tableList.add(TableNameConst.Legal);
			tableList.add(TableNameConst.BankReceipt);
			tableList.add(TableNameConst.Invoice);
			ActionResult<String> checkResult = checkService.deletable(id, "CustomerId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return customerService.Delete(id, userId);

		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Delete Fail");
			logger.error(e.getMessage(), e);
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Customer> Save(HttpServletRequest request, @RequestBody Customer customer) {
		if (customer.getId() != null) {
			customer.setUpdatedAt(new Date());
			customer.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			customer.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			if (customer.getCreatedId() == null) {
				customer.setCreatedId(customer.getUpdatedId());
			}
		} else {
			customer.setStatus(Status.Draft);
			customer.setCreatedAt(new Date());
			customer.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			customer.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		String userId = LoginHelper.GetLoginInfo(request).getUserId();
		ActionResult<User> user = userService.GetById(userId);
		
		logger.info("保存客户信息:ID=" + userId + "tCreatedId=" + customer.getCreatedId());
		if(user.getData() == null || user.getData().getIsSysAdmin() == false){
			if (!customer.getCreatedId().equalsIgnoreCase(userId)) {
				ActionResult<Customer> tempVar = new ActionResult<Customer>();
				tempVar.setSuccess(false);
				tempVar.setMessage("不可以修改他人的记录。");
				return tempVar;
			}
		}
		return customerService.Save(customer);
	}
}
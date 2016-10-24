



package com.smm.ctrm.api.Partner;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.CustomerBalanceService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Basis.CustomerBalance_new;
import com.smm.ctrm.domain.Physical.CustomerBalance2;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.CustomerBalanceDetailParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

@Controller
@RequestMapping("api/Partner/CustomerBalance/")
public class CustomerBalanceApiController {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private CustomerBalanceService customerBalanceService;

	@Resource
	private CommonService commonService;

	

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<CustomerBalance_new> Save(HttpServletRequest request, @RequestBody CustomerBalance_new customerBalance) {
		try {
			if (customerBalance.getId() != null) {
				customerBalance.setUpdatedAt(new Date());
				customerBalance.setUpdatedBy(LoginHelper.GetLoginInfo().getName());
			} else {
				customerBalance.setCreatedAt(new Date());
				customerBalance.setCreatedBy(LoginHelper.GetLoginInfo().getName());
			}
			return customerBalanceService.Save(customerBalance);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
		
	}
	
	@RequestMapping("PagerDetail")
	@ResponseBody
	public ActionResult<List<CustomerBalance>> PagerDetail(HttpServletRequest request, @RequestBody CustomerBalanceDetailParams param) {
		if (param == null) {
			param = new CustomerBalanceDetailParams();
		}
		 //权限
        String userid = LoginHelper.GetLoginInfo(request).getUserId();
        List<String> usersid = commonService.GetUserPermissionByUser(userid);
//        List<String> customerId=customerBalanceService.GetCustomersIdByCreatedId(usersid);
//        
//        if(customerId==null||customerId.size()==0){
//        	return new ActionResult<>(false, "没有操作权限。");
//        }
		Criteria criteria = customerBalanceService.GetCriteria();
		criteria.createAlias("Customer", "customer", JoinType.INNER_JOIN);
//		criteria.add(Restrictions.in("CustomerId", customerId));
		
		
		criteria.add(Restrictions.eq("customer.IsHidden", false));
		criteria.add(Restrictions.eq("customer.Status", Status.Agreed));
		criteria.add(Restrictions.in("customer.CreatedId", usersid));
		
		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			criteria.add(Restrictions.or(Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"),
					Restrictions.like("customer.ShortName", "%" + param.getKeyword() + "%")));
		}
		// 内部台头的标识
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		List<CustomerBalance> customerBalances = customerBalanceService.PagerDetail(criteria, param.getPageSize(),
				param.getPageIndex(), param.getSortBy(), param.getOrderBy(), total);

		customerBalances = commonService.SimplifyDataCustomerBalanceList(customerBalances);

		ActionResult<List<CustomerBalance>> tempVar = new ActionResult<List<CustomerBalance>>();
		tempVar.setSuccess(true);
		tempVar.setTotal(total.getTotal());
		tempVar.setData(customerBalances);
		return tempVar;
	}

	@RequestMapping("PagerDetail2")
	@ResponseBody
	public ActionResult<CustomerBalance2> PagerDetail2(HttpServletRequest request, @RequestBody CustomerBalanceDetailParams param) {
		try {
			 //权限
            String userid = LoginHelper.GetLoginInfo(request).getUserId();
            List<String> usersid = commonService.GetUserPermissionByUser(userid);
            param.setPermissionUserId(usersid);
			CustomerBalance2 data = customerBalanceService.PagerDetail2(param);
			ActionResult<CustomerBalance2> tempVar = new ActionResult<CustomerBalance2>();
			tempVar.setSuccess(true);
			tempVar.setData(data);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<CustomerBalance2> tempVar2 = new ActionResult<CustomerBalance2>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar2;
		}
	}

	@RequestMapping("PagerSummary")
	@ResponseBody
	public ActionResult<List<CustomerBalance>> PagerSummary(HttpServletRequest request, @RequestBody CustomerBalanceDetailParams param) {
		if (param == null) {
			param = new CustomerBalanceDetailParams();
		}
		Criteria criteria = customerBalanceService.GetCriteria();
		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			criteria.add(Restrictions.or(Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"),
					Restrictions.like("customer.ShortName", "%" + param.getKeyword() + "%")));
		}
		// 内部台头的标识
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		List<CustomerBalance> customerBalances = customerBalanceService.PagerDetail(criteria, param.getPageSize(),
				param.getPageIndex(), param.getSortBy(), param.getOrderBy(), total);

		ActionResult<List<CustomerBalance>> tempVar = new ActionResult<List<CustomerBalance>>();
		tempVar.setSuccess(true);
		tempVar.setTotal(total.getTotal());
		tempVar.setData(customerBalances);
		return tempVar;
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<CustomerBalance> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			ActionResult<CustomerBalance> cb=customerBalanceService.GetById(id);
			if(cb.getData()!=null){
				CustomerBalance newCb=customerBalanceService.assemblingBean(cb.getData());
				cb.setData(newCb);
			}
			return cb;
		} catch (RuntimeException ex) {
			ActionResult<CustomerBalance> tempVar = new ActionResult<CustomerBalance>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return customerBalanceService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 一次删除多条记录
	 * 
	 * @param customerBalances
	 * @return
	 */
	@RequestMapping("DeleteCustomerBalances")
	@ResponseBody
	public ActionResult<String> DeleteCustomerBalances(HttpServletRequest request, @RequestBody List<CustomerBalance> customerBalances) {
		try {
			return customerBalanceService.DeleteCustomerBalances(customerBalances);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	/**
	 * 修改初始余额
	 * 
	 * @param customerBalance
	 * @return
	 */
	@RequestMapping("AmendInitBalance")
	@ResponseBody
	public ActionResult<String> AmendInitBalance(HttpServletRequest request, @RequestBody CustomerBalance customerBalance) {
		return customerBalanceService.AmendInitBalance(customerBalance);
	}

	/**
	 * 导入期初余额
	 * 
	 * @param customerBalances
	 * @return
	 */
	@RequestMapping("ImportInitCustomerBalance")
	@ResponseBody
	public ActionResult<String> ImportInitCustomerBalance(HttpServletRequest request, @RequestBody List<CustomerBalance> customerBalances) {
		return customerBalanceService.ImportInitCustomerBalance(customerBalances);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping("ScheduledUpdateCustomerBalance")
	@ResponseBody
	public ActionResult<String> ScheduledUpdateCustomerBalance(HttpServletRequest request) {
		try {
			customerBalanceService.ScheduledUpdateCustomerBalance();

			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage("已经更新全部客户的资金余额。");
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}

	}

	/**
	 * 内部对账: 业务、银行余额之间的内部对账
	 * 
	 * @return
	 */
	@RequestMapping("Reconciliation")
	@ResponseBody
	public ActionResult<String> Reconciliation(HttpServletRequest request) {
		return customerBalanceService.Reconciliation();
	}
	
	
	/**
	 * 客户资金余额
	 */
	@RequestMapping("GetCustomerBalance")
	@ResponseBody
	public ActionResult<List<CustomerBalance_new>> GetCustomerBalance(HttpServletRequest request, @RequestBody CustomerBalanceDetailParams param) {
		try {
			return customerBalanceService.getCustomerBalance(param);
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<List<CustomerBalance_new>> tempVar2 = new ActionResult<List<CustomerBalance_new>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("系统错误");
			return tempVar2;
		}
	}
}
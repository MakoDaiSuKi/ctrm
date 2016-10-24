
package com.smm.ctrm.api.Physical;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.PendingService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.apiClient.PendingParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by zhenghao on 2016/4/21.
 *
 */
@Controller
@RequestMapping("api/Physical/Pending/")
public class PendingApiController {

	@Resource
	private PendingService pendingService;

	@Resource
	private CommonService commonService;

	@Resource
	private HibernateRepository<Invoice> invoiceRepo;

	@Resource
	private HibernateRepository<Customer> customerRepo;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 根据指定的PendingId
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Pending> GetById(HttpServletRequest request, @RequestBody String pendingId) {
		try {

			return pendingService.GetById(pendingId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Pending> tempVar = new ActionResult<Pending>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 撤销申请：客户：回到草拟的状态
	 * 
	 * @param customer
	 * @return
	 */
	@RequestMapping("CancelByCustomer")
	@ResponseBody
	public ActionResult<String> CancelByCustomer(HttpServletRequest request, @RequestBody Customer customer) {
		try {
			StringBuilder msg = new StringBuilder();

			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			boolean b = pendingService.CancelByCustomer(customer, userId, msg);

			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(b);
			tempVar.setMessage(msg.toString());
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("CancelByCustomerId")
	@ResponseBody
	public ActionResult<String> CancelByCustomerId(HttpServletRequest request, @RequestBody String customerId) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pendingService.CancelByCustomerId(customerId, userId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 撤销申请：合同：回到草拟的状态
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("CancelByContract")
	@ResponseBody
	public ActionResult<String> CancelByContract(HttpServletRequest request, @RequestBody Contract contract) {
		try {
			StringBuilder msg = new StringBuilder();
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			boolean b = pendingService.CancelByContract(contract, userId, msg);

			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(b);
			tempVar.setMessage(msg.toString());
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("CancelByContractId")
	@ResponseBody
	public ActionResult<String> CancelByContractId(HttpServletRequest request, @RequestBody String contractId) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pendingService.CancelByContractId(contractId, userId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 撤销申请：发票：回到草拟的状态
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("CancelByInvoice")
	@ResponseBody
	public ActionResult<String> CancelByInvoice(HttpServletRequest request, @RequestBody Invoice invoice) {
		try {
			StringBuilder msg = new StringBuilder();
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pendingService.CancelByInvoice(invoice, userId, msg);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 撤销申请：发票：回到草拟的状态
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("CancelByShip")
	@ResponseBody
	public ActionResult<String> CancelByShip(HttpServletRequest request, @RequestBody ReceiptShip invoice) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pendingService.CancelByShip(invoice, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 撤销申请：付款：回到草拟的状态
	 * 
	 * @param fund
	 * @return
	 */
	@RequestMapping("CancelByFund")
	@ResponseBody
	public ActionResult<String> CancelByFund(HttpServletRequest request, @RequestBody Fund fund) {
		try {
			StringBuilder msg = new StringBuilder();
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			boolean b = pendingService.CancelByFund(fund, userId, msg);

			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(b);
			tempVar.setMessage(msg.toString());
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("CancelByFundId")
	@ResponseBody
	public ActionResult<String> CancelByFundId(HttpServletRequest request, @RequestBody String fundId) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pendingService.CancelByFundId(fundId, userId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据指定的CustomerId，返回该客户的已经提交的申请记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("PendingsByCustomerId")
	@ResponseBody
	public ActionResult<List<Pending>> PendingsByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		try {
			ActionResult<List<Pending>> actionResult =  pendingService.PendingsByCustomerId(customerId);
			return actionResult;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据指定的ContractId，返回该合同的已经提交的申请记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("PendingsByContractId")
	@ResponseBody
	public ActionResult<List<Pending>> PendingsByContractId(HttpServletRequest request,
			@RequestBody String contractId) {
		try {
			return pendingService.PendingsByContractId(contractId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据指定的fundId，返回该付款的已经提交的申请记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("PendingsByFundId")
	@ResponseBody
	public ActionResult<List<Pending>> PendingsByFundId(HttpServletRequest request, @RequestBody String fundId) {
		try {
			return pendingService.PendingsByFundId(fundId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 不限业务，都用这个接口提交审核。Status从草稿变为待审的状态。
	 * 
	 * @param pending
	 * @return
	 */
	@RequestMapping("Ask4Approve")
	@ResponseBody
	public ActionResult<Pending> Ask4Approve(HttpServletRequest request, @RequestBody Pending pending) {
		try {
			if (pending.getId() != null) {
				pending.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				pending.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}

			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			if (!userId.equalsIgnoreCase(pending.getCreatedId())) {
				ActionResult<Pending> tempVar = new ActionResult<Pending>();
				tempVar.setSuccess(true);
				tempVar.setMessage("不可以修改他人的记录。");
				return tempVar;
			}

			return pendingService.Ask4Approve(pending, userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	/**
	 * 待审客户
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("CustomerPendingPager")
	@ResponseBody
	public ActionResult<List<Pending>> CustomerPendingPager(HttpServletRequest request,
			@RequestBody PendingParams param) {
		try {
			if (param == null) {
				param = new PendingParams();
			}

			Criteria criteria = pendingService.GetCriteria();

			criteria.add(Restrictions.isNotNull("CustomerId"));
			criteria.add(Restrictions.isNull("ContractId"));
			criteria.add(Restrictions.isNull("FundId"));

			criteria.createAlias("Customer", "customer", JoinType.INNER_JOIN);
			criteria.add(Restrictions.not(Restrictions.eq("customer.Status", 1)));

			if (param.getIsDone() != null) {
				criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			}
			if (param.getApproverId() != null) {
				criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));
			}

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil refTotal = new RefUtil();

			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);

			pendings = commonService.SimplifyDataPendingList(pendings);
			ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(refTotal.getTotal());
			tempVar.setData(pendings);
			return tempVar;
		} catch (Exception ex) {

			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待审订单
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("ContractPendingPager")
	@ResponseBody
	public ActionResult<List<Pending>> ContractPendingPager(HttpServletRequest request,
			@RequestBody PendingParams param) {
		try {
			if (param == null) {
				param = new PendingParams();
			}
			Criteria criteria = pendingService.GetCriteria();

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNotNull("ContractId"));
			criteria.add(Restrictions.isNull("FundId"));

			criteria.createAlias("Contract", "contract", JoinType.INNER_JOIN);
			criteria.add(Restrictions.not(Restrictions.eq("contract.Status", 1)));

			if (param.getIsDone() != null) {
				criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			}
			if (param.getApproverId() != null) {
				criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));
			}

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);
			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);
			ActionResult<List<Pending>> tempVar = new ActionResult<java.util.List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(refTotal.getTotal());
			tempVar.setData(pendings);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待审发货单
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("ReceiptShipPendingPager")
	@ResponseBody
	public ActionResult<List<Pending>> ReceiptShipPendingPager(HttpServletRequest request,
			@RequestBody PendingParams param) {
		try {
			if (param == null) {
				param = new PendingParams();
			}
			Criteria criteria = pendingService.GetCriteria();

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNotNull("ReceiptShipId"));
			criteria.add(Restrictions.isNull("FundId"));

			criteria.createAlias("ReceiptShip", "receiptShip", JoinType.INNER_JOIN);
			criteria.add(Restrictions.ne("receiptShip.Status", 1));
			criteria.add(Restrictions.eq("receiptShip.Flag", param.getFlag()));

			if (param.getIsDone() != null) {
				criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			}
			if (param.getApproverId() != null) {
				criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));
			}

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);
			pendings = commonService.SimplifyDataPendingList(pendings);
			return new ActionResult<>(true, "", pendings, refTotal);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待审发票
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("InvoicePendingPager")
	@ResponseBody
	public ActionResult<List<Pending>> InvoicePendingPager(HttpServletRequest request,
			@RequestBody PendingParams param) {
		try {
			if (param == null) {
				param = new PendingParams();
			}
			Criteria criteria = pendingService.GetCriteria();

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNotNull("InvoiceId"));
			criteria.add(Restrictions.isNull("FundId"));

			criteria.createAlias("Invoice", "invoice", JoinType.INNER_JOIN);
			criteria.add(Restrictions.ne("invoice.Status", 1));

			if (param.getIsDone() != null) {
				criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			}
			if (param.getApproverId() != null) {
				criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));
			}

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);
			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);
			ActionResult<List<Pending>> tempVar = new ActionResult<java.util.List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(refTotal.getTotal());
			tempVar.setData(pendings);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待审付款
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PaymentPendingPager")
	@ResponseBody
	public ActionResult<List<Pending>> PaymentPendingPager(HttpServletRequest request,
			@RequestBody PendingParams param) {
		try {
			if (param == null) {
				param = new PendingParams();
			}
			Criteria criteria = pendingService.GetCriteria();

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNotNull("FundId"));
			criteria.add(Restrictions.isNull("ContractId"));

			criteria.createAlias("Fund", "fund", JoinType.INNER_JOIN);

			criteria.add(Restrictions.not(Restrictions.eq("fund.Status", 1)));

			if (param.getIsDone() != null) {
				criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			}
			if (param.getApproverId() != null) {
				criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));
			}

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);
			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);

			ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(refTotal.getTotal());
			tempVar.setData(pendings);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待审客户列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("CustomerPending4App")
	@ResponseBody
	public ActionResult<List<Pending>> CustomerPending4App(HttpServletRequest request,
			@RequestBody Map<String, String> params) {
		try {
			if (request == null) {
				ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
				tempVar.setSuccess(true);
				tempVar.setTotal(0);
				tempVar.setMessage("rerquest没有值");
				return tempVar;
			}

			String pageIndex = params.get("PageIndex");
			String pageSize = params.get("PageSize");

			if (params.get("PageIndex") == null || params.get("PageSize") == null || StringUtils.isBlank(pageIndex)
					|| StringUtils.isBlank(pageSize)) {
				ActionResult<List<Pending>> tempVar2 = new ActionResult<List<Pending>>();
				tempVar2.setSuccess(true);
				tempVar2.setTotal(0);
				tempVar2.setMessage("pageIndex  pageSize没有值");
				return tempVar2;
			}

			String approverId = LoginHelper.GetLoginInfo(request).getUserId();

			PendingParams param = new PendingParams();
			param.setPageIndex(Integer.parseInt(pageIndex));
			param.setPageSize(Integer.parseInt(pageSize));
			param.setIsDone(false);
			param.setApproverId(approverId);

			Criteria criteria = pendingService.GetCriteria();
			criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));

			criteria.add(Restrictions.isNotNull("CustomerId"));
			criteria.add(Restrictions.isNull("ContractId"));
			criteria.add(Restrictions.isNull("FundId"));

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);
			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);

			ActionResult<List<Pending>> tempVar3 = new ActionResult<List<Pending>>();
			tempVar3.setSuccess(true);
			tempVar3.setTotal(refTotal.getTotal());
			tempVar3.setData(pendings);
			return tempVar3;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar4 = new ActionResult<List<Pending>>();
			tempVar4.setSuccess(true);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 待审合同列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("ContractPending4App")
	@ResponseBody
	public ActionResult<List<Pending>> ContractPending4App(HttpServletRequest request,
			@RequestBody Map<String, String> params) {
		try {
			if (request == null) {
				ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
				tempVar.setSuccess(true);
				tempVar.setTotal(0);
				tempVar.setMessage("rerquest没有值");
				return tempVar;
			}

			String pageIndex = params.get("PageIndex");
			String pageSize = params.get("PageSize");

			if (params.get("PageIndex") == null || params.get("PageSize") == null || StringUtils.isBlank(pageIndex)
					|| StringUtils.isBlank(pageSize)) {
				ActionResult<List<Pending>> tempVar2 = new ActionResult<List<Pending>>();
				tempVar2.setSuccess(true);
				tempVar2.setTotal(0);
				tempVar2.setMessage("pageIndex  pageSize没有值");
				return tempVar2;
			}

			String approverId = LoginHelper.GetLoginInfo(request).getUserId();

			PendingParams param = new PendingParams();
			param.setPageIndex(Integer.parseInt(pageIndex));
			param.setPageSize(Integer.parseInt(pageSize));
			param.setIsDone(false);
			param.setApproverId(approverId);

			Criteria criteria = pendingService.GetCriteria();
			criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNotNull("ContractId"));
			criteria.add(Restrictions.isNull("FundId"));

			RefUtil refTotal = new RefUtil();
			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);

			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);

			ActionResult<List<Pending>> tempVar3 = new ActionResult<List<Pending>>();
			tempVar3.setSuccess(true);
			tempVar3.setTotal(refTotal.getTotal());
			tempVar3.setData(pendings);
			return tempVar3;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar4 = new ActionResult<List<Pending>>();
			tempVar4.setSuccess(true);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 待审付款列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("FundPending4App")
	@ResponseBody
	public ActionResult<List<Pending>> FundPending4App(HttpServletRequest request,
			@RequestBody Map<String, String> params) {
		try {
			if (request == null) {
				ActionResult<List<Pending>> tempVar = new ActionResult<List<Pending>>();
				tempVar.setSuccess(true);
				tempVar.setTotal(0);
				tempVar.setMessage("rerquest没有值");
				return tempVar;
			}

			String pageIndex = params.get("PageIndex");
			String pageSize = params.get("PageSize");

			if (params.get("PageIndex") == null || params.get("PageSize") == null || StringUtils.isBlank(pageIndex)
					|| StringUtils.isBlank(pageSize)) {
				ActionResult<List<Pending>> tempVar2 = new ActionResult<List<Pending>>();
				tempVar2.setSuccess(true);
				tempVar2.setTotal(0);
				tempVar2.setMessage("pageIndex  pageSize没有值");
				return tempVar2;
			}

			String approverId = LoginHelper.GetLoginInfo(request).getUserId();

			PendingParams param = new PendingParams();
			param.setPageIndex(Integer.parseInt(pageIndex));
			param.setPageSize(Integer.parseInt(pageSize));
			param.setIsDone(false);
			param.setApproverId(approverId);

			Criteria criteria = pendingService.GetCriteria();
			criteria.add((Restrictions.eq("IsDone", param.getIsDone())));
			criteria.add((Restrictions.eq("ApproverId", param.getApproverId())));

			criteria.add(Restrictions.isNull("CustomerId"));
			criteria.add(Restrictions.isNull("ContractId"));
			criteria.add(Restrictions.isNotNull("FundId"));

			RefUtil refTotal = new RefUtil();

			List<Pending> pendings = pendingService.PendingPager(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), refTotal);

			pendings = commonService.SimplifyDataPendingList((List<Pending>) pendings);

			ActionResult<List<Pending>> tempVar3 = new ActionResult<List<Pending>>();
			tempVar3.setSuccess(true);
			tempVar3.setTotal(refTotal.getTotal());
			tempVar3.setData(pendings);

			return tempVar3;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<Pending>> tempVar4 = new ActionResult<List<Pending>>();
			tempVar4.setSuccess(true);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 根据pendingId获得明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("CustomerByPendingId")
	@ResponseBody
	public ActionResult<Customer> CustomerByPendingId(HttpServletRequest request, @RequestBody String pendingId) {
		try {

			return pendingService.CustomerByPendingId(pendingId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Customer> tempVar = new ActionResult<Customer>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据pendingId获得明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("ContractByPendingId")
	@ResponseBody
	public ActionResult<Contract> ContractByPendingId(HttpServletRequest request, @RequestBody String pendingId) {
		try {

			return pendingService.ContractByPendingId(pendingId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据pendingId获得明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("FundByPendingId")
	@ResponseBody
	public ActionResult<Fund> FundByPendingId(HttpServletRequest request, @RequestBody String pendingId) {
		try {
			return pendingService.FundByPendingId(pendingId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Fund> tempVar = new ActionResult<Fund>();
			tempVar.setSuccess(true);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

}
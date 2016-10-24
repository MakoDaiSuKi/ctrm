
package com.smm.ctrm.api.Physical;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.ApproveService;
import com.smm.ctrm.bo.Physical.PendingService;
import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.dto.res.ActionResult;

/**
 * Created by zhenghao on 2016/4/21.
 */
@Controller
@RequestMapping("api/Physical/Approve/")
public class ApproveApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ApproveService approveService;

	@Resource
	private PendingService pendingService;

	@Resource
	private CommonService commonService;

	/**
	 * 根据CustomerId获取，该客户的已经审核的记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetApproveHistoryByCustomerId")
	@ResponseBody
	public ActionResult<List<Approve>> GetApproveHistoryByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		
		try {
			return approveService.ApproveHistoryByCustomerId(customerId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	

	/**
	 * 根据ReceiptShipId获取，该发货单的已经审核的记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetApproveHistoryByReceiptShipId")
	@ResponseBody
	public ActionResult<List<Approve>> GetApproveHistoryByReceiptShipId(HttpServletRequest request,
			@RequestBody String receiptShipId) {
		try {
			return approveService.ApproveHistoryByReceiptShipId(receiptShipId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据InvoiceId获取，该发票的已经审核的记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("PendingsByInvoiceId")
	@ResponseBody
	public ActionResult<List<Pending>> PendingsByInvoiceId(HttpServletRequest request, @RequestBody String invoiceId) {
		try {

			return pendingService.PendingsByInvoiceId(invoiceId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 根据ReceiptShipId获取，该发货单的已经审核的记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("PendingsByShipId")
	@ResponseBody
	public ActionResult<List<Pending>> PendingsByShipId(HttpServletRequest request, @RequestBody String shipId) {
		try {
			return pendingService.PendingsByShipId(shipId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 根据ContractId获取，该合同的已经审核的记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetApproveHistoryByContractId")
	@ResponseBody
	public ActionResult<List<Approve>> GetApproveHistoryByContractId(HttpServletRequest request,
			@RequestBody String contractId) {
		try {
			return approveService.ApproveHistoryByContractId(contractId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据FundId获取，该付款的已经审核的记录
	 */
	@RequestMapping("GetApproveHistoryByFundId")
	@ResponseBody
	public ActionResult<List<Approve>> GetApproveHistoryByFundId(HttpServletRequest request,
			@RequestBody String fundId) {
		try {
			return approveService.ApproveHistoryByFundId(fundId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	/**
	 * 根据FundId获取，该付款的已经审核的记录
	 */
	@RequestMapping("GetApproveHistoryByInvoiceId")
	@ResponseBody
	public ActionResult<List<Approve>> GetApproveHistoryByInvoiceId(HttpServletRequest request,
			@RequestBody String invoiceId) {
		try {
			return approveService.ApproveHistoryByInvoiceId(invoiceId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 客户审批
	 * 
	 * @param approve
	 * @return
	 */
	@RequestMapping("ApproveCustomer4Winform")
	@ResponseBody
	public ActionResult<String> ApproveCustomer4Winform(HttpServletRequest request, @RequestBody Approve approve) {
		try {
			return approveService.ApproveCustomer4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 订单审批
	 * 
	 * @param approve
	 * @return
	 */
	@RequestMapping("ApproveContract4Winform")
	@ResponseBody
	public ActionResult<String> ApproveContract4Winform(HttpServletRequest request, @RequestBody Approve approve) {
		try {
			return approveService.ApproveContract4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 发货单审批
	 * 
	 * @param approve
	 * @return
	 */
	@RequestMapping("ApproveMakeShip4Winform")
	@ResponseBody
	public ActionResult<String> ApproveMakeShip4Winform(HttpServletRequest request, @RequestBody Approve approve) {
		try {
			return approveService.ApproveMakeShip4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 发票审批
	 * 
	 * @param approve
	 * @return
	 */
	@RequestMapping("ApproveInvoice4Winform")
	@ResponseBody
	public ActionResult<String> ApproveInvoice4Winform(HttpServletRequest request, @RequestBody Approve approve) {
		try {
			return approveService.ApproveInvoice4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 付款审批
	 * 
	 * @param approve
	 * @return
	 */
	@RequestMapping("ApprovePayment4Winform")
	@ResponseBody
	public ActionResult<String> ApprovePayment4Winform(HttpServletRequest request, @RequestBody Approve approve) {
		try {
			return approveService.ApprovePayment4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 客户审批
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("ApproveCustomer4App")
	@ResponseBody
	public ActionResult<String> ApproveCustomer4App(HttpServletRequest request,
			@RequestBody Map<String, String> param) {
		try {
			String pendingId = param.get("PendingId");
			String isApproved = param.get("IsApproved");
			String comments = param.get("Comments");

			if (pendingId == null || isApproved == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数不可以为空");
				return tempVar;
			}

			Approve approve = new Approve();
			approve.setPendingId(pendingId);
			approve.setTradeDate(new Date());
			approve.setComments(comments);
			approve.setIsApproved(Boolean.valueOf(isApproved));

			// 此处调用winform的审批逻辑
			return approveService.ApproveCustomer4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 订单审批
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("ApproveContract4App")
	@ResponseBody
	public ActionResult<String> ApproveContract4App(HttpServletRequest request,
			@RequestBody Map<String, String> param) {
		try {
			String pendingId = param.get("PendingId");
			String isApproved = param.get("IsApproved");
			String comments = param.get("Comments");

			if (pendingId == null || isApproved == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数不可以为空");
				return tempVar;
			}

			Approve approve = new Approve();
			approve.setPendingId(pendingId);
			approve.setTradeDate(new Date());
			approve.setComments(comments);
			approve.setIsApproved(Boolean.valueOf(isApproved));

			// 此处调用winform的审批逻辑
			return approveService.ApproveContract4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 付款审批
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("ApproveFund4App")
	@ResponseBody
	public ActionResult<String> ApproveFund4App(HttpServletRequest request, @RequestBody Map<String, String> param) {
		try {
			String pendingId = param.get("PendingId");
			String isApproved = param.get("IsApproved");
			String comments = param.get("Comments");

			if (pendingId == null || isApproved == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数不可以为空");
				return tempVar;
			}

			Approve approve = new Approve();
			approve.setPendingId(pendingId);
			approve.setTradeDate(new Date());
			approve.setComments(comments);
			approve.setIsApproved(Boolean.valueOf(isApproved));

			// 此处调用winform的审批逻辑
			return approveService.ApprovePayment4Winform(approve);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}
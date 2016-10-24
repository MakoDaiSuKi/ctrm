
package com.smm.ctrm.api.Finance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.BaseApiController;
import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.apiClient.FundParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Finance/Fund/")
public class FundApiController extends BaseApiController {

	@Resource
	private FundService fundService;

	@Resource
	private CommonService commonService;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Fund>> Pager(@RequestBody FundParams param) {
		TestTime.start();
		if (param == null) {
			param = new FundParams();
		}
		Criteria criteria = fundService.GetCriteria();
		// 关键字
		if (StringUtils.isNotBlank(param.getKeyword())) {
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.or(Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%")));
		}
		// 查询权限
		String userId = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userId, criteria, "CreatedId");
		// 业务状态
		if (StringUtils.isNotBlank(param.getStatuses())) {
			String[] strings = param.getStatuses().split(",");
			List<Integer> arrs = Arrays.asList(strings).stream().map(s -> Integer.valueOf(s.trim()))
					.collect(Collectors.toList());
			criteria.add(Restrictions.in("Status", arrs));
		}
		// 品种标识 - 多选
		if (StringUtils.isNotBlank(param.getCommodityIds())) {
			String[] split = param.getCommodityIds().split(",");
			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s.trim())).collect(Collectors.toList());
			
			criteria.add(Restrictions.in("CommodityId", arrs));
		}
		// 品种标识 - 单选
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}
		// 内部台头的标识 - 多选
		if (StringUtils.isNotBlank(param.getLegalIds())) {
			String[] split = param.getLegalIds().split(",");			
			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s.trim())).collect(Collectors.toList());
			
			criteria.add(Restrictions.in("LegalId", arrs));
		}
		// 内部台头的标识 - 单选
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}
		// D(ebit) = 付款, C(redit) = 收款
		if (StringUtils.isNotBlank(param.getDC())) {
			criteria.add(Restrictions.eq("DC", param.getDC()));
		}
		// 开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		TestTime.addMilestone("参数构造完成");
		List<Fund> funds = fundService.Funds(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());
		TestTime.addMilestone("查询完成");
		funds = commonService.SimplifyDataFundList(funds);
		TestTime.addMilestone("简化完成");
		logger.info(TestTime.result());
		return new ActionResult<>(true, "", funds, total);
	}

	public String FormatSortBy(String sortBy) {

		if (StringUtils.isEmpty(sortBy))
			return null;
		return sortBy;
	}

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Fund> GetById(@RequestBody String id) {
		try {
			return fundService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("FundsByContractId")
	@ResponseBody
	public ActionResult<List<Fund>> FundsByContractId(@RequestBody String contractId) {
		try {
			return fundService.FundsByContractId(contractId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("FundsByLotId")
	@ResponseBody
	public ActionResult<List<Fund>> FundsByLotId(@RequestBody String lotId) {
		try {
			return fundService.FundsByLotId(lotId);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("FundsByInvoiceId")
	@ResponseBody
	public ActionResult<List<Fund>> FundsByInvoiceId(@RequestBody String InvoiceIds) {
		try {
			return fundService.FundsByInvoiceId(InvoiceIds);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据is删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(@RequestBody String id) {
		try {
			String userId = LoginHelper.GetLoginInfo().getUserId();

			return fundService.Delete(id, userId);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, MessageCtrm.DeleteFaile);
		}
	}

	@RequestMapping("PaymentConfirmed")
	@ResponseBody
	public ActionResult<Fund> PaymentConfirmed(@RequestBody Fund fund) {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			if (fund.getId() != null) {
				fund.setUpdatedId(loginInfo.getUserId());
			} else {
				fund.setCreatedId(loginInfo.getUserId());
			}
			ActionResult<Fund> result =  fundService.PaymentConfirmed(fund);
			return result;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Receive")
	@ResponseBody
	public ActionResult<String> Receive(@RequestBody Fund fund) {
		String userId = LoginHelper.GetLoginInfo().getUserId();
		if (fund.getId() != null) {
			fund.setUpdatedId(userId);
		} else {
			fund.setCreatedId(userId);
		}
		if (!fund.getCreatedId().equalsIgnoreCase(userId)) {
			return new ActionResult<>(false, "不可以修改他人的记录。");
		}

		return fundService.SaveReceive(fund);
	}

	@RequestMapping("Payment")
	@ResponseBody
	public ActionResult<String> Payment(@RequestBody Fund fund) {
		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		if (fund.getId() != null) {
			fund.setUpdatedId(loginInfo.getUserId());
		} else {
			fund.setCreatedId(loginInfo.getUserId());
		}

		String userId = loginInfo.UserId;
		if (!fund.getCreatedId().equalsIgnoreCase(userId)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("不可以修改他人的记录。");
			return tempVar;
		}

		return fundService.Payment(fund);
	}

	/**
	 * 申请付款
	 * 
	 * @param fund
	 * @return
	 */
	@RequestMapping("CreatePaymentDraft")
	@ResponseBody
	public ActionResult<Fund> CreatePaymentDraft(@RequestBody Fund fund) {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			if (fund.getId() != null) {
				fund.setUpdatedId(loginInfo.getUserId());
			} else {
				fund.setCreatedId(loginInfo.getUserId());
			}

			fund.setCreatedId(loginInfo.getUserId());
			return fundService.CreatePaymentDraft(fund);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 从批次直接生成付款申请
	 * 
	 * @param fund
	 * @return
	 */
	@RequestMapping("CreatePaymentDraft4Lot")
	@ResponseBody
	public ActionResult<Fund> CreatePaymentDraft4Lot(@RequestBody Fund fund) {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			if (fund.getId() != null) {
				fund.setUpdatedId(loginInfo.getUserId());
			} else {
				fund.setCreatedId(loginInfo.getUserId());
			}

			fund.setCreatedId(loginInfo.getUserId());
			return fundService.CreatePaymentDraft4Lot(fund);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}
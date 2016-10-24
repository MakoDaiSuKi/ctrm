
package com.smm.ctrm.api.Futures;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.PricingService;
import com.smm.ctrm.domain.Physical.CpSplitPricing;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.apiClient.PricingParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/Pricing/")
public class PricingApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private PricingService pricingService;

	@Resource
	private CommonService commonService;

	/**
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Pricing>> Pager(HttpServletRequest request, @RequestBody PricingParams param) {
		TestTime.start();
		if (param == null) {
			param = new PricingParams();
		}
		Criteria criteria = pricingService.GetCriteria();

		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("MajorMarket", "majorMarket", JoinType.LEFT_OUTER_JOIN);

			Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
					Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
			Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));
			Criterion c = Restrictions.like("majorMarket.Name", "%" + param.getKeyword() + "%");

			criteria.add(Restrictions.or(Restrictions.or(a, b), c));
		}
		String userId = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userId, criteria, "CreatedId");
		// MajorType
		if (!StringUtils.isBlank(param.getMajorType())) {
			criteria.add(Restrictions.eq("MajorType", param.getMajorType()));
		}

		// ContractId
		if (param.getContractId() != null) {
			criteria.add(Restrictions.eq("ContractId", param.getContractId()));
		}

		// LotId
		if (param.getLotId() != null) {
			criteria.add(Restrictions.eq("LotId", param.getLotId()));
		}

		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}

		// ON = 正常, EXT = 改期
		if (!StringUtils.isBlank(param.getPriceTiming())) {
			criteria.add(Restrictions.eq("PriceTiming", param.getPriceTiming()));
		}

		// 开始日期， 和TradeDate比较
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 结束日期， 和TradeDate比较
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		if (param.getMajorEndDate() != null) {
			criteria.add(Restrictions.ge("MajorEndDate", param.getMajorEndDate()));
		}

		// true = 包括固定价明细，false = 不包含固定价
		if (param.getIsPriced() != null && param.getIsPriced() == true) {
			criteria.add(Restrictions.eq("IsPriced", false));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		TestTime.addMilestone("参数构造完成");
		RefUtil total = new RefUtil();
		List<Pricing> pricings = pricingService.Pricings(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());
		TestTime.addMilestone("查询完成");
		pricings = commonService.SimplifyDataPricingList(pricings);
		TestTime.addMilestone("格式化完成");
		logger.info(TestTime.result());
		return new ActionResult<>(true, "", pricings, total);
	}

	@RequestMapping("Pricings")
	@ResponseBody
	public ActionResult<List<Pricing>> Pricings(HttpServletRequest request) {
		List<Pricing> pricings = pricingService.Pricings();
		ActionResult<List<Pricing>> tempVar = new ActionResult<List<Pricing>>();
		tempVar.setData(pricings);
		tempVar.setSuccess(true);
		return tempVar;
	}

	@RequestMapping("PricingByContractId")
	@ResponseBody
	public ActionResult<List<Pricing>> PricingByContractId(HttpServletRequest request, @RequestBody String contractId) {
		try {
			return pricingService.PricingByContractId(contractId);
		} catch (Exception ex) {
			ActionResult<List<Pricing>> tempVar = new ActionResult<List<Pricing>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("PricingByLotId")
	@ResponseBody
	public ActionResult<List<Pricing>> PricingByLotId(HttpServletRequest request, @RequestBody String lotId) {
		try {

			return pricingService.PricingByLotId(lotId);
		} catch (Exception ex) {
			ActionResult<List<Pricing>> tempVar = new ActionResult<List<Pricing>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * @param contractId
	 * @return
	 */
	@RequestMapping("PricingRecordsByContractId")
	@ResponseBody
	public ActionResult<List<PricingRecord>> PricingRecordsByContractId(HttpServletRequest request,
			@RequestBody String contractId) {
		try {
			return pricingService.PricingRecordsByContractId(contractId);
		} catch (Exception ex) {
			ActionResult<List<PricingRecord>> tempVar = new ActionResult<List<PricingRecord>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("PricingRecordsByLotId")
	@ResponseBody
	public ActionResult<List<PricingRecord>> PricingRecordsByLotId(HttpServletRequest request,
			@RequestBody String lotId) {
		try {

			return pricingService.PricingRecordsByLotId(lotId);
		} catch (Exception ex) {
			ActionResult<List<PricingRecord>> tempVar = new ActionResult<List<PricingRecord>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Pricing> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return pricingService.GetById(id);
		} catch (Exception ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 正常点价
	 * 
	 * @param pricing
	 * @return
	 */
	@RequestMapping("SavePricingScheduled")
	@ResponseBody
	public ActionResult<String> SavePricingScheduled(HttpServletRequest request, @RequestBody Pricing pricing) {
		if (pricing.getId() != null) {
			pricing.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			pricing.setUpdatedAt(new Date());
			pricing.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			pricing.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			pricing.setCreatedAt(new Date());
			pricing.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		return pricingService.SavePricingScheduled(pricing);
	}

	/**
	 * 改期点价
	 * 
	 * @param pricing
	 * @return
	 */
	@RequestMapping("SavePricingExtended")
	@ResponseBody
	public ActionResult<String> SavePricingExtended(HttpServletRequest request, @RequestBody Pricing pricing) {
		if (pricing.getId() != null) {
			pricing.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			pricing.setUpdatedAt(new Date());
		} else {
			pricing.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			pricing.setCreatedAt(new Date());
		}
		return pricingService.SavePricingExtended(pricing);
	}

	/**
	 * 删除点价
	 * 
	 * @param pricingId
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String pricingId) {
		try {

			return pricingService.Delete(pricingId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 业务说明在SERVICE层
	 * 
	 * @param pricing
	 * @return
	 */
	@RequestMapping("SplitPricing")
	@ResponseBody
	public ActionResult<String> SplitPricing(HttpServletRequest request, @RequestBody CpSplitPricing pricing) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return pricingService.SplitPricing(pricing, userId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 更新均价点价的价格
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("UpdatePriceById")
	@ResponseBody
	public ActionResult<String> UpdatePriceById(HttpServletRequest request, @RequestBody String pricingId) {
		try {
			pricingService.UpdateAveragePriceById(pricingId);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage("成功");
			return tempVar;
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 更新均价点价的价格
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "UpdateLotPriceByLotId")
	@ResponseBody
	public ActionResult<String> UpdateLotPriceByLotId(HttpServletRequest request, @RequestBody String lotId) {
		try {

			commonService.UpdateLotPriceByLotId(lotId);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage("成功");
			return tempVar;
		} catch (Exception ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}
}
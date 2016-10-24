package com.smm.ctrm.api.Physical;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Basis.CustomerBalanceService;
import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.domain.CpPosition4AllocateToMultiLot;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Physical.CpPosition4AllocateToLot;
import com.smm.ctrm.domain.Physical.CpPosition4RemoveFromLot;
import com.smm.ctrm.domain.Physical.CpSplitLot;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Lot4MTM3;
import com.smm.ctrm.domain.Physical.Lot4Unpriced;
import com.smm.ctrm.domain.Physical.LotPnL;
import com.smm.ctrm.domain.Physical.LotSplit;
import com.smm.ctrm.domain.Physical.Param4LotPnL;
import com.smm.ctrm.domain.Physical.QPRecord;
import com.smm.ctrm.domain.Physical.VmContractLot4Combox;
import com.smm.ctrm.domain.Report.Lot4FeesOverview;
import com.smm.ctrm.domain.Report.Lot4MTM;
import com.smm.ctrm.domain.apiClient.ContractParams;
import com.smm.ctrm.domain.apiClient.LotParams;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PremiumType;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by zhenghao on 2016/4/21.
 *
 *
 */
@Controller
@RequestMapping("api/Physical/Lot/")
public class LotApiController {

	private static Logger logger = Logger.getLogger(LotApiController.class);

	@Resource
	private LotService lotService;

	@Resource
	private CommonService commonService;

	@Resource
	private ProductService productService;

	@Resource
	private CustomerBalanceService customerBalanceService;

	@Resource
	private CheckService checkService;

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Lot>> Pager(HttpServletRequest request, @RequestBody LotParams param) {
		if (param == null) {
			param = new LotParams();
		}

		Date today = DateUtil.doSFormatDate(DateUtil.doFormatDate(new Date(), "yyyyMMdd"), "yyyyMMdd");

		Criteria criteria = lotService.GetCriteria();

		// 加入权限过滤参数
		String userId = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userId, criteria, "CreatedId");

		criteria.createAlias("Contract", "contract", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Legal", "legal", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {

			Criterion a = Restrictions.like("FullNo", "%" + param.getKeyword() + "%");
			Criterion b = Restrictions.like("contract.DocumentNo", "%" + param.getKeyword() + "%");
			Criterion c = Restrictions.like("legal.Name", "%" + param.getKeyword() + "%");
			Criterion d = Restrictions.like("customer.Name", "%" + param.getKeyword() + "%");
			Criterion e = Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%");
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(a);
			disjunction.add(b);
			disjunction.add(c);
			disjunction.add(d);
			disjunction.add(e);
			criteria.add(disjunction);
		}
		
		// 是否套利
		if(param.getIsArbitrage() != null) {
			criteria.add(Restrictions.eq("IsArbitrage", param.getIsArbitrage()));
		}

		// 创建者
		if (StringUtils.isNotBlank(param.getCreatedBy())) {
			criteria.add(Restrictions.like("CreatedBy", "%" + param.getCreatedBy() + "%"));
		}
		if (param.getIsHedged() != null) {
			criteria.add(Restrictions.eq("IsHedged", param.getIsHedged()));
		}
		// 币种
		if (StringUtils.isNotBlank(param.getCurrency())) {
			criteria.add(Restrictions.eq("contract.Currency", param.getCurrency()));
		}

		// 业务类型
		if (StringUtils.isNotBlank(param.getSpotType())) {
			criteria.add(Restrictions.eq("contract.SpotType", param.getSpotType()));
		}

		if (StringUtils.isNotBlank(param.getStatuses())) {
			String[] split = param.getStatuses().split(",");

			Stream<String> stream = Arrays.stream(split);
			List<Integer> arrs = stream.map(s -> Integer.valueOf(String.format("%s", s).trim()))
					.collect(Collectors.toList());
			if (arrs.size() > 0) {
				criteria.add(Restrictions.in("contract.Status", arrs));
			}

		}

		if (StringUtils.isNotBlank(param.getLegalIds())) {

			String[] split = param.getLegalIds().split(",");

			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s.trim())).collect(Collectors.toList());
			if (arrs.size() > 0) {
				criteria.add(Restrictions.in("LegalId", arrs));
			}
		}

		// 客户标识
		if (param.getMarkColor() != 0) {
			criteria.add(Restrictions.eq("MarkColor", param.getMarkColor()));
		}

		// 客户标识
		if (StringUtils.isNotBlank(param.getCustomerId())) {
			criteria.add(Restrictions.eq("contract.CustomerId", param.getCustomerId()));
		}

		// 品种标识
		if (StringUtils.isNotBlank(param.getCommodityId())) {
			criteria.add(Restrictions.eq("contract.CommodityId", param.getCommodityId()));
		}

		// 内部台头的标识
		if (StringUtils.isNotBlank(param.getLegalId())) {
			criteria.add(Restrictions.eq("contract.LegalId", param.getLegalId()));
		}

		// 业务经理
		if (StringUtils.isNotBlank(param.getTraderId())) {
			criteria.add(Restrictions.eq("contract.TraderId", param.getTraderId()));
		}

		// 全部，采购，销售 {P, S}
		if (StringUtils.isNotBlank(param.getSpotDirection())) {
			criteria.add(Restrictions.eq("contract.SpotDirection", param.getSpotDirection()));
		}

		// 全部，已开，未开 {ALL, TRUE, FALSE}
		if (param.getIsInvoiced() != null) {
			criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
		}

		// 全部，已交付，未交付 {ALL, TRUE, FALSE}
		if (param.getIsDelivered() != null) {
			criteria.add(Restrictions.eq("IsDelivered", param.getIsDelivered()));
		}

		// 合同日期（= 业务日期）
		if (param.getTradeDate() != null) {
			/**
			 * 加1天
			 */
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(param.getTradeDate());
			calendar.add(Calendar.DATE, 1);
			Date endDate = calendar.getTime();

			criteria.add(Restrictions.ge("contract.TradeDate", param.getTradeDate()));
			criteria.add(Restrictions.le("contract.TradeDate", endDate));
		}
		// 合同日期：开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("contract.TradeDate", param.getStartDate()));
		}
		// 合同日期：结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("contract.TradeDate", param.getEndDate()));
		}
		// 批次业务日期：开始日期
		if (param.getTradeDateStart() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getTradeDateStart()));
		}
		// 批次业务日期：结束日期
		if (param.getTradeDateEnd() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getTradeDateEnd()));
		}

		// true = 仅列出均价合同
		if (param.getIsAverageLotOnly() == true) {
			criteria.add(Restrictions.or(Restrictions.eq("MajorType", MajorType.Average),
					Restrictions.eq("PremiumType", PremiumType.Average)));
		}

		// true = 落入当前计价期的, false = 点价期外
		if (param.getAverageAtCurrentDuration() != null) {
			if (param.getAverageAtCurrentDuration() == true) {
				criteria.add(Restrictions.ge("MajorStartDate", today));
				criteria.add(Restrictions.le("MajorEndDate", today));
			}
		}

		// 是否只列出、还没有完成点价的
		if (param.getRePricing() == null || (param.getRePricing() != null && !param.getRePricing())) {
			Restrictions.eq("IsPriced", false);
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();

		logger.info("-------------执行查询");

		List<Lot> lots = lotService.Lots(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(),
				param.getOrderBy(), total);

		logger.info("-------------查询结束 size:" + lots.size());

		// lots = commonService.SimplifyDataLotList(lots);

		lots = this.simplifyDateForLots(lots);
		return new ActionResult<>(true, "", lots, total);
	}

	private List<Lot> simplifyDateForLots(List<Lot> lots) {

		if (lots == null || lots.size() == 0)
			return lots;

		for (Lot lot : lots) {

			lot.setLegalCode(lot.getLegal().getCode());
			lot.setLegalName(lot.getLegal().getName());

			String custormerName = "";
			if (lot.getCustomer() != null) {
				custormerName = lot.getCustomer().getName();
			}
			lot.setCustomerName(custormerName);

			String traderName = "";
			if (lot.getContract() != null && lot.getContract().getTrader() != null) {
				traderName = lot.getContract().getTrader().getName();
			}
			lot.setTraderName(traderName);

			String majorMarketName = "";
			if (lot.getMajorMarket() != null) {
				majorMarketName = lot.getMajorMarket().getName();
			}
			lot.setMajorMarketName(majorMarketName);

			Integer digits = 0;
			String unit = "";
			if (lot.getCommodity() != null) {

				digits = lot.getCommodity().getDigits();
				unit = lot.getCommodity().getUnit();
			}

			lot.setDigits(digits);
			lot.setUnit(unit);
		}

		return lots;
	}

	@RequestMapping("PagerAgreed")
	@ResponseBody
	public ActionResult<List<Lot>> PagerAgreed(HttpServletRequest request, @RequestBody LotParams param) {
		if (param == null) {
			param = new LotParams();
		}
		param.setStatuses(String.valueOf(Status.Agreed));
		return Pager(request, param);
	}

	@RequestMapping("Pager4Glue")
	@ResponseBody
	public ActionResult<List<VmContractLot4Combox>> Pager4Glue(HttpServletRequest request,
			@RequestBody(required = false) LotParams param) {
		if (param == null) {
			param = new LotParams();
		}
		param.setStatuses(String.valueOf(Status.Agreed));
		Date today = DateUtil.doSFormatDate(DateUtil.doFormatDate(new Date(), "yyyyMMdd"), "yyyyMMdd");

		Criteria criteria = lotService.GetCriteria();
		criteria.createAlias("Contract", "contract", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
		// 业务状态
		if (!StringUtils.isBlank(param.getStatuses())) {

			String[] split = param.getStatuses().split(",");

			Stream<String> stream = Arrays.stream(split);
			List<Integer> arrs = stream.map(s -> Integer.valueOf(s)).collect(Collectors.toList());

			criteria.add(Restrictions.in("contract.Status", arrs));

		}

		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			criteria.add(Restrictions.or(Restrictions.like("FullNo", "%" + param.getKeyword() + "%"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%")));
		}

		if (!StringUtils.isBlank(param.getLegalIds())) {

			String[] split = param.getLegalIds().split(",");
			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s)).collect(Collectors.toList());
			criteria.add(Restrictions.in("LegalId", arrs));
		}

		// 客户标识
		if (param.getCustomerId() != null) {
			criteria.add(Restrictions.eq("contract.CustomerId", param.getCustomerId()));
		}

		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("contract.CommodityId", param.getCommodityId()));
		}

		// 内部台头的标识
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("contract.LegalId", param.getLegalId()));
		}

		// 全部，采购，销售 {P, S}
		if (!StringUtils.isBlank(param.getSpotDirection())) {
			criteria.add(Restrictions.eq("contract.SpotDirection", param.getSpotDirection()));
		}

		// 全部，已开，未开 {ALL, TRUE, FALSE}
		if (param.getIsInvoiced() != null) {
			criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
		}

		// 全部，已交付，未交付 {ALL, TRUE, FALSE}
		if (param.getIsDelivered() != null) {
			criteria.add(Restrictions.eq("IsDelivered", param.getIsDelivered()));
		}

		// 合同日期（= 业务日期）
		if (param.getTradeDate() != null) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(param.getTradeDate());
			calendar.add(Calendar.DATE, 1);
			Date endDate = calendar.getTime();
			criteria.add(Restrictions.ge("contract.TradeDate", param.getTradeDate()));
			criteria.add(Restrictions.le("contract.TradeDate", endDate));

		}
		// 合同日期：开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("contract.TradeDate", param.getStartDate()));
		}
		// 合同日期：结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("contract.TradeDate", param.getEndDate()));
		}

		// true = 仅列出均价合同
		if (param.getIsAverageLotOnly() == true) {
			criteria.add(Restrictions.or(Restrictions.eq("MajorType", MajorType.Average),
					Restrictions.eq("PremiumType", PremiumType.Average)));
		}

		// true = 落入当前计价期的, false = 点价期外
		if (param.getAverageAtCurrentDuration() != null) {
			if (param.getAverageAtCurrentDuration() == true) {
				criteria.add(Restrictions.ge("MajorStartDate", today));
				criteria.add(Restrictions.le("MajorEndDate", today));
			}
		}

		// 是否只列出、还没有完成点价的
		if (param.getRePricing() == null || (param.getRePricing() != null && !param.getRePricing())) {
			Restrictions.eq("IsPriced", false);
		}

		RefUtil total = new RefUtil();

		logger.info("------开始查询");

		List<Lot> lots = lotService.Lots(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(),
				param.getOrderBy(), total);

		logger.info("------查询结束 size：" + lots.size());

		// logger.info("------开始格式化数据");

		// 开启线程处理数据格式化
		// lots = buttchSimplifyDateForLot(lots);

		// lots = commonService.SimplifyDataLotList(lots);

		// logger.info("-----格式化完成");

		final List<VmContractLot4Combox> vmContractLot4ComboxList = new ArrayList<>();

		if (lots != null) {
			lots.forEach(x -> {
				VmContractLot4Combox vmContractLot4Combox = new VmContractLot4Combox();
				vmContractLot4Combox.setId(x.getId());
				vmContractLot4Combox.setLegalName(x.getLegal() == null ? "" : x.getLegal().getName());
				vmContractLot4Combox.setHeadNo(x.getHeadNo());
				vmContractLot4Combox.setFullNo(x.getFullNo());
				// vmContractLot4Combox.setQuantity(commonService.FormatQuantity(x.getQuantity(),
				// x.getCommodity(), x.getCommodityId()));

				// 保留几位小数
				if (x.getQuantity() == null) {

					x.setQuantity(BigDecimal.ZERO);
				}

				// 计算需要保留几位
				int digits = 3;

				if (x.getCommodity() != null && x.getCommodity().getDigits() != null) {
					digits = x.getCommodity().getDigits();
				}

				// 保留3位小数
				vmContractLot4Combox.setQuantity(x.getQuantity().setScale(digits, RoundingMode.HALF_EVEN));

				vmContractLot4Combox.setCustomerName(x.getCustomer() == null ? "" : x.getCustomer().getName());
				vmContractLot4Combox.setCustomerShortName(x.getCustomerShortName());

				vmContractLot4ComboxList.add(vmContractLot4Combox);
			});
		}
		return new ActionResult<>(true, "", vmContractLot4ComboxList, total);
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager4Hedged")
	@ResponseBody
	public ActionResult<List<Lot>> Pager4Hedged(HttpServletRequest request, @RequestBody LotParams param) {
		if (param == null) {
			param = new LotParams();
		}
		try {

			Date today = DateUtil.doSFormatDate(DateUtil.doFormatDate(new Date(), "yyyyMMdd"), "yyyyMMdd");

			Criteria criteria = lotService.GetCriteria();

			// 加入权限过滤参数
			String userId = LoginHelper.GetLoginInfo().UserId;
			criteria = commonService.AddPermission(userId, criteria, "CreatedId");

			criteria.createAlias("Contract", "contract", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Legal", "legal", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {

				Criterion a = Restrictions.like("FullNo", "%" + param.getKeyword() + "%");
				Criterion b = Restrictions.like("contract.DocumentNo", "%" + param.getKeyword() + "%");
				Criterion c = Restrictions.like("legal.Name", "%" + param.getKeyword() + "%");
				Criterion d = Restrictions.like("customer.Name", "%" + param.getKeyword() + "%");
				Criterion e = Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%");
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(a);
				disjunction.add(b);
				disjunction.add(c);
				disjunction.add(d);
				disjunction.add(e);
				criteria.add(disjunction);
			}

			// 创建者
			if (StringUtils.isNotBlank(param.getCreatedBy())) {
				criteria.add(Restrictions.like("CreatedBy", "%" + param.getCreatedBy() + "%"));
			}
			// 币种
			if (StringUtils.isNotBlank(param.getCurrency())) {
				criteria.add(Restrictions.eq("contract.Currency", param.getCurrency()));
			}

			// 业务类型
			if (StringUtils.isNotBlank(param.getSpotType())) {
				criteria.add(Restrictions.eq("contract.SpotType", param.getSpotType()));
			}

			if (StringUtils.isNotBlank(param.getStatuses())) {
				String[] split = param.getStatuses().split(",");

				Stream<String> stream = Arrays.stream(split);
				List<Integer> arrs = stream.map(s -> Integer.valueOf(String.format("%s", s).trim()))
						.collect(Collectors.toList());
				if (arrs.size() > 0) {
					criteria.add(Restrictions.in("contract.Status", arrs));
				}

			}

			if (StringUtils.isNotBlank(param.getLegalIds())) {

				String[] split = param.getLegalIds().split(",");

				Stream<String> stream = Arrays.stream(split);
				List<String> arrs = stream.map(s -> String.format("%s", s.trim())).collect(Collectors.toList());
				if (arrs.size() > 0) {
					criteria.add(Restrictions.in("LegalId", arrs));
				}
			}

			// 客户标识
			if (param.getMarkColor() != 0) {
				criteria.add(Restrictions.eq("MarkColor", param.getMarkColor()));
			}

			// 客户标识
			if (StringUtils.isNotBlank(param.getCustomerId())) {
				criteria.add(Restrictions.eq("contract.CustomerId", param.getCustomerId()));
			}

			// 品种标识
			if (StringUtils.isNotBlank(param.getCommodityId())) {
				criteria.add(Restrictions.eq("contract.CommodityId", param.getCommodityId()));
			}

			// 内部台头的标识
			if (StringUtils.isNotBlank(param.getLegalId())) {
				criteria.add(Restrictions.eq("contract.LegalId", param.getLegalId()));
			}

			// 业务经理
			if (StringUtils.isNotBlank(param.getTraderId())) {
				criteria.add(Restrictions.eq("contract.TraderId", param.getTraderId()));
			}

			// 全部，采购，销售 {P, S}
			if (StringUtils.isNotBlank(param.getSpotDirection())) {
				criteria.add(Restrictions.eq("contract.SpotDirection", param.getSpotDirection()));
			}

			// 全部，已开，未开 {ALL, TRUE, FALSE}
			if (param.getIsInvoiced() != null) {
				criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
			}

			// 全部，已交付，未交付 {ALL, TRUE, FALSE}
			if (param.getIsDelivered() != null) {
				criteria.add(Restrictions.eq("IsDelivered", param.getIsDelivered()));
			}

			// 合同日期（= 业务日期）
			if (param.getTradeDate() != null) {
				/**
				 * 加1天
				 */
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(param.getTradeDate());
				calendar.add(Calendar.DATE, 1);
				Date endDate = calendar.getTime();

				criteria.add(Restrictions.ge("contract.TradeDate", param.getTradeDate()));
				criteria.add(Restrictions.le("contract.TradeDate", endDate));
			}
			// 合同日期：开始日期
			if (param.getStartDate() != null) {
				criteria.add(Restrictions.ge("contract.TradeDate", param.getStartDate()));
			}
			// 合同日期：结束日期
			if (param.getEndDate() != null) {
				criteria.add(Restrictions.le("contract.TradeDate", param.getEndDate()));
			}
			// 批次业务日期：开始日期
			if (param.getTradeDateStart() != null) {
				criteria.add(Restrictions.ge("TradeDate", param.getTradeDateStart()));
			}
			// 批次业务日期：结束日期
			if (param.getTradeDateEnd() != null) {
				criteria.add(Restrictions.le("TradeDate", param.getTradeDateEnd()));
			}

			// true = 仅列出均价合同
			if (param.getIsAverageLotOnly() == true) {
				criteria.add(Restrictions.or(Restrictions.eq("MajorType", MajorType.Average),
						Restrictions.eq("PremiumType", PremiumType.Average)));
			}

			// true = 落入当前计价期的, false = 点价期外
			if (param.getAverageAtCurrentDuration() != null) {
				if (param.getAverageAtCurrentDuration() == true) {
					criteria.add(Restrictions.ge("MajorStartDate", today));
					criteria.add(Restrictions.le("MajorEndDate", today));
				}
			}

			// 是否只列出、还没有完成点价的
			if (param.getRePricing() == null || (param.getRePricing() != null && !param.getRePricing())) {
				Restrictions.eq("IsPriced", false);
			}
			criteria.add(Restrictions.gt("QuantityHedged", BigDecimal.ZERO));
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();

			logger.info("-------------执行查询");

			List<Lot> lots = lotService.Lots4Hedged(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);

			logger.info("-------------查询结束 size:" + lots.size());
			lots = this.simplifyDateForLots(lots);
			return new ActionResult<>(true, "", lots, total);
		} catch (Exception ex) {
			ActionResult<List<Lot>> temp = new ActionResult<List<Lot>>();
			temp.setSuccess(false);
			temp.setMessage(ex.getMessage());
			return temp;
		}
	}

	/**
	 * 多线程处理 lot list 数据格式化
	 * 
	 * @param lots
	 * @return
	 */
	private List<Lot> buttchSimplifyDateForLot(List<Lot> lots) {

		if (lots == null || lots.size() == 0)
			return null;

		// 获取本机cpu 核心数量. 核心数量 = 线程数量
		int threadNumber = Runtime.getRuntime().availableProcessors();

		// 计算每个线程需要处理的记录数量
		int objNumber = 0;

		if (lots.size() % threadNumber == 0) {

			objNumber = lots.size() / threadNumber;

		} else {
			objNumber = (int) (Math.floor(lots.size() / threadNumber) + 1);
		}

		logger.info("--------------- 开启线程处理 lots. 线程数量：" + threadNumber + "   每个线程处理记录数量：" + objNumber);

		// 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(threadNumber);

		// 回调集合
		List<Future<?>> fList = new ArrayList<>();

		// 分配任务。按集合中元素数量分配线程数
		for (int i = 0; i < threadNumber; i++) {

			int start = i * objNumber;

			int end = (i + 1) * objNumber;

			if (end >= lots.size())
				end = lots.size();

			Callable<?> lable = new LotSimplifyDateCallable(lots.subList(start, end));

			// 启动线程
			Future<?> f = pool.submit(lable);

			fList.add(f);
		}

		// 获取处理结果
		List<Lot> resultList = new ArrayList<>();
		try {
			for (Future<?> f : fList) {
				resultList.addAll((Collection<? extends Lot>) f.get());
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}
		// 关闭线程池
		pool.shutdown();

		logger.info("--------------- 处理结果完成。返回集合 size：" + resultList.size());

		return resultList;
	}

	// 线程处理类
	class LotSimplifyDateCallable implements Callable {

		private List<Lot> lots;

		public LotSimplifyDateCallable(List<Lot> lots) {

			this.lots = lots;
		}

		@Override
		public Object call() throws Exception {

			return commonService.SimplifyDataLotList(lots);

		}
	}

	/**
	 * LotGlueSimple
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping("LotGlueSimple")
	@ResponseBody
	public ActionResult<List<VmContractLot4Combox>> LotGlueSimple(HttpServletRequest request,
			@RequestBody LotParams param) {
		try {
			return lotService.LotsByKeywords(param.getKeyword());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * MTM Query
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping("Lot4MTMQuery")
	@ResponseBody
	public ActionResult<List<Lot4MTM>> Lot4MTMQuery(HttpServletRequest request, @RequestBody LotParams param) {
		return lotService.Lot4MTMQuery(param.getStartDate(), param.getEndDate(), false);
	}

	/**
	 * MTM Query
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Lot4MTMQuery2")
	@ResponseBody
	public ActionResult<List<Lot4MTM>> Lot4MTMQuery2(HttpServletRequest request, @RequestBody LotParams param) {
		return lotService.Lot4MTMQuery(param.getStartDate(), param.getEndDate(), true);
	}

	@RequestMapping("Lot4MTMQueryNew")
	@ResponseBody
	public ActionResult<List<Lot4MTM>> Lot4MTMQueryNew(HttpServletRequest request, @RequestBody ContractParams param) {
		return lotService.Lot4MTMQueryNew(param.getStartDate(), param.getEndDate(), param.getKeyword(),
				param.getCurrency(), param.getSpotType(), param.getStatuses(), param.getLegalId().toString(),
				param.getLegalIds(), param.getCommodityId() != null ? param.getCommodityId().toString() : "");
	}

	@RequestMapping("Lot4MTMQueryNew2")
	@ResponseBody
	public ActionResult<List<Lot4MTM>> Lot4MTMQueryNew2(HttpServletRequest request, @RequestBody MtmParams param) {
		try {
			return lotService.Lot4MTMQueryNew2(param.getStartDate(), param.getEndDate(), param.getInvoiceStartDate(),
					param.getInvoiceEndDate(), param.getKeyword(), param.getCurrency(), param.getSpotType(),
					param.getStatuses(), param.getLegalId(), param.getLegalIds(),
					param.getCommodityId() != null ? param.getCommodityId() : "", param.getInvoiceCreatedId(),
					param.getInvoiceStatus(), param.getAccountYear(), param.getAccountMonth());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Lot4Fees")
	@ResponseBody
	public ActionResult<List<Lot4FeesOverview>> Lot4Fees(HttpServletRequest request,
			@RequestBody ContractParams param) {
		try {
			return lotService.Lot4Fees(param.getStartDate(), param.getEndDate(), param.getKeyword(),
					param.getCurrency(), param.getSpotType(), param.getStatuses(), param.getLegalId(),
					param.getLegalIds(), param.getCommodityId() != null ? param.getCommodityId() : "",
					param.getPageSize(), param.getPageIndex());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * MTM Query
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Lot4MTMQuery3")
	@ResponseBody
	public ActionResult<List<Lot4MTM>> Lot4MTMQuery3(HttpServletRequest request, @RequestBody ContractParams param) {
		try {
			return lotService.Lot4MTMQuery3(param.getStartDate(), param.getEndDate(), param.getKeyword(),
					param.getCurrency(), param.getSpotType(), param.getStatuses(), param.getLegalId(),
					param.getLegalIds(), param.getCommodityId() != null ? param.getCommodityId().toString() : "",
					param.getSpotDirection());

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据批次标识，返回单个实体的信息（包括Brands）
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Lot> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return lotService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据批次标识，返回多个实体的信息（包括Brands）
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetListById")
	@ResponseBody
	public ActionResult<List<Lot>> GetListById(HttpServletRequest request, @RequestBody String ids) {
		try {

			if (ids.trim() == "")
				return new ActionResult<>(false, "参数不合法");

			String[] arryId = ids.split(",");
			List<String> listId = new ArrayList<>();
			for (String str : arryId) {
				listId.add(str);
			}
			Criteria criteria = lotService.GetCriteria();
			criteria.add(Restrictions.in("Id", listId));
			List<Lot> lots = lotService.GetListById(criteria);

			lots = this.simplifyDateForLots(lots);
			return new ActionResult<>(true, "", lots);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 批次一览的信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("LotViewById")
	@ResponseBody
	public ActionResult<Lot> LotViewById(HttpServletRequest request, @RequestBody String id) {
		try {
			return lotService.LotViewById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("Lots")
	@ResponseBody
	public ActionResult<List<Lot>> Lots(HttpServletRequest request) {
		return new ActionResult<>(true, "", lotService.Lots());
	}

	/**
	 * 保值：分配点价结果
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("AllocatePricing")
	@ResponseBody
	public ActionResult<String> AllocatePricing(HttpServletRequest request, @RequestBody Lot lot) {
		return lotService.AllocatePricing(lot);
	}

	/**
	 * 保值：移除点价结果
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("RemovePricing")
	@ResponseBody
	public ActionResult<String> RemovePricing(HttpServletRequest request, @RequestBody Lot lot) {
		return lotService.RemovePricing(lot);
	}

	/**
	 * 保值：分配保值头寸
	 * 
	 * @param allocateToLot
	 * @return
	 */
	@RequestMapping("AllocatePosition")
	@ResponseBody
	public ActionResult<String> AllocatePosition(HttpServletRequest request,
			@RequestBody CpPosition4AllocateToLot allocateToLot) {
		return lotService.AllocatePosition(allocateToLot);
	}

	/**
	 * 保值：移除保值头寸
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("RemovePosition")
	@ResponseBody
	public ActionResult<String> RemovePosition(HttpServletRequest request, @RequestBody CpPosition4RemoveFromLot lot) {
		return lotService.RemovePosition(lot);
	}

	/**
	 * 通用：保存批次，没有特殊处理
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveLot")
	@ResponseBody
	public ActionResult<Lot> SaveLot(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot.getId() != null) {
			lot.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			lot.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		return lotService.SaveLot(lot);
	}

	/**
	 * 保存临单：同时保存合同头 + 唯一的批次
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveContractProvisional")
	@ResponseBody
	public ActionResult<Lot> SaveContractProvisional(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot.getId() != null) {
			lot.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			lot.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		lotService.SaveContractProvisional(lot);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, lot);
	}

	/**
	 * 保存长单：的某个批次
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveLotOfContractRegular")
	@ResponseBody
	public ActionResult<Lot> SaveLotOfContractRegular(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot.getId() != null) {
			lot.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			lot.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		return lotService.SaveLotOfContractRegular(lot);
	}

	/**
	 * 保存批量地发货、出库。改进后的"交付"方式。
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveStorageOuts")
	@ResponseBody
	public ActionResult<String> SaveStorageOuts(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot == null) {
			return new ActionResult<>(false, MessageCtrm.ParamError);
		}
		return lotService.SaveStorageOuts(lot);
	}

	/**
	 * 删除长单的某个批次
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("DeleteLotOfContractRegular")
	@ResponseBody
	public ActionResult<String> DeleteLotOfContractRegular(HttpServletRequest request, @RequestBody String id) {
		try {
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Position);
			tableList.add(TableNameConst.Pricing);
			tableList.add(TableNameConst.Invoice);
			tableList.add(TableNameConst.ReceiptShip);
			tableList.add(TableNameConst.Fund);
			tableList.add(TableNameConst.Storage);
			// tableList.add(TableNameConst.FinishedProduct);

			ActionResult<String> checkResult = checkService.deletable(id, "LotId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			return lotService.DeleteLotOfContractRegular(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 删除临单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("DeleteContractProvisional")
	@ResponseBody
	public ActionResult<String> DeleteContractProvisional(HttpServletRequest request, @RequestBody String id) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return lotService.DeleteContractProvisional(id, userId);
		} catch (Exception ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 确认批次是否已经全部交付（全部收货或者全部发货）
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("ConfirmLotQuantityDelivered")
	@ResponseBody
	public ActionResult<String> ConfirmLotQuantityDelivered(HttpServletRequest request, @RequestBody Lot lot) {
		return lotService.ConfirmLotQuantityDelivered(lot);
	}

	/**
	 * 只是用来更新开票的标志，除了JHC，可能还会有别的用户、有此要求
	 * 
	 * @param lots
	 * @return
	 */
	@RequestMapping("UpdateInvoicFlagOfLots")
	@ResponseBody
	public ActionResult<String> UpdateInvoicFlagOfLots(HttpServletRequest request,
			@RequestBody java.util.ArrayList<Lot> lots) {
		return lotService.UpdateInvoicFlagOfLots(lots);
	}

	/**
	 * 根据Lot.Id，获取同一个合同号下面的全部批次的列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetLotsByLotId")
	@ResponseBody
	public ActionResult<List<Lot>> GetLotsByLotId(HttpServletRequest request, @RequestBody String id) {
		try {
			return lotService.GetLotsByLotId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 获取长单合同的第一个批次
	 * 
	 */
	@RequestMapping("GetFirstLotByContractId")
	@ResponseBody
	public ActionResult<Lot> GetFirstLotByContractId(HttpServletRequest request, @RequestBody String contractId) {
		try {
			return lotService.GetFirstLotByContractId(contractId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据Lot.Id，获取同一个合同号下面的全部批次的列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetLotsByContractId")
	@ResponseBody
	public ActionResult<List<Lot>> GetLotsByContractId(HttpServletRequest request, @RequestBody String id) {
		try {
			return lotService.GetLotsByContractId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 获取指定客户的全部待收款和待付款的批次
	 * 
	 */
	@RequestMapping("LotsByCustomerId")
	@ResponseBody
	public ActionResult<List<Lot>> LotsByCustomerId(HttpServletRequest request, @RequestBody String customerId) {
		try {
			return lotService.LotsByCustomerId(customerId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 标注颜色
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("MarkColor")
	@ResponseBody
	public ActionResult<String> MarkColor(HttpServletRequest request, @RequestBody Lot lot) {
		return lotService.MarkColor(lot);
	}

	/**
	 * 清除批次的颜色分类标志
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("ClearColor")
	@ResponseBody
	public ActionResult<String> ClearColor(HttpServletRequest request, @RequestBody Lot lot) {
		return lotService.ClearColor(lot);
	}

	/**
	 * BVI的特殊业务：同时创建Bvi的销售批次和Sm的采购批次
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveLot4BviToSm")
	@ResponseBody
	public ActionResult<Lot> SaveLot4BviToSm(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot == null || lot.getContractId() == null) {
			ActionResult<Lot> tempVar = new ActionResult<Lot>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		try {
			if (lot.getId() != null) {
				lot.setUpdatedAt(new Date());
				lot.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				lot.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				lot.setCreatedAt(new Date());
				lot.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				lot.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			return lotService.SaveLot4BviToSm(lot);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 修改初始化的批次的执行结果，由于导入时可能有错，允许之后人工修改
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("ModifyExecuted4Initiated")
	@ResponseBody
	public ActionResult<String> ModifyExecuted4Initiated(HttpServletRequest request, @RequestBody Lot lot) {
		try {
			return lotService.ModifyExecuted4Initiated(lot);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	/**
	 * 特殊业务：分拆批次
	 * 
	 * @param cpSplitLot
	 * @return
	 */
	@RequestMapping("SplitLot")
	@ResponseBody
	public ActionResult<String> SplitLot(HttpServletRequest request, @RequestBody CpSplitLot cpSplitLot) {
		String userId = LoginHelper.GetLoginInfo(request).getUserId();
		return lotService.SplitLot(cpSplitLot, userId);
	}

	/**
	 * 生成批次的费用信息
	 * 
	 * @param curLot
	 * @return
	 */
	@RequestMapping("GenerateFees")
	@ResponseBody
	public ActionResult<String> GenerateFees(HttpServletRequest request, @RequestBody Lot curLot) {
		return lotService.GenerateFees(curLot);
	}

	@RequestMapping("DeleteFeesByLotId")
	@ResponseBody
	public ActionResult<String> DeleteFeesByLotId(HttpServletRequest request, @RequestBody String lotId) {
		return lotService.DeleteFeesByLotId(lotId);
	}

	/**
	 * 更新批次的收付款标记
	 * 
	 * @param lotId
	 * @return
	 */
	@RequestMapping("UpdateLotIsFunded")
	@ResponseBody
	public ActionResult<String> UpdateLotIsFunded(HttpServletRequest request, @RequestBody String lotId) {
		if (!StringUtils.isBlank(lotId)) {
			return commonService.UpdateLotIsFunded(lotId);
		} else {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("批次信息错误");
			return tempVar;
		}
	}

	/**
	 * 批次分拆
	 * 
	 * @param lotId
	 * @param splitQuantity
	 * @return
	 */
	@RequestMapping("SplitLotQuantity")
	@ResponseBody
	public ActionResult<String> SplitLotQuantity(HttpServletRequest request, @RequestBody String lotId,
			@RequestBody String splitQuantity) {
		try {
			return lotService.SplitLotQuantity(lotId, new BigDecimal(splitQuantity));
		} catch (Exception ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 结算/试算
	 * 
	 * @param param4LotPnL
	 * @return
	 */
	@RequestMapping("LotSettleTrial")
	@ResponseBody
	public ActionResult<LotPnL> LotSettleTrial(HttpServletRequest request, @RequestBody Param4LotPnL param4LotPnL) {
		try {

			return lotService.LotSettleTrial(param4LotPnL);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("LotSettleOfficial")
	@ResponseBody
	public ActionResult<LotPnL> LotSettleOfficial(HttpServletRequest request, @RequestBody Param4LotPnL param4LotPnL) {
		try {

			return lotService.LotSettleOfficial(param4LotPnL);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("LotPnLById")
	@ResponseBody
	public ActionResult<LotPnL> LotPnLById(HttpServletRequest request, @RequestBody String id) {
		try {
			return lotService.LotPnLById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 更新批次的保值均价
	 * 
	 * @param lotId
	 * @return
	 */
	@RequestMapping("UpdateLotHedgedPrice")
	@ResponseBody
	public ActionResult<String> UpdateLotHedgedPrice(HttpServletRequest request, @RequestBody String lotId) {
		if (!StringUtils.isBlank(lotId)) {
			return lotService.UpdateLotHedgedPrce(lotId);
		} else {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("批次信息错误");
			return tempVar;
		}
	}

	/**
	 * 更新批次的Spread
	 * 
	 * @param lotId
	 * @return
	 */
	@RequestMapping("UpdateLotSpread")
	@ResponseBody
	public ActionResult<String> UpdateLotSpread(HttpServletRequest request, @RequestBody String lotId) {
		if (StringUtils.isNotBlank(lotId)) {

			if (commonService.UpdateFuturesSpread(lotId)) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(true);
				tempVar.setMessage("更新成功");
				return tempVar;
			} else {
				ActionResult<String> tempVar2 = new ActionResult<String>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("更新失败");
				return tempVar2;
			}
		} else {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("批次信息错误");
			return tempVar3;
		}
	}

	/**
	 * 更新批次的费用
	 * 
	 * @param lotId
	 * @return
	 */
	@RequestMapping("UpdateLotFee")
	@ResponseBody
	public ActionResult<String> UpdateLotFee(HttpServletRequest request, @RequestBody String lotId) {
		if (!StringUtils.isBlank(lotId)) {

			if (commonService.UpdateLotFeesByLotId(lotId)) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(true);
				tempVar.setMessage("更新成功");
				return tempVar;
			} else {
				ActionResult<String> tempVar2 = new ActionResult<String>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("更新失败");
				return tempVar2;
			}
		} else {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("批次信息错误");
			return tempVar3;
		}
	}

	/**
	 * 保存修改后的QP
	 * 
	 * @param curQPRecord
	 *            结算日期记录
	 * @return
	 */
	@RequestMapping("SaveQPRecord")
	@ResponseBody
	public ActionResult<QPRecord> SaveQPRecord(HttpServletRequest request, @RequestBody QPRecord curQPRecord) {

		LoginInfoToken token = LoginHelper.GetLoginInfo(request);

		if (token == null)
			return new ActionResult<>(false, "token is null");

		if (curQPRecord.getId() != null) {
			curQPRecord.setUpdatedAt(new Date());
			curQPRecord.setUpdatedBy(token.getName());
			curQPRecord.setUpdatedId(token.getUserId());
		} else {
			curQPRecord.setCreatedAt(new Date());
			curQPRecord.setCreatedBy(token.getName());
			curQPRecord.setCreatedId(token.getUserId());
		}
		return lotService.SaveQPRecord(curQPRecord);
	}

	@RequestMapping("SaveLotOfContractRegularNew")
	@ResponseBody
	public ActionResult<Lot> SaveLotOfContractRegularNew(HttpServletRequest request, @RequestBody Lot lot) {
		try {
			LoginInfoToken token = LoginHelper.GetLoginInfo();
			if (StringUtils.isNotBlank(lot.getId())) {
				lot.setUpdatedId(token.getUserId());
			} else {
				lot.setCreatedId(token.getUserId());
			}
			ActionResult<Lot> returnLot = lotService.SaveLotOfContractRegular_New(lot);
			if (returnLot.isSuccess()) {
				lotService.GenerateFees_New(returnLot.getData());
				// 执行更新价格与余额
				returnLot.setData(commonService.UpdateLotPriceByLotId_New(returnLot.getData().getId()));
			}
			returnLot.setData(lotService.GetById(returnLot.getData().getId()).getData());
			return returnLot;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 查询QP修改历史
	 * 
	 * @param lotId
	 *            批次编号
	 * @return
	 */
	@RequestMapping("PagerQPRecord")
	@ResponseBody
	public ActionResult<List<QPRecord>> PagerQPRecord(HttpServletRequest request, @RequestBody String lotId) {

		logger.info("----lotId:" + lotId);

		return lotService.PagerQPRecord(lotId);
	}

	@RequestMapping("DeleteQPRecord")
	@ResponseBody
	public ActionResult<String> DeleteQPRecord(HttpServletRequest request, @RequestBody QPRecord record) {

		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();

			return lotService.DeleteQPRcord(record, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("CalculateDeliveryedQuantity")
	@ResponseBody
	public ActionResult<Lot> CalculateDeliveryedQuantity(HttpServletRequest request, @RequestBody Lot lot) {
		try {
			if (lot == null) {
				ActionResult<Lot> tempVar = new ActionResult<Lot>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数错误");
				return tempVar;
			}
			// implicit typing in Java:
			QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed(lot);

			lot.setQuantity(quantityMaL.getQuantity() != null ? quantityMaL.getQuantity() : new BigDecimal(0));
			lot.setQuantityDelivered(quantityMaL.getQuantityDeliveryed());
			lot.setQuantityLess(
					quantityMaL.getQuantityLess() != null ? quantityMaL.getQuantityLess() : new BigDecimal(0));
			lot.setQuantityMore(
					quantityMaL.getQuantityMore() != null ? quantityMaL.getQuantityMore() : new BigDecimal(0));
			ActionResult<Lot> tempVar2 = new ActionResult<Lot>();
			tempVar2.setSuccess(true);
			tempVar2.setData(lot);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 保存入帐年月
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("SaveAccountDateByLot")
	@ResponseBody
	public ActionResult<String> SaveAccountDateByLot(HttpServletRequest request, @RequestBody Lot lot) {
		if (lot == null || lot.getId() == null) {
			return new ActionResult<>(true, "参数错误");
		}
		lot.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		return lotService.SaveAccountDateByLot(lot);
	}

	/**
	 * 批次实时MTM
	 */
	@RequestMapping("Lot4MTMQueryNew3")
	@ResponseBody
	public ActionResult<List<Lot4MTM3>> Lot4MTM3Query(HttpServletRequest request, @RequestBody MtmParams mtmParams) {
		try {
			RefUtil total = new RefUtil();
			List<Lot4MTM3> listLost = this.lotService.Lot4MTM3Query(mtmParams, total);
			return new ActionResult<>(true, "", listLost, total);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, "获取行情出错。");
		}
	}

	/**
	 * 未点价批次的风险警示
	 */
	@RequestMapping("Lot4UnpriceRisk")
	@ResponseBody
	public ActionResult<List<Lot4Unpriced>> UnpricedLotRisk(HttpServletRequest request,
			@RequestBody MtmParams mtmParams) {
		try {
			RefUtil total = new RefUtil();
			List<Lot4Unpriced> listLost = this.lotService.UnpricedLotRisk(mtmParams, total);
			ActionResult<List<Lot4Unpriced>> tempVar = new ActionResult<List<Lot4Unpriced>>();
			tempVar.setSuccess(true);
			tempVar.setData(listLost);
			tempVar.setTotal(total.getTotal());
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 待点价记录
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerNoPricing")
	@ResponseBody
	public ActionResult<List<Lot>> PagerNoPricing(HttpServletRequest request, @RequestBody LotParams param) {
		if (param == null)
			param = new LotParams();
		TestTime.start();

		Date today = DateUtil.doSFormatDate(DateUtil.doFormatDate(new Date(), "yyyyMMdd"), "yyyyMMdd");
		Criteria criteria = this.lotService.GetCriteria();

		criteria.createAlias("Contract", "contract", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Legal", "legal", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

		// 关键字
		if (StringUtils.isNotBlank(param.getKeyword())) {
			Criterion a = Restrictions.like("FullNo", "%" + param.getKeyword() + "%");
			Criterion b = Restrictions.like("contract.DocumentNo", "%" + param.getKeyword() + "%");
			Criterion c = Restrictions.like("legal.Name", "%" + param.getKeyword() + "%");
			Criterion d = Restrictions.like("customer.Name", "%" + param.getKeyword() + "%");
			Criterion e = Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%");
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(a);
			disjunction.add(b);
			disjunction.add(c);
			disjunction.add(d);
			disjunction.add(e);
			criteria.add(disjunction);
		}
		if(StringUtils.isNotBlank(param.getFullNo())) {
			criteria.add(Restrictions.like("FullNo", "%" + param.getFullNo() + "%"));
		}
		if(param.getPriceStartDate() != null) {
			criteria.add(Restrictions.ge("MajorStartDate", param.getPriceStartDate()));
		}
		if(param.getPriceEndDate()!=null) {
			criteria.add(Restrictions.le("MajorEndDate", param.getPriceEndDate()));
		}
		if(StringUtils.isNotBlank(param.getPricingType())) {
			criteria.add(Restrictions.eq("PricingType", param.getPricingType()));
		}
		// 权限
		String userid = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userid, criteria, "CreatedId");
		// 创建者
		if (StringUtils.isNotBlank(param.getCreatedBy()))
			criteria.add(Restrictions.like("CreatedBy", "%" + param.getCreatedBy() + "%"));
		// 币种
		if (StringUtils.isNotBlank(param.getCurrency()))
			criteria.add(Restrictions.eq("contract.Currency", param.getCurrency()));

		// 业务类型
		if (StringUtils.isNotBlank(param.getSpotType()))
			criteria.add(Restrictions.eq("contract.SpotType", param.getSpotType()));

		if (StringUtils.isNotBlank(param.getStatuses())) {
			String[] split = param.getStatuses().split(",");
			Stream<String> stream = Arrays.stream(split);
			List<Integer> arrs = stream.map(s -> Integer.valueOf(String.format("%s", s).trim()))
					.collect(Collectors.toList());
			if (arrs.size() > 0) {
				criteria.add(Restrictions.in("contract.Status", arrs));
			}
		}

		if (StringUtils.isNotBlank(param.getLegalIds())) {
			String[] split = param.getLegalIds().split(",");
			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s)).collect(Collectors.toList());
			criteria.add(Restrictions.in("LegalId", arrs));
		}

		// 客户标识
		if (param.getMarkColor() != 0) {
			criteria.add(Restrictions.eq("MarkColor", param.getMarkColor()));
		}

		// 客户标识
		if (StringUtils.isNotBlank(param.getCustomerId())) {
			criteria.add(Restrictions.eq("contract.CustomerId", param.getCustomerId()));
		}

		// 品种标识
		if (StringUtils.isNotBlank(param.getCommodityId())) {
			criteria.add(Restrictions.eq("contract.CommodityId", param.getCommodityId()));
		}

		// 内部台头的标识
		if (StringUtils.isNotBlank(param.getLegalId())) {
			criteria.add(Restrictions.eq("contract.LegalId", param.getLegalId()));
		}
		// 业务经理
		if (StringUtils.isNotBlank(param.getTraderId())) {
			criteria.add(Restrictions.eq("contract.TraderId", param.getTraderId()));
		}

		// 全部，采购，销售 {P, S}
		if (StringUtils.isNotBlank(param.getSpotDirection())) {
			criteria.add(Restrictions.eq("contract.SpotDirection", param.getSpotDirection()));
		}

		// 全部，已开，未开 {ALL, TRUE, FALSE}
		if (param.getIsInvoiced() != null) {
			criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
		}

		// 全部，已交付，未交付 {ALL, TRUE, FALSE}
		if (param.getIsDelivered() != null) {
			criteria.add(Restrictions.eq("IsDelivered", param.getIsDelivered()));
		}
		// 合同日期（= 业务日期）
		if (param.getTradeDate() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(param.getTradeDate());
			calendar.add(Calendar.DATE, 1);
			Date endDate = calendar.getTime();

			criteria.add(Restrictions.ge("contract.TradeDate", param.getTradeDate()));
			criteria.add(Restrictions.le("contract.TradeDate", endDate));
		}
		// 合同日期：开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("contract.TradeDate", param.getStartDate()));
		}
		// 合同日期：结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("contract.TradeDate", param.getEndDate()));
		}

		// true = 仅列出均价合同
		if (param.getIsAverageLotOnly() == true) {
			criteria.add(Restrictions.or(Restrictions.eq("MajorType", MajorType.Average),
					Restrictions.eq("PremiumType", PremiumType.Average)));
		} else {
			criteria.add(Restrictions.or(Restrictions.eq("MajorType", MajorType.Average),
					Restrictions.eq("MajorType", MajorType.Pricing)));
		}

		// true = 落入当前计价期的, false = 点价期外
		if (param.getAverageAtCurrentDuration() != null) {
			if (param.getAverageAtCurrentDuration() == true) {
				criteria.add(Restrictions.ge("MajorStartDate", today));
				criteria.add(Restrictions.le("MajorEndDate", today));
			}
		}

		// 是否只列出、还没有完成点价的
		criteria.add(Restrictions.eq("IsPriced", false));
		criteria.add(Restrictions.eq("Status", 1));

		RefUtil total = new RefUtil();
		TestTime.addMilestone("参数构造完成");
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		List<Lot> lots = lotService.Lots(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(),
				param.getOrderBy(), total);
		TestTime.addMilestone("查询完成");
		Date dtNow = DateUtil.doSFormatDate(new Date(), "yyyy-MM-dd");
		lots.forEach(p -> {
			p.setMarkColor(0);
			Date dtMajorEndDate = p.getMajorEndDate() == null ? DateUtil.doSFormatDate("1970-01-01", "yyyy-MM-dd")
					: DateUtil.doSFormatDate(p.getMajorEndDate(), "yyyy-MM-dd");
			if (DateUtil.getIntervalDays(dtNow, dtMajorEndDate) <= 3) {
				p.setMarkColor(-65536);
			} else if (DateUtil.getIntervalDays(dtNow, dtMajorEndDate) <= 7) {
				p.setMarkColor(-256);
			}
		});
		lots = commonService.SimplifyDataLotList(lots);
		TestTime.addMilestone("简化完成");
		// lots = lots.OrderBy(p => p.MajorEndDate).ToList();
		ActionResult<List<Lot>> tempVar = new ActionResult<>();
		tempVar.setSuccess(true);
		tempVar.setTotal(total.getTotal());
		tempVar.setData(lots);
		logger.info(TestTime.result());
		return tempVar;
	}

	@RequestMapping("NewLoadLotSettle")
	@ResponseBody
	public ActionResult<LotPnL> NewLoadLotSettle(@RequestBody Param4LotPnL param4LotPnL) {
		try {
			return lotService.NewLoadLotSettle(param4LotPnL);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("NewLotSettleOfficial")
	@ResponseBody
	public ActionResult<LotPnL> NewLotSettleOfficial(@RequestBody Param4LotPnL param4LotPnL) {
		try {
			return lotService.NewLotSettleOfficial(param4LotPnL);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 新批次拆分
	 * 
	 * @param LotSplit
	 * @return
	 */
	@RequestMapping("NewLotSplit")
	@ResponseBody
	public ActionResult<String> NewLotSplit(@RequestBody LotSplit LotSplit) {
		try {
			ActionResult<String> result = lotService.txSaveNewLotSplit(LotSplit);
			return result;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 试算
	 * 
	 * @param LotSplit
	 * @return
	 */
	@RequestMapping("NewLotSettleTrial")
	@ResponseBody
	public ActionResult<LotPnL> NewLotSettleTrial(@RequestBody Param4LotPnL param4LotPnL) {
		try {
			return lotService.NewLotSettleTrial(param4LotPnL);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 保值：分配保值头寸
	 * 
	 * 批量
	 * 
	 * @param allocateToLot
	 * @return
	 */
	@RequestMapping("AllocatePositionMulti")
	@ResponseBody
	public ActionResult<String> AllocatePositionMulti(HttpServletRequest request,
			@RequestBody CpPosition4AllocateToMultiLot allocateToMultiLot) {
		return lotService.AllocatePositionMulti(allocateToMultiLot);
	}
}

package com.smm.ctrm.api.Futures;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.Position4BrokerService;
import com.smm.ctrm.bo.Futures.PositionService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Physical.CpSplitPosition;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.apiClient.M2MParams;
import com.smm.ctrm.domain.apiClient.PositionParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/Position/")
public class PositionApiController {

	private static Logger logger = Logger.getLogger(PositionApiController.class);
	@Resource
	private PositionService positionService;
	
	@Resource
	private Position4BrokerService position4BrokerService;

	@Resource
	private LotService lotService;

	@Resource
	private CommonService commonService;

	/**
	 * 头寸对齐，相当于头寸的结算
	 * 
	 * @param positions
	 * @return ActionResult string
	 */
	@RequestMapping("Square")
	@ResponseBody
	public ActionResult<String> Square(HttpServletRequest request, @RequestBody List<Position> positions) {
		try {
			return positionService.Square(positions);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 未分配给批次的头寸
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerAvailable4Lot")
	@ResponseBody
	public ActionResult<List<Position>> PagerAvailable4Lot(HttpServletRequest request,
			@RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}

			Criteria criteria = positionService.GetCriteria();
			criteria.add(Restrictions.isNull("LotId"));

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {

				Criterion d = Restrictions.like("OurRef", "%" + param.getKeyword() + "%");
				Criterion e = Restrictions.like("Comments", "%" + param.getKeyword() + "%");
				Criterion f = Restrictions.like("CarryRef", "%" + param.getKeyword() + "%");
				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(d);
				disjunction.add(e);
				disjunction.add(f);
				criteria.add(disjunction);
			}
			
			// 套利类型
			if(StringUtils.isNotBlank(param.getPurpose())) {
				criteria.add(Restrictions.eq("Purpose", param.getPurpose()));
			}

			// 买入方向
			if (param.getLS() != null) {
				criteria.add(Restrictions.eq("LS", param.getLS()));
			}

			if (param.getCommodityId() != null) {
				criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
			}

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Position> positions = positionService.Positions(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());

			positions = commonService.SimplifyDataPositionList(positions);
			ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(positions);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 未分配给发票的头寸
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerAvailable4Invoice")
	@ResponseBody
	public ActionResult<List<Position>> PagerAvailable4Invoice(HttpServletRequest request,
			@RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}
			Criteria criteria = positionService.GetCriteria();
			criteria.add(Restrictions.isNull("InvoiceId"));
			// 关键字
			if (StringUtils.isBlank(param.getKeyword())) {
				criteria.add(Restrictions.or(Restrictions.like("OurRef", "%" + param.getKeyword() + "%"),
						Restrictions.like("Comments", "%" + param.getKeyword() + "%")));
			}
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Position> positions = positionService.Positions(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());

			positions = commonService.SimplifyDataPositionList(positions);
			ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(positions);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Position>> Pager(HttpServletRequest request, @RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}
			Date today = new Date();

			Criteria criteria = positionService.GetCriteria();

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Broker", "broker", JoinType.LEFT_OUTER_JOIN);

				Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
						Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
				Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
						Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));

				Criterion c = Restrictions.and(Restrictions.isNotNull("BrokerId"),
						Restrictions.like("broker.Name", "%" + param.getKeyword() + "%"));

				Criterion d = Restrictions.like("OurRef", "%" + param.getKeyword() + "%");
				Criterion e = Restrictions.like("Comments", "%" + param.getKeyword() + "%");
				Criterion f = Restrictions.like("CarryRef", "%" + param.getKeyword() + "%");

				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(a);
				disjunction.add(b);
				disjunction.add(c);
				disjunction.add(d);
				disjunction.add(e);
				disjunction.add(f);
				criteria.add(disjunction);
			}
			String userid = LoginHelper.GetLoginInfo().UserId;
			criteria = commonService.AddPermission(userid, criteria, "CreatedId");
			
			// 套利类型
			if(StringUtils.isNotBlank(param.getPurpose())) {
				criteria.add(Restrictions.eq("Purpose", param.getPurpose()));
			}
			
			// 是否仅列出当前的持仓
			// 品种标识
			if (param.getIsSquared() != null && param.getIsSquared()) {
				criteria.add(Restrictions.eq("IsSquared", false));
			}
			if(StringUtils.isNotBlank(param.getPosition4BrokerId())) {
				criteria.add(Restrictions.eq("Position4BrokerId", param.getPosition4BrokerId()));
			}

			// 品种标识
			if (param.getCommodityId() != null) {
				criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
			}

			// 买入方向
			if (param.getLS() != null) {
				criteria.add(Restrictions.eq("LS", param.getLS()));
			}

			// 市场标识
			if (param.getMarketId() != null) {
				criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
			}

			// 头寸类型
			if (!StringUtils.isBlank(param.getForwardType())) {
				criteria.add(Restrictions.eq("ForwardType", param.getForwardType()));
			}
			//头寸用途
			if (!StringUtils.isBlank(param.getPurpose())) {
				criteria.add(Restrictions.eq("Purpose", param.getPurpose()));
			}

			// 开始日期，和PromptDate比较
			if (param.getStartDate() != null) {
				criteria.add(Restrictions.ge("PromptDate", param.getStartDate()));
			}

			// 结束日期，和PromptDate比较
			if (param.getEndDate() != null) {
				criteria.add(Restrictions.le("PromptDate", param.getEndDate()));
			}
			// 开始日期，和TradeDate比较
			if (param.getTradeStartDate() != null) {
				criteria.add(Restrictions.ge("TradeDate", param.getTradeStartDate()));
			}

			// 结束日期，和TradeDate比较
			if (param.getTradeEndDate() != null) {
				criteria.add(Restrictions.le("TradeDate", param.getTradeEndDate()));
			}
			// 头寸是否被分配
			if (param.getIsDistributed() != null) {
				if (param.getIsDistributed())
					criteria.add(Restrictions.isNotNull("LotId"));
				else
					criteria.add(Restrictions.isNull("LotId"));
			}
			// true = 落入当前计价期的, false = 点价期外
			if (param.getAverageAtCurrentDuration() != null) {
				if (param.getAverageAtCurrentDuration() == true) {
					criteria.add(Restrictions.ge("AverageStartDate", today));
					criteria.add(Restrictions.le("AverageEndDate", today));
				} else {
					criteria.add(Restrictions.le("AverageStartDate", today));
					criteria.add(Restrictions.ge("AverageEndDate", today));
				}
			}

			// true = 包括已计价和未计价,false只包含未计价
			if (param.getIsPriced() != null && param.getIsPriced() == true) {
				criteria.add(Restrictions.eq("IsPriced", false));
			}
			if (param.getIsVirtual() != null) {
				criteria.add(Restrictions.eq("IsVirtual", param.getIsVirtual()));
			}

			if (param.getIsAvailable4Allocate2Lot() != null && param.getIsAvailable4Allocate2Lot()) {
				criteria.add(Restrictions.isNull("LotId"));
			}

			if (param.getCreateStartDate() != null) {
				criteria.add(Restrictions.ge("CreatedAt", param.getCreateStartDate()));
			}

			if (param.getCreateEndDate() != null) {
				criteria.add(Restrictions.le("CreatedAt", param.getCreateEndDate()));
			}

			if (param.getCommodityId() != null) {
				criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
			}
			if (param.getInstrumentId() != null) {
				criteria.add(Restrictions.eq("InstrumentId", param.getInstrumentId()));
			}
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
			// 是否调期
			if (param.getIsCarry() != null) {
				if (param.getIsCarry()) {
					criteria.add(Restrictions.eq("IsCarry", param.getIsCarry()));
				} else {
					criteria.add(Restrictions.or(Restrictions.isNull("IsCarry"), Restrictions.eq("IsCarry", false)));
				}
			}
			if (param.getIsCarryAgainst() != null) {
				if (param.getIsCarryAgainst()) {
					criteria.add(Restrictions.eq("IsCarryAgainst", param.getIsCarryAgainst()));
				} else {
					criteria.add(Restrictions.or(Restrictions.isNull("IsCarryAgainst"),
							Restrictions.eq("IsCarryAgainst", false)));
				}
			}
			if (param.getIsSourceOfCarry() != null) {
				if (param.getIsSourceOfCarry()) {
					criteria.add(Restrictions.eq("IsSourceOfCarry", param.getIsSourceOfCarry()));
				} else {
					criteria.add(Restrictions.or(Restrictions.isNull("IsSourceOfCarry"),
							Restrictions.eq("IsSourceOfCarry", false)));
				}
			}
			
			if(param.getOCS() != null)
			{
				criteria.add(Restrictions.eq("OSC", param.getOCS()));
			}
			
			if(param.getBrokerId() != null)
			{
				criteria.add(Restrictions.eq("BrokerId", param.getBrokerId()));
			}

			RefUtil total = new RefUtil();
			List<Position> positions = positionService.Positions(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());

			positions = commonService.SimplifyDataPositionList(positions);

			ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(positions);
			return tempVar;
		} catch (Exception ex) {
			ex.printStackTrace();

			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 分页查询-M2M
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerM2M")
	@ResponseBody
	public ActionResult<List<Position>> PagerM2M(HttpServletRequest request, @RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}
			Date today = new Date();

			Criteria criteria = positionService.GetCriteria();

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Broker", "broker", JoinType.LEFT_OUTER_JOIN);

				Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
						Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
				Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
						Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));

				Criterion c = Restrictions.and(Restrictions.isNotNull("BrokerId"),
						Restrictions.like("broker.Name", "%" + param.getKeyword() + "%"));

				Criterion d = Restrictions.like("OurRef", "%" + param.getKeyword() + "%");
				Criterion e = Restrictions.like("Comments", "%" + param.getKeyword() + "%");

				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(a);
				disjunction.add(b);
				disjunction.add(c);
				disjunction.add(d);
				disjunction.add(e);
				criteria.add(disjunction);
			}

			// 是否仅列出当前的持仓
			// 品种标识
			if (param.getIsSquared() != null && param.getIsSquared()) {
				criteria.add(Restrictions.eq("IsSquared", false));
			}

			// 品种标识
			if (param.getCommodityId() != null) {
				criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
			}

			// 市场标识
			if (param.getMarketId() != null) {
				criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
			}

			// 头寸类型
			if (!StringUtils.isBlank(param.getForwardType())) {
				criteria.add(Restrictions.eq("ForwardType", param.getForwardType()));
			}

			// 开始日期，和PromptDate比较
			if (param.getStartDate() != null) {
				criteria.add(Restrictions.ge("PromptDate", param.getStartDate()));
			}

			// 结束日期，和PromptDate比较
			if (param.getEndDate() != null) {
				criteria.add(Restrictions.le("PromptDate", param.getEndDate()));
			}

			// true = 落入当前计价期的, false = 点价期外
			if (param.getAverageAtCurrentDuration() != null) {
				if (param.getAverageAtCurrentDuration() == true) {
					criteria.add(Restrictions.ge("AverageStartDate", today));
					criteria.add(Restrictions.le("AverageEndDate", today));
				} else {
					criteria.add(Restrictions.le("AverageStartDate", today));
					criteria.add(Restrictions.ge("AverageEndDate", today));
				}
			}

			// true = 包括已计价和未计价,false只包含未计价
			if (param.getIsPriced() != null && param.getIsPriced() == true) {
				criteria.add(Restrictions.eq("IsPriced", false));
			}

			if (param.getIsAvailable4Allocate2Lot() != null && param.getIsAvailable4Allocate2Lot()) {
				criteria.add(Restrictions.isNull("LotId"));
			}

			criteria.createAlias("Market", "market", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("market.Code", "SFE"));

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Position> positions = positionService.Positions(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());

			positions = commonService.SimplifyDataPositionList(positions);
			if (positions != null && positions.size() > 0) {
				Map<String, BigDecimal> sfePrice = new HashMap<>();
				for (Position m2m : positions) {
					// 根据市场，品种，到期日得到M2MPrice
					/*
					 * if (m2m.getMarketCode().toUpperCase().equals("LME")) {
					 * Reuter reuterPrice =
					 * positionService.GetReuterPrice(m2m.getCommodityId(),
					 * m2m.getTradeDate(), m2m.getPromptDate()).getData(); if
					 * (reuterPrice != null) {
					 * m2m.setM2MPrice(reuterPrice.getPrice());
					 * m2m.setM2MAmount(m2m.getQuantityUnSquared().multiply(m2m.
					 * getM2MPrice())); // 未平数量 // * // M2MPrice } else //
					 * 未找到路透数据使用-1表示 { m2m.setM2MPrice(new BigDecimal(-1));
					 * m2m.setM2MAmount(new BigDecimal(-1)); // 未平数量 * M2MPrice
					 * } } else
					 */
					if (m2m.getMarketCode().toUpperCase().equals("SFE")) {
						/**
						 * 获取期货行情
						 */
						if (!sfePrice.containsKey(m2m.getCommodityCode())) {
							commonService.quotationPrice(m2m.getCommodityCode(), sfePrice);
						}
						BigDecimal lastPrice = sfePrice.get(m2m.getCommodityCode());
						if (sfePrice.containsKey(m2m.getCommodityCode())) {
							m2m.setM2MPrice(lastPrice);
							m2m.setM2MAmount(DecimalUtil.nullToZero(m2m.getQuantityUnSquared()).multiply(lastPrice)); // 未平数量
							m2m.setPositionProfit(
									lastPrice.subtract(m2m.getOurPrice()).multiply(DecimalUtil.nullToZero(m2m.getQuantityUnSquared())));// 实时价格-头寸价格=实时利润
						} else {
							ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
							tempVar.setSuccess(true);
							tempVar.setTotal(0);
							tempVar.setData(new ArrayList<Position>());
							tempVar.setMessage("获取行情失败.");
							return tempVar;
						}

					}
				}

				ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
				tempVar.setSuccess(true);
				tempVar.setTotal(total.getTotal());
				tempVar.setData(positions);
				return tempVar;
			} else {
				ActionResult<List<Position>> tempVar2 = new ActionResult<List<Position>>();
				tempVar2.setSuccess(true);
				tempVar2.setTotal(0);
				tempVar2.setData(new ArrayList<Position>());
				return tempVar2;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("getM2MPrice")
	@ResponseBody
	public ActionResult<Reuter> getM2MPrice(HttpServletRequest request, @RequestBody M2MParams param) {
		try {
			if (param.getTradeDate() == null) {
				ActionResult<Reuter> tempVar = new ActionResult<>();
				tempVar.setSuccess(false);
				tempVar.setMessage("交易日期不能为空");
				return tempVar;
			}
			if (param.getPromptDate() == null) {
				ActionResult<Reuter> tempVar2 = new ActionResult<>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("到期日不能为空");
				return tempVar2;
			}
			if (param.getCommodityId() == null) {
				ActionResult<Reuter> tempVar3 = new ActionResult<>();
				tempVar3.setSuccess(false);
				tempVar3.setMessage("商品品种不能为空");
				return tempVar3;
			}
			return positionService.GetReuterPrice(param.getCommodityId(), param.getTradeDate(), param.getPromptDate());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
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
	public ActionResult<Position> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return positionService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("CreateVirtual")
	@ResponseBody
	public ActionResult<String> CreateVirtual(HttpServletRequest request, @RequestBody String positionId) {
		try {

			return positionService.CreateVirtual(positionId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("CreateVirtual2")
	@ResponseBody
	public ActionResult<String> CreateVirtual2(HttpServletRequest request, @RequestBody Position position) {
		try {
			return positionService.CreateVirtual(position);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("GetVirtualSwapPositionByCounterpartId")
	@ResponseBody
	public ActionResult<List<Position>> GetVirtualSwapPositionByCounterpartId(HttpServletRequest request,
			@RequestBody String counterpartId) {
		try {
			List<Position> positions = positionService.GetVirtualSwapPositionByCounterpartId(counterpartId);

			ActionResult<List<Position>> tempVar = new ActionResult<List<Position>>();
			tempVar.setSuccess(true);
			tempVar.setData(positions);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * @param positions
	 * @return
	 */
	@RequestMapping("SaveVirtualSwapPosition")
	@ResponseBody
	public ActionResult<String> SaveVirtualSwapPosition(HttpServletRequest request,
			@RequestBody List<Position> positions) {
		try {

			if (positions == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("提交的数据不能为空！");
				return tempVar;
			}

			for (Position position : positions) {
				if (position.getId() != null) {
					position.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
				} else {
					position.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
				}
			}

			return positionService.SaveVirtualSwapPosition(positions);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param position
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Position position) {
		try {
			if (position.getId() != null) {
				position.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				position.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			ActionResult<Position> result = positionService.Save(position);
			return new ActionResult<>(result.isSuccess(), result.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	@RequestMapping("SaveReturnPosition")
	@ResponseBody
	public ActionResult<Position4Broker> SaveReturnPosition(HttpServletRequest request, @RequestBody Position position) {
		try {
			if (position.getId() != null) {
				position.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				position.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			ActionResult<Position> result = positionService.Save(position);
			if(result.isSuccess())
			{
				String brokerId = result.getData().getBrokerId();
				return position4BrokerService.GetById(brokerId);
			}
			
		    return new ActionResult<>(result.isSuccess(), result.getMessage());
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage(),null);
		}
	}

	/**
	 * 保存头寸与批次关系
	 * 
	 * @param positionList
	 * @return
	 */
	@RequestMapping("Position2Lot")
	@ResponseBody
	public ActionResult<String> Position2Lot(HttpServletRequest request,
			@RequestBody java.util.List<Position> positionList) {
		try {

			logger.info("----------------Position2Lot : position list:" + positionList.size());

			return positionService.Position2Lot(positionList);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
	}

	/**
	 * 取消分配到现货
	 * 
	 * @param position
	 * @return
	 */
	@RequestMapping("UnPosition2Lot")
	@ResponseBody
	public ActionResult<String> UnPosition2Lot(HttpServletRequest request, @RequestBody Position position) {
		try {
			if (position.getId() != null) {
				position.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				position.setUpdatedAt(new Date());
			} else {
				ActionResult<String> tempVar = new ActionResult<>();
				tempVar.setSuccess(false);
				tempVar.setMessage("SaveFail");
				return tempVar;
			}
			return positionService.UnPosition2Lot(position);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
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
			return positionService.Delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 根据id更新均价头寸的价格
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("UpdateAveragePositionById")
	@ResponseBody
	public ActionResult<Position> UpdateAveragePositionById(HttpServletRequest request, @RequestBody String id) {
		try {
			positionService.UpdateAveragePositionById(id);
			ActionResult<Position> tempVar = new ActionResult<Position>();
			tempVar.setSuccess(true);
			tempVar.setStatus(ActionStatus.SUCCESS);
			tempVar.setMessage(MessageCtrm.SaveSuccess);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 更新均价头寸的价格
	 * 
	 * @param position
	 * @return
	 */
	@RequestMapping("UpdateAveragePosition")
	@ResponseBody
	public ActionResult<Position> UpdateAveragePosition(HttpServletRequest request, @RequestBody Position position) {
		try {
			if (position.getId() != null) {
				positionService.UpdateAveragePosition(position);
			}
			ActionResult<Position> tempVar = new ActionResult<Position>();
			tempVar.setSuccess(true);
			tempVar.setStatus(ActionStatus.SUCCESS);
			tempVar.setMessage(MessageCtrm.SaveSuccess);
			tempVar.setData(position);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 导入LME头寸：集合方式。从Excel模板文件中读取、格式化成List对象
	 * 
	 * @param lmes
	 * @return
	 */
	@RequestMapping("ImportLMEsViaList")
	@ResponseBody
	public ActionResult<String> ImportLMEsViaList(HttpServletRequest request, @RequestBody List<Position> lmes) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			String userName = LoginHelper.GetLoginInfo(request).getName();
			return positionService.ImportLMEsViaList(lmes, userId, userName);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 导入LME头寸：流文件方式
	 * 
	 * @param stream
	 * @return
	 */
	@RequestMapping("ImportLMEsViaStream")
	@ResponseBody
	public ActionResult<Position> ImportLMEsViaStream(HttpServletRequest request, @RequestBody InputStream stream)
			throws Exception {
		try {

			positionService.ImportLMEsViaStream(stream);
			ActionResult<Position> tempVar = new ActionResult<Position>();
			tempVar.setSuccess(true);
			tempVar.setStatus(ActionStatus.SUCCESS);
			tempVar.setMessage(MessageCtrm.SaveSuccess);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 业务说明在SERVICE层
	 * 
	 * @param cpSplitPosition
	 * @return
	 */
	@RequestMapping("SplitPosition")
	@ResponseBody
	public ActionResult<String> SplitPosition(HttpServletRequest request,
			@RequestBody CpSplitPosition cpSplitPosition) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			return positionService.SplitPosition(cpSplitPosition, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("PositionsByContractId")
	@ResponseBody
	public ActionResult<List<Position>> PositionsByContractId(HttpServletRequest request,
			@RequestBody String contractId) {
		try {
			return positionService.PositionsByContractId(contractId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("PositionsByLotId")
	@ResponseBody
	public ActionResult<List<Position>> PositionsByLotId(HttpServletRequest request, @RequestBody String id) {
		try {

			return positionService.PositionsByLotId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("PositionsBothWayByInvoiceId")
	@ResponseBody
	public ActionResult<List<Position>> PositionsBothWayByInvoiceId(HttpServletRequest request,
			@RequestBody String id) {
		try {

			return positionService.PositionsBothWayByInvoiceId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("GetCounterPartById")
	@ResponseBody
	public ActionResult<Position> GetCounterPartById(HttpServletRequest request, @RequestBody String id) {
		try {

			return positionService.GetCounterPartById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("GetCarryPostionsById")
	@ResponseBody
	public ActionResult<List<Position>> GetCarryPostionsById(HttpServletRequest request, @RequestBody String id) {
		try {

			return positionService.GetCarryPostionsById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 投机头寸风险警示
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("UndistributePositionRisk")
	@ResponseBody
	public ActionResult<List<Position>> UndistributePositionRisk(HttpServletRequest request,
			@RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}
			return positionService.UndistributePositionRisk(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	/**
	 * 套利
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("Arbitrage")
	@ResponseBody
	public ActionResult<List<Position>> Arbitrage(HttpServletRequest request,
			@RequestBody Position position) {
		try {
			return positionService.Arbitrage(position);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
}
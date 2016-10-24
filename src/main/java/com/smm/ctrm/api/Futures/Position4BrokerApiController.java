
package com.smm.ctrm.api.Futures;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.PositionDelivery;
import com.smm.ctrm.domain.apiClient.Position4BrokerParams;
import com.smm.ctrm.domain.apiClient.PositionParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/Position4Broker/")
public class Position4BrokerApiController {

	private static final Logger logger = Logger.getLogger(Position4BrokerApiController.class);

	@Resource
	private Position4BrokerService position4BrokerService;

	@Resource
	private PositionService positionService;

	@Resource
	private LotService lotService;

	@Resource
	private CommonService commonService;

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Position4Broker>> Pager(HttpServletRequest request, @RequestBody PositionParams param) {
		try {
			if (param == null) {
				param = new PositionParams();
			}
			Date today = new Date();

			Criteria criteria = position4BrokerService.GetCriteria();
			// 查询权限
			String userId = LoginHelper.GetLoginInfo().UserId;
			criteria = commonService.AddPermission(userId, criteria, "CreatedId");
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

			// 经纪商标识
			if (param.getBrokerId() != null) {
				criteria.add(Restrictions.eq("BrokerId", param.getBrokerId()));
			}

			// 头寸类型
			if (!StringUtils.isBlank(param.getForwardType())) {
				criteria.add(Restrictions.eq("ForwardType", param.getForwardType()));
			}
			// 开始日期，和TradeDate比较
			if (param.getTradeStartDate() != null) {
				criteria.add(Restrictions.ge("TradeDate", param.getTradeStartDate()));
			}
			// 结束日期，和TradeDate比较
			if (param.getTradeEndDate() != null) {
				criteria.add(Restrictions.lt("TradeDate", param.getTradeEndDate()));
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

			RefUtil total = new RefUtil();
			List<Position4Broker> position4Brokers = position4BrokerService.Positions(criteria, param.getPageSize(),
					param.getPageIndex(), total, param.getSortBy(), param.getOrderBy());

			position4Brokers = commonService.SimplifyDataPosition4BrokerList(position4Brokers);
			Map<String, List<Position>> groupPositions = new HashMap<>();
			List<Position> allPositions = positionService.getPositionListByPosition4BrokerId(
					position4Brokers.stream().map(x -> x.getId()).collect(Collectors.toList()));
			if (allPositions.size() > 0) {
				groupPositions = allPositions.stream().collect(Collectors.groupingBy(Position::getPosition4BrokerId));
			}
			
			for(Position4Broker broker : position4Brokers) {
				broker.setPositionList(commonService.SimplifyDataPositionList(groupPositions.get(broker.getId())));
			}

			ActionResult<List<Position4Broker>> tempVar = new ActionResult<List<Position4Broker>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(position4Brokers);
			return tempVar;
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<List<Position4Broker>> tempVar2 = new ActionResult<List<Position4Broker>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
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
	public ActionResult<Position4Broker> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return position4BrokerService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Position4Broker> tempVar = new ActionResult<Position4Broker>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
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
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Position4Broker position) {
		try {
			if (position.getId() != null) {
				position.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				position.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			return position4BrokerService.Save(position);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, MessageCtrm.SaveFaile);
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
			return position4BrokerService.Delete(id);
		} catch (Exception ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("GenerateBrokerPosition")
	@ResponseBody
	public ActionResult<String> GenerateBrokerPosition(HttpServletRequest request) {
		try {
			return position4BrokerService.GenerateBrokerPosition();
		} catch (Exception ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Square")
	@ResponseBody
	public ActionResult<String> Square(HttpServletRequest request) {
		try {
			return position4BrokerService.Square();
		} catch (Exception ex) {

			ex.printStackTrace();

			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	@RequestMapping("PosDelivery")
	@ResponseBody
	public ActionResult<String> PositionDelivery(HttpServletRequest request,@RequestBody Position4BrokerParams pos4BParams) {
		try {
			return position4BrokerService.posDelivery(pos4BParams);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}
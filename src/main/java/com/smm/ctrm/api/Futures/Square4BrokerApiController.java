
package com.smm.ctrm.api.Futures;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.Square4BrokerService;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.Square4Broker;
import com.smm.ctrm.domain.apiClient.PositionParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/Square4Broker")
public class Square4BrokerApiController {

	@Resource
	private Square4BrokerService square4BrokerService;

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
	public ActionResult<List<Square4Broker>> Pager(HttpServletRequest request, @RequestBody PositionParams param) {
		if (param == null) {
			param = new PositionParams();
		}
		Criteria criteria = square4BrokerService.GetCriteria();
		//查询权限
		String userId=LoginHelper.GetLoginInfo().UserId;
		List<String> usersid = commonService.GetUserPermissionByUser(userId);
		List<String>  ids = commonService.GetSquare4BrokenIdByUsersId(usersid);	
		if(ids!=null&&ids.size()>0){
			HashSet<String> hs = new HashSet<String>(ids);
			criteria.add(Restrictions.in("Id", hs));
		}
		
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("PromptDate", param.getStartDate()));
		}
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("PromptDate", param.getEndDate()));
		}

		criteria.createAlias("Long", "long", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Short", "short", JoinType.LEFT_OUTER_JOIN);

		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			Criterion a = Restrictions.and(Restrictions.isNotNull("LongId"),
					Restrictions.like("long.OurRef", "%" + param.getKeyword() + "%"));
			Criterion b = Restrictions.and(Restrictions.isNotNull("ShortId"),
					Restrictions.like("short.OurRef", "%" + param.getKeyword() + "%"));

			criteria.add(Restrictions.or(a, b));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		List<Square4Broker> squares = square4BrokerService.PositionsSquared(criteria, param.getPageSize(),
				param.getPageIndex(), total, param.getSortBy(), param.getOrderBy());

		squares = commonService.SimplifyDataSquare4BrokerList(squares);

		ActionResult<List<Square4Broker>> tempVar = new ActionResult<List<Square4Broker>>();
		tempVar.setData(squares);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Square4Broker> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return square4BrokerService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Square4Broker> tempVar = new ActionResult<Square4Broker>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param square
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult Save(HttpServletRequest request, @RequestBody Square4Broker square) {
		try {
			if (square.getId() != null) {
				square.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				square.setUpdatedAt(new Date());
			} else {
				square.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				square.setCreatedAt(new Date());
			}
			return square4BrokerService.Save(square);
		} catch (RuntimeException ex) {
			ActionResult<Square4Broker> tempVar = new ActionResult<Square4Broker>();
			tempVar.setSuccess(false);
			tempVar.setData(square);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
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
			return square4BrokerService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("SqaureMannually")
	@ResponseBody
	public ActionResult<String> SqaureMannually(HttpServletRequest request, @RequestBody List<Position4Broker> longs,
			@RequestBody List<Position4Broker> shorts) {
		return square4BrokerService.SqaureMannually(longs, shorts);
	}

	@RequestMapping("SqaureAutomatically")
	@ResponseBody
	public ActionResult<String> SqaureAutomatically(HttpServletRequest request) {
		return square4BrokerService.SqaureAutomatically();
	}
}

package com.smm.ctrm.api.Maintain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.CalendarService;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.apiClient.CalendarParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/Calendar/")
public class CalendarApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private CalendarService calendarService;

	@Resource
	private CommonService commonService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Calendars")
	@ResponseBody
	public ActionResult<List<Calendar>> Calendars(HttpServletRequest request) {
		try {
			return new ActionResult<>(true, "", calendarService.Calendars());
		} catch (Exception ex) {
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
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Calendar> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return calendarService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param calendar
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Calendar calendar) {
		try {
			return calendarService.Save(calendar);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
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
			return calendarService.Delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
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
	public ActionResult<List<Calendar>> Pager(HttpServletRequest request, @RequestBody CalendarParams param) {
		try {
			if (param == null) {
				param = new CalendarParams();
			}
			Criteria criteria = calendarService.GetCriteria(); // 市场品种标识
			if (param.getMarketId() != null) {
				criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
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
			return calendarService.GetPager(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(),
					param.getOrderBy(), total);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}
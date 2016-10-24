



package com.smm.ctrm.api.Maintain;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.FetchCalendarService;
import com.smm.ctrm.domain.Maintain.FetchCalendar;
import com.smm.ctrm.domain.Maintain.VmFetchCalendar;
import com.smm.ctrm.domain.apiClient.CalendarParams;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Maintain/FetchCalendar/")
public class FetchCalendarApiController {
	
	@Resource
	private FetchCalendarService fetchCalendarService;

	@Resource
	private CommonService commonService;

	

	/**
	 *  根据现有的市场行情数据，生成全部的交易日历
	 * @param calendarParams
	 * @return
	 */
	public ActionResult<String> GenerateCalendars(HttpServletRequest request, @RequestBody CalendarParams calendarParams) {

		if (calendarParams == null) {
			calendarParams = new CalendarParams();
		}
		fetchCalendarService.GenerateLmbCalendar(calendarParams.getStartDate(), calendarParams.getEndDate()); // LMB
		fetchCalendarService.GenerateDsmeCalendar(calendarParams.getStartDate(), calendarParams.getEndDate()); // 国内市场
		fetchCalendarService.GenerateLmeCalendar(calendarParams.getStartDate(), calendarParams.getEndDate()); // LME
		fetchCalendarService.GenerateSfeCalendar(calendarParams.getStartDate(), calendarParams.getEndDate()); // SFE
		/// #endregion

		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setMessage("根据现有的市场行情数据，生成了全部的交易日历。");
		return tempVar;

	}

	/**
	 * 根据tradeDate取得VmFetchCalendar
	 * 
	 * @param tradeDate
	 * @return
	 */
	@RequestMapping("GetVmById")
	@ResponseBody
	public ActionResult<VmFetchCalendar> GetVmById(HttpServletRequest request, @RequestBody Date tradeDate) {
		try {
			return fetchCalendarService.GetVmById(tradeDate);
		} catch (Exception ex) {
			ActionResult<VmFetchCalendar> tempVar = new ActionResult<VmFetchCalendar>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
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
	public ActionResult<FetchCalendar> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return fetchCalendarService.GetById(id);
		} catch (Exception ex) {
			ActionResult<FetchCalendar> tempVar = new ActionResult<FetchCalendar>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存： VmFetchCalendar
	 * 
	 * @param vm
	 * @return
	 */
	@RequestMapping("")
	@ResponseBody
	public ActionResult<String> SaveVm(HttpServletRequest request, @RequestBody VmFetchCalendar vm) {
		try {
			return fetchCalendarService.SaveVm(vm);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存: FetchCalendar
	 * 
	 * @param calendar
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody FetchCalendar calendar) {
		try {
			if (calendar.getId()!=null) {
				calendar.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				calendar.setUpdatedAt(new Date());
			} else {
				calendar.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				calendar.setCreatedAt(new Date());
			}
			return fetchCalendarService.Save(calendar);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
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
			return fetchCalendarService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
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
	public ActionResult Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		/*int total;
		if (param == null) {
			param = new QuotationParams();
		}
		// typing in Java:
		var criteria = fetchCalendarService.GetCriteria();
		// 市场品种标识
		if (param.MarketId != null) {
			criteria.Add(Restrictions.Eq("MarketId", param.MarketId));
		}
		// 开始日期
		if (param.StartDate != null && param.StartDate != java.util.Date.getMinValue()) {
			criteria.Add(Restrictions.Ge("TradeDate", param.StartDate));
		}
		// 结束日期
		if (param.EndDate != null && param.EndDate != java.util.Date.getMinValue()) {
			criteria.Add(Restrictions.Le("TradeDate", param.EndDate));
		}

		param.SortBy = getCommonService().FormatSortBy(param.SortBy);

		RefObject<Integer> tempRef_total = new RefObject<Integer>(total);
		// typing in Java:
		var calendars = fetchCalendarService.FetchCalendars(criteria, param.PageSize, param.PageIndex,
				tempRef_total, param.SortBy, param.OrderBy);
		total = tempRef_total.argvalue;

		ActionResult<java.util.List<FetchCalendar>> tempVar = new ActionResult<java.util.List<FetchCalendar>>();
		tempVar.Data = calendars;
		tempVar.Total = total;
		tempVar.setSuccess(true);
		return tempVar;*/
		return null;
	}
}
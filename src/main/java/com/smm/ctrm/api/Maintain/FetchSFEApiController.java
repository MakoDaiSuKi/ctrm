



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
import com.smm.ctrm.bo.Maintain.FetchSFEService;
import com.smm.ctrm.domain.Maintain.FetchSFE;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Maintain/FetchSFE/")
public class FetchSFEApiController {
	
	@Resource
	private FetchSFEService fetchSFEService;

	@Resource
	private CommonService commonService;


	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<FetchSFE> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return fetchSFEService.GetById(id);
		} catch (Exception ex) {
			ActionResult<FetchSFE> tempVar = new ActionResult<FetchSFE>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param fetchSFE
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody FetchSFE fetchSFE) {
		try {
			if (fetchSFE.getId()!=null) {
				fetchSFE.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				fetchSFE.setUpdatedAt(new Date());
			} else {
				fetchSFE.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				fetchSFE.setCreatedAt(new Date());
			}
			return fetchSFEService.Save(fetchSFE);
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
			return fetchSFEService.Delete(id);
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
	@RequestMapping("")
	@ResponseBody
	public ActionResult Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		/*int total;
		if (param == null) {
			param = new QuotationParams();
		}
		// typing in Java:
		var criteria = fetchSFEService.GetCriteria();
		// 市场品种标识
		if (param.MarketId != null) {
			criteria.Add(Restrictions.Eq("MarketId", param.MarketId));
		}
		// 品种标识
		if (param.CommodityId != null) {
			criteria.Add(Restrictions.Eq("CommodityId", param.CommodityId));
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
		var sfes = fetchSFEService.FetchSFEs(criteria, param.PageSize, param.PageIndex, tempRef_total,
				param.SortBy, param.OrderBy);
		total = tempRef_total.argvalue;
		ActionResult<java.util.List<FetchSFE>> tempVar = new ActionResult<java.util.List<FetchSFE>>();
		tempVar.Data = sfes;
		tempVar.Total = total;
		tempVar.Success = true;
		return tempVar;*/
		return null;
	}
}
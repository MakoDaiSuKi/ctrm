



package com.smm.ctrm.api.Maintain;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.FetchDSMEService;
import com.smm.ctrm.domain.Maintain.FetchDSME;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/FetchDSME/")
public class FetchDSMEApiController {
	
	@Resource
	private FetchDSMEService fetchDSMEService;

	@Resource
	private CommonService commonService;


	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		if (param == null) {
			param = new QuotationParams();
		}
		Criteria criteria = fetchDSMEService.GetCriteria();
		// 市场品种标识
		if (param.getMarketId() != null) {
			criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
		}
		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}
		// 开始日期
		if (param.getStartDate() != null ) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null ) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total=new RefUtil();
		
		List<FetchDSME> domestics = fetchDSMEService.FetchDsmEs(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());

		ActionResult<List<FetchDSME>> tempVar = new ActionResult<List<FetchDSME>>();
		tempVar.setData(domestics);
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
	@RequestMapping("")
	@ResponseBody
	public ActionResult<FetchDSME> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return fetchDSMEService.GetById(id);
		} catch (Exception ex) {
			ActionResult<FetchDSME> tempVar = new ActionResult<FetchDSME>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param fetchDSME
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody FetchDSME fetchDSME) {
		try {
			if (fetchDSME.getId()!=null) {
				fetchDSME.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				fetchDSME.setUpdatedAt(new Date());
			} else {
				fetchDSME.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				fetchDSME.setUpdatedAt(new Date());
			}
			return fetchDSMEService.Save(fetchDSME);
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
			return fetchDSMEService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
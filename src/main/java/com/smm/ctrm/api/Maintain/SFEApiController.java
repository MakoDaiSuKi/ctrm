



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
import com.smm.ctrm.bo.Maintain.SFEService;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/SFE/")
public class SFEApiController {
	
	@Resource
	private SFEService sfeService;

	@Resource
	private CommonService commonService;


	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("SFEs")
	@ResponseBody
	public ActionResult<List<SFE>> SFEs(HttpServletRequest request) {
		// typing in Java:
		List<SFE> sfes = sfeService.SFEs();
		ActionResult<List<SFE>> tempVar = new ActionResult<List<SFE>>();
		tempVar.setData(sfes);
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
	public ActionResult GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return sfeService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param sfe
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<SFE> Save(HttpServletRequest request, @RequestBody SFE sfe) {
		try {
			if (sfe.getId()!=null) {
				sfe.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				sfe.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return sfeService.Save(sfe);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			ActionResult<SFE> tempVar = new ActionResult<SFE>();
			tempVar.setSuccess(false);
			tempVar.setData(sfe);
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
			return sfeService.Delete(id);
		} catch (RuntimeException ex) {
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
	public ActionResult<List<SFE>> Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		try {
			
		
		if (param == null) {
			param = new QuotationParams();
		}
		// typing in Java:
		Criteria criteria = sfeService.GetCriteria();
		// 市场品种标识
		if (param.getMarketId() != null) {
			criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
		}
		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}
		// 开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate() ));
		}
		// 结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		RefUtil total=new RefUtil();
		// typing in Java:
		List<SFE> storages = sfeService.SFEs(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());
		ActionResult<List<SFE>> tempVar = new ActionResult<List<SFE>>();
		tempVar.setData(storages);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);;
		return tempVar;
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<List<SFE>> tempVar = new ActionResult<List<SFE>>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;		}
	}
}
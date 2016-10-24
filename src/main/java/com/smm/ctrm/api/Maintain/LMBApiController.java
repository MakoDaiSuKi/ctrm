



package com.smm.ctrm.api.Maintain;

import java.util.ArrayList;
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
import com.smm.ctrm.bo.Maintain.LMBService;
import com.smm.ctrm.domain.Maintain.LMB;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/LMB/")
public class LMBApiController {
	
	@Resource
	private LMBService LMBService;
	
	@Resource
	private CommonService commonService;

	

	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<LMB>> Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		if (param == null) {
			param = new QuotationParams();
		}
		Criteria criteria = LMBService.GetCriteria();
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
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total=new RefUtil();
		
		List<LMB> domestics = LMBService.LMBs(criteria, param.getPageSize(), param.getPageIndex(), total, param.getSortBy(),
				param.getOrderBy());
		if(domestics == null){
			domestics = new ArrayList<LMB>();
		}

		ActionResult<List<LMB>> tempVar = new ActionResult<List<LMB>>();
		tempVar.setData(domestics);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("")
	@ResponseBody
	public ActionResult<List<LMB>> Domestics(HttpServletRequest request) {
		List<LMB> domestics = LMBService.LMBs();
		ActionResult<List<LMB>> tempVar = new ActionResult<List<LMB>>();
		tempVar.setData(domestics);
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
	public ActionResult<LMB> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return LMBService.GetById(id);
		} catch (Exception ex) {
			ActionResult<LMB> tempVar = new ActionResult<LMB>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param LMB
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<LMB> Save(HttpServletRequest request, @RequestBody LMB LMB) {
		try {
			if (LMB.getId()!=null) {
				LMB.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				LMB.setUpdatedAt(new Date());
			} else {
				LMB.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				LMB.setCreatedAt(new Date());
			}
			return LMBService.Save(LMB);
		} catch (Exception ex) {
			ActionResult<LMB> tempVar = new ActionResult<LMB>();
			tempVar.setSuccess(false);
			tempVar.setData(LMB);
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
			return LMBService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
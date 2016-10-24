



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
import com.smm.ctrm.bo.Maintain.DSMEService;
import com.smm.ctrm.domain.Maintain.DSME;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/DSME")
public class DSMEApiController {
	
	@Resource
	private DSMEService DSMEService;

	@Resource
	private CommonService commonService;


	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<DSME>> Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		
		if (param == null) {
			param = new QuotationParams();
		}
		Criteria criteria = DSMEService.GetCriteria();
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
		
		List<DSME> domestics = DSMEService.Dsmes(criteria, param.getPageSize(), param.getPageIndex(), total, param.getSortBy(),
				param.getOrderBy());

		ActionResult<List<DSME>> tempVar = new ActionResult<List<DSME>>();
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
	@RequestMapping("Domestics")
	@ResponseBody
	public ActionResult<List<DSME>> Domestics(HttpServletRequest request) {
		List<DSME> domestics = DSMEService.Dsmes();
		ActionResult<List<DSME>> tempVar = new ActionResult<List<DSME>>();
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
	public ActionResult<DSME> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return DSMEService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<DSME> tempVar = new ActionResult<DSME>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param dsme
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult Save(HttpServletRequest request, @RequestBody DSME dsme) {
		try {
			if (dsme.getId()!=null) {
				dsme.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				dsme.setUpdatedAt(new Date());
			} else {
				dsme.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				dsme.setCreatedAt(new Date());
			}
			return DSMEService.Save(dsme);
		} catch (Exception ex) {
			ActionResult<DSME> tempVar = new ActionResult<DSME>();
			tempVar.setSuccess(false);
			tempVar.setData(dsme);
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
			return DSMEService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
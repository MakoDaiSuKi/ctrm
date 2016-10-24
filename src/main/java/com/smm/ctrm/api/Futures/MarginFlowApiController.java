
package com.smm.ctrm.api.Futures;

import java.util.Date;
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
import com.smm.ctrm.bo.Futures.MarginFlowService;
import com.smm.ctrm.domain.Physical.MarginFlow;
import com.smm.ctrm.domain.apiClient.MarginFlowParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/MarginFlow/")
public class MarginFlowApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private MarginFlowService marginFlowService;

	@Resource
	private CommonService commonService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("MarginFlows")
	@ResponseBody
	public ActionResult<List<MarginFlow>> MarginFlows(HttpServletRequest request) {
		try {
			return marginFlowService.MarginFlows();
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
	public ActionResult<MarginFlow> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return marginFlowService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody MarginFlow marginFlow) {
		try {
			if (marginFlow.getId() != null) {
				marginFlow.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				marginFlow.setUpdatedAt(new Date());
			} else {
				marginFlow.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				marginFlow.setCreatedAt(new Date());
			}
			return marginFlowService.Save(marginFlow);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE,ex.getMessage());
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return marginFlowService.Delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 分页数据
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<MarginFlow>> Pager(HttpServletRequest request, @RequestBody MarginFlowParams param) {
		try {
			if (param == null) {
				param = new MarginFlowParams();
			}
			Criteria criteria = marginFlowService.GetCriteria();

			if (param.getTradeDate() != null) {
				criteria.add(Restrictions.eq("TradeDate", param.getTradeDate()));
			}

			RefUtil total = new RefUtil();
			List<MarginFlow> exchanges = marginFlowService.Pager(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getOrderBy(), param.getSortBy());

			List<MarginFlow> list = commonService.SimplifyDataMarginFlowList(exchanges);
			ActionResult<List<MarginFlow>> tempVar = new ActionResult<>();
			tempVar.setSuccess(true);
			tempVar.setData(list);
			tempVar.setTotal(total.getTotal());
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
}
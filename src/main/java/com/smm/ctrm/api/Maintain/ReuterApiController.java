



package com.smm.ctrm.api.Maintain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.ReuterService;
import com.smm.ctrm.bo.impl.Maintain.ReuterServiceImpl;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/Reuter/")
public class ReuterApiController {
	
	private static final Logger logger=Logger.getLogger(ReuterApiController.class);
	@Resource
	private ReuterService reuterService;

	@Resource
	private CommonService commonService;


	/**
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Reuter>> Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		try {
			if (param == null) {
				param = new QuotationParams();
			}
			Criteria criteria = reuterService.GetCriteria();

			if (param.getCommodityId() != null &&!"".equals(param.getCommodityId())) {
				criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
			}

			if (param.getStartDate() != null ) {
				criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
			}
			if (param.getEndDate() != null ) {
				criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
			}

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total=new RefUtil();
			List<Reuter> storages = reuterService.Storages(criteria, param.getPageSize(), param.getPageIndex(), total,
					param.getSortBy(), param.getOrderBy());
			ActionResult<List<Reuter>> tempVar = new ActionResult<List<Reuter>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			ActionResult<List<Reuter>> tempVar2 = new ActionResult<List<Reuter>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			tempVar2.setData(null);
			return tempVar2;
		}
	}

	/**
	 * 逐个导入
	 * @param reuter
	 * @return
	 */
	@RequestMapping("Import1By1")
	@ResponseBody
	public ActionResult<String> Import1By1(HttpServletRequest request, @RequestBody Reuter reuter) {
		try {
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			String userName = LoginHelper.GetLoginInfo(request).getName();
			return reuterService.Import1By1(reuter, userId, userName);
			
        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

	}

}
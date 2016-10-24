



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.InstrumentService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.domain.apiClient.InstrumentParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Basis/Instrument/")
public class InstrumentApiController {
	
	@Resource
	private InstrumentService instrumentService;

	@Resource
	private CommonService commonService;


	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Instrument>> Pager(HttpServletRequest request, @RequestBody InstrumentParams param) {
		if (param == null) {
			param = new InstrumentParams();
		}


		//Date today =DateUtil.doSFormatDate(DateUtil.doFormatDate(new Date(), "yyyyMMdd"),"yyyyMMdd");

		Criteria criteria = instrumentService.GetCriteria();

		if (param.getMarketId() != null) {
			criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
		}
		if (param.getForwardType() != null) {
			criteria.add(Restrictions.eq("ForwardType", param.getForwardType()));
		}


		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total=new RefUtil();
		
		List<Instrument> lots = instrumentService.Instruments(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());
		
		lots = commonService.SimplifyDataInstrumentList(lots);
		
		ActionResult<List<Instrument>> tempVar = new ActionResult<List<Instrument>>();
		tempVar.setSuccess(true);
		tempVar.setTotal(total.getTotal());
		tempVar.setData(lots);
		return tempVar;
	}

	@RequestMapping("Instruments")
	@ResponseBody
	public ActionResult<List<Instrument>> Instruments(HttpServletRequest request) {
		return instrumentService.Instruments();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackInstruments")
	@ResponseBody
	public ActionResult<List<Instrument>> BackInstruments(HttpServletRequest request) {
		return instrumentService.BackInstruments();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Instrument> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return instrumentService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Instrument> tempVar = new ActionResult<Instrument>();
			tempVar.setSuccess(false);
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
			return instrumentService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param instrument
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Instrument instrument) {
		try {
			return instrumentService.Save(instrument);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	
}
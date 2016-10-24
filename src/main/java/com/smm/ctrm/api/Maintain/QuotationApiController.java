



package com.smm.ctrm.api.Maintain;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.DSMEService;
import com.smm.ctrm.bo.Maintain.LMEService;
import com.smm.ctrm.bo.Maintain.SFEService;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Maintain/Quotation/")
public class QuotationApiController {
	
	@Resource
	private SFEService SFEService;
	
	@Resource
	private LMEService LMEService;
	
	@Resource
	private DSMEService DSMEService;
	
	@Resource
	private CommonService CommonService;


	/**
	 * 获取指定 市场 + 品种标识 的最新价格
	 * 
	 * @return 字符串 - 在客户端转成decimal类型
	 */
	@RequestMapping("GetM2MPriceByMarketIdAndCommodityId")
	@ResponseBody
	public ActionResult<String> GetM2MPriceByMarketIdAndCommodityId(HttpServletRequest request, @RequestBody String ids) {
		//根据Id判断市场类型，根据不同的市场、调用不同的方法
		//返回 <ActionResult<string>
		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setMessage("逻辑还没有实现");
		return tempVar;
	}

	/**
	 * 返回指定品种的、默认 期货 市场的最新价格
	 * 
	 * @param id
	 *            品种的Id
	 * @return 字符串 - 在客户端转成decimal类型
	 */
	@RequestMapping("GetFuturesM2MPriceByCommodityId")
	@ResponseBody
	public ActionResult<String> GetFuturesM2MPriceByCommodityId(HttpServletRequest request, @RequestBody String id) {

		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setMessage("逻辑还没有实现");
		return tempVar;
	}

	/**
	 * 返回指定品种的、默认 现货 市场的最新价格
	 * 
	 * @param id
	 *            品种的Id
	 * @return 字符串 - 在客户端转成decimal类型
	 */
	@RequestMapping("GetSpotM2MPriceByCommodityId")
	@ResponseBody
	public ActionResult<String> GetSpotM2MPriceByCommodityId(HttpServletRequest request, @RequestBody String id) {

		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setMessage("逻辑还没有实现");
		return tempVar;
	}
}

package com.smm.ctrm.api.Physical;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.HedgeNumberService;
import com.smm.ctrm.domain.Physical.HedgeNumber;
import com.smm.ctrm.domain.apiClient.HedgeNumberParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

/**
 * 
 * @author zengshihua
 *
 */
@Controller
@RequestMapping("api/Physical/HedgeNumber/")
public class HedgeNumberApiController {


	@Resource
	private CommonService commonService;
	
	@Resource
	private HedgeNumberService hedgeNumberService;

	private Logger logger = Logger.getLogger(this.getClass());
	
	
	/**
	 * 获取编号
	 */
	@RequestMapping("GetNo")
	@ResponseBody
	public ActionResult<String> GetNo(HttpServletRequest request, @RequestBody String  hnParams) {
		try {
			return hedgeNumberService.GetNo(hnParams);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SYS_ERROR);
		}
	}
	
	
	/**
	 * 保存
	 * 
	 * @param tip
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<HedgeNumber> Save(HttpServletRequest request, @RequestBody HedgeNumber hedgeNumber) {
		try {
			if (hedgeNumber.getId() != null) {
				hedgeNumber.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				hedgeNumber.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			return hedgeNumberService.Save(hedgeNumber);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
	}
	
	/**
	 * 根据ID获取保值编号信息
	 * 
	 * @param tip
	 * @return
	 */
	@RequestMapping("GetHedgeNumberById")
	@ResponseBody
	public ActionResult<HedgeNumber> GetHedgeNumberById(HttpServletRequest request, @RequestBody String hNId) {
		try {
			return hedgeNumberService.GetHedgeNumberById(hNId);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SYS_ERROR);
		}
	}
	
	/**
	 * 获取保值编号列表
	 * 
	 * @param tip
	 * @return
	 */
	@RequestMapping("GetHedgeNumbers")
	@ResponseBody
	public ActionResult<List<HedgeNumber>> GetHedgeNumbers(HttpServletRequest request, @RequestBody HedgeNumberParams hnParams) {
		try {
			return hedgeNumberService.GetHedgeNumbers(hnParams);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SYS_ERROR);
		}
	}
	
	/**
	 * 删除
	 * 
	 * @param tip
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String hnId) {
		try {
			return hedgeNumberService.Delete(hnId);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.DeleteFaile);
		}
	}
	
}
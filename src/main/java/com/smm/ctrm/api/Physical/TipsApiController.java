
package com.smm.ctrm.api.Physical;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.TipsService;
import com.smm.ctrm.domain.Physical.Tip;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by zhenghao on 2016/4/21.
 */
@Controller
@RequestMapping("api/Physical/Tips/")
public class TipsApiController {

	@Resource
	private TipsService tipsService;

	@Resource
	private CommonService commonService;

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Tip> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return tipsService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Tip> tempVar = new ActionResult<Tip>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("Tips")
	@ResponseBody
	public ActionResult<List<Tip>> Tips(HttpServletRequest request) {
		List<Tip> tips = tipsService.Tips();
		ActionResult<List<Tip>> tempVar = new ActionResult<List<Tip>>();
		tempVar.setSuccess(true);
		tempVar.setData(tips);
		return tempVar;
	}

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return tipsService.Delete(id);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(MessageCtrm.SaveFaile);
			return tempVar;
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
	public ActionResult<Tip> Save(HttpServletRequest request, @RequestBody Tip tip) {
		try {
			if (tip.getId() != null) {
				tip.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				tip.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			return tipsService.Save(tip);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
		}
	}

	/**
	 * 获取指定批次的全部笔记
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("TipsByLotId")
	@ResponseBody
	public ActionResult<List<Tip>> TipsByLotId(HttpServletRequest request, @RequestBody String lotId) {
		try {

			return tipsService.TipsByLotId(lotId);
		} catch (RuntimeException ex) {
			ActionResult<List<Tip>> tempVar = new ActionResult<List<Tip>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
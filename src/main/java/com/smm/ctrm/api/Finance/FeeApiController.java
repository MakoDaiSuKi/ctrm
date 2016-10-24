
package com.smm.ctrm.api.Finance;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.FeeService;
import com.smm.ctrm.domain.Physical.Fee;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;

@Controller
@RequestMapping("api/Finance/Fee/")
public class FeeApiController {

	@Resource
	private FeeService feeService;

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
	public ActionResult<Fee> GetById(@RequestBody String id, HttpServletRequest request) {
		try {

			return feeService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Fee> tempVar = new ActionResult<Fee>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(String id, HttpServletRequest request) {
		try {
			return feeService.Delete(id);
		} catch (Exception ex) {
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
	 * @param fee
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Fee fee) {
		try {
			return feeService.Save(fee);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 获取指定批次的全部发票
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("FeesByLotId")
	@ResponseBody
	public ActionResult<List<Fee>> FeesByLotId(HttpServletRequest request, @RequestBody String id) {
		try {

			return feeService.FeesByLotId(id);
		} catch (Exception ex) {
			ActionResult<List<Fee>> tempVar = new ActionResult<List<Fee>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}
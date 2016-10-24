
package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Logs.CommonLogService;
import com.smm.ctrm.domain.Log.ExceptionLog;
import com.smm.ctrm.domain.apiClient.LogsParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Basis/ExceptionLog/")
public class ExceptionLogApiController {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private CommonLogService commonLogService;

	/**
	 * 分页列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<ExceptionLog>> Pager(HttpServletRequest request, @RequestBody LogsParams param) {
		try {
			if (param == null) {
				param = new LogsParams();
			}
			Criteria criteria = commonLogService.GetCriteria4ExceptionLog();

			RefUtil total = new RefUtil();
			List<ExceptionLog> logs = commonLogService.ExceptionLogs(criteria, param.getPageSize(),
					param.getPageIndex(), total, param.getSortBy(), param.getOrderBy());
			return new ActionResult<>(true, "", logs, total);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 获取单条记录的详细信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("LogById")
	@ResponseBody
	public ActionResult<ExceptionLog> LogById(HttpServletRequest request, @RequestBody String id) {
		try {
			return commonLogService.ExceptionLogById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 一次删除多条记录
	 * 
	 * @param exceptionLogs
	 * @return
	 */
	@RequestMapping("DeleteLogs")
	@ResponseBody
	public ActionResult<String> DeleteLogs(HttpServletRequest request, @RequestBody List<ExceptionLog> exceptionLogs) {
		try {
			return commonLogService.DeleteExceptionLogs(exceptionLogs);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 保存
	 * 
	 * @param exceptionLog
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody ExceptionLog exceptionLog) {
		try {
			exceptionLog.setCreatedAt(new Date());
			exceptionLog.setLevelName("Exception");
			return commonLogService.SaveExceptionLog(exceptionLog);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}
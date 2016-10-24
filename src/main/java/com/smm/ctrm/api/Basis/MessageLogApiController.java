



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Logs.CommonLogService;
import com.smm.ctrm.domain.Log.ExceptionLog;
import com.smm.ctrm.domain.Log.MessageLog;
import com.smm.ctrm.domain.apiClient.LogsParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Basis/MessageLog/")
public class MessageLogApiController {
	
	@Resource
	private CommonLogService commonLogService;

	/**
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<MessageLog>> Pager(HttpServletRequest request, @RequestBody LogsParams param) {
		try {
			if (param == null) {
				param = new LogsParams();
			}
			Criteria criteria = commonLogService.GetCriteria4MessageLog();

			RefUtil total=new RefUtil();
			
			List<MessageLog> logs = commonLogService.MessageLogs(criteria, param.getPageSize(), param.getPageIndex(), total,
					param.getSortBy(), param.getOrderBy());

			ActionResult<List<MessageLog>> tempVar = new ActionResult<List<MessageLog>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(logs);
			return tempVar;
			
		} catch (Exception ex) {
			ActionResult<List<MessageLog>> tempVar2 = new ActionResult<List<MessageLog>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}

	}
	@RequestMapping("LogById")
	@ResponseBody
	public ActionResult<ExceptionLog> LogById(HttpServletRequest request, @RequestBody String id) {
		try {
			return commonLogService.ExceptionLogById(id);
		} catch (Exception ex) {
			ActionResult<ExceptionLog> tempVar = new ActionResult<ExceptionLog>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	/**
	 * param list
	 * @param request
	 * @param messageLogs
	 * @return
	 */
	@RequestMapping("DeleteLogs")
	@ResponseBody
	public ActionResult<String> DeleteLogs(HttpServletRequest request, @RequestBody List<MessageLog> messageLogs) {
		try {
			return commonLogService.DeleteMessageLogs(messageLogs);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	/**
	 * 保存
	 * 
	 * @param log
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<MessageLog> Save(HttpServletRequest request, @RequestBody MessageLog log) {
		log.setCreatedAt(new Date());
		log.setCreaterId(LoginHelper.GetLoginInfo(request).getUserId());
		log.setCreatedBy(LoginHelper.GetLoginInfo(request).getAccount());

		return commonLogService.SaveMessageLog(log);
	}
}
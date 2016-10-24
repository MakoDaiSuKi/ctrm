package com.smm.ctrm.bo.Logs;

import com.smm.ctrm.domain.Log.ExceptionLog;
import com.smm.ctrm.domain.Log.MessageLog;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

import org.hibernate.Criteria;

import java.util.List;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
public interface CommonLogService {

    ActionResult<String> SaveExceptionLog(ExceptionLog exceptionLog);

    ActionResult<MessageLog> SaveMessageLog(MessageLog messageLog);


    ActionResult<ExceptionLog> ExceptionLogById(String id);


    ActionResult<MessageLog> MessageLogById(String id);


    List<ExceptionLog> ExceptionLogs(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);


    List<MessageLog> MessageLogs(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);


    ActionResult<String> DeleteExceptionLogs(List<ExceptionLog> exceptionLogs);


    ActionResult<String> DeleteMessageLogs(List<MessageLog> messageLogs);

	Criteria GetCriteria4MessageLog();

	Criteria GetCriteria4ExceptionLog();


}

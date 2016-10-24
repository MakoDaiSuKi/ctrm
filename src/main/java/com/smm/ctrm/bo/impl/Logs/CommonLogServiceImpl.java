package com.smm.ctrm.bo.impl.Logs;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Logs.CommonLogService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Log.ExceptionLog;
import com.smm.ctrm.domain.Log.MessageLog;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/26.
 *
 */
@Service
public class CommonLogServiceImpl implements CommonLogService {

	@Autowired
	private HibernateRepository<MessageLog> repository;

	@Autowired
	private HibernateRepository<ExceptionLog> exceptionLogepository;

	@Override
	public ActionResult<String> SaveExceptionLog(ExceptionLog exceptionLog) {
		this.exceptionLogepository.SaveOrUpdate(exceptionLog);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<MessageLog> SaveMessageLog(MessageLog messageLog) {
		this.repository.SaveOrUpdate(messageLog);
		return new ActionResult<>(true, "", messageLog);
	}

	@Override
	public ActionResult<ExceptionLog> ExceptionLogById(String id) {
		return new ActionResult<>(true, "", exceptionLogepository.getOneById(id, ExceptionLog.class));
	}

	@Override
	public ActionResult<MessageLog> MessageLogById(String id) {
		return new ActionResult<>(true, "", repository.getOneById(id, MessageLog.class));
	}

	@Override
	public List<ExceptionLog> ExceptionLogs(Criteria criteria, int pageSize, int pageIndex, RefUtil total,
			String orderBy, String orderSort) {
		return this.exceptionLogepository.GetPage(criteria, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}

	@Override
	public List<MessageLog> MessageLogs(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String orderSort) {
		return this.repository.GetPage(criteria, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}

	@Override
	public ActionResult<String> DeleteExceptionLogs(List<ExceptionLog> exceptionLogs) {
		if (exceptionLogs != null && exceptionLogs.size() > 0) {
			exceptionLogs.forEach(e -> this.exceptionLogepository.PhysicsDelete(e.getId(), ExceptionLog.class));
		}
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<String> DeleteMessageLogs(List<MessageLog> messageLogs) {

		if (messageLogs != null && messageLogs.size() > 0) {
			messageLogs.forEach(e -> this.repository.PhysicsDelete(e.getId(), MessageLog.class));
		}
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public Criteria GetCriteria4MessageLog() {
		return this.repository.CreateCriteria(MessageLog.class);
	}

	@Override
	public Criteria GetCriteria4ExceptionLog() {
		return this.exceptionLogepository.CreateCriteria(ExceptionLog.class);
	}
}

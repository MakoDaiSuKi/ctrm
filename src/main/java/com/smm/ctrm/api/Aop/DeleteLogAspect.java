package com.smm.ctrm.api.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Log.MessageLog;
import com.smm.ctrm.util.LoginHelper;

@Component
@Aspect
public class DeleteLogAspect {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	HibernateRepository<MessageLog>  messageLogRepo;
	
	@Pointcut("execution(public * com.smm.ctrm.dao.HibernateRepository.PhysicsDelete(..))")
	public void sleeppoint() {
		System.out.println("Aop");
	}

	@AfterReturning("sleeppoint()")
	public void afterSleep(JoinPoint point) {
		try{
			String entityId;
			Class<?> clazz; 
			if(point.getArgs().length == 2) {
				entityId = (String) point.getArgs()[0];
				clazz = (Class<?>) point.getArgs()[1];
			} else {
				HibernateEntity entity = (HibernateEntity) point.getArgs()[0];
				entityId = entity.getId();
				clazz = entity.getClass();
			}
			saveDeleteMessageLog(entityId, clazz);
		} catch(RuntimeException e) {
			logger.error("生成删除日志时，出错" + e.getMessage(), e);
		}
	}
	
	private void saveDeleteMessageLog(String entityId, Class<?> clazz){
		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		MessageLog msgLog = new MessageLog();
		msgLog.setCreaterId(loginInfo.getUserId());
		msgLog.setCreaterName(loginInfo.getName());
		msgLog.setDataId(entityId);
		msgLog.setClassName(clazz.getName());
		msgLog.setMethodName("delete");
		msgLog.setMessageInfo("删除成功");
		messageLogRepo.Save(msgLog);
	}
}

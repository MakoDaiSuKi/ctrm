package com.smm.ctrm.bo.impl.Logs;

import com.smm.ctrm.bo.Logs.MessageLogService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Log.MessageLog;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hao.zheng on 2016/4/26.
 *
 */
@Service
public class MessageLogServiceImpl implements MessageLogService {


    private static final Logger logger=Logger.getLogger(MessageLogServiceImpl.class);

    @Autowired
    private HibernateRepository<MessageLog> repository;


    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(MessageLog.class);
    }

    @Override
    public void Save(MessageLog messageLog) {

        this.repository.SaveOrUpdate(messageLog);
    }
}

package com.smm.ctrm.bo.Logs;

import com.smm.ctrm.domain.Log.MessageLog;
import org.hibernate.Criteria;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
public interface MessageLogService {


    Criteria GetCriteria();

    void Save(MessageLog messageLog);
}

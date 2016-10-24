package com.smm.ctrm.bo.impl.Report;

import com.smm.ctrm.bo.Report.FuturesReportService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Report.R1;
import com.smm.ctrm.dto.res.ActionResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class FuturesReportServiceImpl implements FuturesReportService {

    private static final Logger logger=Logger.getLogger(FuturesReportServiceImpl.class);

    @Autowired
    private HibernateRepository<Account> repository;


    @Override
    public ActionResult<List<R1>> R1Report() {

        ActionResult<List<R1>> result=new ActionResult<>();

        result.setSuccess(false);
        result.setMessage("还未实现");

        return result;
    }
}

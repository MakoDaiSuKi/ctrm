package com.smm.ctrm.bo.impl.Futures;

import com.smm.ctrm.bo.Futures.PortfolioService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.MarginFlow;
import com.smm.ctrm.domain.Physical.Portfolio;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class PortfolioServiceImpl implements PortfolioService {


    private static final Logger logger=Logger.getLogger(PortfolioServiceImpl.class);

    @Autowired
    private HibernateRepository<Portfolio> repository;


    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(Portfolio.class);
    }

    @Override
    public ActionResult<String> Save(Portfolio portfolio) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(portfolio);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,Portfolio.class);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<Portfolio> GetById(String id) {

        ActionResult<Portfolio> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,Portfolio.class));

        return result;
    }

    @Override
    public List<Portfolio> Portfolios() {

        DetachedCriteria where=DetachedCriteria.forClass(Portfolio.class);
        where.add(Restrictions.eq("IsHidden", false));

        return this.repository.GetQueryable(Portfolio.class).where(where).toList();
    }

    @Override
    public List<Portfolio> Portfolios(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

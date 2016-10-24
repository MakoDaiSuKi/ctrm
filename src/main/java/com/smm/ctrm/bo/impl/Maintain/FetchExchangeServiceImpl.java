package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.FetchExchangeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Maintain.FetchDSME;
import com.smm.ctrm.domain.Maintain.FetchExchange;
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
public class FetchExchangeServiceImpl implements FetchExchangeService {


    private static final Logger logger=Logger.getLogger(FetchExchangeServiceImpl.class);

    @Autowired
    private HibernateRepository<FetchExchange> repository;

    @Override
    public void GetCriteria() {

        this.repository.CreateCriteria(FetchExchange.class);

    }

    @Override
    public ActionResult<String> Save(FetchExchange fetchExchange) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(FetchExchange.class);
            where.add(Restrictions.eq("MarketId", fetchExchange.getMarketId()));
            where.add(Restrictions.eq("TradeDate", fetchExchange.getTradeDate()));
            where.add(Restrictions.ne("Id", fetchExchange.getId()));

            List<FetchExchange> list=this.repository.GetQueryable(FetchExchange.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(fetchExchange);

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

            this.repository.PhysicsDelete(id,FetchExchange.class);

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
    public ActionResult<FetchExchange> GetById(String id) {

        ActionResult<FetchExchange> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,FetchExchange.class));

        return result;
    }

    @Override
    public List<FetchExchange> FetchExchanges(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

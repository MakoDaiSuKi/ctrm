package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.BrokerService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class BrokerServiceImpl implements BrokerService {


    private static final Logger logger=Logger.getLogger(BrokerServiceImpl.class);

    @Autowired
    private HibernateRepository<Broker> repository;


    @Override
    public ActionResult<Broker> Save(Broker broker) {

        ActionResult<Broker> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(broker);

            result.setSuccess(true);
            result.setData(broker);
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

            this.repository.PhysicsDelete(id, Broker.class);

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
    public ActionResult<List<Broker>> Brokers() {

        DetachedCriteria where=DetachedCriteria.forClass(Broker.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Broker> list=this.repository.GetQueryable(Broker.class).where(where).toCacheList();

        ActionResult<List<Broker>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Broker>> BackBrokers() {

        List<Broker> list=this.repository.GetQueryable(Broker.class).toList();

        ActionResult<List<Broker>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Broker> GetById(String id) {


        ActionResult<Broker> result= new ActionResult<>();

        try{

            Broker broker=this.repository.getOneById(id, Broker.class);

            result.setSuccess(true);
            result.setData(broker);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }
}

package com.smm.ctrm.bo.impl.Futures;

import com.smm.ctrm.bo.Futures.MarginService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Physical.Margin;
import com.smm.ctrm.domain.Physical.MarginFlow;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

import org.apache.log4j.Logger;
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
public class MarginServiceImpl implements MarginService {

    private static final Logger logger=Logger.getLogger(MarginServiceImpl.class);

    @Autowired
    private HibernateRepository<Margin> repository;


    @Override
    public ActionResult<String> Save(Margin margin) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(margin);

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

            this.repository.PhysicsDelete(id,Margin.class);

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
    public ActionResult<Margin> GetById(String id) {

        ActionResult<Margin> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,Margin.class));

        return result;
    }

    @Override
    public ActionResult<List<Margin>> Margins() {

        DetachedCriteria where=DetachedCriteria.forClass(Margin.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Margin> list=this.repository.GetQueryable(Margin.class).where(where).toList();

        ActionResult<List<Margin>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

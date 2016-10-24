package com.smm.ctrm.bo.impl.Futures;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Futures.CommissionService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Commission;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CommissionServiceImpl implements CommissionService {


    private static final Logger logger=Logger.getLogger(CommissionServiceImpl.class);

    @Autowired
    private HibernateRepository<Commission> repository;


    @Override
    public ActionResult<Commission> Save(Commission commission) {

        ActionResult<Commission> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(commission);

            result.setSuccess(true);
            result.setData(commission);

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

            this.repository.PhysicsDelete(id,Commission.class);

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
    public ActionResult<Commission> GetById(String id) {

        ActionResult<Commission> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,Commission.class));

        return result;
    }

    @Override
    public ActionResult<List<Commission>> Commissions() {

        DetachedCriteria where=DetachedCriteria.forClass(Commission.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Commission> list=this.repository.GetQueryable(Commission.class).where(where).toList();

        ActionResult<List<Commission>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

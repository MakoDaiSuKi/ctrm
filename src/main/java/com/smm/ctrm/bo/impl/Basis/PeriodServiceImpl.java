package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.PeriodService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Period;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class PeriodServiceImpl implements PeriodService {


    private static final Logger logger=Logger.getLogger(PeriodServiceImpl.class);

    @Autowired
    private HibernateRepository<Period> repository;

    @Override
    public ActionResult<List<Period>> Periods() {

        DetachedCriteria where=DetachedCriteria.forClass(Period.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("IsHidden", false));

        where.add(Restrictions.or(

                Restrictions.eq("",""),
                Restrictions.eq("","")
        ));
        where.add(Restrictions.and(

        ));

        List<Period> list=this.repository.GetQueryable(Period.class).where(where).toList();

        ActionResult<List<Period>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Period>> BackPeriods() {

        List<Period> list=this.repository.GetQueryable(Period.class).OrderBy(Order.desc("Code")).toList();

        ActionResult<List<Period>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Period> Save(Period period) {

        ActionResult<Period> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(period);

            result.setSuccess(true);
            result.setData(period);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id)
    {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Period.class);

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
    public ActionResult<Period> GetById(String id) {

        ActionResult<Period> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Period.class));

        return result;
    }

	@Override
	public ActionResult<List<Period>> Set2Current(Period period) {

        DetachedCriteria where=DetachedCriteria.forClass(Period.class);
        where.add(Restrictions.eq("Id", period.getId()));

        List<Period> list=this.repository.GetQueryable(Period.class).where(where).toList();

        ActionResult<List<Period>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
	}
}

package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.PricingShortcutService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.PricingShortcut;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class PricingShortcutServiceImpl implements PricingShortcutService {


    private static final Logger logger=Logger.getLogger(PricingShortcutServiceImpl.class);

    @Autowired
    private HibernateRepository<PricingShortcut> repository;


    @Override
    public ActionResult<List<PricingShortcut>> PricingShortcuts() {

        DetachedCriteria where=DetachedCriteria.forClass(PricingShortcut.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<PricingShortcut> list=this.repository.GetQueryable(PricingShortcut.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<PricingShortcut>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<PricingShortcut>> BackPricingShortcuts() {

        List<PricingShortcut> list=this.repository.GetQueryable(PricingShortcut.class).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<PricingShortcut>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<PricingShortcut> Save(PricingShortcut org) {

        ActionResult<PricingShortcut> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(org);

            result.setSuccess(true);
            result.setData(org);

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

            this.repository.PhysicsDelete(id, PricingShortcut.class);

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
    public ActionResult<PricingShortcut> GetById(String id) {

        ActionResult<PricingShortcut> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, PricingShortcut.class));

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}


}

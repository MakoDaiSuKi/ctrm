package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.PricingTypeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.PricingShortcut;
import com.smm.ctrm.dto.res.ActionResult;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class PricingTypeServiceImpl implements PricingTypeService {

    private static final Logger logger=Logger.getLogger(PricingTypeServiceImpl.class);

    @Autowired
    private HibernateRepository<PricingShortcut> repository;




    @Override
    public ActionResult<List<PricingShortcut>> PricingTypes() {

        DetachedCriteria where=DetachedCriteria.forClass(PricingShortcut.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<PricingShortcut> list=this.repository.GetQueryable(PricingShortcut.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<PricingShortcut>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<PricingShortcut>> BackPricingTypes() {

        List<PricingShortcut> list=this.repository.GetQueryable(PricingShortcut.class).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<PricingShortcut>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

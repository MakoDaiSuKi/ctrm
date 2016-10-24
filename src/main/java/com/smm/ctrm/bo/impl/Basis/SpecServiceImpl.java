package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.SpecService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class SpecServiceImpl implements SpecService {


    private static final Logger logger=Logger.getLogger(SpecServiceImpl.class);

    @Autowired
    private HibernateRepository<Spec> repository;

    @Override
    public ActionResult<List<Spec>> Specs() {

        DetachedCriteria where=DetachedCriteria.forClass(Spec.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Spec> list=this.repository.GetQueryable(Spec.class).where(where).OrderBy(Order.desc("OrderIndex")).toCacheList();

        ActionResult<List<Spec>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Spec>> BackSpecs() {

        List<Spec> list=this.repository.GetQueryable(Spec.class).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Spec>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Spec>> SpecsByCommodityId(String commodityId) {

        DetachedCriteria where=DetachedCriteria.forClass(Spec.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("CommodityId", commodityId));

        List<Spec> list=this.repository.GetQueryable(Spec.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Spec>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Spec> Save(Spec spec) {

        ActionResult<Spec> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(Spec.class);
            if(StringUtils.isNotBlank(spec.getId())) {
            	where.add(Restrictions.ne("Id", spec.getId()));
            }
            where.add(Restrictions.eq("CommodityId", spec.getCommodityId()));
            where.add(Restrictions.or(Restrictions.eq("Name", spec.getName()),Restrictions.eq("Code",spec.getCode())));

            List<Spec> list=this.repository.GetQueryable(Spec.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(spec);

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

            this.repository.PhysicsDelete(id, Spec.class);

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
    public ActionResult<Spec> GetById(String id) {

        ActionResult<Spec> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Spec.class));

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}


}

package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.ResourceService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger logger=Logger.getLogger(ResourceServiceImpl.class);

    @Autowired
    private HibernateRepository<Resource> repository;

    @Override
    public ActionResult<List<Resource>> Resources() {

        DetachedCriteria where=DetachedCriteria.forClass(Resource.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Resource> list=this.repository.GetQueryable(Resource.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Resource>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Resource> Save(Resource resource) {

        ActionResult<Resource> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(resource);

            result.setSuccess(true);
            result.setData(resource);
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

            this.repository.PhysicsDelete(id, Resource.class);

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
    public ActionResult<Resource> GetById(String id) {

        ActionResult<Resource> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Resource.class));

        return result;
    }

	@Override
	public ActionResult<List<Resource>> Pager(RefUtil total, String orderSort, Criteria param, Integer pageIndex,
			Integer pageSize, String orderBy) {
		return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total);
	}

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}


}

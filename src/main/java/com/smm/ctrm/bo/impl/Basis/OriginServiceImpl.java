package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.OriginService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class OriginServiceImpl implements OriginService {


    private static final Logger logger=Logger.getLogger(OriginServiceImpl.class);

    @Autowired
    private HibernateRepository<Origin> repository;

    @Autowired
    private HibernateRepository<Commodity> commodityRepo;

    @Override
    public ActionResult<List<Origin>> OriginsByCommodityId(String commodityId) {

        DetachedCriteria where=DetachedCriteria.forClass(Origin.class);
        where.add(Restrictions.eq("CommodityId", commodityId));

        List<Origin> list=this.repository.GetQueryable(Origin.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Origin>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Origin>> Origins() {

        DetachedCriteria where=DetachedCriteria.forClass(Origin.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Origin> list=this.repository.GetQueryable(Origin.class).where(where).OrderBy(Order.desc("OrderIndex")).toCacheList();
        addCommodity(list);
        ActionResult<List<Origin>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Origin>> BackOrigins() {

        List<Origin> list=this.repository.GetQueryable(Origin.class).OrderBy(Order.desc("OrderIndex")).toList();
        addCommodity(list);
        ActionResult<List<Origin>> result= new ActionResult<>();
        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Origin> Save(Origin origin) {

        ActionResult<Origin> result= new ActionResult<>();

        try{
        	/**
        	 * 检查重名
        	 */
        	DetachedCriteria dc=DetachedCriteria.forClass(Origin.class);
        	if(StringUtils.isNotBlank(origin.getId())) {
        		dc.add(Restrictions.ne("Id",origin.getId()));
        	}
        	dc.add(Restrictions.eq("CommodityId", origin.getCommodityId()));
        	dc.add(Restrictions.or(Restrictions.eq("Name", origin.getName()),Restrictions.eq("Code", origin.getCode())));
        	
        	Origin obj=this.repository.GetQueryable(Origin.class).where(dc).firstOrDefault();

        	if(obj==null){
        		this.repository.SaveOrUpdate(origin);

                result.setSuccess(true);
                result.setMessage(MessageCtrm.SaveSuccess);
        	}else{
        		result.setSuccess(false);
                result.setMessage(MessageCtrm.DuplicatedName);
        	}
            

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }
    
    private void addCommodity(List<Origin> list) {
    	for(Origin origin : list) {
    		if(StringUtils.isNotBlank(origin.getCommodityId())) {
    			origin.setCommodity(commodityRepo.getOneById(origin.getCommodityId(), Commodity.class));
    		}
    	}
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Origin.class);

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
    public ActionResult<Origin> GetById(String id) {

        ActionResult<Origin> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Origin.class));

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}


}

package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.MarketService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class MarketServiceImpl implements MarketService {

    private static final Logger logger=Logger.getLogger(MarketServiceImpl.class);

    @Autowired
    private HibernateRepository<Market> repository;


    @Override
    public ActionResult<List<Market>> Markets() {

        DetachedCriteria where=DetachedCriteria.forClass(Market.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Market> list=this.repository.GetQueryable(Market.class).where(where).OrderBy(Order.desc("OrderIndex")).toCacheList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Market>> BackMarkets() {

        List<Market> list=this.repository.GetQueryable(Market.class).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Market> Save(Market org) {

        ActionResult<Market> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(Market.class);
            if(StringUtils.isNotBlank(org.getId())) {
            	where.add(Restrictions.ne("Id", org.getId()));
            }
            where.add(Restrictions.or(Restrictions.eq("Name",org.getName()),Restrictions.eq("Code",org.getCode())));

            List<Market> list=this.repository.GetQueryable(Market.class).where(where).toList();

            if(list!=null && list.size()>0){

                result.setSuccess(false);
                result.setMessage(MessageCtrm.DuplicatedName);
                result.setData(org);
                return result;
            }

            //保存数据
            this.repository.SaveOrUpdate(org);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
            result.setData(org);

        }catch (Exception e){

            logger.error(e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(org);
            result.setStatus("error");
        }

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id)
    {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, Market.class);

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
    public ActionResult<Market> GetById(String id) {

        ActionResult<Market> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Market.class));

        return result;
    }



    @Override
    public ActionResult<List<Market>> SpotMarkets() {

        DetachedCriteria where=DetachedCriteria.forClass(Market.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("MarketType", Market.MARK_TYPE_Physical));

        List<Market> list=this.repository.GetQueryable(Market.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    @Override
    public ActionResult<List<Market>> BackSpotMarkets() {

        DetachedCriteria where=DetachedCriteria.forClass(Market.class);
        where.add(Restrictions.eq("MarketType", Market.MARK_TYPE_Physical));

        List<Market> list=this.repository.GetQueryable(Market.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Market>> FuturesMarkets() {

        DetachedCriteria where=DetachedCriteria.forClass(Market.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("MarketType", Market.MARK_TYPE_Futures));

        List<Market> list=this.repository.GetQueryable(Market.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Market>> BackFuturesMarkets() {

        DetachedCriteria where=DetachedCriteria.forClass(Market.class);
        where.add(Restrictions.eq("MarketType", Market.MARK_TYPE_Futures));

        List<Market> list=this.repository.GetQueryable(Market.class).where(where).OrderBy(Order.desc("OrderIndex")).toList();

        ActionResult<List<Market>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}
}

package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.FetchDSMEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Maintain.FetchDSME;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class FetchDSMEServiceImpl implements FetchDSMEService {

    private static final Logger logger=Logger.getLogger(FetchDSMEServiceImpl.class);

    @Autowired
    private HibernateRepository<FetchDSME> repository;


    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(FetchDSME.class);
    }

    @Override
    public ActionResult<String> Save(FetchDSME fetchDSME) {

        ActionResult<String> result= new ActionResult<>();

        try{
            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(FetchDSME.class);
            where.add(Restrictions.eq("MarketId", fetchDSME.getMarketId()));
            where.add(Restrictions.eq("CommodityId", fetchDSME.getCommodityId()));
            where.add(Restrictions.eq("SpecId", fetchDSME.getSpecId()));
            where.add(Restrictions.ne("Id", fetchDSME.getId()));

            List<FetchDSME> list=this.repository.GetQueryable(FetchDSME.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(fetchDSME);

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

            this.repository.PhysicsDelete(id,FetchDSME.class);

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
    public ActionResult<FetchDSME> GetById(String id) {

        ActionResult<FetchDSME> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,FetchDSME.class));

        return result;
    }

    @Override
    public List<FetchDSME> FetchDsmEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

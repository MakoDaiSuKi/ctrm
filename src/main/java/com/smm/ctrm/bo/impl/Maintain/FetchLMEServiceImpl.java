package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.FetchLMEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Maintain.FetchDSME;
import com.smm.ctrm.domain.Maintain.FetchLME;
import com.smm.ctrm.domain.apiClient.Api.FetchExchange;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
public class FetchLMEServiceImpl implements FetchLMEService {

    private static final Logger logger=Logger.getLogger(FetchLMEServiceImpl.class);

    @Autowired
    private HibernateRepository<FetchLME> repository;


    @Override
    public void GetCriteria() {

        this.repository.CreateCriteria(FetchLME.class);
    }

    @Override
    public ActionResult<String> Save(FetchLME fetchLME) {

        ActionResult<String> result= new ActionResult<>();

        try{
            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(FetchLME.class);
            where.add(Restrictions.eq("TradeDate", fetchLME.getTradeDate()));
            where.add(Restrictions.eq("CommodityId", fetchLME.getCommodityId()));
            where.add(Restrictions.ne("Id", fetchLME.getId()));

            List<FetchLME> list=this.repository.GetQueryable(FetchLME.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(fetchLME);

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

            this.repository.PhysicsDelete(id,FetchLME.class);

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
    public ActionResult<FetchLME> GetById(String id) {

        ActionResult<FetchLME> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,FetchLME.class));

        return result;
    }

    @Override
    public List<FetchLME> FetchLMEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

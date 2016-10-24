package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.FetchSFEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Maintain.FetchLME;
import com.smm.ctrm.domain.Maintain.FetchSFE;
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
public class FetchSFEServiceImpl implements FetchSFEService {

    private static final Logger logger=Logger.getLogger(FetchSFEServiceImpl.class);

    @Autowired
    private HibernateRepository<FetchSFE> repository;

    @Override
    public void GetCriteria() {

        this.repository.CreateCriteria(FetchSFE.class);
    }

    @Override
    public ActionResult<String> Save(FetchSFE fetchSFE) {

        ActionResult<String> result= new ActionResult<>();

        try{
            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(FetchSFE.class);
            where.add(Restrictions.eq("TradeDate", fetchSFE.getTradeDate()));
            where.add(Restrictions.eq("CommodityId", fetchSFE.getCommodityId()));
            where.add(Restrictions.ne("Id", fetchSFE.getId()));

            List<FetchSFE> list=this.repository.GetQueryable(FetchSFE.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(fetchSFE);

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

            this.repository.PhysicsDelete(id,FetchSFE.class);

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
    public ActionResult<FetchSFE> GetById(String id) {

        ActionResult<FetchSFE> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,FetchSFE.class));

        return result;
    }

    @Override
    public List<FetchSFE> FetchSFEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

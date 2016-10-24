package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.LMBService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Maintain.FetchSFE;
import com.smm.ctrm.domain.Maintain.LMB;
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
public class LMBServiceImpl implements LMBService {

    private static final Logger logger=Logger.getLogger(LMBServiceImpl.class);

    @Autowired
    private HibernateRepository<LMB> repository;

    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(LMB.class);
    }

    @Override
    public ActionResult<LMB> Save(LMB lmb) {

        ActionResult<LMB> result= new ActionResult<>();

        //检查重复
        DetachedCriteria where=DetachedCriteria.forClass(LMB.class);
        where.add(Restrictions.eq("TradeDate", lmb.getTradeDate()));
        where.add(Restrictions.eq("CommodityId", lmb.getCommodityId()));
        where.add(Restrictions.eq("SpecId", lmb.getSpecId()));
        if(lmb.getId() != null && !"".equals(lmb.getId())) {
            where.add(Restrictions.ne("Id", lmb.getId()));
        }

        List<LMB> list=this.repository.GetQueryable(LMB.class).where(where).toList();

        if(list!=null && list.size()>0) {
//            throw new Exception(MessageCtrm.DuplicatedName);
            result.setSuccess(false);
            result.setMessage(MessageCtrm.DuplicatedName);
            return result;
        }
        this.repository.SaveOrUpdate(lmb);
        result.setSuccess(true);
        result.setMessage(MessageCtrm.SaveSuccess);
        result.setData(lmb);
        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();
        this.repository.PhysicsDelete(id,LMB.class);

        result.setSuccess(true);
        result.setMessage(MessageCtrm.DeleteSuccess);

        return result;
    }

    @Override
    public ActionResult<LMB> GetById(String id) {

        ActionResult<LMB> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,LMB.class));

        return result;
    }

    @Override
    public List<LMB> LMBs() {

        DetachedCriteria where=DetachedCriteria.forClass(LMB.class);
        where.add(Restrictions.eq("IsHidden", false));

        return this.repository.GetQueryable(LMB.class).where(where).toList();

    }

    @Override
    public List<LMB> LMBs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

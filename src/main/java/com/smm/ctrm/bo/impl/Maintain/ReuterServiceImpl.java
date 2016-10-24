package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.ReuterService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class ReuterServiceImpl implements ReuterService {

    @Autowired
    private HibernateRepository<Reuter> repository;

    @Autowired
    private HibernateRepository<LME> lmeHibernateRepository;

    @Override
    public Criteria GetCriteria() {

        return  this.repository.CreateCriteria(Reuter.class);

    }

    @Override
    public ActionResult<String> Import1By1(Reuter reuter,String userId, String userName) {

            if(reuter==null) return new ActionResult<>(false,"reuter is null");

            reuter.setCreatedAt(new Date());
            reuter.setCreatedBy(userName);
            reuter.setCreatedId(userId);

            DetachedCriteria where=DetachedCriteria.forClass(Reuter.class);
            where.add(Restrictions.eq("CommodityId", reuter.getCommodityId()));
            where.add(Restrictions.eq("TradeDate", reuter.getTradeDate()));
            where.add(Restrictions.eq("PromptDate", reuter.getPromptDate()));
            where.add(Restrictions.eq("Basis", reuter.getBasis()));
            List<Reuter> list=this.repository.GetQueryable(Reuter.class).where(where).toList();

            if(list==null || list.size()==0){

                this.repository.SaveOrUpdate(reuter);

            }else{

                Reuter exists=list.get(0);
                exists.setPrice(reuter.getPrice());
                this.repository.SaveOrUpdate(exists);

            }

            //更新Lme价格行情
            if (reuter.getPromptDate() == null||reuter.getPromptDate() == new Date())
            {
                where=DetachedCriteria.forClass(LME.class);
                where.add(Restrictions.eq("TradeDate", reuter.getTradeDate()));
                where.add(Restrictions.eq("CommodityId", reuter.getCommodityId()));

                List<LME> temp_list=this.lmeHibernateRepository.GetQueryable(LME.class).where(where).toList();

                LME lme=null;

                if(temp_list!=null && temp_list.size()>0) lme=temp_list.get(0);

                if (lme != null)
                {
                    if (reuter.getBasis().toUpperCase().equals("CASH"))
                    {
                        lme.setCashSell(reuter.getPrice());
                    }
                    if (reuter.getBasis().toUpperCase().equals("3M"))
                    {
                        lme.setM3Sell(reuter.getPrice());
                    }
                    this.lmeHibernateRepository.SaveOrUpdate(lme);
                }
                else
                {
                    if (reuter.getBasis().toUpperCase().equals("CASH") )
                    {
                        lme = new LME();

                        lme.setTradeDate(reuter.getTradeDate());
                        lme.setCommodityId(reuter.getCommodityId());
                        lme.setCashSell(reuter.getPrice());
                        lme.setCreatedBy("reuter");

                    }
                    if (reuter.getBasis().toUpperCase().equals("3M"))
                    {
                        lme = new LME();

                        lme.setTradeDate(reuter.getTradeDate());
                        lme.setCommodityId(reuter.getCommodityId());
                        lme.setCashSell(reuter.getPrice());
                        lme.setCreatedBy("reuter");
                    }
                    if (lme!=null)
                        this.lmeHibernateRepository.SaveOrUpdate(lme);
                }
            }

            return new ActionResult<>(true,"导入成功");

    }

    @Override
    public List<Reuter> Storages(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

package com.smm.ctrm.bo.impl.Maintain;

import com.alibaba.fastjson.JSON;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.LMEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.Maintain.DSME;
import com.smm.ctrm.domain.Maintain.LMB;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.domain.apiClient.WebApiClient;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class LMEServiceImpl implements LMEService {

    private static final Logger logger=Logger.getLogger(LMEServiceImpl.class);

    @Autowired
    private HibernateRepository<LME> repository;

    @Autowired
    private CommonService commonService;

    @Override
    public ActionResult<String> Sync() {

        try{

            List<LME> lmes = GetLMEs4Sync();

            if(lmes==null || lmes.size()==0) throw new Exception("LME list is null");

            //过滤重复记录
            lmes.forEach(lme -> {

                DetachedCriteria where=DetachedCriteria.forClass(LME.class);
                where.add(Restrictions.eq("TradeDate", lme.getTradeDate()));
                where.add(Restrictions.eq("CommodityId", lme.getCommodityId()));
                where.add(Restrictions.ne("Id", lme.getId()));

                List<LME> list=this.repository.GetQueryable(LME.class).where(where).toList();

                if(list==null || list.size()==0){
                    this.repository.SaveOrUpdate(lme);
                }
            });

            return new ActionResult<>(true,"同步成功");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }
    }

    private List<LME> GetLMEs4Sync() {


        LoginInfo syncLoginInfo = this.commonService.GetSyncLoginInfo();

        if (syncLoginInfo == null)  return null;

        List<LME> lmes = null;


        Date targetDate= DateUtil.getDiffDate(new Date(),-7);

        HashMap<String,String> param=new HashMap<>();

        param.put("StartDate",targetDate.toString());


        WebApiClient webApiClient=new WebApiClient(CommonService.SyncUrl, syncLoginInfo.toString());

        String result=webApiClient.Post("api/Maintain/LME/Pager",param);

        if(!StringUtils.isEmpty(result)){

            ActionResult<List<LME>> resultObj= JSON.parseObject(result, ActionResult.class);

            if(resultObj.isSuccess()){

                lmes=resultObj.getData();
            }
        }

        return lmes;
    }

    @Override
    public ActionResult<String> SaveLmes(List<LME> lmes) {

        try{

            if(lmes==null || lmes.size()==0) throw new Exception("参数为空");

            //过滤重复记录
            lmes.forEach(lme -> {

                DetachedCriteria where=DetachedCriteria.forClass(LME.class);
                where.add(Restrictions.eq("TradeDate", lme.getTradeDate()));
                where.add(Restrictions.eq("CommodityId", lme.getCommodityId()));
                where.add(Restrictions.ne("Id", lme.getId()));

                List<LME> list=this.repository.GetQueryable(LME.class).where(where).toList();

                if(list==null || list.size()==0){
                    this.repository.SaveOrUpdate(lme);
                }
            });

            return new ActionResult<>(true,MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }
    }

    @Override
    public ActionResult<String> Save(LME lme) {

        ActionResult<String> result= new ActionResult<>();

   
            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(LME.class);
            where.add(Restrictions.eq("TradeDate", lme.getTradeDate()));
            where.add(Restrictions.eq("CommodityId", lme.getCommodityId()));
            where.add(Restrictions.neOrIsNotNull("Id", lme.getId()));

            List<LME> list=this.repository.GetQueryable(LME.class).where(where).toList();

            if(list!=null && list.size()>0) {

                LME dataLem=list.get(0);

                dataLem.setCashBuy(lme.getCashBuy());
                dataLem.setM3Buy(lme.getM3Buy());
                dataLem.setM3Sell(lme.getM3Sell());
                dataLem.setM15Buy(lme.getM15Buy());
                dataLem.setM15Sell(lme.getM15Sell());
                dataLem.setM27Buy(lme.getM27Buy());
                dataLem.setM27Sell(lme.getM27Sell());

                this.repository.SaveOrUpdate(dataLem);

            }else{

                this.repository.SaveOrUpdate(lme);
            }

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);

      
        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,LME.class);

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
    public ActionResult<String> DeleteLmes(List<LME> lmes) {

        ActionResult<String> result= new ActionResult<>();

        try{

            if(lmes!=null && lmes.size()>0) lmes.forEach(lme -> this.repository.PhysicsDelete(lme.getId(),LME.class));

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
    public ActionResult<LME> GetById(String id) {

        ActionResult<LME> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,LME.class));

        return result;
    }

    @Override
    public Criteria GetCriteria() {

        return  this.repository.CreateCriteria(LME.class);
    }

    @Override
    public List<LME> LMEs() {

        DetachedCriteria where=DetachedCriteria.forClass(LME.class);
        where.add(Restrictions.eq("IsHidden", false));

        return this.repository.GetQueryable(LME.class).where(where).toList();
    }

    @Override
    public List<LME> LMEs(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy, RefUtil total) {

        return this.repository.GetPage(criteria,pageSize,pageIndex,sortBy,orderBy,total).getData();
    }


}

package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.EstPremiumSetupService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.EstPremiumSetup;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class EstPremiumSetupServiceImpl implements EstPremiumSetupService {

    private static final Logger logger=Logger.getLogger(EstPremiumSetupServiceImpl.class);

    @Autowired
    private HibernateRepository<EstPremiumSetup> repository;


    @Override
    public ActionResult<EstPremiumSetup> Save(EstPremiumSetup estPremiumSetup) {

        ActionResult<EstPremiumSetup> result= new ActionResult<>();

        //检查是否已存在
        DetachedCriteria where=DetachedCriteria.forClass(EstPremiumSetup.class);
        where.add(Restrictions.eq("EstDischarging", estPremiumSetup.getEstDischarging()));
        if(estPremiumSetup.getId()!=null){
        	where.add(Restrictions.ne("Id", estPremiumSetup.getId()));
        }
        List<EstPremiumSetup> list=this.repository.GetQueryable(EstPremiumSetup.class).where(where).toList();

        if(list != null&&list.size() > 0){

            result.setSuccess(false);
            result.setMessage(MessageCtrm.DuplicatedName);

        }else{
			try {
				this.repository.SaveOrUpdate(estPremiumSetup);
			} catch (RuntimeException e) {
				return new ActionResult<>(Boolean.FALSE, e.getMessage());
			}
            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
        }

        return result;

    }

    @Override
    public ActionResult<String> Delete(String guid) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(guid, EstPremiumSetup.class);

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
    public ActionResult<EstPremiumSetup> GetById(String id) {

        ActionResult<EstPremiumSetup> result= new ActionResult<>();

        try{

            EstPremiumSetup obj= this.repository.getOneById(id, EstPremiumSetup.class);

            result.setSuccess(true);
            result.setData(obj);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<List<EstPremiumSetup>> EstPremiumSetups() {

        List<EstPremiumSetup> list=this.repository.GetQueryable(EstPremiumSetup.class).toList();

        ActionResult<List<EstPremiumSetup>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

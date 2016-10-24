package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.FeeSetupService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.FeeSetup;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class FeeSetupServiceImpl implements FeeSetupService {

    private static final Logger logger=Logger.getLogger(FeeSetupServiceImpl.class);

    @Autowired
    private HibernateRepository<FeeSetup> repository;


    @Override
    public ActionResult<FeeSetup> Save(FeeSetup feeSetup) {


        ActionResult<FeeSetup> result= new ActionResult<>();

        try{
//        	if(StringUtils.isNotBlank(feeSetup.getId())) {
//        		FeeSetup fst = this.repository.getOneById(feeSetup.getId(), FeeSetup.class);
//        		if(fst == null) {
//        			feeSetup.setId(null);
//        		}
//        	}
        	
        	
            this.repository.SaveOrUpdate(feeSetup);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
//            result.setData(feeSetup);

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

            this.repository.PhysicsDelete(id, FeeSetup.class);

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
    public ActionResult<FeeSetup> GetById(String id) {

        ActionResult<FeeSetup> result= new ActionResult<>();

        try{

            FeeSetup obj= this.repository.getOneById(id, FeeSetup.class);

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
    public ActionResult<List<FeeSetup>> FeeSetups() {

        List<FeeSetup> list=this.repository.GetQueryable(FeeSetup.class).toList();
        

        ActionResult<List<FeeSetup>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    @Override
    public ActionResult<List<FeeSetup>> FeeSeupsByFeeType(String feeType) {

        DetachedCriteria where=DetachedCriteria.forClass(FeeSetup.class);
        where.add(Restrictions.eq("FeeType", feeType));

        List<FeeSetup> list=this.repository.GetQueryable(FeeSetup.class).where(where).toList();

        ActionResult<List<FeeSetup>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }
}

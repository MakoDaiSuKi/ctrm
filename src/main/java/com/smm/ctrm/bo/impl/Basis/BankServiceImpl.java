package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.BankService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class BankServiceImpl implements BankService {


    private static final Logger logger=Logger.getLogger(BankServiceImpl.class);

    @Autowired
    private HibernateRepository<Bank> repository;

    @Override
    public ActionResult<List<Bank>> Banks() {

        DetachedCriteria where=DetachedCriteria.forClass(Bank.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Bank> list=this.repository.GetQueryable(Bank.class).where(where).toCacheList();

        ActionResult<List<Bank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    @Override
    public ActionResult<List<Bank>> BackBanks() {

        List<Bank> list=this.repository.GetQueryable(Bank.class).toList();

        ActionResult<List<Bank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    @Override
    public ActionResult<Bank> Save(Bank brand) {

        ActionResult<Bank> result= new ActionResult<>();

        try{

        	/**
        	 * 检查重名
        	 */
        	DetachedCriteria dc=DetachedCriteria.forClass(Bank.class);
        	if(brand.getId()!=null){
        		dc.add(Restrictions.ne("Id", brand.getId()));
        	}
        	dc.add(Restrictions.or(Restrictions.eq("Name", brand.getName()),Restrictions.eq("Code", brand.getCode())));
        	
        	List<Bank> obj=this.repository.GetQueryable(Bank.class).where(dc).toList();
        	if(obj!=null&&obj.size()>0){
        		result.setSuccess(false);
                result.setMessage(MessageCtrm.DuplicatedName);
        	}
        	
            this.repository.SaveOrUpdate(brand);

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

            this.repository.PhysicsDelete(id, Bank.class);

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
    public ActionResult<Bank> GetById(String id) {

        ActionResult<Bank> result= new ActionResult<>();

        try{

            Bank bank=this.repository.getOneById(id,Bank.class);

            result.setSuccess(true);
            result.setData(bank);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());
 
        }

        return result;
    }
}

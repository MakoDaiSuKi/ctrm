package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.AccountService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger=Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private HibernateRepository<Account> repository;

    @Override
    public ActionResult<List<Account>> Accounts() {

        DetachedCriteria where=DetachedCriteria.forClass(Account.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Account> list=this.repository.GetQueryable(Account.class).where(where).OrderBy(Order.desc("Code")).toCacheList();

        ActionResult<List<Account>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Account>> BackAccounts() {

        List<Account> list=this.repository.GetQueryable(Account.class).OrderBy(Order.asc("Code")).toList();

        ActionResult<List<Account>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }
    
    @Override
    public ActionResult<Account> Save(Account account) {

        ActionResult<Account> result= new ActionResult<>();

        try{

        	/**
        	 * 检查重名
        	 */
        	DetachedCriteria dc=DetachedCriteria.forClass(Account.class);
        	
        	if(account.getId()!=null){
        		dc.add(Restrictions.ne("Id", account.getId()));
        	}
        	dc.add(Restrictions.or(
        			Restrictions.eq("Name", account.getName()),
        			Restrictions.eq("Code", account.getCode()))
        		);
        	List<Account> obj=this.repository.GetQueryable(Account.class).where(dc).toList();
        	if(obj!=null&&obj.size()>0){
        		return new ActionResult<>(false, MessageCtrm.DuplicatedName);
        	}
            this.repository.SaveOrUpdate(account);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
            result.setData(account);

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

            this.repository.PhysicsDelete(id, Account.class);

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
    public ActionResult<Account> GetById(String id) {

        ActionResult<Account> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,Account.class));

        return result;
    }

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}


}

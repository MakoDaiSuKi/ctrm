package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CustomerBankService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CustomerBankServiceImpl implements CustomerBankService {

    private static final Logger logger=Logger.getLogger(CustomerBankServiceImpl.class);

    @Autowired
    private HibernateRepository<CustomerBank> repository;
    
    @Autowired
    private HibernateRepository<CustomerTitle> customerTitleRepository;
    
    
    @Autowired
    private HibernateRepository<Bank> bankRepository;
    
    
    


    @Override
    public ActionResult<CustomerBank> Save(CustomerBank titleBank) {

        ActionResult<CustomerBank> result= new ActionResult<>();

        try{
        	/**
        	 *  检查重名
        	 */
        	DetachedCriteria dc=DetachedCriteria.forClass(CustomerBank.class);
        	dc.add(Restrictions.eq("CustomerTitleId", titleBank.getCustomerTitleId()));
        	dc.add(Restrictions.eq("BankId", titleBank.getBankId()));
        	dc.add(Restrictions.eq("Branch", titleBank.getBranch()));
        	if(titleBank.getId()!=null){
        		dc.add(Restrictions.ne("Id", titleBank.getId()));
        	}else{
        		dc.add(Restrictions.isNotNull("Id"));
        	}
        	List<CustomerBank> obj=this.repository.GetQueryable(CustomerBank.class).where(dc).toList();
        	if(obj!=null&&obj.size()>0){
        		return new ActionResult<>(false, MessageCtrm.DuplicatedName);
        	}
            this.repository.SaveOrUpdate(titleBank);

            result.setSuccess(true);
            result.setData(titleBank);
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

            this.repository.PhysicsDelete(id, CustomerBank.class);

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
    public ActionResult<List<CustomerBank>> CustomerBanks() {

        DetachedCriteria where=DetachedCriteria.forClass(CustomerBank.class);
        where.add(Restrictions.eq("IsHidden",false));

        List<CustomerBank> list=this.repository.GetQueryable(CustomerBank.class).where(where).toList();
        ActionResult<List<CustomerBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<CustomerBank> GetById(String id) {

        ActionResult<CustomerBank> result= new ActionResult<>();

        try{

            CustomerBank customerBank= this.repository.getOneById(id, CustomerBank.class);
            assemblingBean(customerBank);
            result.setSuccess(true);
            result.setData(customerBank);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<List<CustomerBank>> GetCustomerBanksByCustomerId(String customerId) {


        DetachedCriteria where=DetachedCriteria.forClass(CustomerBank.class);
        where.createAlias("CustomerTitle", "customerTitle", JoinType.INNER_JOIN);
        //where.add(Restrictions.isNotNull("CustomerTitle"));
        where.add(Restrictions.eq("customerTitle.CustomerId",customerId));

        List<CustomerBank> list=this.repository.GetQueryable(CustomerBank.class).where(where).toList();

        ActionResult<List<CustomerBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<List<CustomerBank>> GetCustomerBanksByCustomerTitleId(String customerTitleId) {

        DetachedCriteria where=DetachedCriteria.forClass(CustomerBank.class);
        where.add(Restrictions.eq("CustomerTitleId",customerTitleId));

        List<CustomerBank> list=this.repository.GetQueryable(CustomerBank.class).where(where).toList();

        ActionResult<List<CustomerBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }
    
    /**
     * 以下是打掉关系
     */
    public List<CustomerBank> assemblingBeanList(List<CustomerBank> cb){
    	if(cb.size()==0)return null;
    	for (CustomerBank customerBank : cb) {
    		assemblingBean(customerBank);
		}
    	return cb;
	}
	
	public void assemblingBean(CustomerBank cb){
		if(cb!=null){
			if(cb.getCustomerTitleId()!=null){
				CustomerTitle ct=this.customerTitleRepository.getOneById(cb.getCustomerTitleId(), CustomerTitle.class);
				cb.setCustomerTitle(ct);
			}
			if(cb.getBankId()!=null){
				Bank bank=this.bankRepository.getOneById(cb.getBankId(), Bank.class);
				cb.setBank(bank);
			}
		}
	}
}

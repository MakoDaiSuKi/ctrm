package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.LiaisonService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Liaison;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class LiaisonServiceImpl implements LiaisonService {


    private static final Logger logger=Logger.getLogger(LiaisonServiceImpl.class);

    @Autowired
    private HibernateRepository<Liaison> repository;
    
    @Autowired
    private HibernateRepository<Customer> customerRepository;


    @Override
    public ActionResult<Liaison> Save(Liaison liaison) {

        ActionResult<Liaison> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(liaison);

            result.setSuccess(true);
            result.setData(liaison);
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

            this.repository.PhysicsDelete(id, Liaison.class);

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
    public ActionResult<List<Liaison>> Liaisons(String customerId) {

        DetachedCriteria where=DetachedCriteria.forClass(Liaison.class);
        where.add(Restrictions.eq("CustomerId", customerId));

        List<Liaison> list=this.repository.GetQueryable(Liaison.class).where(where).toList();

        ActionResult<List<Liaison>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public List<Liaison> Liaisons() {

        DetachedCriteria where=DetachedCriteria.forClass(Liaison.class);
        where.add(Restrictions.eq("IsHidden", false));
        
        return assemblingBeanList(this.repository.GetQueryable(Liaison.class).where(where).toList());

    }

    @Override
    public ActionResult<Liaison> GetById(String id) {

        ActionResult<Liaison> result= new ActionResult<>();
        Liaison liaison=this.repository.getOneById(id, Liaison.class);
        assemblingBean(liaison);
        result.setSuccess(true);
        result.setData(liaison);

        return result;
    }
    
    /**
     * 以下是打掉关系
     */
    public List<Liaison> assemblingBeanList(List<Liaison> ct){
    	if(ct.size()==0)return null;
    	for (Liaison liaison : ct) {
    		assemblingBean(liaison);
		}
    	return ct;
	}
	
	public void assemblingBean(Liaison ct){
		if(ct!=null){
			if(ct.getCustomerId()!=null){
				Customer c=this.customerRepository.getOneById(ct.getCustomerId(), Customer.class);
				ct.setCustomer(c);
			}
		}
	}
}

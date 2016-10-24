package com.smm.ctrm.bo.impl.Basis;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.bo.Basis.LegalService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class LegalServiceImpl implements LegalService {

    private static final Logger logger=Logger.getLogger(LegalServiceImpl.class);

    @Autowired
    private HibernateRepository<Legal> repository;

    @Autowired
    private HibernateRepository<Bvi2Sm> bvi2SmHibernateRepository;

    @Autowired
    private HibernateRepository<Customer> customerHibernateRepository;

    @Autowired
    private HibernateRepository<CustomerTitle> customerTitleHibernateRepository;

    @Autowired
    private HibernateRepository<Contract> contractHibernateRepository;
    
    @Autowired
    private HibernateRepository<Org> orgRepo;


    @Autowired
    private CustomerService customerService;

	@Override
    public ActionResult<List<Legal>> Legals() {

        DetachedCriteria where=DetachedCriteria.forClass(Legal.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Legal> list=this.repository.GetQueryable(Legal.class).where(where).OrderBy(Order.desc("Code")).toList();
        for (int i=0;i<list.size();i++) {
			if(list.get(i).getOrgId()!=null){
				Org l=this.orgRepo.getOneById(list.get(i).getOrgId(),Org.class);
				list.get(i).setOrg(l);
			}
		}
        ActionResult<List<Legal>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Legal>> Legals(String orgId) {

        DetachedCriteria where=DetachedCriteria.forClass(Legal.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("OrgId", orgId));

        List<Legal> list=this.repository.GetQueryable(Legal.class).where(where).OrderBy(Order.desc("Code")).toCacheList();
        
        for(Legal legal : list) {
        	if(StringUtils.isNotBlank(legal.getOrgId())) {
        		legal.setOrg(orgRepo.getOneById(legal.getOrgId(), Org.class));
        	}
        }

        ActionResult<List<Legal>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Legal> Save(Legal legal) {

        try{

            //检查重复

            DetachedCriteria where=DetachedCriteria.forClass(Legal.class);
            where.add(Restrictions.eq("Name", legal.getName()));
            where.add(Restrictions.eq("Code", legal.getCode()));
            where.add(Restrictions.ne("Id", legal.getId()));

            List<Legal> legals=this.repository.GetQueryable(Legal.class).where(where).toList();

            if(legals!=null && legals.size()>0) return new ActionResult<>(false,MessageCtrm.DuplicatedName);

            /**是否存在内部客户*/
            DetachedCriteria dc=DetachedCriteria.forClass(Customer.class);
            dc.add(Restrictions.eq("Name",legal.getName()));
            Customer customers=this.customerHibernateRepository.GetQueryable(Customer.class).where(dc).firstOrDefault();
            
            if(customers!=null){
            	legal.setCustomerId(customers.getId());
            }else{
            	//----------------------
            	 if (legal.getIsInternalCustomer()){
                     Customer customer = new Customer();
                     customer.setName(legal.getCustomerName());
                     customer.setIsInternalCustomer(true);
                     customer.setStatus(ActionStatus.Status_Agreed);
                     customer.setCreatedAt(new Date());
                     customer.setCreatedBy(legal.getCreatedBy());
                     customer.setCreatedId(legal.getCreatedId());
                     customer.setStatus(Status.Agreed);
                     customer.setIsIniatiated(true);

                     ActionResult<Customer> ret = this.customerService.Save(customer);

                     if (ret != null && ret.getData() != null)
                         legal.setCustomerId(ret.getData().getId());
                 } else {
                     legal.setCustomerId(null);
                 }
            }

            this.repository.SaveOrUpdate(legal);

            //-------------------------

            Bvi2Sm bvi2Sm=new Bvi2Sm();

            List<Bvi2Sm> bvi2Sms=this.bvi2SmHibernateRepository.GetList(Bvi2Sm.class);

            if(bvi2Sms!=null && bvi2Sms.size()>0) bvi2Sm=bvi2Sms.get(0);

            List<Legal> legalList=this.repository.GetList(Legal.class);


            //获取BVI台头的数据, 代码Code = SB

            bvi2Sm.setBviLegalCode("SB");

            final Bvi2Sm finalBvi2Sm = bvi2Sm;
            Legal sb = legalList.stream().filter(lg -> lg.getCode().equals(finalBvi2Sm.getBviLegalCode())).findFirst().orElse(null);
            List<CustomerTitle> customerTitleList=this.customerTitleHibernateRepository.GetList(CustomerTitle.class);
            if(sb != null){            	
            Customer customer = this.customerHibernateRepository.getOneById(sb.getCustomerId(),Customer.class);

            

            CustomerTitle title = customerTitleList.stream().filter(ct -> customer.getId().equals(ct.getCustomerId())).findFirst().orElse(null);

            bvi2Sm.setBviLegalId(sb.getId());
            bvi2Sm.setBviLegalName(sb.getName());
            bvi2Sm.setBviCustomerId(sb.getCustomerId());

            if (customer != null)
            {

                bvi2Sm.setBviCustomerName(customer.getName());

                if (title != null)
                {
                    bvi2Sm.setBviTitleId(title.getId());
                    bvi2Sm.setBviTitleName(title.getName());
                }
            }
            }
            //获取商贸台头的数据, 代码Code = SM
            bvi2Sm.setBviLegalCode("SM");

            Legal sm = legalList.stream().filter(lg -> finalBvi2Sm.getSmLegalCode().equals(lg.getCode())).findFirst().orElse(null);

            if (sm != null)
            {
            	Customer customer4SM = this.customerHibernateRepository.getOneById(sm.getCustomerId(),Customer.class);
            	CustomerTitle title4SM = customerTitleList.stream().filter(ct -> customer4SM.getId().equals(ct.getCustomerId())).findFirst().orElse(null);
            	
            	bvi2Sm.setBviLegalId(sb.getId());
                bvi2Sm.setBviLegalName(sb.getName());
                bvi2Sm.setBviCustomerId(sb.getCustomerId());


                if (customer4SM != null)
                {
                    bvi2Sm.setSmCustomerName(customer4SM.getName());

                    if (title4SM != null)
                    {
                        bvi2Sm.setSmTitleId(title4SM.getId());
                        bvi2Sm.setSmTitleName(title4SM.getName());
                    }
                }
            }

            //--------------------

            this.bvi2SmHibernateRepository.SaveOrUpdate(bvi2Sm);

            return new ActionResult<>(true,MessageCtrm.SaveSuccess);

        }catch (Exception e){

        	logger.error(e.getMessage(), e);
        	return new ActionResult<>(false,e.getMessage());
            
        }

    }

    @Override
    public ActionResult<String> Delete(String legalId) {


        try{

            DetachedCriteria where=DetachedCriteria.forClass(Contract.class);
            where.add(Restrictions.eq("LegalId", legalId));

            List<Contract> contracts=this.contractHibernateRepository.GetQueryable(Contract.class).where(where).toList();

            if(contracts!=null && contracts.size()>0) throw new Exception("已经存在相关的合同记录，不允许删除");

            Legal legal=this.repository.getOneById(legalId,Legal.class);

            if(legal==null) throw new Exception("legal is null");


            if (legal.getIsInternalCustomer())
            {
                where=DetachedCriteria.forClass(Customer.class);
                where.add(Restrictions.eq("Name", legal.getCustomerName()));

                List<Customer> customers=this.customerHibernateRepository.GetQueryable(Customer.class).where(where).toList();

                Customer customer=null;

                if(customers!=null && customers.size()>0) customer=customers.get(0);

                if (customer != null)
                {
                    where=DetachedCriteria.forClass(Contract.class);
                    where.add(Restrictions.eq("CustomerId", customer.getId()));

                    contracts=this.contractHibernateRepository.GetQueryable(Contract.class).where(where).toList();

                    if(contracts!=null && contracts.size()>0) throw new Exception("已经存在作为内部客户的相关合同记录，不允许删除");
                }
            }

            this.repository.PhysicsDelete(legalId,Legal.class);

            return new ActionResult<>(true,"删除成功");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<Legal> GetById(String id) {

        ActionResult<Legal> result= new ActionResult<>();
        Legal legal= this.repository.getOneById(id, Legal.class);
        if(StringUtils.isNotBlank(legal.getOrgId())) {
    		legal.setOrg(orgRepo.getOneById(legal.getOrgId(), Org.class));
    	}
        result.setSuccess(true);
        result.setData(legal);

        return result;
    }

    @Override
    public void MoveUp(String id) {

    }


    @Override
    public void ModifyDefaultLegal(String id) {

    }

	@Override
	public ActionResult<List<Legal>> Pager(RefUtil total, String orderSort, Criteria param, Integer pageIndex,
			Integer pageSize, String orderBy) {
		return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total);

	}

	@Override
	public void MoveDown(String id) {

	}
}

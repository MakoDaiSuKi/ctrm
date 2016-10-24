package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.LegalBankService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.LegalBank;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class LegalBankServiceImpl implements LegalBankService {


    private static final Logger logger=Logger.getLogger(LegalBankServiceImpl.class);

    @Autowired
    private HibernateRepository<LegalBank> repository;
    
    @Autowired
    private HibernateRepository<Legal> leaglRepo;
    
    @Autowired
    private HibernateRepository<Bank> bankRepo;
    
    @Autowired HibernateRepository<Org> orgRepo;

    
    
    @Override
    public ActionResult<List<LegalBank>> LegalBanks() {

        DetachedCriteria where=DetachedCriteria.forClass(LegalBank.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<LegalBank> list=this.repository.GetQueryable(LegalBank.class).where(where).toList();
        addLegalAndBank(list);

        ActionResult<List<LegalBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    private void addLegalAndBank(List<LegalBank> list){
    	for(LegalBank lb : list) {
        	if(StringUtils.isNotBlank(lb.getLegalId())) {
        		Legal legal = leaglRepo.getOneById(lb.getLegalId(), Legal.class);
        		if(StringUtils.isNotBlank(legal.getOrgId())) {
        			legal.setOrg(orgRepo.getOneById(legal.getOrgId(), Org.class));
        		}
        		lb.setLegal(legal);
        	}
        	if(StringUtils.isNotBlank(lb.getBankId())) {
        		lb.setBank(bankRepo.getOneById(lb.getBankId(), Bank.class));
        	}
        }
    }
    
    @Override
    public ActionResult<List<LegalBank>> LegalBanksByLegalId(String legalId) {


        DetachedCriteria where=DetachedCriteria.forClass(LegalBank.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("LegalId", legalId));

        List<LegalBank> list=this.repository.GetQueryable(LegalBank.class).where(where).toList();
        addLegalAndBank(list);
        ActionResult<List<LegalBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<LegalBank>> BackLegalBanks() {

        List<LegalBank> list=this.repository.GetQueryable(LegalBank.class).toList();

        ActionResult<List<LegalBank>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;

    }

    @Override
    public ActionResult<LegalBank> Save(LegalBank legalBank) {

        ActionResult<LegalBank> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(LegalBank.class);
            where.add(Restrictions.eq("LegalId", legalBank.getLegalId()));
            where.add(Restrictions.eq("BankId", legalBank.getBankId()));
            where.add(Restrictions.eq("Branch", legalBank.getBranch()));
            if(StringUtils.isNotBlank(legalBank.getId())) {
            	where.add(Restrictions.ne("Id", legalBank.getId()));
            }

            List<LegalBank> list=this.repository.GetQueryable(LegalBank.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            //保存
            this.repository.SaveOrUpdate(legalBank);

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

            this.repository.PhysicsDelete(id, LegalBank.class);

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
	public LegalBank GetById(String id) {

       return this.repository.getOneById(id, LegalBank.class);
	}
}

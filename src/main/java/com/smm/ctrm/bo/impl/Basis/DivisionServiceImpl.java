package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.DivisionService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class DivisionServiceImpl implements DivisionService {

    private static final Logger logger=Logger.getLogger(DivisionServiceImpl.class);

    @Autowired
    private HibernateRepository<Division> repository;
    
    @Autowired
    private HibernateRepository<Org> orgRepository;
    

    @Override
    public ActionResult<List<Division>> Pager(RefUtil total, String orderSort, String orderBy, Criteria param, Integer pageSize, Integer pageIndex) {

    	return this.repository.GetPage(param, pageSize, pageIndex, orderBy, orderSort,total);

    }

    @Override
    public ActionResult<List<Division>> Divisions() {

        DetachedCriteria where=DetachedCriteria.forClass(Division.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Division> list=this.repository.GetQueryable(Division.class).where(where).toList();

        ActionResult<List<Division>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<List<Division>> GetDivisionsByOrgId(String orgId) {

        DetachedCriteria where=DetachedCriteria.forClass(Division.class);
        where.add(Restrictions.eq("OrgId", orgId));

        List<Division> list=this.repository.GetQueryable(Division.class).where(where).toList();

        ActionResult<List<Division>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(assemblingBeanList(list));

        return result;
    }

    @Override
    public ActionResult<Division> Save(Division division) {

        ActionResult<Division> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(division);

            result.setSuccess(true);
            result.setData(division);
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

            this.repository.PhysicsDelete(id, Division.class);

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
    public ActionResult<Division> GetById(String id) {

        ActionResult<Division> result= new ActionResult<>();

        try{

            Division obj= this.repository.getOneById(id, Division.class);
            assemblingBean(obj);
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
    public void MoveUp(String id) {

    }

	@Override
	public void MoveDown(String id) {

	}

	
    /**
     * 以下是打掉关系
     */
    public List<Division> assemblingBeanList(List<Division> ct){
    	if(ct.size()==0)return null;
    	for (Division division : ct) {
    		assemblingBean(division);
		}
    	return ct;
	}
	
	public void assemblingBean(Division ct){
		if(ct!=null){
			/**
			 * 组装Parent
			 */
			Division parentDiv=null;
			if(ct.getParentId()!=null){
				parentDiv=this.repository.getOneById(ct.getParentId(),Division.class);
				ct.setParent(parentDiv);
			}
			/**
			 * 组装Org
			 */
			if(ct.getOrgId()!=null){
				Org org=this.orgRepository.getOneById(ct.getOrgId(), Org.class);
				ct.setOrg(org);
			}
			/**
			 * 组装Children
			 */
			DetachedCriteria dc=DetachedCriteria.forClass(Division.class);
			dc.add(Restrictions.eq("ParentId", ct.getId()));
			List<Division> childrenDiv=this.repository.GetQueryable(Division.class).where(dc).toList();
			ct.setChildren(childrenDiv);
			
			/**
			 * 不进行递归，只取一级
			 */
			if(parentDiv!=null){
				assemblingBean(parentDiv);
			}
		}
	}
}

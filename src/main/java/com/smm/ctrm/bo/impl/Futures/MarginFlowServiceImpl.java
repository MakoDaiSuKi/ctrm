package com.smm.ctrm.bo.impl.Futures;

import com.smm.ctrm.bo.Futures.MarginFlowService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Physical.Commission;
import com.smm.ctrm.domain.Physical.MarginFlow;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class MarginFlowServiceImpl implements MarginFlowService {

    private static final Logger logger=Logger.getLogger(MarginFlowServiceImpl.class);

    @Autowired
    private HibernateRepository<MarginFlow> repository;


    @Override
    public ActionResult<String> Save(MarginFlow marginFlow) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(marginFlow);

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

        this.repository.PhysicsDelete(id,MarginFlow.class);

        result.setSuccess(true);
        result.setMessage(MessageCtrm.DeleteSuccess);

        return result;
    }

    @Override
    public ActionResult<MarginFlow> GetById(String id) {

        ActionResult<MarginFlow> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,MarginFlow.class));

        return result;
    }

    @Override
    public ActionResult<List<MarginFlow>> MarginFlows() {

        DetachedCriteria where=DetachedCriteria.forClass(MarginFlow.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<MarginFlow> list=this.repository.GetQueryable(MarginFlow.class).where(where).toList();

        ActionResult<List<MarginFlow>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

	@Override
	public Criteria GetCriteria() {
		return repository.CreateCriteria(MarginFlow.class);
	}

	@Override
	public List<MarginFlow> Pager(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String sortBy) {
		return repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}
}

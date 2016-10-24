package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.RemarkService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Remark;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class RemarkServiceImpl implements RemarkService {


    private static final Logger logger=Logger.getLogger(RemarkServiceImpl.class);

    @Autowired
    private HibernateRepository<Remark> repository;


    @Override
    public ActionResult<List<Remark>> Remarks() {

        DetachedCriteria where=DetachedCriteria.forClass(Remark.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Remark> list=this.repository.GetQueryable(Remark.class).where(where).toList();

        ActionResult<List<Remark>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<List<Remark>> BackRemarks() {

        List<Remark> list=this.repository.GetQueryable(Remark.class).toList();

        ActionResult<List<Remark>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<Remark> Save(Remark remark) {

        ActionResult<Remark> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(remark);

            result.setSuccess(true);
            //result.setData(remark);
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

            this.repository.PhysicsDelete(id, Remark.class);

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
    public ActionResult<Remark> GetById(String id) {

        ActionResult<Remark> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id, Remark.class));

        return result;
    }
}

package com.smm.ctrm.bo.impl.Finance;

import com.smm.ctrm.bo.Finance.InvoiceGradeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Physical.InvoiceGrade;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
public class InvoiceGradeServiceImpl implements InvoiceGradeService {

    private static final Logger logger=Logger.getLogger(InvoiceGradeServiceImpl.class);

    @Autowired
    private HibernateRepository<InvoiceGrade> repository;

    @Override
    public void GetCriteria() {

        this.repository.CreateCriteria(InvoiceGrade.class);
    }

    @Override
    public ActionResult<String> Save(InvoiceGrade invoiceGrade) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //有效性检查
            if (invoiceGrade == null || StringUtils.isEmpty(invoiceGrade.getGradeId()) || StringUtils.isEmpty(invoiceGrade.getInvoiceId())){

                throw new Exception("异常信息： invoiceGrade related");

            }

//            InvoiceGrade data=this.repository.getOneById(invoiceGrade.getId());
//
//            if(data==null) throw new Exception("原始记录不存在");

            this.repository.SaveOrUpdate(invoiceGrade);

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
    public ActionResult<String> Delete(String invoiceGradeId) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(invoiceGradeId,InvoiceGrade.class);

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
    public ActionResult<InvoiceGrade> GetById(String invoiceGradeId) {

        ActionResult<InvoiceGrade> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(invoiceGradeId,InvoiceGrade.class));

        return result;
    }

    @Override
    public ActionResult<List<InvoiceGrade>> GetByInvoiceId(String invoiceId) {

        DetachedCriteria where=DetachedCriteria.forClass(InvoiceGrade.class);
        where.add(Restrictions.eq("InvoiceId", invoiceId));

        List<InvoiceGrade> list=this.repository.GetQueryable(InvoiceGrade.class).where(where).toList();

        ActionResult<List<InvoiceGrade>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

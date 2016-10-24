package com.smm.ctrm.bo.impl.Finance;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.LCService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.LC;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
public class LCServiceImpl implements LCService {


    private static final Logger logger=Logger.getLogger(LCServiceImpl.class);

    @Autowired
    private HibernateRepository<LC> repository;

    @Autowired
    private HibernateRepository<Invoice> invoiceHibernateRepository;


    @Autowired
    private CommonService commonService;


    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(LC.class);
    }

    @Override
    public ActionResult<LC> Save(LC lc) {

        if (lc == null)  return  new ActionResult<>(false,"lc is null");

        if (lc.getInvoices() != null && lc.getInvoices().size() > 0){

            String[] InvoiceNos= (String[]) lc.getInvoices().stream().map(Invoice::getInvoiceNo).toArray();

            lc.setInvoiceNos(StringUtils.join(InvoiceNos,"/")) ;
        }
        else
        {
            lc.setInvoiceNos(null);
        }



        try{

            //先去除以前的发票的标志
            if (!StringUtils.isEmpty(lc.getId()))
            {
                LC exist = repository.getOneById(lc.getId(),LC.class);

                for (Invoice inv : exist.getInvoices())
                {
                    inv.setLcId(null);
                    invoiceHibernateRepository.SaveOrUpdate(inv);
                }
            }

            //保存LC
            String lcid = repository.SaveOrUpdateRetrunId(lc);

            //更新发票的LcId
            if (lc.getInvoices() != null){

                for (Invoice invoice : lc.getInvoices())
                {
                    Invoice t = invoiceHibernateRepository.getOneById(invoice.getId(),Invoice.class);
                    if (t == null) continue;
                    t.setLcId(lcid);
                    invoiceHibernateRepository.SaveOrUpdate(t);
                }


            }

            if (lc.getInvoices() != null)
            {

                Map<String,List<Invoice>> temp_map= lc.getInvoices().stream().collect(Collectors.groupingBy(Invoice::getLotId));

                for(String lotId:temp_map.keySet()){

                    commonService.UpdateLotPriceByLotId(lotId);
                }
            }

            LC newLc = repository.getOneById(lcid,LC.class);

            return new ActionResult<>(true,MessageCtrm.SaveSuccess, newLc);

        }catch (Exception e){
            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> Import1By1(LC lc) {

        if (lc == null)  return new ActionResult<>(false,"lc is null");

        DetachedCriteria where=DetachedCriteria.forClass(LC.class);
        where.add(Restrictions.eq("LcNo", lc.getLcNo()));
        List<LC> lcExist = repository.GetQueryable(LC.class).where(where).toList();

        if (lcExist!=null && lcExist.size() > 0)
        {

            List<LC> templist=lcExist.stream().filter(x->x.getInvoices()!=null && x.getInvoices().size()>0).collect(Collectors.toList());

            if (templist.size()>0)   return new ActionResult<>(false,"该信用证已经存在并且已经分配到发票");

            //删除现有数据
            lcExist.forEach(repository::PhysicsDelete);
        }


        try{

            repository.SaveOrUpdate(lc);

            return new ActionResult<>(true,null);

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> Delete(String id) {

        LC lc= repository.getOneById(id,LC.class);

        if(lc==null) return new ActionResult<>(false,"lc is null");

        try{

            lc.getInvoices().forEach(invoice -> {

                Invoice t= invoiceHibernateRepository.getOneById(invoice.getId(),Invoice.class);

                if(t!=null){

                    t.setLcId(null);
                    invoiceHibernateRepository.SaveOrUpdate(t);
                }

            });


            repository.PhysicsDelete(lc);

            return new ActionResult<>(true,"删除成功");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<LC> GetById(String id) {

        ActionResult<LC> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,LC.class));

        return result;
    }

    @Override
    public List<LC> LCs() {

        return this.repository.GetList(LC.class);

    }

    @Override
    public List<LC> LCs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}

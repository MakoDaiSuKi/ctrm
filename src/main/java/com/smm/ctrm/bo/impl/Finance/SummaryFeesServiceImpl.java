package com.smm.ctrm.bo.impl.Finance;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.SummaryFeesService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
public class SummaryFeesServiceImpl implements SummaryFeesService {

    private static final Logger logger=Logger.getLogger(SummaryFeesServiceImpl.class);

    @Autowired
    private HibernateRepository<SummaryFees> repository;


    @Autowired
    private HibernateRepository<Storage> storageHibernateRepository;


    @Autowired
    private CommonService commonService;

    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(SummaryFees.class);
    }

    @Override
    public ActionResult<SummaryFees> Save(SummaryFees sumFee) {

        if (sumFee.getId() == null)
        {
            repository.SaveOrUpdate(sumFee);

            return new ActionResult<>(true,MessageCtrm.SaveSuccess,sumFee);

        }

        SummaryFees oldSumFee = repository.getOneById(sumFee.getId(),SummaryFees.class);

        try {
            BeanUtils.copyProperties(oldSumFee,sumFee);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


        //删除不存在的
        for (int i = oldSumFee.getStorages().size()-1; i >= 0; i--)
        {

            final int finalI = i;
            List<Storage> tempList= sumFee.getStorages().stream().filter(x->x.getId().equals(oldSumFee.getStorages().get(finalI).getId())).collect(Collectors.toList());

            if (tempList==null || tempList.size()==0)
            {
                oldSumFee.getStorages().remove(i);
                //oldSumFee.Storages[i].SummaryFeesList.Remove(oldSumFee);
            }
        }


        //添加未关联上的记录
        if (sumFee.getStorages() != null)
        {
            for (Storage storage : sumFee.getStorages())
            {
                if ( oldSumFee.getStorages().stream().filter(x->x.getId().equals(storage.getId())).toArray().length==0)
                {
                    Storage s = storageHibernateRepository.getOneById(storage.getId(),Storage.class);
                    oldSumFee.getStorages().add(s);
                    //oldSumFee.Storages.Add(storage);
                }
            }
        }


        repository.SaveOrUpdate(oldSumFee);

        if (oldSumFee.getStorages().size() > 0) //更新SummaryNote发票对应的批次的费用
        {
            Map<String,List<Storage>> temp_map=oldSumFee.getStorages().stream().collect(Collectors.groupingBy(Storage::getLotId));

            for(String lotId:temp_map.keySet()){

                commonService.UpdateLotFeesByLotId(lotId);
            }
        }


        return new ActionResult<>(true,MessageCtrm.SaveSuccess,sumFee);
    }

    @Override
    public ActionResult<String> Delete(String sumFeeId) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //批量删除
            SummaryFees fees=this.repository.getOneById(sumFeeId,SummaryFees.class);

            if(fees==null) throw new Exception("数据不存在");

            fees.getStorages().forEach(storage -> fees.getStorages().remove(storage));


            this.repository.PhysicsDelete(sumFeeId,SummaryFees.class);

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
    public ActionResult<SummaryFees> GetById(String sumFeeId) {

        ActionResult<SummaryFees> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(sumFeeId,SummaryFees.class));

        return result;
    }

    @Override
    public ActionResult<List<SummaryFees>> SummaryFeesByInvoiceId(String invoiceId) {

        DetachedCriteria where=DetachedCriteria.forClass(SummaryFees.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("InvoiceId", invoiceId));

        List<SummaryFees> list=this.repository.GetQueryable(SummaryFees.class).where(where).toList();

        ActionResult<List<SummaryFees>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }
}

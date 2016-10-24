package com.smm.ctrm.bo.impl.Physical;

import java.util.List;

import com.smm.ctrm.bo.Common.CommonService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Physical.DischargingPriceDiffService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.DischargingPriceDiff;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

import javax.annotation.Resource;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class DischargingPriceDiffServiceImpl implements DischargingPriceDiffService {

    private static final Logger logger=Logger.getLogger(DischargingPriceDiffServiceImpl.class);

    @Autowired
    private HibernateRepository<DischargingPriceDiff> repository;

    @Resource
    private CommonService commonService;


    @Override
    public void GetCriteria() {

        this.repository.CreateCriteria(DischargingPriceDiff.class);

    }

    @Override
    public ActionResult<DischargingPriceDiff> Save(DischargingPriceDiff pricediff) {

        ActionResult<DischargingPriceDiff> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(DischargingPriceDiff.class);
            where.add(Restrictions.eq("Discharging", pricediff.getDischarging()));
            where.add(Restrictions.eq("ContractId", pricediff.getContractId()));
            if(pricediff.getId()!=null){
            	where.add(Restrictions.ne("Id", pricediff.getId()));
            }else{
            	where.add(Restrictions.isNotNull("Id"));
            }

            List<DischargingPriceDiff> list=this.repository.GetQueryable(DischargingPriceDiff.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception("该到达地点价差已经存在");

            this.repository.SaveOrUpdate(pricediff);

            result.setSuccess(true);
            result.setData(pricediff);
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

            this.repository.PhysicsDelete(id, DischargingPriceDiff.class);

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
    public ActionResult<DischargingPriceDiff> GetById(String id) {

        ActionResult<DischargingPriceDiff> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(commonService.SimplifyData(this.repository.getOneById(id, DischargingPriceDiff.class)));

        return result;
    }

    @Override
    public ActionResult<List<DischargingPriceDiff>> PriceDiffsByContractId(String contractId) {

        DetachedCriteria where=DetachedCriteria.forClass(DischargingPriceDiff.class);
        where.add(Restrictions.eq("IsHidden", false));
        where.add(Restrictions.eq("ContractId", contractId));

        List<DischargingPriceDiff> list=this.repository.GetQueryable(DischargingPriceDiff.class).where(where).toList();
        list = commonService.SimplifyData(list);
        ActionResult<List<DischargingPriceDiff>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public List<DischargingPriceDiff> PriceDiffs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }

	@Override
	public List<DischargingPriceDiff> PriceDiffs() {
		
		return this.repository.GetList(DischargingPriceDiff.class);
	}
}

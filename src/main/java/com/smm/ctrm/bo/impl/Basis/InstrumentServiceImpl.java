package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.InstrumentService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class InstrumentServiceImpl implements InstrumentService {


    private static final Logger logger=Logger.getLogger(InstrumentServiceImpl.class);

    @Autowired
    private HibernateRepository<Instrument> repository;


    @Override
    public ActionResult<List<Instrument>> Instruments() {


        DetachedCriteria where=DetachedCriteria.forClass(Instrument.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<Instrument> list=this.repository.GetQueryable(Instrument.class).where(where).OrderBy(Order.desc("Code")).toList();

        ActionResult<List<Instrument>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;


    }

    @Override
    public ActionResult<List<Instrument>> BackInstruments() {

        List<Instrument> list=this.repository.GetQueryable(Instrument.class).toList();

        ActionResult<List<Instrument>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<String> Save(Instrument instrument) {
        ActionResult<String> result= new ActionResult<>();
        try{
            //检查重名
            DetachedCriteria where=DetachedCriteria.forClass(Instrument.class);
        	where.add(Restrictions.neOrIsNotNull("Id", instrument.getId()));
            where.add(Restrictions.eq("MarketId", instrument.getMarketId()));
            where.add(Restrictions.eq("CommodityId", instrument.getCommodityId()));
            where.add(Restrictions.eq("PromptDate", instrument.getPromptDate()));
            Instrument ins=this.repository.GetQueryable(Instrument.class).where(where).firstOrDefault();
            if(ins != null) throw new Exception(MessageCtrm.DuplicatedName);
            this.repository.SaveOrUpdate(instrument);
            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;

    }

    @Override
    public ActionResult<String> Delete(String instrumentId) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(instrumentId, Instrument.class);

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
    public ActionResult<Instrument> GetById(String instrumentId) {

        ActionResult<Instrument> result= new ActionResult<>();

        try{

            Instrument account= this.repository.getOneById(instrumentId, Instrument.class);

            result.setSuccess(true);
            result.setData(account);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

	@Override
	public Criteria GetCriteria() {
		
		return this.repository.CreateCriteria(Instrument.class);
	}

	@Override
	public List<Instrument> Instruments(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
		
		return this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}
}

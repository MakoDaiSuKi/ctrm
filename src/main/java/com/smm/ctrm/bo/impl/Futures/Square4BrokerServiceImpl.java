package com.smm.ctrm.bo.impl.Futures;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.Square4BrokerService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.Square4Broker;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class Square4BrokerServiceImpl implements Square4BrokerService {


    private static final Logger logger=Logger.getLogger(Square4BrokerServiceImpl.class);

    @Autowired
    private HibernateRepository<Square4Broker> repository;


    @Autowired
    private HibernateRepository<Position4Broker> position4BrokerRepository;

    @Autowired
    private CommonService commonService;

    @Override
    public ActionResult<Square4Broker> GetById(String id) {

        ActionResult<Square4Broker> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,Square4Broker.class));

        return result;
    }

    @Override
    public ActionResult<String> Save(Square4Broker position) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(position);

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

            this.repository.PhysicsDelete(id,Square4Broker.class);

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
    public ActionResult<String> SqaureMannually(List<Position4Broker> longs, List<Position4Broker> shorts) {

        ActionResult<String> result= new ActionResult<>();

        result.setSuccess(false);
        result.setMessage("功能待实现");

        return result;
    }

    @Override
    public ActionResult<String> SqaureAutomatically() {

        DetachedCriteria where=DetachedCriteria.forClass(Position4Broker.class);
        where.add(Restrictions.eq("IsAccounted", false));
        where.add(Restrictions.eq("IsSquared", false));
        where.add(Restrictions.ne("QuantityUnSquared", 0));

        List<Position4Broker> list=this.position4BrokerRepository.GetQueryable(Position4Broker.class).where(where).toList();

        List<Position4Broker> longs=list.stream().filter(p -> p.getLS().equals(ActionStatus.LS_LONG) && p.getQuantityUnSquared().compareTo(BigDecimal.ZERO)==1).collect(Collectors.toList());

        List<Position4Broker> shorts=list.stream().filter(p -> p.getLS().equals(ActionStatus.LS_SHORT) && p.getQuantityUnSquared().compareTo(BigDecimal.ZERO)==1).collect(Collectors.toList());

        return this.commonService.Square4Broker(longs, shorts);
    }

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Square4Broker.class);
	}

	@Override
	public List<Square4Broker> PositionsSquared(Criteria criteria, int pageSize, int pageIndex, RefUtil total,
			String sortBy, String orderBy) {
		return repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}
}

package com.smm.ctrm.bo.impl.Futures;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Futures.PositionGatherService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.PositionGather;
import com.smm.ctrm.domain.apiClient.PositionGatherParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Service
public class PositionGatherServiceImpl implements PositionGatherService{

	@Resource
	HibernateRepository<PositionGather> posotionGatherRepo;
	
	@Override
	public ActionResult<String> Save(PositionGather positionGather) {
		posotionGatherRepo.Save(positionGather);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<List<PositionGather>> Pager(PositionGatherParams param) {
		Criteria criteria = posotionGatherRepo.CreateCriteria(PositionGather.class);
		if(StringUtils.isNotBlank(param.getBrokerId())){
			criteria.add(Restrictions.eq("BrokerId", param.getBrokerId()));
		}
		if(StringUtils.isNotBlank(param.getInstrumentId())){
			criteria.add(Restrictions.eq("InstrumentId", param.getInstrumentId()));
		}
		if (param.getTradeStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getTradeStartDate()));
		}
		if (param.getTradeEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getTradeEndDate()));
		}
		RefUtil total = new RefUtil();
		return posotionGatherRepo.GetPage(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(), param.getOrderBy(), total);
	}

	@Override
	public ActionResult<String> Delete(String id) {
		posotionGatherRepo.PhysicsDelete(id, PositionGather.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

}

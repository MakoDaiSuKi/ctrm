package com.smm.ctrm.bo.Futures;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.CpSplitPosition4Broker;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.apiClient.Position4BrokerParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
public interface Position4BrokerService {
	ActionResult<String> Square();
	Criteria GetCriteria();
	ActionResult<String> Save(Position4Broker position);
	ActionResult<String> Delete(String id);
	ActionResult<String> SplitPosition(CpSplitPosition4Broker cpSplitPosition, String userId);
	ActionResult<Position4Broker> GetById(String positionId);
	ActionResult<String> GenerateBrokerPosition();
	List<Position4Broker> Positions(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);
	ActionResult<String> posDelivery(Position4BrokerParams pos4bParams);

}
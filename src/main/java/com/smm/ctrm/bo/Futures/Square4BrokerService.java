package com.smm.ctrm.bo.Futures;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface Square4BrokerService {
	Criteria GetCriteria();
	ActionResult<Square4Broker> GetById(String id);
	ActionResult<String> Save(Square4Broker position);
	ActionResult<String> Delete(String id);
	ActionResult<String> SqaureMannually(List<Position4Broker> longs, List<Position4Broker> shorts);
	ActionResult<String> SqaureAutomatically();
	List<Square4Broker> PositionsSquared(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);

}
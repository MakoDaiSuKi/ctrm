package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface BrokerService {
	ActionResult<Broker> Save(Broker broker);
	ActionResult<String> Delete(String id);
	ActionResult<List<Broker>> Brokers();
	ActionResult<List<Broker>> BackBrokers();
	ActionResult<Broker> GetById(String id);

}
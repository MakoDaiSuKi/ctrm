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
public interface CommodityService {
	ActionResult<Commodity> Save(Commodity commodity);
	ActionResult<String> Delete(String id);
	ActionResult<List<Commodity>> Commodities();
	ActionResult<List<Commodity>> BackCommodities();
	ActionResult<Commodity> GetById(String id);

}
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
public interface MarketService {
	ActionResult<List<Market>> Markets();
	ActionResult<List<Market>> BackMarkets();
	ActionResult<Market> Save(Market org);
	ActionResult<String> Delete(String id);
	ActionResult<Market> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);
	ActionResult<List<Market>> SpotMarkets();
	ActionResult<List<Market>> BackSpotMarkets();
	ActionResult<List<Market>> FuturesMarkets();
	ActionResult<List<Market>> BackFuturesMarkets();

}
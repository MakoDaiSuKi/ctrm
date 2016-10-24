package com.smm.ctrm.bo.Report;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Physical.DailyPosition;
import com.smm.ctrm.domain.Report.*;
public interface SpotReportService {
	ActionResult<List<R1>> R1Report();
	ActionResult<List<R2>> R2Report();
	ActionResult<List<R3>> R3Report();
	ActionResult<List<R3>> R4Report();
	ActionResult<List<R5>> R5Report();
	ActionResult<List<R5>> R6Report();
	ActionResult<List<R7>> R7Report();
	ActionResult<List<R7>> R8Report();
	ActionResult<List<DailyPosition>> DailyPositionReportSave(List<DailyPosition> daily);
	void TradeReport(String CommodityId, String ProductName, String LegalId, Date dtStart, String BrandId, Date dtEnd);
	void UpdateModelStorage(String corporationId, String warehouseId, String brandId, Integer storageType, String commodityId);
	ActionResult<ModelStorage> Save(ModelStorage modelStorage);
	ActionResult<String> Delete(String id);
	ActionResult<List<ModelStorage>> ModelStorages();
	ActionResult<ModelStorage> GetById(String id);

}
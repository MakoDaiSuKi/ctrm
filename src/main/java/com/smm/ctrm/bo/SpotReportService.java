package com.smm.ctrm.bo;

import com.smm.ctrm.domain.Physical.DailyPosition;
import com.smm.ctrm.domain.Report.*;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.dto.res.ActionResult;

import java.util.Date;
import java.util.List;

/**
 * Created by zhenghao on 2016/4/21.
 *
 */
public interface SpotReportService {


    ActionResult<List<R1>> R1Report();

    ActionResult<List<R2>> R2Report();

    ActionResult<List<R3>> R3Report();

    ActionResult<List<R3>> R4Report();

    ActionResult<List<R5>> R5Report();

    ActionResult<List<R5>> R6Report();

    ActionResult<List<R7>> R7Report();

    ActionResult<List<R8>> R8Report();

    List<R11> R11Report();

    List<R12> R12Report();

    List<R13> R13Report();

    List<R14> R14Report();


    List<PositionApprove> PositionApproveReport(Date dtEnd, String brokerIds);

    List<PositionDetail> PositionDetailReport(Date dtEnd, String brokerIds);

    ActionResult<List<DailyPosition>> DailyPositionReportSave(List<DailyPosition> daily);

    List<DailyPosition> DailyPositionReportSearch(Date dtEnd, String brokerIds);

    List<EstUnHedge> EstUnHedgeReport(Date startTime, Date endTime);

    TradeReport TradeReport(Date dtStart, Date dtEnd, String BrandId, String LegalId,String CommodityId,String ProductName);

	void UpdateModelStorage(String corporationId, String commodityId, String brandId, String warehouseId,
			int storageType);
	ActionResult<List<LotPnl>> LotPnlQuery(MtmParams params);

}

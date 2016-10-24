
package com.smm.ctrm.api.Report;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.SpotReportService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.domain.Physical.DailyPosition;
import com.smm.ctrm.domain.Report.EstUnHedge;
import com.smm.ctrm.domain.Report.PositionApprove;
import com.smm.ctrm.domain.Report.PositionDetail;
import com.smm.ctrm.domain.Report.R1;
import com.smm.ctrm.domain.Report.R11;
import com.smm.ctrm.domain.Report.R12;
import com.smm.ctrm.domain.Report.R13;
import com.smm.ctrm.domain.Report.R14;
import com.smm.ctrm.domain.Report.R2;
import com.smm.ctrm.domain.Report.R3;
import com.smm.ctrm.domain.Report.R5;
import com.smm.ctrm.domain.Report.R7;
import com.smm.ctrm.domain.Report.R8;
import com.smm.ctrm.domain.Report.TradeReport;
import com.smm.ctrm.domain.Report.LotPnl;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.domain.apiClient.StorageParams;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Report/SpotReport/")
public class SpotReportApiController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private CommonService commonService;

	@Resource
	private SpotReportService spotReportService;
	@Resource
	private LotService lotService;

	@RequestMapping("R1Report")
	@ResponseBody
	public ActionResult<List<R1>> R1Report(HttpServletRequest request) {
		return spotReportService.R1Report();
	}

	@RequestMapping("R2Report")
	@ResponseBody
	public ActionResult<List<R2>> R2Report(HttpServletRequest request) {
		return spotReportService.R2Report();
	}

	@RequestMapping("R3Report")
	@ResponseBody
	public ActionResult<List<R3>> R3Report(HttpServletRequest request) {
		return spotReportService.R3Report();
	}

	@RequestMapping("R4Report")
	@ResponseBody
	public ActionResult<List<R3>> R4Report(HttpServletRequest request) {
		return spotReportService.R4Report();
	}

	@RequestMapping("R5Report")
	@ResponseBody
	public ActionResult<List<R5>> R5Report(HttpServletRequest request) {
		return spotReportService.R5Report();
	}

	@RequestMapping("R6Report")
	@ResponseBody
	public ActionResult<List<R5>> R6Report(HttpServletRequest request) {
		return spotReportService.R6Report();
	}

	@RequestMapping("R7Report")
	@ResponseBody
	public ActionResult<List<R7>> R7Report(HttpServletRequest request) {
		return spotReportService.R7Report();
	}

	@RequestMapping("R8Report")
	@ResponseBody
	public ActionResult<List<R8>> R8Report(HttpServletRequest request) {
		return spotReportService.R8Report();
	}

	@RequestMapping("R11Report")
	@ResponseBody
	public ActionResult<List<R11>> R11Report(HttpServletRequest request) {
		try {
			List<R11> r11=spotReportService.R11Report();
			return new ActionResult<>(Boolean.TRUE, "", r11);
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<List<R11>> tempVar = new ActionResult<List<R11>>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("R12Report")
	@ResponseBody
	public ActionResult<List<R12>> R12Report(HttpServletRequest request) {
		try {
			return new ActionResult<>(Boolean.TRUE, "", spotReportService.R12Report());
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			ActionResult<List<R12>> tempVar = new ActionResult<List<R12>>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保值日期与预计销售日期不一致的批次列表
	 * 
	 * @return
	 * 
	 * @return
	 */
	@RequestMapping("R13Report")
	@ResponseBody
	public ActionResult<List<R13>> R13Report(HttpServletRequest request) {
		try {
			return new ActionResult<>(Boolean.TRUE, "", spotReportService.R13Report());
		} catch (RuntimeException ex) {
			ActionResult<List<R13>> tempVar = new ActionResult<List<R13>>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * SPBOOK
	 * 
	 * @return
	 * 
	 * @return
	 */
	@RequestMapping("R14Report")
	@ResponseBody
	public ActionResult<List<R14>> R14Report(HttpServletRequest request) {
		try {
			return new ActionResult<>(Boolean.TRUE, "", spotReportService.R14Report());
		} catch (RuntimeException ex) {
			ActionResult<List<R14>> tempVar = new ActionResult<List<R14>>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * SPBOOK
	 * 
	 * @return
	 * 
	 * @return
	 */
	@RequestMapping("TradeReportSale")
	@ResponseBody
	public ActionResult<TradeReport> TradeReportSale(HttpServletRequest request, @RequestBody StorageParams param) {
		try {
			ActionResult<TradeReport> tempVar = new ActionResult<TradeReport>();
			tempVar.setSuccess(Boolean.TRUE);
			tempVar.setData(spotReportService.TradeReport(param.getStartDate(), param.getEndDate(), param.getBrandId(),
					param.getLegalId(), param.getCommodityId(), param.getProductName()));
			return tempVar;
		} catch (Exception ex) {
			ActionResult<TradeReport> tempVar = new ActionResult<TradeReport>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}
	}

	@RequestMapping("PositionApproveReport")
	@ResponseBody
	public ActionResult<List<PositionApprove>> PositionApproveReport(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			
			List<PositionApprove> result = spotReportService.PositionApproveReport(param.getEndDate(),param.getBrokerIds());
			
			ActionResult<List<PositionApprove>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.TRUE);
			tempVar.setData(spotReportService.PositionApproveReport(param.getEndDate(), param.getBrokerIds()));
			return tempVar;
		} catch (Exception ex) {
			ActionResult<List<PositionApprove>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}

	}

	@RequestMapping("PositionDetailReport")
	@ResponseBody
	public ActionResult<List<PositionDetail>> PositionDetailReport(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			ActionResult<List<PositionDetail>> tempVar = new ActionResult<>();
			commonService.GenerateBrokerPosition();
			tempVar.setSuccess(Boolean.TRUE);
			tempVar.setData(spotReportService.PositionDetailReport(param.getEndDate(), param.getBrokerIds()));

			return tempVar;

		} catch (RuntimeException ex) {
			ActionResult<List<PositionDetail>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}

	}

	@RequestMapping("DailyPositionReportSave")
	@ResponseBody
	public ActionResult<List<DailyPosition>> DailyPositionReportSave(HttpServletRequest request,
			@RequestBody List<DailyPosition> daily) {
		return spotReportService.DailyPositionReportSave(daily);
	}

	@RequestMapping("DailyPositionReportSearch")
	@ResponseBody
	public ActionResult<List<DailyPosition>> DailyPositionReportSearch(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			commonService.GenerateBrokerPosition();
			ActionResult<List<DailyPosition>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.TRUE);
			tempVar.setData(spotReportService.DailyPositionReportSearch(param.getEndDate(), param.getBrokerIds()));
			return tempVar;

		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<List<DailyPosition>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	@RequestMapping("EstUnHedgeReport")
	@ResponseBody
	public ActionResult<List<EstUnHedge>> EstUnHedgeReport(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			ActionResult<List<EstUnHedge>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.TRUE);
			tempVar.setData(spotReportService.EstUnHedgeReport(param.getStartDate(), param.getEndDate()));
			return tempVar;

		} catch (RuntimeException ex) {
			ActionResult<List<EstUnHedge>> tempVar = new ActionResult<>();
			tempVar.setSuccess(Boolean.FALSE);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	//批次盈亏查询
	@RequestMapping("LotPnlQuery")
	@ResponseBody
	public ActionResult<List<LotPnl>> LotPnlQuery(HttpServletRequest request,
			@RequestBody MtmParams param) {		
		try {
			return spotReportService.LotPnlQuery(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	// 不清楚是否需要下面的api
	@RequestMapping("UpdateModelStorage")
	@ResponseBody
	public String UpdateModelStorage(String corporationId, String commodityId, String brandId, String warehouseId,
			int storageType) {
		return null;
	}
	
	
}
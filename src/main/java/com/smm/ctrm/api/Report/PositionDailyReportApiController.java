package com.smm.ctrm.api.Report;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Report.PositionDailyReportService;
import com.smm.ctrm.domain.apiClient.PositionDailyReportParam;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyReport;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Report/PositionDailyReport")
public class PositionDailyReportApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	PositionDailyReportService dailyReportService;

	@RequestMapping("Today")
	@ResponseBody
	public ActionResult<PositionDailyReport> Today(@RequestBody PositionDailyReportParam dailyReportParams) {
//		List<PositionDailyBase> AddedTodayList = new ArrayList<>();
		if (dailyReportParams.getBeginDate() == null || dailyReportParams.getEndDate() == null)
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.ParamError + "开始或结束时间为空");

		try {
			PositionDailyReport report = dailyReportService.getDailyReport(dailyReportParams);
			return new ActionResult<>(Boolean.TRUE, "", report);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

}

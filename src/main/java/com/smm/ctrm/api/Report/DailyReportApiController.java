package com.smm.ctrm.api.Report;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Report.DailyReportService;
import com.smm.ctrm.domain.apiClient.DailyReportParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.DailyReport;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Report/DailyReport")
public class DailyReportApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	DailyReportService dailyReportService;

	@RequestMapping("Today")
	@ResponseBody
	public ActionResult<DailyReport> Today(@RequestBody DailyReportParams dailyReportParams) {
		if(StringUtils.isBlank(dailyReportParams.getFlag()) || dailyReportParams.getFlag().length() != 7) {
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.ParamError + "标记为空，或长度错误");
		}
		if(dailyReportParams.getBeginDate()==null || dailyReportParams.getEndDate()==null)
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.ParamError+"开始或结束时间为空");
		
		try {
			DailyReport report = dailyReportService.getDailyReport(dailyReportParams);
			return new ActionResult<>(Boolean.TRUE, "", report);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

}

package com.smm.ctrm.bo.Report;

import java.io.IOException;
import java.util.Date;

import com.smm.ctrm.domain.apiClient.PositionDailyReportParam;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyReport;

public interface PositionDailyReportService {

	public PositionDailyReport getDailyReport(PositionDailyReportParam dailyReportParams) throws IOException;

	public void DailyToJson(Date date) throws IOException;

	void clearSession();
}

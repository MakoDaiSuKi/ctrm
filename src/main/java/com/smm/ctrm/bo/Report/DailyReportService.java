package com.smm.ctrm.bo.Report;

import java.io.IOException;
import java.util.Date;

import com.smm.ctrm.domain.apiClient.DailyReportParams;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.DailyReport;

public interface DailyReportService {
	public DailyReport getDailyReport(DailyReportParams dailyReportParams) throws IOException;
	
	public void DailyToJson(String Flag,Date date) throws IOException;

	void clearSession();
}

package com.smm.ctrm.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.smm.ctrm.bo.Basis.CustomerBalanceService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.bo.Report.DailyReportService;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.DailyReport;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;

public class CustomerBalanceJob {
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@Resource
	CustomerBalanceService customerBalanceService;
	
	@Resource
	StorageService storageService;
	
	public synchronized void execute() {
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】开始生成客户资金余额");
		try {
			List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
			
			if(dataSourceKey.size()>0){
				dataSourceKey.forEach(p->{
					try {
						//设置数据源
						logger.info("设置数据源:"+p);
						DataSourceContextHolder.setDataSourceType(p);
						customerBalanceService.getCustomerBalanceForJob(null);
						customerBalanceService.clearSession(); // session级别缓存未区分多数据源，因此要清空
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】结束生成客户资金余额");
	}
	
}

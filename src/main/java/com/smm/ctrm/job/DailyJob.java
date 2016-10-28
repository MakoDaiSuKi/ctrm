package com.smm.ctrm.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.bo.Report.DailyReportService;
import com.smm.ctrm.bo.Report.PositionDailyReportService;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.DailyReport;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;

public class DailyJob {
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@Resource
	DailyReportService dailyReportService;
	
	@Resource
	StorageService storageService;
	
	@Resource
	PositionDailyReportService positionDailyReportService;
	
	
	public synchronized void execute() {
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】开始生成采购和销售日报");
		try {
			List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
			
			Date date=new Date();
			if(dataSourceKey.size()>0){
				dataSourceKey.forEach(p->{
					try {
						//设置数据源
						logger.info("设置数据源:"+p);
						DataSourceContextHolder.setDataSourceType(p);
						dailyReportService.DailyToJson(DailyReport.BUY,date);
						dailyReportService.DailyToJson(DailyReport.SELL,date);
						dailyReportService.clearSession(); // session级别缓存未区分多数据源，因此要清空
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】结束生成采购和销售日报");
	}
	
	public synchronized void StoragesHolding() {
		try {
			logger.info("开始生成库存统计");
			List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
			if(dataSourceKey.size()>0){
				dataSourceKey.forEach(p->{
					try {
						//设置数据源
						logger.info("设置数据源:"+p);
						DataSourceContextHolder.setDataSourceType(p);
						storageService.DailyStoragesHoldingToJson();
						storageService.clearSession();  // session级别缓存未区分多数据源，因此要清空
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
			}
			logger.info("结束生成库存统计");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	@Transactional
	public synchronized void PositionDailyReport() {
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】开始生成头寸日报");
		try {
			List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
			
			if(dataSourceKey.size()>0){
				dataSourceKey.forEach(p->{
					try {
						//设置数据源
						logger.info("设置数据源:"+p);
						DataSourceContextHolder.setDataSourceType(p);
						positionDailyReportService.DailyToJson(new Date());
						positionDailyReportService.clearSession(); // session级别缓存未区分多数据源，因此要清空
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】结束生成头寸日报");
	}
}

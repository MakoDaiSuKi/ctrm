package com.smm.ctrm.job;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.smm.ctrm.bo.Physical.SpotPriceEstimateService;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;

public class SpotPriceEstimateJob {
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@Autowired
	private SpotPriceEstimateService spotPriceEstimateService;
	
	public synchronized void execute() {
		logger.info("【" + DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】开始生成现货暂估");
		try {
			List<String> dataSourceKey = DataSourceConfig.getDatasourcenamelist();

			if (dataSourceKey.size() > 0) {
				dataSourceKey.forEach(p -> {
					try {
						// 设置数据源
						logger.info("设置数据源:" + p);
						DataSourceContextHolder.setDataSourceType(p);
						spotPriceEstimateService.everyDaySpotPrice();
						spotPriceEstimateService.clearSession(); // session级别缓存未区分多数据源，因此要清空
					} catch (Exception e) {
						e.printStackTrace();
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【" + DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】结束生成现货暂估");
	}
}

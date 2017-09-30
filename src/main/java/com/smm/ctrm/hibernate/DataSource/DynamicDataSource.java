package com.smm.ctrm.hibernate.DataSource;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

public class DynamicDataSource extends AbstractRoutingDataSource {
	/*
	 * 该方法必须要重写 方法是为了根据数据库标示符取得当前的数据库
	 */
	@Override
	public Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceName();
	}

	@Override
	public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
		super.setDataSourceLookup(dataSourceLookup);
	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		DataSourceConfig config = new DataSourceConfig();
		Map<Object, Object> map = config.builder();
		super.setTargetDataSources(map);
		if (map.size() > 0) {
			super.setDefaultTargetDataSource(config.getDefaultDataSource());
		}
		// 重点
		super.afterPropertiesSet();
	}

}
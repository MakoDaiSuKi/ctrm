package com.smm.ctrm.bo;

import org.hibernate.cache.redis.DataSourceContextHolderKey;

import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;

	public class DataSourceContextHolderKeyImpl extends DataSourceContextHolderKey {
	
	@Override
	public String getKey() {
		return DataSourceContextHolder.getDataSourceName();
	}
	
}

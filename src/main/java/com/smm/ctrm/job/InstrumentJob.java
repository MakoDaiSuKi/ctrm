package com.smm.ctrm.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;

public class InstrumentJob {

	private Logger logger = Logger.getLogger(this.getClass());

	@Value("#{ctrm['updateInstrument.URL']}")
	private String updateInstrumentUrl;

	@Resource
	RestTemplate restTemplate;

	@Resource
	HibernateRepository<Commodity> commodityRepo;

	@Resource
	HibernateRepository<Market> marketRepo;

	@Resource
	HibernateRepository<Instrument> instrumentRepo;

	
	public void execute() {

		try {
			logger.info("获取所有期货合约数据");
			ParameterizedTypeReference<ActionResult<List<Instrument>>> typeRef = new ParameterizedTypeReference<ActionResult<List<Instrument>>>() {
			};
			ResponseEntity<ActionResult<List<Instrument>>> responseEntity = restTemplate.exchange(updateInstrumentUrl,
					HttpMethod.POST, null, typeRef);
			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				logger.info("获取数据失败, " + responseEntity);
				return;
			}
			ActionResult<List<Instrument>> result = responseEntity.getBody();
			if (!result.isSuccess()) {
				logger.info("服务端返回错误，" + result.getMessage());
				return;
			}
			List<String> dataSourceKey = DataSourceConfig.getDatasourcenamelist();
			if (dataSourceKey.size() > 0) {
				dataSourceKey.forEach(p -> {
					// 设置数据源
					logger.info("将要更新:" + p);
					DataSourceContextHolder.setDataSourceType(p);
					doSingleOne(result.getData());
					instrumentRepo.Clear(); // session级别缓存未区分多数据源，因此要清空
				});
			}

		} catch (Exception ex) {
			logger.error(DataSourceContextHolder.getDataSourceName() + ", 更新期货合约数据失败！" + ex.getMessage(), ex);
		}
		logger.info("更新期货合约数据完成");
	}

	private void doSingleOne(final List<Instrument> sourceList) {

		List<Commodity> commodityList = commodityRepo.GetList(Commodity.class);

		Map<String, Commodity> commodityCodeMap = new HashMap<>();
		for (Commodity commodity : commodityList) {
			commodityCodeMap.put(commodity.getCode().toLowerCase(), commodity);
		}

		List<Market> marketList = marketRepo.GetList(Market.class);

		Map<String, Market> marketCodeMap = new HashMap<>();
		for (Market market : marketList) {
			marketCodeMap.put(market.getCode().toLowerCase(), market);
		}
		List<Instrument> instrumentList = instrumentRepo.GetList(Instrument.class);
		Map<String, Instrument> instrumentCodeMap = new HashMap<>();
		for (Instrument instrument : instrumentList) {
			instrumentCodeMap.put(instrument.getCode().toLowerCase(), instrument);
		}

		for (Instrument ins : sourceList) {
			if (ins.getName() != null && ins.getName().length() >= 2) {
				Commodity commodity = commodityCodeMap.get(ins.getName().substring(0, 2).toLowerCase());
				if (commodity != null) {
					ins.setCommodityId(commodity.getId());
				}
			}
			if (ins.getMarket() != null && (ins.getMarket().getCode() != null || ins.getMarketCode() != null)) {
				String marketCode = null;
				if (ins.getMarket().getCode() != null) {
					marketCode = ins.getMarket().getCode();
				} else if (ins.getMarketCode() != null) {
					marketCode = ins.getMarketCode();
				}
				if (marketCode != null) {
					Market market = marketCodeMap.get(marketCode.toLowerCase());
					if (market != null) {
						ins.setMarketId(market.getId());
						ins.setMarketCode(market.getCode());
						ins.setMarketName(market.getName());
					}
				}
			}
			ins.setUpdatedAt(null);
			ins.setUpdatedBy(null);
			ins.setId(null);
			if (ins.getCode() != null) {
				Instrument instrument = instrumentCodeMap.get(ins.getCode().toLowerCase());
				if (instrument == null) {
					instrumentRepo.SaveOrUpdate(ins);
				}
			}
		}
	}
	
}

package com.smm.ctrm.controller;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.apiClient.ContractParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.hibernate.DataSource.DynamicDataSource;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.RedisUtil;

@org.springframework.stereotype.Controller
@RequestMapping("api")
@ResponseBody
public class Controller {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	DynamicDataSource dynamicDataSource;

	@Resource
	HibernateRepository<User> userRepo;
	
	@Resource
	private ContractService contractService;

	@RequestMapping("test")
	@ResponseBody
	public ActionResult<String> isRunNormal() {
		String message = MessageFormat.format("服务器正常运行中。项目路径：{0}；加载的机构有：{1}；在线用户：{2}", System.getProperty("smm.ctrm"),
				DataSourceConfig.getDatasourcenamelist(), getOnlineUsersStr());
		return new ActionResult<>(Boolean.TRUE, message);
	}

	private String getOnlineUsersStr() {
		Set<String> tokenSet = RedisUtil.keys("_AUTH_*");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(String token : tokenSet) {
			sb.append(token.substring(6) + ",");
		}
		return sb.substring(0, sb.length() - 1) + "]";
	}

	@RequestMapping("refreshDatasource")
	@ResponseBody
	public ActionResult<String> refreshDynamicDataSource() {
		try {
			dynamicDataSource.setTargetDataSources(null);
			return new ActionResult<>(Boolean.TRUE, "数据源刷新成功,现有数据源： " + DataSourceConfig.getDatasourcenamelist());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, "数据源刷新失败 " + ex.getMessage());
		}
	}

	@RequestMapping("clearSecondLevelCache")
	@ResponseBody
	public ActionResult<String> clearSecondLevelCache() {
		try {
			Set<String> loginToken = RedisUtil.keys("_AUTH_*");
			Map<String, String> tokenValueMap = new HashMap<>();
			for (String token : loginToken) {
				tokenValueMap.put(token, RedisUtil.get(token));
			}
			RedisUtil.flushDB();
			for (Entry<String, String> entry : tokenValueMap.entrySet()) {
				RedisUtil.set(entry.getKey(), entry.getValue());
			}
			return new ActionResult<>(Boolean.TRUE, "清空二级缓存成功 ");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, "清空二级缓存失败 " + ex.getMessage());
		}
	}

	@RequestMapping("exec")
	@ResponseBody
	public ActionResult<String> exec(String sql) {
		String prefix = "password:123456";
		if (StringUtils.isBlank(sql) || !sql.startsWith(prefix)) {
			return new ActionResult<>(Boolean.TRUE, "请输入密码");
		}
		logger.info("【" + DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】开始执行");
		String str = sql.substring(prefix.length());
		List<String> dataSourceKey = DataSourceConfig.getDatasourcenamelist();
		if (dataSourceKey.size() > 0) {
			dataSourceKey.forEach(p -> {
				try {
					// 设置数据源
					logger.info("设置数据源:" + p);
					DataSourceContextHolder.setDataSourceType(p);
					userRepo.ExecuteNonQuery(str);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return new ActionResult<>(Boolean.TRUE, "执行成功 ");
	}
	
	/** 桑基图
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("getSanKey")
	@ResponseBody
	public ActionResult<String> getSanKey(ContractParams param) {
		try {
			return contractService.getSanKey(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

}

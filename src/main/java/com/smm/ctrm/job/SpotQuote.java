package com.smm.ctrm.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.smm.ctrm.bo.Maintain.DSMEService;
import com.smm.ctrm.util.DateUtil;

/**
 * 现货行情
 * 
 * @author zengshihua
 *
 */
public class SpotQuote {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private DSMEService dsmeService;

	public void execute() {
		logger.info("【" + DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】开始SMM现货行情数据接入");
		try {
			dsmeService.fetchQuote();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【" + DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "】结束SMM现货行情数据接入");
	}
}

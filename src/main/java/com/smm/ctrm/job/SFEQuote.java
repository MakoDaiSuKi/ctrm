package com.smm.ctrm.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.smm.ctrm.bo.Maintain.SFEService;
import com.smm.ctrm.util.DateUtil;

/**
 * 
 * 上期所行情数据接入
 * 
 * @author zengshihua
 *
 */
public class SFEQuote {

	private Logger logger=Logger.getLogger(this.getClass());
	
	@Autowired
	private SFEService sfeService;
	
	public void execute() {
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】开始上期所行情数据接入");
		try {
			sfeService.fetchQuote();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("【"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"】结束上期所行情数据接入");
	}
}

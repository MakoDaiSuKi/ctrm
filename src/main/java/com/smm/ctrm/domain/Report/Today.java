package com.smm.ctrm.domain.Report;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zhaoyutao
 * end是明天的最开始
 */
public class Today {
	
	/**
	 * 今天的最开始，yyyy-MM-dd 00:00:00.000
	 */
	private Date start;
	
	/**
	 * 明天的最开始，yyyy-MM-dd+1 00:00:00.000
	 */
	private Date end;
	
	/**
	 * @return
	 */
	public static Today build() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Today today = new Today();
		today.start = calendar.getTime();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		today.end = calendar.getTime();
		return today;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}
}

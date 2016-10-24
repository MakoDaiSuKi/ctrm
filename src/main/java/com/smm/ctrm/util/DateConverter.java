package com.smm.ctrm.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @author zengshihua
 *
 */
public class DateConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return value;
		}
		if (value instanceof Long) {
			Long longValue = (Long) value;
			return new Date(longValue.longValue());
		}
		if (value instanceof String) {
			if (StringUtils.isNumeric((String) value)) {
				return new Date(Long.valueOf((String) value));
			}
			Date endTime = null;
			try {
				if (((String) value).length() > 19) {
					value = ((String) value).substring(0, 19);
				}
				endTime = DateUtils.parseDate(value.toString(),
						new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss" });
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return endTime;
		}
		return null;
	}
}

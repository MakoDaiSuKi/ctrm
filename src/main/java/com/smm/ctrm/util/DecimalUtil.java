package com.smm.ctrm.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalUtil {
	public static BigDecimal nullToZero(BigDecimal bg) {
		return bg == null ? BigDecimal.ZERO : bg;
	}

	public static BigDecimal getProcedureData(Object obj) {
		if (obj == null) {
			return BigDecimal.ZERO;
		} else {
			return (BigDecimal) obj;
		}
	}

	public static BigDecimal divideForQuantity(BigDecimal x, BigDecimal y) {
		return x.divide(y, 3, RoundingMode.HALF_UP);
	}

	public static BigDecimal divideForPrice(BigDecimal x, BigDecimal y) {
		if(y.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO;
		return x.divide(y, 4, RoundingMode.HALF_UP);
	}

	public static String getThousandsSeparator(BigDecimal bg, int scale) {
		if (bg == null) {
			return "";
		}
		String formatStr = ",##0.";
		for (int i = 0; i < scale; i++) {
			formatStr += "0";
		}
		DecimalFormat df = new DecimalFormat(formatStr); // 这里的formatType可以写成
															// #,###.00
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		return df.format(bg);
	}

	/**
	 * 通用计算方法 四舍五入
	 * 
	 * @param value
	 * @param deci
	 * @return
	 */
	public static BigDecimal Round4s5r(BigDecimal bg, int deci) {
		return bg.setScale(deci, RoundingMode.HALF_UP);
	}
}

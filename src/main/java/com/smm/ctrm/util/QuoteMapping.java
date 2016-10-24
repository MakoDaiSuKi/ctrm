package com.smm.ctrm.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zengshihua
 *
 */
public class QuoteMapping {

	public static final String MARKET_CODE_SMM = "SMM";

	/**
	 * ProductID
	 */
	public static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("CU", "201102250376");
		map.put("AL", "201102250376");
		map.put("PB", "201102250376");
		map.put("ZN", "201102250376");
		map.put("NI", "201102250376");
		map.put("SN", "201102250376");
		map.put("AU", "201102250376");
		map.put("AG", "201102250376");
		map.put("CO", "201102250376");
		map.put("MN", "201102250376");
		map.put("SI", "201102250376");
		map.put("BI", "201102250376");
		map.put("MG", "201102250376");
		map.put("SB", "201102250376");
		map.put("QJK", "201102250376");
		map.put("MO", "201102250376");
		map.put("IN", "201102250376");
		map.put("DU", "201102250376");
		map = Collections.unmodifiableMap(map);
	}

}

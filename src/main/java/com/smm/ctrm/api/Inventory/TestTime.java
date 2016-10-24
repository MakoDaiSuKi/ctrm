package com.smm.ctrm.api.Inventory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestTime {
	private static long startTime;

	private final static Map<String, Long> mileStones = new LinkedHashMap<>();

	public static String result() {
		StringBuilder sb = new StringBuilder();
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		sb.append("\n"+df.format(startTime) + " 开始时间" + "\n");
		for(Entry<String, Long> entry : mileStones.entrySet()) {
			sb.append(df.format(entry.getValue()) + " " + entry.getKey() +"\n");
		}
		sb.append(df.format(System.currentTimeMillis()) + " 结束时间");
		return sb.toString();
	}

	public static void start(){
		mileStones.clear();
		TestTime.startTime = System.currentTimeMillis();
	}

	public static void addMilestone(String text) {
		mileStones.put(text, System.currentTimeMillis());
	}
}

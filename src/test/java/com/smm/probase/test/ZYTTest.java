package com.smm.probase.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZYTTest {
	
	private static boolean stopRequest;
	
//	public static synchronized boolean isStopRequest() {
//		return stopRequest;
//	}
//
//	public static void setStopRequest(boolean stopRequest) {
//		ZYTTest.stopRequest = stopRequest;
//	}

	public static void abc(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte i = 0;
				Date start = new Date(System.currentTimeMillis());
				while (!stopRequest) {
					i++;
				}
				Date end = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
				System.out.println(sdf.format(end) + "\n" + sdf.format(start));
			}
		}).start();
		TimeUnit.SECONDS.sleep(1);
		stopRequest = Boolean.TRUE;
	}
	
	
	public static void main(String[] args) {
		List<String> srcList = new ArrayList<>();
		srcList.add("1");
		srcList.add("4");
		srcList.add("x");
		srcList.add("b");
		
		List<String> desc = new ArrayList<>();
		
		
	}
	
}

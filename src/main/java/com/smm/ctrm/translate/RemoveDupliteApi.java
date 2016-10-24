package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RemoveDupliteApi {
	private final static String path = "C:/Users/zhaoyutao/work/ctrm/ctrm/src/main/resources/javaApiForCSharp.json";
	private final static String pathOut = "C:/Users/zhaoyutao/work/out.txt";

	public static void main(String[] args) throws Exception {
		BufferedReader bfr = new BufferedReader(new FileReader(new File(path)));
		Set<String> set = new LinkedHashSet<>();
		String line = null;
		while ((line = bfr.readLine()) != null) {
			set.add(line.trim());
		}
		List<String> array = new ArrayList<>(set);
		array.sort((p1, p2)-> p1.compareTo(p2));
		bfr.close();
		FileOutputStream fis = new FileOutputStream(new File(pathOut));
		for (String str : array) {
			fis.write(("\t" + str + "\n").getBytes());
		}
		fis.close();
	}
}

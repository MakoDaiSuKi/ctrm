package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class TransGetSet {

	private static TransGetSet instance = new TransGetSet();

	public static void main(String[] args) throws Exception {
		File file = new File("E:\\code.java");
		instance.parse(file);
	}



	StringBuilder result = new StringBuilder();
	final String equalStr = " = ";
	final String excludeStr = "if";
	Pattern excludePattern = Pattern.compile(excludeStr);


	public void parse(File file) throws Exception {

		BufferedReader bfr = new BufferedReader(new FileReader(file));
		String line = "";
		String[] leftRight;

		while ((line = bfr.readLine()) != null) {
			if(line.contains(equalStr) && !excludePattern.matcher(line).find()) {
				leftRight = line.split(equalStr);
				String[] setM = leftRight[0].split("\\.");
				if(setM != null && setM.length > 1) {
					result.append(setM[0])
					.append(".set" + setM[1]+"(");
				}
				String[] getM = leftRight[1].split("\\.");
				if(getM != null && getM.length > 1) {
					for(int i = 0; i < getM.length; i++) {
						if(i == 0) {
							result.append(getM[0]);
							continue;
						}
						if(i == getM.length - 1) {
							result.append(".get" + getM[i].substring(0, getM[i].length() - 1)+"()");
							continue;
						}
						result.append(".get" + getM[i]+"()");
					}
				} else {
					result.append(leftRight[1]);
				}
				result.append(");\n");
			} else {
				result.append(line+"\n");
			}
			
		}

		System.out.println(result);
		bfr.close();
		printAllEntity();
	}

	private void printAllEntity() {
		
	}
}

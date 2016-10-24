package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddJsonIgnoreAndJsonProperty {
	public static AddJsonIgnoreAndJsonProperty instance = new AddJsonIgnoreAndJsonProperty();
	boolean isFileClassEnd = Boolean.FALSE;
	static String rootPath = "/Users/zhaoyutao/Documents/workspace/ctrm/ctrm/src/main/java/com/smm/ctrm/domain/apiClient";
	String PREFIX_PATH = "/Users/zhaoyutao/Documents/tmp/domain";
	File myFile = null;
	String className="";
	public static void main(String[] args) throws Exception {
		handleAllFiles(new File(rootPath));
	}
	StringBuilder fileContent= new StringBuilder();
	public static void handleAllFiles(File dir) throws Exception {
		if (dir.isDirectory()) {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				String suffix = fs[i].getName().substring(fs[i].getName().lastIndexOf(".") + 1);
				if (!fs[i].isDirectory() && suffix.equalsIgnoreCase("java")) {
					instance.parse(fs[i]);
				}
				if (fs[i].isDirectory()) {
					handleAllFiles(fs[i]);
				}
			}
		} else {
			instance.parse(dir);
		}
	}
	BufferedReader bfr;
	public void parse(File file) throws Exception {
		myFile = file;
		className = file.getName().substring(0, file.getName().lastIndexOf("."));
		hit = Boolean.FALSE;
		bfr = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = bfr.readLine()) != null && !isFileClassEnd) {
			parseLine(line);
		}
		File outFile = new File(PREFIX_PATH +"/" + file.getParentFile().getName() +"/" + file.getName());
		if(!outFile.exists()) {
			try{
			outFile.createNewFile();
			}catch(Exception ex) {
				System.out.println(outFile.getAbsolutePath());
			}
		}
		FileOutputStream fos = new FileOutputStream(outFile);
		fos.write(fileContent.toString().getBytes());
		
		if(!hit) System.out.println(file.getAbsolutePath());
		fileContent = new StringBuilder();
		bfr.close();
	}
	String jsonBackAnnotionReg = "^\\s{0,5}@JsonBackReference\\s{0,2}";
	String propertyReg = "^\\s{0,5}private\\s{0,2}[a-zA-Z0-9]+\\s{0,2}([a-zA-Z0-9]+);$";
	String parentName = "";
	String getMethod = "\\s{0,3}public\\s{0,2}[final]?[a-zA-Z0-9]+ get[a-zA-Z0-9]+\\(\\) \\{";
	
	
	Pattern jsonBackAnnotionPattern = Pattern.compile(jsonBackAnnotionReg);
	Pattern propertyPattern = Pattern.compile(propertyReg);
	Pattern getMethodPattern = Pattern.compile(getMethod);
	
	
	boolean hit = Boolean.FALSE;
	String lastLine = "";
	String PRE_Req_Map ="api";
	String CONTROLLER_STR ="@Controller";
	String requestBody = "@ResponseBody";
	public void parseLine(String line) throws Exception{
		
		line = handleLine(line);
		lastLine = line;
		fileContent.append("\n" + line);
	}
	private String handleLine(String line) {
		line = line.replace("final ", "");
		if(propertyPattern.matcher(line).find() && !lastLine.contains("@JsonProperty")) {
			String arr[] = line.trim().split(" ");
			String propertyName = arr[arr.length - 1].trim().substring(0, arr[arr.length - 1].trim().length() - 1);
			return "\t@JsonProperty(value = \"" + propertyName + "\")\n" + line;
		}
		if(getMethodPattern.matcher(line).find() && !lastLine.contains("@JsonIgnore")) {
			
			return "\t@JsonIgnore\n" + line;
		}
		return line;
	}
	
	private String handleLine1(String line) throws IOException{
		String jsonBackLine = line;
		String followLines = "";
		String currLine = "";
		String propertyName= "";
		while((currLine = bfr.readLine())!=null) {
			followLines += "\n" + currLine;
			Matcher matcher = propertyPattern.matcher(currLine);
			if(matcher.find()) {
				propertyName= matcher.group(1);
				break;
			}
		}
		return jsonBackLine + "(\""+ className +"_" + propertyName + "\")" + followLines;
	}
}

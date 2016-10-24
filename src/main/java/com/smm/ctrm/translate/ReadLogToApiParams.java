package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class ReadLogToApiParams {
	public static ReadLogToApiParams instance = new ReadLogToApiParams();
	boolean isFileClassEnd = Boolean.FALSE;
	static String rootPath = "/Users/zhaoyutao/Downloads/CsharpLog.txt";
	String API_PARAMS = "/Users/zhaoyutao/Documents/tmp/apiPara.json";
	String API_PARAMS_NO_REPEAT = "/Users/zhaoyutao/Documents/tmp/apiParaNoRepeat.json";
	Map<String, String> paramsMap = new LinkedHashMap<>();
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
		File outFile = new File(API_PARAMS);
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
		
		fos = new FileOutputStream(new File(API_PARAMS_NO_REPEAT));
		List<String> keyList = paramsMap.keySet().stream().sorted().collect(Collectors.toList());
		for(String key : keyList) {
			if(key.contains("api/Basis")) {
				fileContent.append(paramsMap.get(key));
			}
		}
		fos.write(fileContent.toString().getBytes());
		bfr.close();
	}
	String jsonBackAnnotionReg = "^\\s{0,5}@JsonBackReference\\s{0,2}$";
	String propertyReg = "^\\s{0,5}private\\s{0,2}[a-zA-Z0-9]+\\s{0,2}([a-zA-Z0-9]+);$";
	String parentName = "";
	
	Pattern jsonBackAnnotionPattern = Pattern.compile(jsonBackAnnotionReg);
	Pattern propertyPattern = Pattern.compile(propertyReg);
	private Pattern idPattern = Pattern.compile("\"[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}\"");
	
	boolean hit = Boolean.FALSE;
	String lastLine = "";
	String PRE_Req_Map ="api";
	String CONTROLLER_STR ="@Controller";
	String requestBody = "@ResponseBody";
	public void parseLine(String line) throws Exception{
		
		line = handleLine(line);
		lastLine = line;
		fileContent.append(line);
	}
	private String handleLine(String line) throws IOException{
		if(StringUtils.isBlank(line.trim())){
			return "";
		}
//		{\"url\":\"api/Basis/Market/Markets\",\"parameter\":null}
		String[] arr = line.split("\\t");
		if(idPattern.matcher(arr[2]).find()) {
			arr[2] = "\"" + arr[2] + "\"";
		}
		String rltLine = "{\"url\":\"" + arr[1] + "\",\"parameter\":" + arr[2] + "}\n";
		paramsMap.put(arr[1], rltLine);
		return rltLine;
	}
}

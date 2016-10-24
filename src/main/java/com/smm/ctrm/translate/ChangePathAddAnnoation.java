package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.ResponseBody;

public class ChangePathAddAnnoation {
	
	public static ChangePathAddAnnoation instance = new ChangePathAddAnnoation();
	boolean isFileClassEnd = Boolean.FALSE;
	static String rootPath = "/Users/zhaoyutao/Documents/workspace/ctrm/ctrm/src/main/java/com/smm/ctrm/api/";
	String PREFIX_PATH = "/Users/zhaoyutao/Documents/tmp/api";
	File myFile = null;
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
		hit = Boolean.FALSE;
		bfr = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = bfr.readLine()) != null && !isFileClassEnd) {
			parseLine(line);
		}
//		System.out.println(fileContent.toString());
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
	String classAnnotionReg = "^public\\s{0,2}class\\s{0,2}[a-zA-Z0-9]+\\s{0,2}\\{$";
	String requestMapping = "@RequestMapping";
	String importReg = "import org.springframework.web.bind.annotation.RequestMapping;";
	String parentName = "";
	
	Pattern classAnnotionPattern = Pattern.compile(classAnnotionReg);
	Pattern requestMappingPattern = Pattern.compile(requestMapping);
	Pattern importRegPattern = Pattern.compile(importReg);
	
	boolean hit = Boolean.FALSE;
	String lastLine = "";
	String PRE_Req_Map ="api";
	String CONTROLLER_STR ="@Controller";
	String requestBody = "@ResponseBody";
	public void parseLine(String line) throws Exception{
		Matcher match = requestMappingPattern.matcher(line);
		if(!hit && match.find()) {
			String nextLine = bfr.readLine();
			if(classAnnotionPattern.matcher(nextLine).find()) {
				String parentFolderName = myFile.getParentFile().getName();
				int start = line.indexOf(PRE_Req_Map);
				String rqStr = "@RequestMapping(\"" + PRE_Req_Map + "/"+parentFolderName + line.substring(start + PRE_Req_Map.length());
				line = CONTROLLER_STR +"\n" + rqStr +"\n" + nextLine;
				hit = Boolean.TRUE;
			}
		}
		if(importRegPattern.matcher(line).find()) {
			line = "import org.springframework.stereotype.Controller;" + "\n"+ line;
		}
		lastLine = line;
		fileContent.append("\n" + line);
	}
}

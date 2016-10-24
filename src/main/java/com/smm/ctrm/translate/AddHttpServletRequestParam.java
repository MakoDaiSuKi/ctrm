package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.RequestBody;

public class AddHttpServletRequestParam {
	public static AddHttpServletRequestParam instance = new AddHttpServletRequestParam();
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
	String methodReg = "^\\s{0,10}public\\s{0,2}ActionResult";
	
	Pattern classAnnotionPattern = Pattern.compile(classAnnotionReg);
	Pattern requestMappingPattern = Pattern.compile(requestMapping);
	Pattern importRegPattern = Pattern.compile(importReg);
	Pattern methodRegPattern = Pattern.compile(methodReg);
	
	boolean hit = Boolean.FALSE;
	String lastLine = "";
	String PRE_Req_Map ="api";
	String CONTROLLER_STR ="@Controller";
	String requestBody = "@ResponseBody";
	public void parseLine(String line) throws Exception{
//		Matcher match = requestMappingPattern.matcher(line);
//		if(!hit && match.find()) {
//			String nextLine = bfr.readLine();
//			if(classAnnotionPattern.matcher(nextLine).find()) {
//				String parentFolderName = myFile.getParentFile().getName();
//				int start = line.indexOf(PRE_Req_Map);
//				String rqStr = "@RequestMapping(\"" + PRE_Req_Map + "/"+parentFolderName + line.substring(start + PRE_Req_Map.length());
//				line = CONTROLLER_STR +"\n" + rqStr +"\n" + nextLine;
//				hit = Boolean.TRUE;
//			}
//		}
		
		if(methodRegPattern.matcher(line).find()) {
			line = handleLine(line);
		}
		if(importRegPattern.matcher(line).find()) {
			line = "import org.springframework.web.bind.annotation.RequestBody;" + "\n"+ line;
		}
		lastLine = line;
		fileContent.append("\n" + line);
	}
	private String handleLine(String line){
		int rightK = line.lastIndexOf(")");
		int leftK = line.lastIndexOf("(");
		List<String> paraList = Arrays.asList(line.substring(leftK + 1, rightK).split(","));
		String rltLine = "HttpServletRequest request";
		for(String para : paraList) {
			if(para.trim().startsWith("HttpServletRequest")){
				continue;
			}
			rltLine += ", @RequestBody " + para.trim();
		}
		return line.substring(0, leftK) + "(" + rltLine + line.substring(rightK);
	}
	
	
}

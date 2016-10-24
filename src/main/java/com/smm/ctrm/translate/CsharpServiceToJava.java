package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.smm.ctrm.translate.element.IServiceInterface;
import com.smm.ctrm.translate.element.ServiceInterface;
import com.smm.ctrm.translate.element.ServiceMethod;
import com.smm.ctrm.translate.element.TranslateUtils;

public class CsharpServiceToJava implements IServiceInterface{
	
	public static CsharpServiceToJava instance = new CsharpServiceToJava();
	
	String mainReg = "\\s*public\\s{1,2}ActionResult<[a-zA-Z0-9<>\\?\\.]+>|public\\s{1,2}[a-zA-Z]+";
	
	Pattern mainPattern = Pattern.compile(mainReg);
	
	String haveReturnReg = "\\s*public\\s{1,2}ActionResult<([a-zA-Z0-9<>\\?\\.]+)>\\s{1,2}([a-zA-Z0-9]+)\\((.*)\\)";
	Pattern haveReturnPattern = Pattern.compile(haveReturnReg);
	
	String voidReg = "\\s*public\\s{1,2}([a-zA-Z]+)\\s{1,2}([a-zA-Z]+)\\((.*)\\)";
	
	Pattern voidPattern = Pattern.compile(voidReg);
	
	/*public static void main(String[] args) {
		
		Matcher m = instance.voidPattern.matcher("public string GetVerifyMessages(Guid userId)");
		if(m.find()) {
			System.out.println(m.group(1)+m.group(2) +m.group(3));
		}
	}*/
	
	BufferedReader bfr;
	
	private List<ServiceInterface> serviceList = new ArrayList<>();
	
	private List<ServiceMethod> entityList = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		
		String rootPath = "F:/svn/trunk/HedgeStudio.Service";
//		rootPath += "/HedgeStudio.Service.Common/CommonService.cs";
		instance.handleAllFiles(new File(rootPath));
	}
	
	int k = 0;

	public void handleAllFiles(File dir) throws Exception {
		serviceList = new ArrayList<>();
		if (dir.isDirectory()) {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				String suffix = fs[i].getName().substring(fs[i].getName().lastIndexOf(".") + 1);
				if (!fs[i].isDirectory() && suffix.equalsIgnoreCase("cs")) {
					instance.parseService(fs[i]);
				}
				if (fs[i].isDirectory()) {
					handleAllFiles(fs[i]);
				}
			}
		} else {
			instance.parseService(dir);
		}
	}
	
	public void parseService(File file) throws Exception{
		entityList = new ArrayList<>();
		bfr = new BufferedReader(new FileReader(file));
		String line = "";
		ServiceInterface serviceObj = new ServiceInterface();
		serviceObj.setFolderName(file.getParent().substring(file.getParent().lastIndexOf(".") + 1));
		serviceObj.setServiceName(file.getName().split("\\.")[0]);
		while ((line = bfr.readLine()) != null) {
			/*k++;
			if(k >= 121) {
				System.out.println(line);
			}*/
			parseServiceLine(line);
		}
		serviceObj.setList(entityList);
		serviceList.add(serviceObj);
		bfr.close();
		printAllEntity();
	}
	
	ServiceMethod serviceMethod;
	
	private void parseServiceLine(String line) {
		
		Matcher mainMatch = mainPattern.matcher(line);
		
		if(mainMatch.find()) {
			
			String matchStr = mainMatch.group(0).trim().replace(" ", "");
			if(matchStr.startsWith("publicActionResult")){
				Matcher haveReturnMatch = haveReturnPattern.matcher(line);
				if(haveReturnMatch.find()) {
					System.out.println(k);
					if(StringUtils.isBlank(haveReturnMatch.group(3).trim())) {
						serviceMethod = new ServiceMethod();
						serviceMethod.setType(METHOD_WITHOUT_PARAMS);
						serviceMethod.setReturnStr(TranslateUtils.typeConvert(haveReturnMatch.group(1)));
						serviceMethod.setMethodName(haveReturnMatch.group(2));
						entityList.add(serviceMethod);
					} else {
						serviceMethod = new ServiceMethod();
						serviceMethod.setType(METHOD_WITH_PARAMS);
						serviceMethod.setReturnStr(TranslateUtils.typeConvert(haveReturnMatch.group(1)));
						serviceMethod.setMethodName(haveReturnMatch.group(2));
						serviceMethod.setParamsMap(TranslateUtils.convertParamStrToMap(haveReturnMatch.group(3)));
						entityList.add(serviceMethod);
					}
				}
			} else {
				
				Matcher voidMatch = voidPattern.matcher(line);
				if(voidMatch.find()) {
					System.out.println(k);
					if(StringUtils.isBlank(voidMatch.group(3).trim())) {
						serviceMethod = new ServiceMethod();
						serviceMethod.setType(VOID_METHOD_WITHOUT_PARAMS);
						serviceMethod.setReturnStr(voidMatch.group(1));
						serviceMethod.setMethodName(voidMatch.group(2));
						entityList.add(serviceMethod);
					} else {
						serviceMethod = new ServiceMethod();
						serviceMethod.setType(VOID_METHOD_WITH_PARAMS);
						serviceMethod.setReturnStr(voidMatch.group(1));
						serviceMethod.setMethodName(voidMatch.group(2));
						serviceMethod.setParamsMap(TranslateUtils.convertParamStrToMap(voidMatch.group(3)));
						entityList.add(serviceMethod);
					}
				}
			}
		}
	}
	
	private void printAllEntity() throws Exception{
		for(ServiceInterface service : serviceList) {
			File parentFile = new File(PREFIX_PATH + "/" + service.getFolderName());
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			convertToJava(service);
		}
	}
	private void convertToJava(ServiceInterface service) throws Exception {
		FileOutputStream fos = null;
		File file = new File(PREFIX_PATH + "/" + service.getFolderName() + "/"  + service.getServiceName() + ".java");
		if(!file.exists()) {
			file.createNewFile();
		}
		fos = new FileOutputStream(file);
		
		fos.write(WrieServiceCode.write(service).getBytes());
		fos.close();
	}
}

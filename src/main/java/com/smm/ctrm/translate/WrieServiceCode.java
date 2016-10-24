package com.smm.ctrm.translate;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.smm.ctrm.translate.element.IServiceInterface;
import com.smm.ctrm.translate.element.ServiceInterface;
import com.smm.ctrm.translate.element.ServiceMethod;
import com.smm.ctrm.translate.element.TranslateUtils;

public class WrieServiceCode {
	public static TranslateUtils utilInstance = new TranslateUtils();
	
	public final static String JAVA_FILE_PREFIX=""
			+ buildLineNoTab("import java.math.BigDecimal;")
			+ buildLineNoTab("import java.util.List;")
			+ buildLineNoTab("import java.util.Map;")
			+ buildLineNoTab("import java.util.Date;")
			+ buildLineNoTab("import org.hibernate.Criteria;")
			+ buildLineNoTab("import com.smm.ctrm.dto.res.ActionResult;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Basis.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Maintain.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Physical.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Report.*;")
			;
	
	public static String write(ServiceInterface service) {
		StringBuilder javaContent = new StringBuilder();
		javaContent
		.append("package com.smm.ctrm.bo." + service.getFolderName() + ";\n\n")
		.append(JAVA_FILE_PREFIX)
		.append(buildLineNoTab("public interface " + service.getServiceName() +" {"))
		.append(buildMethodCode(service));
		return javaContent.toString();
	}
	
	
	private static String buildMethodCode(ServiceInterface service) {
		StringBuilder str = new StringBuilder();
		for(ServiceMethod method : service.getList()) {
			
			if(method.getType().equals(IServiceInterface.METHOD_WITH_PARAMS)) {
				
				str.append(
						buildLine("ActionResult<" 
									+ method.getReturnStr() 
									+ "> " + method.getMethodName() 
									+"(" + getParamsStr(method.getParamsMap()) +");"));
				
			} else if(method.getType().equals(IServiceInterface.METHOD_WITHOUT_PARAMS)){
				
				str.append(
						buildLine("ActionResult<" 
									+ method.getReturnStr() 
									+ "> " + method.getMethodName() 
									+"();"));
				
			} else if(method.getType().equals(IServiceInterface.VOID_METHOD_WITH_PARAMS)){
				
				str.append(
						buildLine("void " + method.getMethodName() 
									+"(" + getParamsStr(method.getParamsMap()) +");"));
				
			} else if(method.getType().equals(IServiceInterface.VOID_METHOD_WITHOUT_PARAMS)){
				
				str.append(
						buildLine("void " + method.getMethodName() 
									+"();"));
				
			}
			
		}
		str.append("\n}");
		return str.toString();
	}
	
	private static String getParamsStr(Map<String, String> paramsMap){
		String str ="";
		for(Entry<String, String> entry : paramsMap.entrySet()) {
			if(StringUtils.isNotBlank(str)) {
				str += ", ";
			}
			str += entry.getValue() +" " + entry.getKey();
		}
		
		return str;
	}

	private static String buildLineNoTab(String line) {
		return line + "\n";
	}
	
	private static String buildLine(String line) {
		return "\t" + line + "\n";
	}

	private static String buildLine(String line, int tabNum) {
		String tabStr = "";
		for(int i = 0; i< tabNum; i++) {
			tabStr +="\t";
		}
		return tabStr + line + "\n";
	}
	
}
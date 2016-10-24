package com.smm.ctrm.translate.element;

import java.util.HashMap;
import java.util.Map;

public class ServiceMethod {
	
	String type;
	
	String returnStr;
	
	String methodName;
	
	Map<String, String> paramsMap = new HashMap<>();
	
	public String getReturnStr() {
		return returnStr;
	}

	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}

	public String getMethodName() {
		return methodName;
	}

	public Map<String, String> getParamsMap() {
		return paramsMap;
	}

	public void setParamsMap(Map<String, String> paramsMap) {
		this.paramsMap = paramsMap;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ServiceMethod [type=" + type + ", returnStr=" + returnStr + ", methodName=" + methodName
				+ ", paramsMap=" + paramsMap + "]";
	}
	
}

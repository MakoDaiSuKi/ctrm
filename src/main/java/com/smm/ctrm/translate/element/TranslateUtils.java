package com.smm.ctrm.translate.element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

public class TranslateUtils {

	public static String typeConvert(String type) {

		if (StringUtils.isBlank(type)) {
			return "";
		}
		if (type.equals("string")) {
			return "String";
		}
		else if (type.equals("int") || type.equals("int?") || type.equals("Int32?") || type.equals("Int32")) {
			return "Integer";
		}
		else if (type.equals("Guid") || type.equals("Guid?")) {
			return "String";
		}
		else if (type.equals("Decimal?") || type.equals("Decimal") || type.equals("decimal") || type.equals("decimal?")) {
			return "BigDecimal";
		}
		else if (type.equals("bool") || type.equals("bool?")) {
			return "Boolean";
		}
		else if (type.equals("DateTime?") || type.equals("DateTime")) {
			return "Date";
		}
		else if (type.equals("long")) {
			return "Long";
		}
		else if (type.startsWith("IList") || type.startsWith("List")) {
			return "List<" + typeConvert(type.substring(type.indexOf("<") + 1, type.indexOf(">"))) +">";
		}
		else if (type.startsWith("System.")) {
			return typeConvert(type.split("\\.")[1]);
		} else if(type.equals("ICriteria")) {
			return "Criteria";
		}
		return type;
	}
	
	public static Map<String, String> convertPropsToMap(String props){
		props = props.replace(",", "&");
		props = props.replace("\"", "");
		props = props.replace(" ", "");
		Map<String, String> propsMap = new HashMap<>();
		
		MultiMap<String> values= new MultiMap<>();
		UrlEncoded.decodeTo(props, values, "utf-8");
		for(Entry<String, List<String>> entry : values.entrySet()) {
			propsMap.put(entry.getKey(), entry.getValue().get(0));
		}
		return propsMap;
	}
	
	public static Map<String, String> convertParamStrToMap(String params){
		Map<String, String> propsMap = new HashMap<>();
		try{
			String[] paramsArr = params.split(",");
			for(String str : paramsArr) {
				String[] pair = str.trim().split(" ");
				propsMap.put(pair[pair.length - 1].trim(), typeConvert(pair[pair.length - 2].trim()));
			}
		} catch(ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return propsMap;
	}
	
}

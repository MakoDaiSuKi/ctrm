package com.smm.ctrm.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import com.smm.ctrm.translate.element.Domain;
import com.smm.ctrm.translate.element.IDomain;
import com.smm.ctrm.translate.element.Property;
import com.smm.ctrm.translate.element.TranslateUtils;

public class CSharpEntityToJava implements IDomain{

	public static CSharpEntityToJava instance = new CSharpEntityToJava();

	String lastLine = "";
	
	List<Property> entityList = new ArrayList<>();
	
	List<Domain> tblList = new ArrayList<>(); 
	
	Domain classObj = new Domain();
	
	public Property entity = new Property();
	
	boolean isFileClassEnd = Boolean.FALSE;
	
	
	public static void main(String[] args) throws Exception {
		
		String rootPath = "C:/Users/Public/entity";
//		rootPath = "F:/svn/trunk/HedgeStudio.Framework/HedgeStudio.Framework.WinForm/ApiClient";
		handleAllFiles(new File(rootPath));
		System.out.println(instance.ttMapProps);
	}

	public static void handleAllFiles(File dir) throws Exception {
		if (dir.isDirectory()) {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				String suffix = fs[i].getName().substring(fs[i].getName().lastIndexOf(".") + 1);
				if (!fs[i].isDirectory() && suffix.equalsIgnoreCase("cs")) {
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
		classObj = new Domain();
		entityList = new ArrayList<>();
		bfr = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = bfr.readLine()) != null && !isFileClassEnd) {
			parseLine(line);
		}
		isFileClassEnd = Boolean.FALSE;
		bfr.close();
		classObj.setEntityList(entityList);
		tblList.add(classObj);
		printAllEntity();
	}
	
	Map<String, String> ttMapProps = new HashMap<>();
	private void printAllEntity() throws Exception{
		for(Domain tbl : tblList) {
			File parentFile = new File(PREFIX_PATH + "/" + tbl.getNamespace());
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			convertToJava(tbl);
		}
	}
	
	private void convertToJava(Domain tbl) throws Exception {
		FileOutputStream fos = null;
		File file = new File(PREFIX_PATH + "/" + tbl.getNamespace() + "/"  
				+ (StringUtils.isBlank(tbl.getNameType()) ? tbl.getClassName() : tbl.getNameType()) + ".java");
		if(!file.exists()) {
			file.createNewFile();
		}
		fos = new FileOutputStream(file);
		
		fos.write(WriteDomainCode.write(tbl).getBytes());
		fos.close();
	}
	
	public void parseLine(String line) throws Exception{
		Matcher match = patternExclude.matcher(line);
		if (match.find()) {
			String matchStr = match.group(0).trim();
			if(matchStr.equals("[Class(Table")) {
				Matcher classAnnotionMatch = classAnnotionPattern.matcher(line);
				if(classAnnotionMatch.find()) {
					Map<String, String> classAnnotionMap = TranslateUtils.convertPropsToMap(classAnnotionMatch.group(1));
					classObj.setNameType(classAnnotionMap.get("NameType").substring(classAnnotionMap.get("NameType").indexOf("(") + 1,
							classAnnotionMap.get("NameType").length() - 1));
					classObj.setSchema(classAnnotionMap.get("Schema"));
					classObj.setTable(classAnnotionMap.get("Table"));
				}
			}
			else if(matchStr.contains("</summary>")) {
				String comment = lastLine.substring(lastLine.indexOf("///") + 3);
				if(StringUtils.isBlank(classObj.getComment())) {
					classObj.setComment(comment);
				} else {
					entity.setComment(comment);
				}
			}
			else if(matchStr.equals("[Property")) {
				Matcher columnMatch = columnPattern.matcher(line);
				if(columnMatch.find()) {
					String propertyProps = columnMatch.group(1);
					Map<String, String> propertyPropsMap = TranslateUtils.convertPropsToMap(propertyProps);
					entity.setColumn(propertyPropsMap.get("Column"));
					entity.setAnnotion(propertyPropsMap.toString());
					entity.setAnnotionMap(propertyPropsMap);
				}
			}
			else if(matchStr.equals("[ManyToOne") || matchStr.equals("[ManyToMany")|| matchStr.equals("[OneToMany")) {
				Matcher manyToOneMatch = manyToOnePattern.matcher(line);
				if(manyToOneMatch.find()) {
					String manyToOneProsStr = TranslateUtils.typeConvert(manyToOneMatch.group(2));
					entity.setAnnotionMap(TranslateUtils.convertPropsToMap(manyToOneProsStr));
					entity.setReference("@" + manyToOneMatch.group(1));
				} else {
					Matcher shortMatcher = shortRefPattern.matcher(line);
					if(shortMatcher.find()) {
						String nextLine = bfr.readLine();
						if(nextLine.trim().endsWith(")]")) {
							String totalPropsStr = line + nextLine;
							manyToOneMatch = manyToOnePattern.matcher(totalPropsStr);
							if(manyToOneMatch.find()) {
								String manyToOneProsStr = TranslateUtils.typeConvert(manyToOneMatch.group(2));
								entity.setAnnotionMap(TranslateUtils.convertPropsToMap(manyToOneProsStr));
								entity.setReference("@" + manyToOneMatch.group(1));
							}
						}
					}
				}
			}
			else if(matchStr.equals("public virtual")) {
				
				Matcher propertyMatch = propertyPattern.matcher(line);
				if(propertyMatch.find()) {
					String type = TranslateUtils.typeConvert(propertyMatch.group(1));
					String property = propertyMatch.group(2);
					entity.setType(type);
					entity.setProperty(property);
				}
				entityList.add(entity);
				entity = new Property();
			}
			else if(matchStr.startsWith("namespace HedgeStudio.Entity")) {
				classObj.setNamespace(line.split("\\.")[2]);
				
			}
			else if(matchStr.equals("public class")){
				
				if(StringUtils.isNotBlank(classObj.getClassName())) {
					isFileClassEnd = true;
					return;
				}
				if(line.indexOf(":") > -1) {
					classObj.setClassName(line.substring(line.indexOf("class") + 6, line.indexOf(":") - 1));
				} else {
					classObj.setClassName(line.substring(line.indexOf("class") + 6));
				}
				
			}
			else if(matchStr.equals("public")) {
				Matcher normalPropMatch = normalPropPattern.matcher(line);
				if(normalPropMatch.find()) {
					String type = TranslateUtils.typeConvert(normalPropMatch.group(1));
					String property = normalPropMatch.group(2);
					entity.setType(type);
					entity.setProperty(property);
				}
				entityList.add(entity);
				entity = new Property();
			}
		}
		lastLine = line;
	}
	
}

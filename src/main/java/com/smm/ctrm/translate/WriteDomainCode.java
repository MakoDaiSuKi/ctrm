package com.smm.ctrm.translate;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.smm.ctrm.translate.element.Domain;
import com.smm.ctrm.translate.element.Property;
import com.smm.ctrm.translate.element.TranslateUtils;

public class WriteDomainCode {
	
	public static TranslateUtils utilInstance = new TranslateUtils();
	
	public final static String JAVA_FILE_PREFIX=""
			
			+ buildLineNoTab("import java.math.BigDecimal;")
			+ buildLineNoTab("import java.util.*;")
			+ buildLineNoTab("import javax.persistence.Column;")
			+ buildLineNoTab("import javax.persistence.ConstraintMode;")
			+ buildLineNoTab("import javax.persistence.Entity;")
			+ buildLineNoTab("import javax.persistence.FetchType; ")
			+ buildLineNoTab("import javax.persistence.ForeignKey;")
			+ buildLineNoTab("import javax.persistence.JoinColumn;")
			+ buildLineNoTab("import javax.persistence.ManyToOne;")
			+ buildLineNoTab("import javax.persistence.OneToMany;")
			+ buildLineNoTab("import javax.persistence.ManyToMany;")
			+ buildLineNoTab("import javax.persistence.Table;")
			+ buildLineNoTab("import javax.persistence.Transient;")
			
			+ buildLineNoTab("import com.smm.ctrm.domain.HibernateEntity;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Basis.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Maintain.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Physical.*;")
			+ buildLineNoTab("import com.smm.ctrm.domain.Report.*;")
			
			+ buildLineNoTab("import org.hibernate.annotations.Cache;")
			+ buildLineNoTab("import org.hibernate.annotations.CacheConcurrencyStrategy;")
			+ buildLineNoTab("import org.hibernate.annotations.Fetch;")
			+ buildLineNoTab("import org.hibernate.annotations.FetchMode;")
			+ buildLineNoTab("import org.hibernate.annotations.NotFound;")
			+ buildLineNoTab("import org.hibernate.annotations.NotFoundAction;")
			+ buildLineNoTab("import com.fasterxml.jackson.annotation.JsonProperty;");
	
	public static String write(Domain tbl) {
		StringBuilder javaContent = new StringBuilder();
		javaContent
		.append("package com.smm.ctrm.domain." + tbl.getNamespace() + ";\n\n")
		.append(JAVA_FILE_PREFIX);
		if(StringUtils.isBlank(tbl.getTable())) {
			javaContent.append(buildLineNoTab("public class " + tbl.getClassName() + " extends HibernateEntity {"))
			.append(buildLine("private static final long serialVersionUID = " +new Date().getTime()+"L;"))
			.append(getPropJavaCode(tbl));
		} else {
			javaContent.append(buildLineNoTab("@Entity"))
			.append(buildLineNoTab("@Table(name = \""+ tbl.getTable()+"\", schema=\""+tbl.getSchema()+"\")"))
//			.append(buildLineNoTab("@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)"))
			.append(buildLineNoTab("public class " + tbl.getNameType() + " extends HibernateEntity {"))
			.append(buildLine("private static final long serialVersionUID = " +new Date().getTime()+"L;"))
			.append(getPropJavaCode(tbl));
		}
		
		return javaContent.toString();
	}
	
	
	private static String getPropJavaCode(Domain tbl) {
		StringBuilder str = new StringBuilder();
		for(Property entity : tbl.getEntityList()) {
			if(entity.getReference() == null) {
				str.append(buildLine("/**"))
				.append(buildLine(" *" + (entity.getComment() == null ? "" : entity.getComment())))
				.append(buildLine(" */"));
				
				if(StringUtils.isNotBlank(entity.getColumn())) {
					str.append(buildProperty(entity));
				} else {
					if("Report".equals(tbl.getNamespace()) || StringUtils.isBlank(tbl.getTable())){
						
					} else {
						str.append(buildLine("@Transient"));
					}
				}
				str.append(buildLine("@JsonProperty(value = \""+entity.getProperty()+"\")"))
				.append(buildLine("private " +entity.getType() + " " + entity.getProperty() + ";"));
			} else {
				str.append(buildLine(entity.getReference()+"(fetch = "+getLazyType(entity) + getJavaClassTypeStr(entity) +")"))
				.append(getFetchMode(entity))
				.append(getJoinColumnStr(entity))
				.append(getNotFoundStr(entity))
				.append(buildLine("@JsonProperty(value = \""+entity.getProperty()+"\")"))
				.append(buildLine("private " +entity.getType() + " " + entity.getProperty() + ";"));
			}
		}
		str.append(addGetSetMethods(tbl)).append("\n}");
		return str.toString();
	}
	
	private static String addGetSetMethods(Domain tbl) {
		
		StringBuilder result = new StringBuilder();
		
		for(Property entity : tbl.getEntityList()) {
			result.append(buildGetSetMethod(entity.getProperty(), entity.getType()));
		}
		
		return result.toString();
	}
	
	private final static String GET_PREFIX = " get";

	private final static String SET_PREFIX = " set";

	public static String buildGetSetMethod(String property, String type) {

		if (StringUtils.isBlank(property)) {
			return "";
		}

		String mainPart = Character.toUpperCase(property.charAt(0)) + property.substring(1);
		String getMethod = "\tpublic " + type + GET_PREFIX + mainPart + "()" + "{\n" + "\t\treturn " + property + ";\n\t}";
		String setMethod = "\tpublic void" + SET_PREFIX + mainPart + "(" + type + " " + property + ")" + "{\n" + "\t\tthis."
				+ property + "=" + property + ";\n\t}\n";
		return getMethod + "\n" + setMethod;
	}


	private static String getNotFoundStr(Property entity) {
		Map<String, String> map = entity.getAnnotionMap();
		String notFound = map.get("NotFound");
		if(StringUtils.isNoneBlank(notFound)) {
			if(notFound.equals("NotFoundMode.Ignore")){
				return buildLine("@NotFound(action=NotFoundAction.IGNORE)");
			}
		}
		return "";
	}
	private static String getFetchMode(Property entity){
		Map<String, String> map = entity.getAnnotionMap();
		String fetch = map.get("Fetch");
		if(StringUtils.isNotBlank(fetch)) {
			String[] strArr = fetch.split("\\.");
			fetch = strArr[0] + "." + strArr[1].toUpperCase();
			
			return buildLine("@Fetch("+ fetch +")");
		}
		return "";
	}
	
	private static String getJoinColumnStr(Property entity) {
		Map<String, String> map = entity.getAnnotionMap();
		String str = "";
		for(Entry<String, String> entry : map.entrySet()) {
			if(entry.getKey().equals("Column")) {
				if(!str.equals("")) {
					str += ", ";
				}
				str += "name = \"" + entry.getValue()+"\"";
			}
			
			else if(entry.getKey().equals("Insert")) {
				if(!str.equals("")) {
					str += ", ";
				}
				str += "insertable = " + entry.getValue();
			}
			else if(entry.getKey().equals("Update")) {
				if(!str.equals("")) {
					str += ", ";
				}
				str += "updatable = " + entry.getValue();
			}
			else if(entry.getKey().equals("ForeignKey")) {
				if(!str.equals("")) {
					str += ", ";
				}
				str += "foreignKey = @ForeignKey(name=\"none\")";
			}
		}
		if(StringUtils.isNotBlank(str)) {
			str = "@JoinColumn(" + str + ")";
			return buildLine(str);
		}
		
		return "";
	}
	private static String getJavaClassTypeStr(Property entity){
		String classType = entity.getAnnotionMap().get("ClassType").trim();
		if(StringUtils.isNotBlank(classType)) {
			classType = classType.substring(classType.indexOf("(") + 1, classType.length() - 1);
			return ", targetEntity = " + classType +".class";
		}
		return "";
	}
	
	private static String buildProperty(Property entity){
		String str="@Column(name = \""+entity.getColumn()+"\"";
		if(entity.getAnnotionMap() != null) {
			for(Entry<String, String> entry : entity.getAnnotionMap().entrySet()) {
				if(entry.getKey().equals("NotNull")) {
					str += ", nullable = " + (Boolean.valueOf(entry.getValue())? "false" : "true");
				} else if(entry.getKey().equals("Length")) {
					str += ", length = " + entry.getValue();
				} else if(entry.getKey().equals("Type")) {
					
					str += ", columnDefinition = " + (entry.getValue().equals("StringClob") ? "CLOB" : "BLOB");
				}
			}
		}
		return buildLine(str +")");
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
	
	private static String getLazyType(Property entity) {
		String lazy = entity.getAnnotionMap().get("Lazy");
		if(StringUtils.isNotBlank(lazy) && lazy.equals("Laziness.False")) {
			return "FetchType.EAGER";
		}
		return "FetchType.LAZY";
	}
}

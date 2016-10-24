package com.smm.ctrm.translate.element;

import java.util.regex.Pattern;

public interface IDomain {
	
	String PREFIX_PATH = "C:/Users/Public/entity/Output";
	
	String regExclude = "^\\s*(\\[Class\\(Table|///\\s*</summary>|\\[Property|\\[(ManyToOne|ManyToMany|OneToMany)|public virtual|namespace HedgeStudio\\.Entity\\.|public class|public)";
	
	String columnReg = "^\\s*\\[Property\\((.*)\\)\\]";
	
	String propertyReg = "^\\s*public\\s+virtual\\s+([a-zA-Z0-9<>\\?\\.]+)\\s+([a-zA-Z0-9]+)";
	
	String manyToOneReg = "^\\s*\\[(ManyToOne|ManyToMany|OneToMany)\\((.*)\\)\\]";
	
	String shortRefReg = "^\\s*\\[(ManyToOne|ManyToMany|OneToMany)\\(.*";
	
	String classAnnotionReg = "^\\s*\\[Class\\((.*)\\)\\]";
	
	String normalPropReg = "public\\s{1,2}([a-zA-Z0-9<>\\?\\.]+)\\s{1,2}([a-zA-Z0-9_\\?\\.]+)\\s{0,2}\\{\\s{0,2}get;\\s{0,2}set;\\s{0,2}\\}";
	
	Pattern patternExclude = Pattern.compile(regExclude);

	Pattern columnPattern = Pattern.compile(columnReg);
	
	Pattern propertyPattern = Pattern.compile(propertyReg);
	
	Pattern manyToOnePattern = Pattern.compile(manyToOneReg);
	
	Pattern shortRefPattern = Pattern.compile(shortRefReg);
	
	Pattern classAnnotionPattern = Pattern.compile(classAnnotionReg);
	
	Pattern normalPropPattern = Pattern.compile(normalPropReg);
	
	
}


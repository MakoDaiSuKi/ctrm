package com.smm.ctrm.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StandardBasicTypes;

public class DialectExtends extends SQLServer2012Dialect{
	
	
	public DialectExtends(){
		super();
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
	}
	
}

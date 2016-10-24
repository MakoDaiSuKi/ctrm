package com.smm.ctrm.hibernate;

import javax.persistence.Table;

public class HTools {

	public static <T> String getTableName(Class<T> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		return table.schema() + "." + table.name();
	}
}

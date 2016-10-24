package com.smm.ctrm.domain;

public class NumTypes {

	public static String Contract = new NumType("Normal", "Contract").NumTypeStr();

	public static String Invoice = new NumType("Normal", "Invoice").NumTypeStr();

	public static String Split_Storage = new NumType("Split", "Storage").NumTypeStr();

	public static String Split_Position = new NumType("Split", "Position").NumTypeStr();

}

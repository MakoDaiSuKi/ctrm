package com.smm.ctrm.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AttachType {

	/**
	 * Customer
	 */
	public final static String Customer = "Customer";
	/**
	 * Contract
	 */
	public final static String Contract = "Contract";
	/**
	 * ContractDoc
	 */
	public final static String ContractDoc = "ContractDoc";
	/**
	 * Storage
	 */
	public final static String Storage = "Storage";
	/**
	 * Invoice
	 */
	public final static String Invoice = "Invoice";
	/**
	 * Fund
	 */
	public final static String Fund = "Fund";
	/**
	 * Pricing
	 */
	public final static String Pricing = "Pricing";
	/**
	 * Position
	 */
	public final static String Position = "Position";
	/**
	 * NA
	 */
	public final static String NA = "NA";
	/**
	 * ReceiptShip
	 */
	public final static String ReceiptShip = "ReceiptShip"; 

	public String Value;
	public String Name;

	public AttachType(String value, String name) {
		super();
		Value = value;
		Name = name;
	}

	public static List<AttachType> AttachTypes() {
		List<AttachType> attachTypes = new ArrayList<AttachType>();
		AttachType a1 = new AttachType("Customer", "客户");
		attachTypes.add(a1);
		AttachType a2 = new AttachType("Contract", "合同");
		attachTypes.add(a2);
		AttachType a3 = new AttachType("Storage", "交付");
		attachTypes.add(a3);
		AttachType a4 = new AttachType("Invoice", "发票");
		attachTypes.add(a4);
		AttachType a5 = new AttachType("Fund", "资金");
		attachTypes.add(a5);
		AttachType a6 = new AttachType("Pricing", "点价");
		attachTypes.add(a6);
		AttachType a7 = new AttachType("Position", "头寸");
		attachTypes.add(a7);
		AttachType a8 = new AttachType("NA", "NA");
		attachTypes.add(a8);

		return attachTypes;
	}

	public static String GetName(String s) {
		if (StringUtils.isBlank(s))
			return "Unknown";
		switch (s) {
		case Customer:
			return "客户";
		case Contract:
			return "合同";
		case Storage:
			return "交付";
		case Invoice:
			return "发票";
		case Fund:
			return "资金";
		case Pricing:
			return "点价";
		case Position:
			return "头寸";
		case NA:
			return "NA";
		default:
			return "Unknown";
		}
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}

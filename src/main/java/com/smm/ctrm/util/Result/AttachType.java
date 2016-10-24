package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;

	///#region 附件类型
public class AttachType
{
	public static final String Customer = "Customer";
	public static final String Contract = "Contract";
	public static final String Storage = "Storage";
	public static final String Invoice = "Invoice";
	public static final String Fund = "Fund";
	public static final String Pricing = "Pricing";
	public static final String Position = "Position";
	public static final String NA = "NA";

	private String Value;
	public final String getValue()
	{
		return Value;
	}
	public final void setValue(String value)
	{
		Value = value;
	}
	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	public static java.util.ArrayList<AttachType> AttachTypes()
	{
		AttachType tempVar = new AttachType();
		tempVar.setValue(Customer);
		tempVar.setName("客户");
		AttachType tempVar2 = new AttachType();
		tempVar2.setValue(Contract);
		tempVar2.setName("合同");
		AttachType tempVar3 = new AttachType();
		tempVar3.setValue(Storage);
		tempVar3.setName("交付");
		AttachType tempVar4 = new AttachType();
		tempVar4.setValue(Invoice);
		tempVar4.setName("发票");
		AttachType tempVar5 = new AttachType();
		tempVar5.setValue(Fund);
		tempVar5.setName("资金");
		AttachType tempVar6 = new AttachType();
		tempVar6.setValue(Pricing);
		tempVar6.setName("点价");
		AttachType tempVar7 = new AttachType();
		tempVar7.setValue(Position);
		tempVar7.setName("头寸");
		AttachType tempVar8 = new AttachType();
		tempVar8.setValue(NA);
		tempVar8.setName("NA");
		java.util.ArrayList<AttachType> attachTypes = new java.util.ArrayList<AttachType>(java.util.Arrays.asList(new AttachType[]{ tempVar, tempVar2, tempVar3, tempVar4, tempVar5, tempVar6, tempVar7, tempVar8 }));
		return attachTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Customer:
		if (Customer.equals(s))
		{
				return "客户";
		}
//ORIGINAL LINE: case Contract:
		else if (Contract.equals(s))
		{
				return "合同";
		}
//ORIGINAL LINE: case Storage:
		else if (Storage.equals(s))
		{
				return "交付";
		}
//ORIGINAL LINE: case Invoice:
		else if (Invoice.equals(s))
		{
				return "发票";
		}
//ORIGINAL LINE: case Fund:
		else if (Fund.equals(s))
		{
				return "资金";
		}
//ORIGINAL LINE: case Pricing:
		else if (Pricing.equals(s))
		{
				return "点价";
		}
//ORIGINAL LINE: case Position:
		else if (Position.equals(s))
		{
				return "头寸";
		}
//ORIGINAL LINE: case NA:
		else if (NA.equals(s))
		{
				return "NA";
		}
		else
		{
				return "Unknown";
		}
	}
}
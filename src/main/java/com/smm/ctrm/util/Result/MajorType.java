package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 价格类型 --- 主要价格的部分
public class MajorType
{
	public static final String Fix = "F";
	public static final String Pricing = "P";
	public static final String Average = "A";

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

	public static java.util.ArrayList<MajorType> PriceTypes()
	{
		MajorType tempVar = new MajorType();
		tempVar.setValue(Fix);
		tempVar.setName("固定价");
		MajorType tempVar2 = new MajorType();
		tempVar2.setValue(Pricing);
		tempVar2.setName("点价");
		MajorType tempVar3 = new MajorType();
		tempVar3.setValue(Average);
		tempVar3.setName("均价");
		java.util.ArrayList<MajorType> majorTypes = new java.util.ArrayList<MajorType>(java.util.Arrays.asList(new MajorType[]{ tempVar, tempVar2, tempVar3 }));
		return majorTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Fix:
		if (Fix.equals(s))
		{
				return "固定价";
		}
//ORIGINAL LINE: case Pricing:
		else if (Pricing.equals(s))
		{
				return "点价";
		}
//ORIGINAL LINE: case Average:
		else if (Average.equals(s))
		{
				return "均价";
		}
		else
		{
				return "Unknown";
		}
	}
}
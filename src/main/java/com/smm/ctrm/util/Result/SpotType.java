package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 现货买卖的类型
public class SpotType
{
	public static final String Purchase = "B";
	public static final String Sell = "S";

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

	public static java.util.ArrayList<SpotType> SpotTypes()
	{
		SpotType tempVar = new SpotType();
		tempVar.setValue(Purchase);
		tempVar.setName("采购");
		SpotType tempVar2 = new SpotType();
		tempVar2.setValue(Sell);
		tempVar2.setName("销售");
		java.util.ArrayList<SpotType> spotTypes = new java.util.ArrayList<SpotType>(java.util.Arrays.asList(new SpotType[]{ tempVar, tempVar2 }));
		return spotTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Purchase:
		if (Purchase.equals(s))
		{
				return "采购";
		}
//ORIGINAL LINE: case Sell:
		else if (Sell.equals(s))
		{
				return "销售";
		}
		else
		{
				return "Unknown";
		}
	}
}
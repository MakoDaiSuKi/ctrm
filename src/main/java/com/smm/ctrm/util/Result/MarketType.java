package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 现货买卖的类型
public class MarketType
{
	public static final String Physical = "P";
	public static final String Futures = "F";

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

	public static java.util.ArrayList<MarketType> MarketTypes()
	{
		MarketType tempVar = new MarketType();
		tempVar.setValue(Physical);
		tempVar.setName("现货");
		MarketType tempVar2 = new MarketType();
		tempVar2.setValue(Futures);
		tempVar2.setName("期货");
		java.util.ArrayList<MarketType> marketTypes = new java.util.ArrayList<MarketType>(java.util.Arrays.asList(new MarketType[]{ tempVar, tempVar2 }));
		return marketTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Physical:
		if (Physical.equals(s))
		{
				return "现货";
		}
//ORIGINAL LINE: case Futures:
		else if (Futures.equals(s))
		{
				return "期货";
		}
		else
		{
				return "Unknown";
		}
	}
}
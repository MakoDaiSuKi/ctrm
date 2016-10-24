package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 价格类型 --- 主要价格的部分
public class PriceTiming
{
	public static final String Onschedule = "O";
	public static final String Extension = "E";

	private String Value;
	public final String getValue()
	{
		return Value;
	}
	public final void setValue(String value)
	{
		Value = value;
	}
	private String privateName;
	public final String getName()
	{
		return privateName;
	}
	public final void setName(String value)
	{
		privateName = value;
	}

	public static java.util.ArrayList<PriceTiming> PriceTimings()
	{
		PriceTiming tempVar = new PriceTiming();
		tempVar.setValue(Onschedule);
		tempVar.setName("按期");
		PriceTiming tempVar2 = new PriceTiming();
		tempVar2.setValue(Extension);
		tempVar2.setName("改期");
		java.util.ArrayList<PriceTiming> priceTimes = new java.util.ArrayList<PriceTiming>(java.util.Arrays.asList(new PriceTiming[]{ tempVar, tempVar2 }));
		return priceTimes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Onschedule:
		if (Onschedule.equals(s))
		{
				return "按期";
		}
//ORIGINAL LINE: case Extension:
		else if (Extension.equals(s))
		{
				return "改期";
		}
		else
		{
				return "Unknown";
		}
	}
}
package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 价格类型 --- 升贴水的部分
public class PremiumType
{
	public static final String Fix = "F";
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

	public static java.util.ArrayList<PremiumType> PremiumTypes()
	{
		PremiumType tempVar = new PremiumType();
		tempVar.setValue(Fix);
		tempVar.setName("固定");
		PremiumType tempVar2 = new PremiumType();
		tempVar2.setValue(Average);
		tempVar2.setName("均价");
		java.util.ArrayList<PremiumType> premiumTypes = new java.util.ArrayList<PremiumType>(java.util.Arrays.asList(new PremiumType[]{ tempVar, tempVar2 }));
		return premiumTypes;
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
				return "升贴水";
		}
//ORIGINAL LINE: case Average:
		else if (Average.equals(s))
		{
				return "均价升贴水";
		}
		else
		{
				return "Unknown";
		}
	}
}
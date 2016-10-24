package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 期货买卖的类型
public class ForwardType
{
	public static final String Futures = "F";
	public static final String Average = "A";
	public static final String Option = "O";

	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}
	private String Value;
	public final String getValue()
	{
		return Value;
	}
	public final void setValue(String value)
	{
		Value = value;
	}

	public static java.util.ArrayList<ForwardType> ForwardTypes()
	{
		ForwardType tempVar = new ForwardType();
		tempVar.setValue(Futures);
		tempVar.setName("期货");
		ForwardType tempVar2 = new ForwardType();
		tempVar2.setValue(Average);
		tempVar2.setName("均价");
		ForwardType tempVar3 = new ForwardType();
		tempVar3.setValue(Option);
		tempVar3.setName("期权");
		java.util.ArrayList<ForwardType> forwardTypes = new java.util.ArrayList<ForwardType>(java.util.Arrays.asList(new ForwardType[]{ tempVar, tempVar2, tempVar3 }));
		return forwardTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Futures:
		if (Futures.equals(s))
		{
				return "期货";
		}
//ORIGINAL LINE: case Average:
		else if (Average.equals(s))
		{
				return "均价";
		}
//ORIGINAL LINE: case Option:
		else if (Option.equals(s))
		{
				return "期权";
		}
		else
		{
				return "Unknown";
		}
	}

}
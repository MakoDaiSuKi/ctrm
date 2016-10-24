package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#region 交付的方向
public class MT4Storage
{
	/** 
	 发货
	 
	*/
	public static final String Make = "M";
	/** 
	 收货
	 
	*/
	public static final String Take = "T";

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

	public static java.util.ArrayList<MT4Storage> MTs()
	{
		MT4Storage tempVar = new MT4Storage();
		tempVar.setValue(Take);
		tempVar.setName("收");
		MT4Storage tempVar2 = new MT4Storage();
		tempVar2.setValue(Make);
		tempVar2.setName("发");
		java.util.ArrayList<MT4Storage> mTs4Storage = new java.util.ArrayList<MT4Storage>(java.util.Arrays.asList(new MT4Storage[]{ tempVar, tempVar2 }));
		return mTs4Storage;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Take:
		if (Take.equals(s))
		{
				return "收";
		}
//ORIGINAL LINE: case Make:
		else if (Make.equals(s))
		{
				return "发";
		}
		else
		{
				return "Unknown";
		}
	}
}
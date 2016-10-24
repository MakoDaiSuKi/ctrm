package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 发票的方向
public class MT4Invoice
{
	public static final String Make = "M";
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

	public static java.util.ArrayList<MT4Invoice> MTs4Invoice()
	{
		MT4Invoice tempVar = new MT4Invoice();
		tempVar.setValue(Take);
		tempVar.setName("收");
		MT4Invoice tempVar2 = new MT4Invoice();
		tempVar2.setValue(Make);
		tempVar2.setName("开");
		java.util.ArrayList<MT4Invoice> mTs = new java.util.ArrayList<MT4Invoice>(java.util.Arrays.asList(new MT4Invoice[]{ tempVar, tempVar2 }));
		return mTs;
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
				return "开";
		}
		else
		{
				return "Unknown";
		}
	}
}
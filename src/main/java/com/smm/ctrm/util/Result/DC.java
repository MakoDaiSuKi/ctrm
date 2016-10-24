package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 资金出入的方向
public class DC
{
	public static final String Debit = "D";
	public static final String Credit = "C";
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
	public static java.util.ArrayList<DC> DCs()
	{
		DC tempVar = new DC();
		tempVar.setValue(Debit);
		tempVar.setName("付款");
		DC tempVar2 = new DC();
		tempVar2.setValue(Credit);
		tempVar2.setName("收款");
		java.util.ArrayList<DC> dcs = new java.util.ArrayList<DC>(java.util.Arrays.asList(new DC[]{ tempVar, tempVar2 }));
		return dcs;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Debit:
		if (Debit.equals(s))
		{
				return "付款";
		}
//ORIGINAL LINE: case Credit:
		else if (Credit.equals(s))
		{
				return "收款";
		}
		else
		{
				return "Unknown";
		}
	}
}
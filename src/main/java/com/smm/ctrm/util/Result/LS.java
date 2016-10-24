package com.smm.ctrm.util.Result;


	///#endregion


	///#region 期货买卖的方向
public class LS
{
	public static final String LONG = "L";
	public static final String SHORT = "S";

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

	public static java.util.ArrayList<LS> LSs()
	{
		LS tempVar = new LS();
		tempVar.setValue(SHORT);
		tempVar.setName("多");
		LS tempVar2 = new LS();
		tempVar2.setValue(LONG);
		tempVar2.setName("空");
		java.util.ArrayList<LS> lSs = new java.util.ArrayList<LS>(java.util.Arrays.asList(new LS[]{ tempVar, tempVar2 }));
		return lSs;
	}
}
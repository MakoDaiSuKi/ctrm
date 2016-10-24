package com.smm.ctrm.util.Result;


	///#endregion

	///#region 通用的业务状态标志
public class Status
{
	/**
	 * 草拟
	 */
	public static final int Draft = -9;
	/**
	 * 待审
	 */
	public static final int Pending = 0;
	/**
	 * 同意
	 */
	public static final int Agreed = 1;
	/**
	 * 否决
	 */
	public static final int Deny = -1;
	/**
	 * 结束
	 */
	public static final int Close = 8;
	/**
	 * 取消
	 */
	public static final int Cancel = 9;

	private int Value;
	public final int getValue()
	{
		return Value;
	}
	public final void setValue(int value)
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

	public static java.util.ArrayList<Status> Statuses()
	{
		Status tempVar = new Status();
		tempVar.setValue(Draft);
		tempVar.setName("草拟");
		Status tempVar2 = new Status();
		tempVar2.setValue(Pending);
		tempVar2.setName("待审");
		Status tempVar3 = new Status();
		tempVar3.setValue(Agreed);
		tempVar3.setName("同意");
		Status tempVar4 = new Status();
		tempVar4.setValue(Deny);
		tempVar4.setName("否决");
		Status tempVar5 = new Status();
		tempVar5.setValue(Close);
		tempVar5.setName("结束");
		Status tempVar6 = new Status();
		tempVar6.setValue(Cancel);
		tempVar6.setName("取消");
		java.util.ArrayList<Status> statuses = new java.util.ArrayList<Status>(java.util.Arrays.asList(new Status[]{ tempVar, tempVar2, tempVar3, tempVar4, tempVar5, tempVar6 }));
		return statuses;
	}

	public static String GetName(int s)
	{
		switch (s)
		{
			case Draft:
				return "草拟";
			case Pending:
				return "待审";
			case Agreed:
				return "同意";
			case Deny:
				return "否决";
			case Close:
				return "结束";
			case Cancel:
				return "取消";
			default:
				return "Unknown";
		}
	}
}
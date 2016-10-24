package com.smm.ctrm.domain.Basis;

/*
 * 收款水单状态
 * */
public class BankReceiptStatus {
	/**
	 * 登记
	 */
	public static final int Registered = 1;
	/**
	 * 已认领
	 */
	public static final int Confirmed = 2;
	/**
	 * 认领完毕
	 */
	public static final int ConfirmedFinish = 3;
	
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
	
	
	public static java.util.ArrayList<BankReceiptStatus> BankReceiptStatusList()
	{
		BankReceiptStatus tempVar = new BankReceiptStatus();
		tempVar.setValue(Registered);
		tempVar.setName("登记");
		BankReceiptStatus tempVar2 = new BankReceiptStatus();
		tempVar2.setValue(Confirmed);
		tempVar2.setName("已认领");
		BankReceiptStatus tempVar3 = new BankReceiptStatus();
		tempVar3.setValue(ConfirmedFinish);
		tempVar3.setName("认领完毕");
		
		java.util.ArrayList<BankReceiptStatus> statuses 
		= new java.util.ArrayList<BankReceiptStatus>(java.util.Arrays.asList(new BankReceiptStatus[]{ tempVar, tempVar2, tempVar3}));
		return statuses;
	}

	public static String GetName(int s)
	{
		switch (s)
		{
			case Registered:
				return "登记";
			case Confirmed:
				return "已认领";
			case ConfirmedFinish:
				return "认领完毕";
			default:
				return "Unknown";
		}
	}
}

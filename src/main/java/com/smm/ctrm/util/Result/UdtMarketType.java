package com.smm.ctrm.util.Result;

public enum UdtMarketType
{
	Future,
	Spot,
	All;

	public int getValue()
	{
		return this.ordinal();
	}

	public static UdtMarketType forValue(int value)
	{
		return values()[value];
	}
}
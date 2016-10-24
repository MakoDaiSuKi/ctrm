package com.smm.ctrm.util.Result;

public class LeagalnFeeType
{
	public static java.util.ArrayList<String> getBviFeeType()
	{
		return new java.util.ArrayList<String>(java.util.Arrays.asList(new String[] { FeeCode.Transportation }));
	}
	/** 
	 商贸销售
	 
	*/
	public static java.util.ArrayList<String> getSmSFeeType()
	{
		return new java.util.ArrayList<String>(java.util.Arrays.asList(new String[] { FeeCode.Transportation, FeeCode.Test, FeeCode.Insurance, FeeCode.Cost, FeeCode.BankDocumentsFee, FeeCode.BuyDocumentsFee, FeeCode.DisputeFine, FeeCode.HedgeFee, FeeCode.Other }));
	}
	/** 
	 商贸采购
	 
	*/
	public static java.util.ArrayList<String> getSmBFeeType()
	{
		return new java.util.ArrayList<String>(java.util.Arrays.asList(new String[] { FeeCode.Transportation }));
	}
	public static java.util.ArrayList<String> getXycFeeType()
	{
		return new java.util.ArrayList<String>(java.util.Arrays.asList(new String[] { FeeCode.Transportation }));
	}
}
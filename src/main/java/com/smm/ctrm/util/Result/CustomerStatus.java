package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 客户往来的状态分类
public class CustomerStatus
{
	public static final String NORMAL = "NORMAL";
	public static final String PROHIBITED = "PROHIBITED";
	public static final String GIVEUP = "GIVEUP";

	private String StatusValue;
	public final String getStatusValue()
	{
		return StatusValue;
	}
	public final void setStatusValue(String value)
	{
		StatusValue = value;
	}
	private String StatusName;
	public final String getStatusName()
	{
		return StatusName;
	}
	public final void setStatusName(String value)
	{
		StatusName = value;
	}

	public static java.util.ArrayList<CustomerStatus> CustomerStatuses()
	{
		CustomerStatus tempVar = new CustomerStatus();
		tempVar.setStatusValue(NORMAL);
		tempVar.setStatusName("正常");
		CustomerStatus tempVar2 = new CustomerStatus();
		tempVar2.setStatusValue(PROHIBITED);
		tempVar2.setStatusName("禁止交易");
		CustomerStatus tempVar3 = new CustomerStatus();
		tempVar3.setStatusValue(GIVEUP);
		tempVar3.setStatusName("已放弃");
		java.util.ArrayList<CustomerStatus> customerStatuses = new java.util.ArrayList<CustomerStatus>(java.util.Arrays.asList(new CustomerStatus[]{ tempVar, tempVar2, tempVar3 }));
		return customerStatuses;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "NA";
		}
//		switch (s)
//ORIGINAL LINE: case NORMAL:
		if (NORMAL.equals(s))
		{
				return "正常";
		}
//ORIGINAL LINE: case PROHIBITED:
		else if (PROHIBITED.equals(s))
		{
				return "禁止交易";
		}
//ORIGINAL LINE: case GIVEUP:
		else if (GIVEUP.equals(s))
		{
				return "已放弃";
		}
		else
		{
				return "NA";
		}
	}
}
package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 费用类型 --- 杂费
public class FeeCode
{
	public static final String Transportation = "1";
	public static final String Test = "2";
	public static final String Custom = "3";
	public static final String Bank = "4";
	public static final String Inventory = "5";
	public static final String Insurance = "6";
	public static final String Cost = "7";
	public static final String Goods = "9";
	public static final String Other = "10";
	public static final String BankDocumentsFee = "11";
	public static final String BuyDocumentsFee = "12";
	public static final String HedgeFee = "13";
	public static final String DisputeFine = "14";
	public static final String NA = "-1";

	private String Value;
	public final String getValue()
	{
		return Value;
	}
	public final void setValue(String value)
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

	public static java.util.ArrayList<FeeCode> FeeCodes()
	{
		FeeCode tempVar = new FeeCode();
		tempVar.setValue(Transportation);
		tempVar.setName("运输费");
		FeeCode tempVar2 = new FeeCode();
		tempVar2.setValue(Test);
		tempVar2.setName("检验费");
		FeeCode tempVar3 = new FeeCode();
		tempVar3.setValue(Custom);
		tempVar3.setName("报关费");
		FeeCode tempVar4 = new FeeCode();
		tempVar4.setValue(Bank);
		tempVar4.setName("银行费用");
		FeeCode tempVar5 = new FeeCode();
		tempVar5.setValue(Inventory);
		tempVar5.setName("仓储费用");
		FeeCode tempVar6 = new FeeCode();
		tempVar6.setValue(Insurance);
		tempVar6.setName("保险费");
		FeeCode tempVar7 = new FeeCode();
		tempVar7.setValue(Cost);
		tempVar7.setName("资金成本");
		FeeCode tempVar8 = new FeeCode();
		tempVar8.setValue(Goods);
		tempVar8.setName("货款");
		FeeCode tempVar9 = new FeeCode();
		tempVar9.setValue(Other);
		tempVar9.setName("其它");
		FeeCode tempVar10 = new FeeCode();
		tempVar10.setValue(BankDocumentsFee);
		tempVar10.setName("银行文件费");
		FeeCode tempVar11 = new FeeCode();
		tempVar11.setValue(BuyDocumentsFee);
		tempVar11.setName("采购文件费");
		FeeCode tempVar12 = new FeeCode();
		tempVar12.setValue(HedgeFee);
		tempVar12.setName("套保费");
		FeeCode tempVar13 = new FeeCode();
		tempVar13.setValue(DisputeFine);
		tempVar13.setName("争议罚款费");
		FeeCode tempVar14 = new FeeCode();
		tempVar14.setValue(NA);
		tempVar14.setName("NA");
		java.util.ArrayList<FeeCode> feeCodes = new java.util.ArrayList<FeeCode>(java.util.Arrays.asList(new FeeCode[]{ tempVar, tempVar2, tempVar3, tempVar4, tempVar5, tempVar6, tempVar7, tempVar8, tempVar9, tempVar10, tempVar11, tempVar12, tempVar13, tempVar14 }));
		return feeCodes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case Transportation:
		if (Transportation.equals(s))
		{
				return "运输费";
		}
//ORIGINAL LINE: case Test:
		else if (Test.equals(s))
		{
				return "检验费";
		}
//ORIGINAL LINE: case Custom:
		else if (Custom.equals(s))
		{
				return "报关费";
		}
//ORIGINAL LINE: case Bank:
		else if (Bank.equals(s))
		{
				return "银行费用";
		}
//ORIGINAL LINE: case Inventory:
		else if (Inventory.equals(s))
		{
				return "仓储费用";
		}
//ORIGINAL LINE: case Insurance:
		else if (Insurance.equals(s))
		{
				return "保险费";
		}
//ORIGINAL LINE: case Cost:
		else if (Cost.equals(s))
		{
				return "资金成本";
		}
//ORIGINAL LINE: case Goods:
		else if (Goods.equals(s))
		{
				return "货款";
		}
//ORIGINAL LINE: case Other:
		else if (Other.equals(s))
		{
				return "其它";
		}
//ORIGINAL LINE: case BankDocumentsFee:
		else if (BankDocumentsFee.equals(s))
		{
				return "银行文件费";
		}
//ORIGINAL LINE: case BuyDocumentsFee:
		else if (BuyDocumentsFee.equals(s))
		{
				return "采购文件费";
		}
//ORIGINAL LINE: case DisputeFine:
		else if (DisputeFine.equals(s))
		{
				return "争议罚款费";
		}
//ORIGINAL LINE: case HedgeFee:
		else if (HedgeFee.equals(s))
		{
				return "套保费";
		}
//ORIGINAL LINE: case NA:
		else if (NA.equals(s))
		{
				return "NA";
		}
		else
		{
				return "Unknown";
		}
	}
}
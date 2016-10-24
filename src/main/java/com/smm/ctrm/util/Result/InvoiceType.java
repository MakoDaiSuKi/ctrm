package com.smm.ctrm.util.Result;

import org.apache.commons.lang3.StringUtils;


	///#endregion


	///#region 发票类型
public class InvoiceType
{
	public static final String MultiLots = "L";
	public static final String Final = "F";
	public static final String Provisional = "P";
	public static final String Adjust = "A";
	public static final String Misc = "M";
	public static final String Note = "N";
	public static final String SummaryNote = "SN";
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

	public static java.util.ArrayList<InvoiceType> InvoiceTypes()
	{
		InvoiceType tempVar = new InvoiceType();
		tempVar.setValue(MultiLots);
		tempVar.setName("多批");
		InvoiceType tempVar2 = new InvoiceType();
		tempVar2.setValue(Final);
		tempVar2.setName("正式");
		InvoiceType tempVar3 = new InvoiceType();
		tempVar3.setValue(Provisional);
		tempVar3.setName("临时");
		InvoiceType tempVar4 = new InvoiceType();
		tempVar4.setValue(Adjust);
		tempVar4.setName("调整");
		InvoiceType tempVar5 = new InvoiceType();
		tempVar5.setValue(Misc);
		tempVar5.setName("费用");
		InvoiceType tempVar6 = new InvoiceType();
		tempVar6.setValue(Note);
		tempVar6.setName("Note");
		InvoiceType tempVar7 = new InvoiceType();
		tempVar7.setValue(SummaryNote);
		tempVar7.setName("Note");
		java.util.ArrayList<InvoiceType> invoiceTypes = new java.util.ArrayList<InvoiceType>(java.util.Arrays.asList(new InvoiceType[] { tempVar, tempVar2, tempVar3, tempVar4, tempVar5, tempVar6, tempVar7 }));
		return invoiceTypes;
	}

	public static String GetName(String s)
	{
		if (StringUtils.isBlank(s))
		{
			return "Unknown";
		}
//		switch (s)
//ORIGINAL LINE: case MultiLots:
		if (MultiLots.equals(s))
		{
				return "多批";
		}
//ORIGINAL LINE: case Final:
		else if (Final.equals(s))
		{
				return "正式";
		}
//ORIGINAL LINE: case Provisional:
		else if (Provisional.equals(s))
		{
				return "临时";
		}
//ORIGINAL LINE: case Adjust:
		else if (Adjust.equals(s))
		{
				return "调整";
		}
//ORIGINAL LINE: case Misc:
		else if (Misc.equals(s))
		{
				return "费用";
		}
//ORIGINAL LINE: case Note:
		else if (Note.equals(s))
		{
				return "Note";
		}
//ORIGINAL LINE: case SummaryNote:
		else if (SummaryNote.equals(s))
		{
				return "Note";
		}
		else
		{
				return "Unknown";
		}
	}
}
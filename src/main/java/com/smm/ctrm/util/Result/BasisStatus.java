package com.smm.ctrm.util.Result;

	///#endregion

/** 
 
 
*/
public class BasisStatus
{
	/** 
	 
	 
	*/
	private String BasisType;
	public final String getBasisType()
	{
		return BasisType;
	}
	public final void setBasisType(String value)
	{
		BasisType = value;
	}
	/** 
	 
	 
	*/
	private String BasisName;
	public final String getBasisName()
	{
		return BasisName;
	}
	public final void setBasisName(String value)
	{
		BasisName = value;
	}
	/** 
	 
	 
	*/
	private String privateBasisValue;
	public final String getBasisValue()
	{
		return privateBasisValue;
	}
	public final void setBasisValue(String value)
	{
		privateBasisValue = value;
	}

	/** 
	 
	 
	 @return 
	*/
	public static java.util.ArrayList<BasisStatus> MarketBasisStatus()
	{
		BasisStatus tempVar = new BasisStatus();
		tempVar.setBasisType("SPOT");
		tempVar.setBasisName("最低");
		tempVar.setBasisValue("LOW");
		BasisStatus tempVar2 = new BasisStatus();
		tempVar2.setBasisType("SPOT");
		tempVar2.setBasisName("平均");
		tempVar2.setBasisValue("AVERAGE");
		BasisStatus tempVar3 = new BasisStatus();
		tempVar3.setBasisType("SPOT");
		tempVar3.setBasisName("最高");
		tempVar3.setBasisValue("HIGH");
		BasisStatus tempVar4 = new BasisStatus();
		tempVar4.setBasisType("LME");
		tempVar4.setBasisName("现货价");
		tempVar4.setBasisValue("CASH");
		BasisStatus tempVar5 = new BasisStatus();
		tempVar5.setBasisType("LME");
		tempVar5.setBasisName("3月价");
		tempVar5.setBasisValue("3M");
		BasisStatus tempVar6 = new BasisStatus();
		tempVar6.setBasisType("SFE");
		tempVar6.setBasisName("日结算价");
		tempVar6.setBasisValue("SETTLE");
		BasisStatus tempVar7 = new BasisStatus();
		tempVar7.setBasisType("SFE");
		tempVar7.setBasisName("日加权均价");
		tempVar7.setBasisValue("AVERAGE");
		BasisStatus tempVar8 = new BasisStatus();
		tempVar8.setBasisType("PLATTS");
		tempVar8.setBasisName("最低");
		tempVar8.setBasisValue("LOW");
		BasisStatus tempVar9 = new BasisStatus();
		tempVar9.setBasisType("PLATTS");
		tempVar9.setBasisName("平均");
		tempVar9.setBasisValue("AVERAGE");
		BasisStatus tempVar10 = new BasisStatus();
		tempVar10.setBasisType("PLATTS");
		tempVar10.setBasisName("最高");
		tempVar10.setBasisValue("HIGH");
		java.util.ArrayList<BasisStatus> basises = new java.util.ArrayList<BasisStatus>(java.util.Arrays.asList(new BasisStatus[]{ tempVar, tempVar2, tempVar3, tempVar4, tempVar5, tempVar6, tempVar7, tempVar8, tempVar9, tempVar10 }));
			//new BasisStatus { BasisType = "LMB", BasisName="低幅",BasisValue = "LOW" },
			//new BasisStatus { BasisType = "LMB", BasisName="高幅",BasisValue = "HIGH" },
		return basises;
	}

}
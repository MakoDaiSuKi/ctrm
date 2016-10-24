package com.smm.ctrm.util.Result;

/** 
 
 
*/
public class YesNoStatus
{
	/** 
	 
	 
	*/
	private byte StatusID;
	
	/** 
	 
	 
	*/
	private String StatusName;

	/** 
	 
	 
	 @return 
	*/
	public static java.util.ArrayList<YesNoStatus> InitialStatus()
	{
		YesNoStatus tempVar = new YesNoStatus();
		byte no = 0;
		byte yes = 1;
		tempVar.setStatusID(no);
		tempVar.setStatusName("否");
		YesNoStatus tempVar2 = new YesNoStatus();
		tempVar2.setStatusID(yes);
		tempVar2.setStatusName("是");
		java.util.ArrayList<YesNoStatus> lstStatus = new java.util.ArrayList<YesNoStatus>(java.util.Arrays.asList(new YesNoStatus[]{ tempVar, tempVar2 }));
		return lstStatus;
	}

	public byte getStatusID() {
		return StatusID;
	}

	public void setStatusID(byte statusID) {
		StatusID = statusID;
	}

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}
}
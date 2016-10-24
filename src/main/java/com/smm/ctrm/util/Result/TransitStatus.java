package com.smm.ctrm.util.Result;

public class TransitStatus
{
	public static String OnRoad = "在途";
	public static String InStorage = "在库";
	public static String NA = "NA"; //未知

	public static String OutOfTrunk = "已卸货";
	public static String Storaged = "仓储库存";
	public static String OnRoadByTrunk = "陆运在途";
	public static String OnRoadByShip = "海运在途";

	//public static string getStatusName(string StatusKey)
	//{
	//    switch (StatusKey)
	//    {
	//        case "OnRoad":
	//            return OnRoad;
	//        case "InStorage":
	//            return InStorage;
	//        case "NA":
	//            return NA;
	//        case "Storaged":
	//            return Storaged;
	//        case "OnRoadByTrunk":
	//            return OnRoadByTrunk;
	//        case "OnRoadByShip":
	//            return OnRoadByShip;
	//    }
	//    return "";
	//}
	/** 
	  在途
	 
	 @param compareStatus
	 @return 
	*/
	public static boolean IsOnRoad(String compareStatus)
	{
		if (compareStatus.indexOf("在途") >= 0)
		{
			return true;
		}
		return false;
	}
	public static boolean IsOnRoadByShip(String compareStatus)
	{
		if (compareStatus.indexOf("海运在途") >= 0)
		{
			return true;
		}
		return false;
	}
	/** 
	 在库
	 
	 @param compareStatus
	 @return 
	*/
	public static boolean IsInStorage(String compareStatus)
	{
		if (compareStatus.indexOf("在库") >= 0)
		{
			return true;
		}
		if (compareStatus.indexOf("仓储库存") >= 0)
		{
			return true;
		}
		return false;
	}
}
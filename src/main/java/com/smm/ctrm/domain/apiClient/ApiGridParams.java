
package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

///#region 相同成员
public class ApiGridParams
{
	/** 
	 关键字
	 
	*/
	@JsonProperty(value = "Keyword")
	private String Keyword;
	public String getKeyword()
	{
		return Keyword;
	}
	public void setKeyword(String value)
	{
		Keyword = value;
	}

	/** 
	 每页记录数
	 
	*/
	@JsonProperty(value = "PageSize")
	private int PageSize;
	public int getPageSize()
	{
		return PageSize;
	}
	public void setPageSize(int value)
	{
		PageSize = value;
	}

	/** 
	 第几页
	 
	*/
	@JsonProperty(value = "PageIndex")
	private int PageIndex;
	public int getPageIndex()
	{
		return PageIndex;
	}
	public void setPageIndex(int value)
	{
		PageIndex = value;
	}

	/** 
	 排序字段
	 
	*/
	@JsonProperty(value = "SortBy")
	private String SortBy;
	public String getSortBy()
	{
		return SortBy;
	}
	public void setSortBy(String value)
	{
		SortBy = value;
	}

	/** 
	 排序的方向（升序 or 降序）
	 
	*/
	@JsonProperty(value = "OrderBy")
	private String OrderBy;
	public String getOrderBy()
	{
		return OrderBy;
	}
	public void setOrderBy(String value)
	{
		OrderBy = value;
	}
	@Override
	public String toString() {
		return "ApiGridParams [Keyword=" + Keyword + ", PageSize=" + PageSize + ", PageIndex=" + PageIndex + ", SortBy="
				+ SortBy + ", OrderBy=" + OrderBy + "]";
	}
}
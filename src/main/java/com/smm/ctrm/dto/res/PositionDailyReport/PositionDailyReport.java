package com.smm.ctrm.dto.res.PositionDailyReport;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyReport {
	
	/**
	 * 当天新增的头寸信息
	 */
	@JsonProperty(value = "AddedTodayList")
	private List<PositionDailyBase> AddedTodayList = new ArrayList<>();
	
	/**
	 * 当天平仓的头寸
	 */
	@JsonProperty(value = "UnravelList")
	private List<PositionDailyUnravel> UnravelList = new ArrayList<>();
	
	/**
	 * 当天交割的头寸
	 */
	@JsonProperty(value = "DeliveryList")
	private List<PositionDailyDelivery> DeliveryList = new ArrayList<>();
	
	/**
	 * 当天换月的头寸
	 */
	@JsonProperty(value = "FuturesInMonthList")
	private List<PositionDailyFuturesInMonth> FuturesInMonthList = new ArrayList<>();

	public List<PositionDailyBase> getAddedTodayList() {
		return AddedTodayList;
	}

	public void setAddedTodayList(List<PositionDailyBase> addedTodayList) {
		AddedTodayList = addedTodayList;
	}

	public List<PositionDailyUnravel> getUnravelList() {
		return UnravelList;
	}

	public void setUnravelList(List<PositionDailyUnravel> unravelList) {
		UnravelList = unravelList;
	}

	public List<PositionDailyDelivery> getDeliveryList() {
		return DeliveryList;
	}

	public void setDeliveryList(List<PositionDailyDelivery> deliveryList) {
		DeliveryList = deliveryList;
	}

	public List<PositionDailyFuturesInMonth> getFuturesInMonthList() {
		return FuturesInMonthList;
	}

	public void setFuturesInMonthList(List<PositionDailyFuturesInMonth> futuresInMonthList) {
		FuturesInMonthList = futuresInMonthList;
	}

}

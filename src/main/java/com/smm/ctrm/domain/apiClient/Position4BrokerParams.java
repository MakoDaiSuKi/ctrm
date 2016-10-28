
package com.smm.ctrm.domain.apiClient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.PositionDelivery;
import com.smm.ctrm.domain.Physical.Storage;

/**
 * 头寸交割参数
 * 
 */
public class Position4BrokerParams {

	/**
	 * 经纪商头寸
	 */
	@JsonProperty(value = "Position4Broker")
	private Position4Broker Position4Broker;

	/**
	 * 头寸交割信息
	 */
	@JsonProperty(value = "PositionDelivery")
	private PositionDelivery PositionDelivery;

	/**
	 * 交割仓单
	 */
	@JsonProperty(value = "StorageList")
	private List<Storage> StorageList;
	
	
	/**
	 * 平仓头寸
	 */
	@JsonProperty(value = "PBList")
	private List<Position4Broker> PBList;
	
	
	/**
	 *  换月头寸
	 */
	@JsonProperty(value = "ChangeMonthList")
	private List<Position4Broker> ChangeMonthList;
	
	
	
	
	
	

	public Position4Broker getPosition4Broker() {
		return Position4Broker;
	}

	public void setPosition4Broker(Position4Broker position4Broker) {
		Position4Broker = position4Broker;
	}

	public PositionDelivery getPositionDelivery() {
		return PositionDelivery;
	}

	public void setPositionDelivery(PositionDelivery positionDelivery) {
		PositionDelivery = positionDelivery;
	}

	public List<Storage> getStorageList() {
		return StorageList;
	}

	public void setStorageList(List<Storage> storageList) {
		StorageList = storageList;
	}

	public List<Position4Broker> getPBList() {
		return PBList;
	}

	public void setPBList(List<Position4Broker> pBList) {
		PBList = pBList;
	}

	public List<Position4Broker> getChangeMonthList() {
		return ChangeMonthList;
	}

	public void setChangeMonthList(List<Position4Broker> changeMonthList) {
		ChangeMonthList = changeMonthList;
	}
	
}

package com.smm.ctrm.domain.apiClient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.PositionDelivery;
import com.smm.ctrm.domain.Physical.Storage;

/**
 * 头寸交割参数
 * 
 */
public class Position4BrokerParams extends ApiGridParams {

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
}
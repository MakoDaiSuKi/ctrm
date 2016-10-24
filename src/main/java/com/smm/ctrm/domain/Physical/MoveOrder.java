package com.smm.ctrm.domain.Physical;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Warehouse;

@Entity
@Table(name = "MoveOrder", schema = "Physical")
public class MoveOrder extends HibernateEntity{

	private static final long serialVersionUID = 1L;

	
	/**
	 * 移库单号
	 */
	@Column(name = "MoveNo")
	@JsonProperty(value = "MoveNo")
	private String MoveNo;
	
	/**
	 * 移出仓库
	 */
	@Column(name = "WarehouseOutId")
	@JsonProperty(value = "WarehouseOutId")
	private String WarehouseOutId;
	public String getMoveNo() {
		return MoveNo;
	}

	public void setMoveNo(String moveNo) {
		MoveNo = moveNo;
	}

	public String getWarehouseOutId() {
		return WarehouseOutId;
	}

	public void setWarehouseOutId(String warehouseOutId) {
		WarehouseOutId = warehouseOutId;
	}

	public Warehouse getWarehouseOut() {
		return WarehouseOut;
	}

	public void setWarehouseOut(Warehouse warehouseOut) {
		WarehouseOut = warehouseOut;
	}

	public String getWarehouseInId() {
		return WarehouseInId;
	}

	public void setWarehouseInId(String warehouseInId) {
		WarehouseInId = warehouseInId;
	}

	public Warehouse getWarehouseIn() {
		return WarehouseIn;
	}

	public void setWarehouseIn(Warehouse warehouseIn) {
		WarehouseIn = warehouseIn;
	}

	public Date getMoveDate() {
		return MoveDate;
	}

	public void setMoveDate(Date moveDate) {
		MoveDate = moveDate;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "WarehouseOutId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "WarehouseOut")
	private Warehouse WarehouseOut;
	
	/**
	 * 移入仓库
	 */
	@Column(name = "WarehouseInId")
	@JsonProperty(value = "WarehouseInId")
	private String WarehouseInId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "WarehouseInId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "WarehouseIn")
	private Warehouse WarehouseIn;
	
	/**
	 * 移库日期
	 */
	@Column(name = "MoveDate")
	@JsonProperty(value = "MoveDate")
	private Date MoveDate;
}

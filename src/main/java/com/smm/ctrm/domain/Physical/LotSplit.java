package com.smm.ctrm.domain.Physical;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LotSplit {

	@JsonProperty(value = "Lots")
	/// <summary>
	/// 拆分的合同批次
	/// </summary>
	public List<Lot> Lots;

	/// <summary>
	/// 拆分的货物
	/// </summary>
	@JsonProperty(value = "Split_Storage")
	public List<Storage> Split_Storage;

	/// <summary>
	/// 拆分的资金
	/// </summary>
	@JsonProperty(value = "Split_Fund")
	public List<Fund> Split_Fund;

	/// <summary>
	/// 拆分的发票
	/// </summary>
	@JsonProperty(value = "Split_Invoice")
	public List<Invoice> Split_Invoice;

	/// <summary>
	/// 拆分的费用
	/// </summary>
	@JsonProperty(value = "Split_Fee")
	public List<Invoice> Split_Fee;

	/// <summary>
	/// 拆分的点价
	/// </summary>
	@JsonProperty(value = "Split_Price")
	public List<Pricing> Split_Price;

	/// <summary>
	/// 拆分的点价明细
	/// </summary>
	@JsonProperty(value = "Split_PriceRecord")
	public List<PricingRecord> Split_PriceRecord;

	/// <summary>
	/// 拆分套保
	/// </summary>
	@JsonProperty(value = "Split_Position")
	public List<Position> Split_Position;

	public List<Lot> getLots() {
		return Lots;
	}

	public void setLots(List<Lot> lots) {
		Lots = lots;
	}

	public List<Storage> getSplit_Storage() {
		return Split_Storage;
	}

	public void setSplit_Storage(List<Storage> split_Storage) {
		Split_Storage = split_Storage;
	}

	public List<Fund> getSplit_Fund() {
		return Split_Fund;
	}

	public void setSplit_Fund(List<Fund> split_Fund) {
		Split_Fund = split_Fund;
	}

	public List<Invoice> getSplit_Invoice() {
		return Split_Invoice;
	}

	public void setSplit_Invoice(List<Invoice> split_Invoice) {
		Split_Invoice = split_Invoice;
	}

	public List<Invoice> getSplit_Fee() {
		return Split_Fee;
	}

	public void setSplit_Fee(List<Invoice> split_Fee) {
		Split_Fee = split_Fee;
	}

	public List<Pricing> getSplit_Price() {
		return Split_Price;
	}

	public void setSplit_Price(List<Pricing> split_Price) {
		Split_Price = split_Price;
	}

	public List<PricingRecord> getSplit_PriceRecord() {
		return Split_PriceRecord;
	}

	public void setSplit_PriceRecord(List<PricingRecord> split_PriceRecord) {
		Split_PriceRecord = split_PriceRecord;
	}

	public List<Position> getSplit_Position() {
		return Split_Position;
	}

	public void setSplit_Position(List<Position> split_Position) {
		Split_Position = split_Position;
	}

}
